package com.furnaghan.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

public final class ReflectionUtil {

    private static final Joiner ARG_JOINER = Joiner.on(", ");

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

    public static Class<?>[] getTypes(final Object... args) {
        final Class<?>[] types = new Class<?>[args.length];
        for (int i = 0;i < args.length;i++) {
            types[i] = args[i].getClass();
        }
        return types;
    }

    public static String toString(final String name, final Object... args) {
        return String.format("%s(%s)", name, ARG_JOINER.join(args));
    }

    @SuppressWarnings("unchecked")
    public static <U> Set<Class<U>> getAssignableTypes(final Type[] types, final Class<?> from) {
        final ImmutableSet.Builder<Class<U>> result = ImmutableSet.builder();
        for (Type type : types) {
            if (from.isAssignableFrom((Class<?>) type)) {
                result.add((Class<U>) type);
            }
        }
        return result.build();
    }

    public static <T> Set<Method> getMethods(final Collection<Class<T>> types) {
        final Set<Method> methods = Sets.newHashSet();
        types.forEach(t -> methods.addAll(Arrays.asList(t.getMethods())));
        return Collections.unmodifiableSet(methods);
    }

    public static <T> void checkReturnType(final Method method, final Class<T> resultType) {
        checkReturnType(method, resultType, String.format("Return type of %s is %s, not %s",
                method.getName(), method.getReturnType(), resultType));
    }

    public static <T> void checkReturnType(final Method method, final Class<T> resultType, final Object errorMessage) {
        checkArgument(Void.class.isAssignableFrom(resultType) || resultType.isAssignableFrom(method.getReturnType()),
                errorMessage);
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
