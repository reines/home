package com.furnaghan.home.registry.config;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;

import java.util.Map;

public interface ConfigurationStore {
    void save(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration);
    Map<String, Optional<Configuration>> load(final Class<? extends Component> type);
}
