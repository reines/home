package com.furnaghan.util;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;

import java.lang.reflect.Type;
import java.util.Set;

public final class ReflectionUtil {

    private static final Joiner ARG_JOINER = Joiner.on(", ");

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

    private ReflectionUtil() {}
}
