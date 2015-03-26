package com.furnaghan.home.policy.config;

import com.furnaghan.home.component.registry.store.ConfigurationStoreFactory;
import com.furnaghan.home.policy.store.PolicyStoreFactory;
import com.furnaghan.home.policy.store.ScriptStoreFactory;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PolicyServerConfiguration {

    @Min(1)
    private final int threads = 10;

    @Valid
    @NotNull
    private final ScriptStoreFactory scriptStore = null;

    @Valid
    @NotNull
    private final PolicyStoreFactory policyStore = null;

    @Valid
    @NotNull
    private final ConfigurationStoreFactory configurationStore = null;

    public int getThreads() {
        return threads;
    }

    public ScriptStoreFactory getScriptStore()
    {
        return scriptStore;
    }

    public PolicyStoreFactory getPolicyStore()
    {
        return policyStore;
    }

    public ConfigurationStoreFactory getConfigurationStore()
    {
        return configurationStore;
    }
}
