package com.furnaghan.home.component.registry.store.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.component.registry.store.ConfigurationStoreFactory;

import javax.validation.constraints.NotNull;
import java.io.File;

@JsonTypeName("file")
public class FileConfigurationStoreFactory implements ConfigurationStoreFactory {

    @NotNull
    @JsonProperty
    private final File path = null;

    @Override
    public ConfigurationStore create() {
        return new FileConfigurationStore(path);
    }
}
