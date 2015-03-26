package com.furnaghan.home.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.io.CharStreams;
import org.apache.commons.lang.reflect.MethodUtils;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.*;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Set;

public final class ReflectionUtil {

    private static final Joiner ARG_JOINER = Joiner.on(", ");

    public static <T> Set<Class<T>> discoverServices(final Class<T> parent) {
        final ClassLoader classLoader = parent.getClassLoader();

        final Set<Class<T>> serviceClasses = Sets.newHashSet();
        try {
            final Enumeration<URL> resources = classLoader.getResources("META-INF/services/" + parent.getName());
            while (resources.hasMoreElements()) {
                final URL url = resources.nextElement();
                try (final Reader reader = new InputStreamReader(url.openStream())) {
                    final Collection<Class<T>> types = Collections2.transform(
                            CharStreams.readLines(reader), ReflectionUtil.classLoader(classLoader));
                    serviceClasses.addAll(types);
                }
            }
        }
        catch (IOException e) {
            throw Throwables.propagate(e);
        }
        return Collections.unmodifiableSet( serviceClasses );
    }

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

    public static Optional<Class<?>> getClass(final String name) {
        try {
            return Optional.of(Class.forName(name));
        } catch (ClassNotFoundException e) {
            return Optional.absent();
        }
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
