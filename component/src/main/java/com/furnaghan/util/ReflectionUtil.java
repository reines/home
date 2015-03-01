package com.furnaghan.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang.reflect.MethodUtils;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

public final class ReflectionUtil {

    private static final Joiner ARG_JOINER = Joiner.on(", ");

    @SuppressWarnings("unchecked")
    public static <T> T proxy(final Class<T> type, final InvocationHandler handler) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, handler);
    }

    public static <T> Function<String, Class<T>> classLoader(final ClassLoader loader) {
        return new Function<String, Class<T>>() {
            @Nullable
            @Override
            @SuppressWarnings("unchecked")
            public Class<T> apply(String input) {
                try {
                    return (Class<T>) loader.loadClass(input);
                } catch (ClassNotFoundException e) {
                    throw Throwables.propagate(e);
                }
            }
        };
    }

    public static <T> TypeReference<T> createTypeReference(final Class<T> type, final Class<?>... parameterTypes) {
        return new TypeReference<T>() {
            @Override
            public Type getType() {
                return new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return parameterTypes;
                    }

                    @Override
                    public Type getRawType() {
                        return type;
                    }

                    @Override
                    public Type getOwnerType() {
                        return type;
                    }
                };
            }
        };
    }

    public static Class<?>[] getTypes(final Object[] args) {
        final Class<?>[] types = new Class<?>[args.length];
        for (int i = 0;i < args.length;i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }

    public static String toString(final String name, final Object[] args) {
        return String.format("%s(%s)", name, ARG_JOINER.join(args));
    }

    @SuppressWarnings("unchecked")
    public static <U> Set<Class<U>> getAssignableTypes(final Type[] types, final Class<?> from) {
        final ImmutableSet.Builder<Class<U>> result = ImmutableSet.builder();
        for (final Type type : types) {
            if (from.equals(type)) {
                continue;
            }

            // This class inherits from the desired type
            if (from.isAssignableFrom((Class<?>) type)) {
                final Class<U> target = (Class<U>) type;

                // Add this class
                result.add(target);

                // Add any superclass
                final Type superclasses = target.getGenericSuperclass();
                if (superclasses != null) {
                    result.addAll(getAssignableTypes(new Type[]{superclasses}, from));
                }

                // Add any interfaces
                final Type[] interfaces = target.getGenericInterfaces();
                if (interfaces.length > 0) {
                    result.addAll(getAssignableTypes(interfaces, from));
                }
            }
        }
        return result.build();
    }

    public static Optional<Method> getMethod(final Class<?> type, final String name, final Class<?>... parameterTypes) {
        return Optional.fromNullable(MethodUtils.getMatchingAccessibleMethod(type, name, parameterTypes));
    }

    public static <T> Optional<Constructor<T>> getConstructor(final Class<T> type, final Class<?>... parameterTypes) {
        try {
            return Optional.of(type.getConstructor(parameterTypes));
        }
        catch (NoSuchMethodException e) {
            return Optional.absent();
        }
    }

    private ReflectionUtil() {}
}
