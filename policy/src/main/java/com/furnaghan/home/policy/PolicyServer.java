package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.script.Script;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.furnaghan.home.component.Components.getName;
import static com.furnaghan.home.policy.EventListener.proxy;
import static com.google.common.base.Preconditions.checkNotNull;

public class PolicyServer implements Managed {

    private static final Logger logger = LoggerFactory.getLogger( PolicyServer.class );

    private final Map<String, Component<?>> components;
    private final List<EventListener> listeners;
    private final PolicyManager policies;

    public PolicyServer(final ExecutorService executor) {
        components = Maps.newConcurrentMap();
        listeners = Lists.newCopyOnWriteArrayList();
        policies = new PolicyManager(executor);

        // Add a logging listener
        listeners.add(EventListener.logger(logger));

        // Add a policy manager - triggers policies when appropriate
        listeners.add(policies);
    }

    @Override
    public void start() { }

    @Override
    public void stop() { }

    public boolean register(final Class<? extends Component.Listener> type, final String event, final Class<?>[] parameterTypes, final Script script) {
        return policies.register(type, event, parameterTypes, script);
    }

    /**
     * Registers a named component with this policy server.
     *
     * @param name          The name by which to refer to this instance of the component.
     * @param component     The component itself.
     * @return              True if the component was registered, False if this name was already in use.
     */
    public <T extends Component.Listener> boolean register(final String name, final Component<T> component) {
        final boolean added = components.put(name, component) == null;
        if (!added) {
            logger.debug("Not registering {}, there is already a component called '{}'", component, name);
            return false;
        }

        final T listener = proxy(component, name, EventListener.delegate(listeners));
        logger.info("Registered {} '{}'", getName(component), name);
        component.addListener(listener);
        return true;
    }

    /**
     * Triggers an action upon a named component, and returns the result.
     *
     * @param name                          The name of the component on which to trigger the acton.
     * @param action                        The name of the action to trigger.
     * @param resultType                    The type of result to be returned.
     * @param args                          Arguments to pass to the action.
     * @throws IllegalArgumentException     If there is no action with the given name and argument types.
     */
    public <T> T call(final String name, final String action, final Class<T> resultType, final Object... args) {
        final Component<?> component = components.get(name);
        checkNotNull(component, "Unknown component: " + name);

        final Class<?> type = component.getClass();
        final Class<?>[] parameterTypes = ReflectionUtil.getTypes(args);

        try {
            final Optional<Method> method = ReflectionUtil.getMethod(type, action, parameterTypes);
            if (!method.isPresent()) {
                throw new IllegalArgumentException(String.format(
                        "Unable to find method: %s.%s", name, ReflectionUtil.toString(action, args)
                ));
            }

            return resultType.cast(method.get().invoke(component, args));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format(
                    "Attempted to call inaccessible method: %s.%s", name, ReflectionUtil.toString(action, args)
            ));
        } catch (InvocationTargetException e) {
            throw Throwables.propagate(e.getCause());
        }
    }

    /**
     * Triggers an action upon a named component.
     *
     * @param name                          The name of the component on which to trigger the acton.
     * @param action                        The name of the action to trigger.
     * @param args                          Arguments to pass to the action.
     * @throws IllegalArgumentException     If there is no action with the given name and argument types.
     */
    public void call(final String name, final String action, final Object... args) {
        call(name, action, Void.class, args);
    }
}
