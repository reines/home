package com.furnaghan.home.component.registry.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ConfigurationStoreFactory extends Discoverable {
    ConfigurationStore create();
}
