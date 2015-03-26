package com.furnaghan.home.policy.store.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.policy.store.ScriptStoreFactory;

import javax.validation.constraints.NotNull;
import java.io.File;

@JsonTypeName("file")
public class FileScriptStoreFactory implements ScriptStoreFactory {

    @NotNull
    @JsonProperty
    private final File path = null;

    @Override
    public ScriptStore create() {
        return new FileScriptStore(path);
    }
}
