package com.furnaghan.home.policy.store;

import com.furnaghan.home.policy.Policy;
import com.google.common.collect.Lists;
import io.dropwizard.lifecycle.Managed;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class PolicyStore implements Managed {

    public static interface Listener {
        void onPolicyAdded(final String name, final Policy policy);
        void onPolicyRemoved(final String name, final Policy policy);
    }

    private final Collection<Listener> listeners = Lists.newCopyOnWriteArrayList();

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    protected final void trigger(final Consumer<Listener> action) {
        listeners.forEach(action);
    }

    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception { }

    public abstract void save(final String name, final Policy policy);
}
