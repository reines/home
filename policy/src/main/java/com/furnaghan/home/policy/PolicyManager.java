package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;

public class PolicyManager implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger( PolicyManager.class );

    private final Multimap<Method, Policy> triggers = HashMultimap.create(); // TODO: thread safety?
    private final ComponentRegistry registry;
    private final ExecutorService executor;

    public PolicyManager(final ComponentRegistry registry, final ExecutorService executor) {
        this.registry = registry;
        this.executor = executor;
    }

    public void register(final Policy policy) {
        final Set<Class<Component.Listener>> listenerTypes = ReflectionUtil.getAssignableTypes(
                policy.getClass().getGenericInterfaces(), Component.Listener.class);
        logger.debug("Registered policy {} with types {}", policy, listenerTypes);

        final Set<Method> methods = ReflectionUtil.getMethods(listenerTypes);
        methods.forEach(method -> triggers.put(method, policy));
    }

    private Collection<Policy> getAffectedPolicies(final Component<?> component, final String event, final Class<?>[] parameterTypes) {
        final Set<Class<Component.Listener>> listenerTypes = Components.getListenerTypes(component.getClass());

        final Set<Method> methods = Sets.newHashSet();
        listenerTypes.forEach(type -> {
            final Optional<Method> method = ReflectionUtil.getMethod(type, event, parameterTypes);
            if (method.isPresent()) {
                methods.add(method.get());
            }
        });

        return FluentIterable.from(methods)
                .transformAndConcat(new Function<Method, Iterable<Policy>>() {
                    @Nullable
                    @Override
                    public Iterable<Policy> apply(@Nullable Method input) {
                        return triggers.get(input);
                    }
                })
                .toSet();
    }

    @Override
    public void onEvent(final Component<?> component, final String name, final String event, final Object... args) {
        final Class<?>[] parameterTypes = ReflectionUtil.getTypes(args);
        for (final Policy policy : getAffectedPolicies(component, event, parameterTypes)) {
            final Optional<Method> method = ReflectionUtil.getMethod(policy.getClass(), event, parameterTypes);
            if (method.isPresent()) {
                executor.submit(() -> trigger(name, component, policy, method.get(), args));
            }
        }
    }

    private void trigger(final String name, final Component<?> component, final Policy policy, final Method method, final Object... args) {
        Context.set(name, component, registry);
        try {
            method.invoke(policy, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.warn("Failed to call {} on policy", ReflectionUtil.toString(method.getName(), args), e);
        } finally {
            Context.clear();
        }
    }
}
