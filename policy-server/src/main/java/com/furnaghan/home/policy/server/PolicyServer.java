package com.furnaghan.home.policy.server;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.Script;
import com.furnaghan.home.script.ScriptFactory;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import static com.furnaghan.home.component.Components.getName;
import static com.furnaghan.home.policy.server.EventListener.proxy;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class PolicyServer implements PolicyList {

    private static final Logger logger = LoggerFactory.getLogger( PolicyServer.class );

    private final Map<String, Component<?>> components;
    private final List<EventListener> listeners;
    private final Map<Policy, Script> policies;
    private final ScriptManager scripts;
    private final ScriptStore scriptStore;
    private final ScriptFactory scriptFactory;

    public PolicyServer(final ExecutorService executor, final ScriptStore scriptStore, final ScriptFactory scriptFactory) {
        this.scriptStore = scriptStore;
        this.scriptFactory = scriptFactory;

        components = Maps.newConcurrentMap();
        listeners = Lists.newCopyOnWriteArrayList();
        policies = Maps.newConcurrentMap();
        scripts = new ScriptManager(executor);

        // Add a logging listener
        listeners.add(EventListener.logger(logger));

        // Add a script manager - triggers scripts when appropriate
        listeners.add(scripts);
    }

    @Override
    public Collection<Policy> list() {
        return Collections.unmodifiableCollection(policies.keySet());
    }

    public synchronized boolean register(final Policy policy) {
        if (policies.containsKey(policy)) {
            return true;
        }

        final Optional<CharSource> source = scriptStore.load(policy.getScript());
        checkState(source.isPresent(), "Unable to find script: " + policy.getScript());

        final String type = Files.getFileExtension(policy.getScript());
        final Script script = scriptFactory.load(source.get(), type);
        final boolean registered = scripts.register(policy.getType(), policy.getEvent(),
                policy.getParameterTypes(), script);

        if (registered) {
            policies.put(policy, script);
            return true;
        }

        return false;
    }

    public synchronized boolean remove(final Policy policy) {
        final Optional<Script> script = Optional.fromNullable(policies.get(policy));
        return script.isPresent() && scripts.remove(policy.getType(), policy.getEvent(),
                policy.getParameterTypes(), script.get());
    }

    /**
     * Registers a named component with this policy server.
     *
     * @param name          The name by which to refer to this instance of the component.
     * @param component     The component itself.
     * @return              True if the component was registered, False if this name was already in use.
     */
    public synchronized <T extends Component.Listener> boolean register(final String name, final Component<T> component) {
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

    public synchronized boolean remove(final String name) {
        return components.remove(name) != null;
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
