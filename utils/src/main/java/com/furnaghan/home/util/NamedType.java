package com.furnaghan.home.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class NamedType {

    public static NamedType of(final Class<?> type) {
        return new NamedType(type, type.getSimpleName());
    }

    @JsonCreator
    public static NamedType fromString(final String name) {
        final Optional<Class<?>> type = ReflectionUtil.getClass(name);
        checkArgument(type.isPresent(), "No such type: " + name);
        return of(type.get());
    }

    private final Class<?> type;
    private final String name;

    private NamedType(final Class<?> type, final String name) {
        this.type = type;
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public boolean isTypeOf(final Class<?> target) {
        return !type.isAssignableFrom(target);
    }
}
