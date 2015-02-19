package com.furnaghan.home.component;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class Component<T extends Component.Listener> {

    public static interface Listener {
    }

    private final Collection<T> listeners;

    public Component() {
        listeners = Lists.newCopyOnWriteArrayList();
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
