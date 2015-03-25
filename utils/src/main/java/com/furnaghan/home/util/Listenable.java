package com.furnaghan.home.util;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class Listenable<T> {

    private final Collection<T> listeners = Lists.newCopyOnWriteArrayList();

    public void addListener(final T listener) {
        listeners.add(listener);
    }

    public void removeListener(final T listener) {
        listeners.remove(listener);
    }

    protected final void trigger(final Consumer<T> action) {
        listeners.forEach(action);
    }
}
