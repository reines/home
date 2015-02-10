package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.clock.system.SystemClockComponent;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.furnaghan.home.policy.EventListener.proxy;
import static com.furnaghan.util.ReflectionUtil.checkReturnType;
import static com.google.common.base.Preconditions.checkArgument;

public class PolicyServer {

    private static final Logger logger = LoggerFactory.getLogger( PolicyServer.class );

    public static void main(String... args) {
        final PolicyServer server = new PolicyServer();

        server.register("test_clock", new SystemClockComponent());
        server.call("test_clock", "testAction", "hello world");
    }

    private final Map<String, Component<?>> components;
    private final List<EventListener> listeners;

    public PolicyServer() {
        components = Maps.newConcurrentMap();
        listeners = Lists.newCopyOnWriteArrayList();

        // Add a logging listener
        listeners.add(EventListener.logger(logger));
    }

    protected Optional<Component<?>> getComponent(final String name) {
        return Optional.ofNullable(components.get(name));
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

        final T listener = proxy(name, component.getListenerType(), EventListener.delegate(listeners));
        logger.info("Registered {} as '{}'", component, name);
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
    @SuppressWarnings("unchecked")
    public <T> T call(final String name, final String action, final Class<T> resultType, final Object... args) {
        final Optional<Component<?>> component = getComponent(name);
        checkArgument(component.isPresent(), "Unknown component: " + name);

        final Class<?> type = component.get().getClass();
        final Class<?>[] argTypes = ReflectionUtil.getTypes(args);

        try {
            final Method method = type.getMethod(action, argTypes);
            checkReturnType(method, resultType);

            return (T) method.invoke(component.get(), args);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new IllegalArgumentException(String.format(
                    "Unknown action: %s.%s", name, ReflectionUtil.toString(action, args)
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
