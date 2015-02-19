package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Function;
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

public class PolicyManager implements EventListener {

    private static final Logger logger = LoggerFactory.getLogger( PolicyManager.class );

    private final Multimap<Method, Policy> triggers = HashMultimap.create();

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
            try {
                final Method method = type.getMethod(event, parameterTypes);
                methods.add(method);
            } catch (NoSuchMethodException e) {
                /* unused: no such method is fine - just don't trigger this policy */
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
            try {
                final Method method = policy.getClass().getMethod(event, parameterTypes);
                method.invoke(policy, args);
            } catch (NoSuchMethodException e) {
                    /* unused: no such method is fine - just don't trigger this policy */
            } catch (InvocationTargetException | IllegalAccessException e) {
                logger.warn("Failed to call {} on policy", ReflectionUtil.toString(event, args), e);
            }
        }
    }
}
