package com.furnaghan.home.test.api;

public class Type {

    public static Type of(final Class<?> type) {
        return new Type(type, type.getSimpleName());
    }

    private final Class<?> type;
    private final String name;

    public Type(final Class<?> type, final String name) {
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
