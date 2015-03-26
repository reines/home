package com.furnaghan.home.policy.store.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.PolicyStoreFactory;

import javax.validation.constraints.NotNull;
import java.io.File;

@JsonTypeName("file")
public class FilePolicyStoreFactory implements PolicyStoreFactory {

    @NotNull
    @JsonProperty
    private final File path = null;

    @Override
    public PolicyStore create() {
        return new FilePolicyStore(path);
    }
}
