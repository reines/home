package com.furnaghan.home.component.storage.local;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;

import javax.validation.constraints.NotNull;
import java.io.File;

public class LocalStorageConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private File root;

    public File getRoot() {
        return root;
    }
}
