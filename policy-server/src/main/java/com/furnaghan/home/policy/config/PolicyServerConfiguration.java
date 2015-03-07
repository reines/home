package com.furnaghan.home.policy.config;

import com.furnaghan.home.component.registry.config.ConfigurationStore;
import com.furnaghan.home.component.registry.config.FileConfigurationStore;
import com.furnaghan.home.policy.store.FilePolicyStore;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.ResourceScriptStore;
import com.furnaghan.home.policy.store.ScriptStore;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PolicyServerConfiguration {

    @Valid
    @NotNull
    private final ScriptStore scriptStore = new ResourceScriptStore("/scripts"); // TODO: configure

    @Valid
    @NotNull
    private final PolicyStore policyStore = new FilePolicyStore("/tmp/home-policies"); // TODO: configure

    @Valid
    @NotNull
    private final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config"); // TODO: configure

    @Min(1)
    private final int threads = 10;

    public ScriptStore getScriptStore() {
        return scriptStore;
    }

    public PolicyStore getPolicyStore() {
        return policyStore;
    }

    public ConfigurationStore getConfigurationStore() {
        return configurationStore;
    }

    public int getThreads() {
        return threads;
    }
}
