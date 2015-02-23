package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.registry.ComponentRegistry;
import com.google.common.base.Optional;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkState;

public class Context {

    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();

    protected static void set(final String name, final Component<?> component, final ComponentRegistry registry) {
        threadLocal.set(new Context(name, component, registry));
    }

    private static Context context() {
        final Context context = threadLocal.get();
        checkState(context != null, "Context can only be retrieved from the primary thread.");
        return context;
    }

    public static String getName() {
        return context().name;
    }

    @SuppressWarnings("unchecked")
    public static <T extends ComponentType> T getComponent() {
        return (T) context().component;
    }

    public static <T extends ComponentType> Optional<T> findByName(final String name) {
        return context().registry.getComponent(name);
    }

    public static <T extends ComponentType> Collection<T> findByType(final Class<T> type) {
        return context().registry.getComponents(type);
    }

    protected static void clear() {
        threadLocal.remove();
    }

    private final String name;
    private final Component<?> component;
    private final ComponentRegistry registry;

    private Context(final String name, final Component<?> component, final ComponentRegistry registry) {
        this.name = name;
        this.component = component;
        this.registry = registry;
    }
}
