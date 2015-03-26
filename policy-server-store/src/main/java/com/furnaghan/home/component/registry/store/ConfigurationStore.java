package com.furnaghan.home.component.registry.store;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.util.Listenable;
import com.google.common.base.Optional;

import java.util.Collection;

public abstract class ConfigurationStore extends Listenable<ConfigurationStoreListener> {
    public abstract void save(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
    public abstract boolean delete(final Class<? extends Component> componentType, final String name);
    public abstract Collection<Optional<Configuration>> load(Class<? extends Component> componentType);
}
