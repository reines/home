package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;

public class Context {

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();

    protected static void set(final String name, final Component<?> component) {
        threadLocal.set(new Context(name, component));
    }

    protected static void clear() {
        threadLocal.remove();
    }

    private final String name;
    private final Component<?> component;

    private Context(final String name, final Component<?> component) {
        this.name = name;
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public Component<?> getComponent() {
        return component;
    }

    public static Context get() {
        return threadLocal.get();
    }
}
