package com.furnaghan.home.policy.config;

import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.ScriptStore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PolicyServerConfiguration {

    @Min(1)
    private final int threads = 10;

    @Valid
    @NotNull
    private final ScriptStore scriptStore = null;

    @Valid
    @NotNull
    private final PolicyStore policyStore = null;

    @Valid
    @NotNull
    private final ConfigurationStore configurationStore = null;

    public int getThreads() {
        return threads;
    }

    public ScriptStore getScriptStore()
    {
        return scriptStore;
    }

    public PolicyStore getPolicyStore()
    {
        return policyStore;
    }

    public ConfigurationStore getConfigurationStore()
    {
        return configurationStore;
    }
}
