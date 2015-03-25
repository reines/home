package com.furnaghan.home.component.registry.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.util.Listenable;
import com.google.common.base.Optional;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.Managed;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class ConfigurationStore extends Listenable<ConfigurationStore.Listener> implements Managed, Discoverable {

    public static interface Listener {
        void onConfigurationAdded(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
        void onConfigurationRemoved(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
    }

    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception { }

    public abstract void save(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
}
