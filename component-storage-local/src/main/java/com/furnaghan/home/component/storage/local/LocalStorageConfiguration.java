package com.furnaghan.home.component.storage.local;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;

import javax.validation.constraints.NotNull;
import java.io.File;

public class LocalStorageConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final File root;

    @JsonCreator
    public LocalStorageConfiguration(
            @JsonProperty("root") final File root) {
        this.root = root;
    }

    public File getRoot() {
        return root;
    }
}
