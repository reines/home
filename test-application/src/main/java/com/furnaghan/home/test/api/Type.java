package com.furnaghan.home.test.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class Type {

    public static Type of(final Class<?> type) {
        return new Type(type, type.getSimpleName());
    }

    @JsonCreator
    public static Type fromString(final String name) {
        final Optional<Class<?>> type = ReflectionUtil.getClass(name);
        checkArgument(type.isPresent(), "No such type: " + name);
        return of(type.get());
    }

    private final Class<?> type;
    private final String name;

    private Type(final Class<?> type, final String name) {
        this.type = type;
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
