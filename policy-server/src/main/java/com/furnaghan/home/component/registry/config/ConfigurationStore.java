package com.furnaghan.home.component.registry.config;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import io.dropwizard.lifecycle.Managed;

import java.util.Collection;
import java.util.function.Consumer;

public abstract class ConfigurationStore implements Managed {

    public static interface Listener {
        void onConfigurationAdded(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
        void onConfigurationRemoved(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
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

    public abstract void save(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
}
