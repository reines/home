package com.furnaghan.home.component;

import com.furnaghan.util.ReflectionUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Consumer;

public abstract class Component<T extends Component.Listener> {

    public static interface Listener {
    }

    public static Iterator<Component> load() {
        return ServiceLoader.load(Component.class).iterator();
    }

    private final Collection<T> listeners;

    public Component() {
        listeners = Lists.newCopyOnWriteArrayList();
    }

    @SuppressWarnings("unchecked")
    public <U extends ComponentType> Set<Class<U>> getComponentTypes() {
        final Type[] types = this.getClass().getGenericInterfaces();
        return ReflectionUtil.getAssignableTypes(types, ComponentType.class);
    }

    @SuppressWarnings("unchecked")
    public Class<T> getListenerType() {
        final Type[] types = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments();
        return Iterables.getOnlyElement(ReflectionUtil.getAssignableTypes(types, Component.Listener.class));
    }

    public final void addListener(final T listener) {
        listeners.add(listener);
    }

    protected final void trigger(final Consumer<T> action) {
        listeners.forEach(action);
    }

    public final void removeListener(final T listener) {
        listeners.remove(listener);
    }
}
