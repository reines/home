package com.furnaghan.home.policy.server;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.script.ParameterMap;
import com.furnaghan.home.script.Script;
import com.furnaghan.home.util.ReflectionUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class ScriptManager implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger( ScriptManager.class );

    private final Multimap<Method, Script> scripts = HashMultimap.create();
    private final ExecutorService executor;

    public ScriptManager(final ExecutorService executor) {
        this.executor = executor;
    }

    public boolean register(final Class<?> type, final String event, final Class<?>[] parameterTypes, final Script script) {
        final Optional<Method> method = ReflectionUtil.getMethod(type, event, parameterTypes);
        if (!method.isPresent()) {
            logger.warn("Failed to register policy for {}, no such method", ReflectionUtil.toString(event, parameterTypes));
            return false;
        }

        logger.info("Registered policy for {}", ReflectionUtil.toString(event, parameterTypes));
        return scripts.put(method.get(), script);
    }

    public boolean remove(final Class<?> type, final String event, final Class<?>[] parameterTypes, final Script script) {
        final Optional<Method> method = ReflectionUtil.getMethod(type, event, parameterTypes);
        if (!method.isPresent()) {
            logger.warn("Failed to remove policy for {}, no such method", ReflectionUtil.toString(event, parameterTypes));
            return false;
        }

        return scripts.remove(method.get(), script);
    }

    private Collection<Script> getScripts(final Component<?> component, final String event, final Class<?>[] parameterTypes) {
        final Set<Class<Component.Listener>> listenerTypes = Components.getListenerTypes(component.getClass());
        return FluentIterable.from(listenerTypes)
                .transformAndConcat(new Function<Class<Component.Listener>, Iterable<Method>>() {
                    @Nullable
                    @Override
                    public Iterable<Method> apply(@Nullable Class<Component.Listener> input) {
                        return ReflectionUtil.getMethod(input, event, parameterTypes).asSet();
                    }
                })
                .transformAndConcat(new Function<Method, Iterable<Script>>() {
                    @Nullable
                    @Override
                    public Iterable<Script> apply(@Nullable Method input) {
                        return scripts.get(input);
                    }
                })
                .toSet();
    }

    @Override
    public void onEvent(final Component<?> component, final String name, final String event, final ParameterMap params) {
        getScripts(component, event, ReflectionUtil.getTypes(params.values()))
                .forEach(s -> executor.submit(() -> trigger(name, component, s, params)));
    }

    private void trigger(final String name, final Component<?> component, final Script script, final ParameterMap params) {
        Context.set(name, component);
        try {
            script.run(params);
        } catch (Exception e) {
            logger.warn("Failed to run script: {}", script, e);
        } finally {
            Context.clear();
        }
    }
}
