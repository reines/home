package com.furnaghan.home.component.registry.store;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;

public interface ConfigurationStoreListener {
    void onConfigurationAdded(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
    void onConfigurationRemoved(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
}
