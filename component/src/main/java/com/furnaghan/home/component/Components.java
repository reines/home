package com.furnaghan.home.component;

import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.collect.Iterables;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public final class Components {

    public static String getName(final Class<? extends Component> component) {
        return component.getSimpleName();
    }

    public static String getName(final Component<?> component) {
        return getName(component.getClass());
    }

    public static <T extends ComponentType> Set<Class<T>> getComponentTypes(final Class<? extends Component> component) {
        final Type[] types = component.getGenericInterfaces();
        return ReflectionUtil.getAssignableTypes(types, ComponentType.class);
    }

    public static <T extends Component.Listener> Set<Class<T>> getListenerTypes(final Class<? extends Component> component) {
        final Type[] types = ((ParameterizedType) component.getGenericSuperclass()).getActualTypeArguments();
        return ReflectionUtil.getAssignableTypes(types, Component.Listener.class);
    }

    public static <T extends Component.Listener> Class<T> getListenerType(final Class<? extends Component> component) {
        final Type[] types = ((ParameterizedType) component.getGenericSuperclass()).getActualTypeArguments();
        return Iterables.getOnlyElement(ReflectionUtil.getAssignableTypes(types, Component.Listener.class));
    }

    public static <T extends Configuration> Optional<Class<T>> getConfigurationType(final Class<? extends Component> component) {
        for (final Constructor<?> constructor : component.getConstructors()) {
            final Set<Class<T>> configurationTypes = ReflectionUtil.getAssignableTypes(
                    constructor.getParameterTypes(), Configuration.class);
            if (!configurationTypes.isEmpty()) {
                return Optional.of(Iterables.getOnlyElement(configurationTypes));
            }
        }

        return Optional.absent();
    }

    public static <T extends Component<?>> T create(final Class<T> componentType, final Optional<Configuration> configuration) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        final Optional<Constructor<T>> noArgConstructor = ReflectionUtil.getConstructor(componentType);
        if (noArgConstructor.isPresent()) {
            return noArgConstructor.get().newInstance();
        }

        if (configuration.isPresent()) {
            final Class<?> configurationType = configuration.get().getClass();
            final Optional<Constructor<T>> configuredConstructor = ReflectionUtil.getConstructor(componentType, configurationType);
            if (configuredConstructor.isPresent()) {
                return configuredConstructor.get().newInstance(configuration.get());
            }
        }

        throw new IllegalArgumentException("No suitable constructor for component: " + getName(componentType));
    }

    private Components() {}
}
