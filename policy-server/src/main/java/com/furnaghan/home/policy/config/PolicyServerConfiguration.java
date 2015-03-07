package com.furnaghan.home.policy.config;

import com.furnaghan.home.component.registry.config.ConfigurationStore;
import com.furnaghan.home.component.registry.config.FileConfigurationStore;
import com.furnaghan.home.policy.store.FilePolicyStore;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.ResourceScriptStore;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.JavaxScriptFactory;
import com.furnaghan.home.script.ScriptFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PolicyServerConfiguration {

    @Valid
    @NotNull
    private final ScriptStore scriptStore = new ResourceScriptStore("/scripts"); // TODO: configure

    @Valid
    @NotNull
    private final ScriptFactory scriptFactory = new JavaxScriptFactory(); // TODO: configure

    @Valid
    @NotNull
    private final PolicyStore policyStore = new FilePolicyStore("/tmp/home-policies"); // TODO: configure

    @Valid
    @NotNull
    private final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config"); // TODO: configure

    public ScriptStore getScriptStore() {
        return scriptStore;
    }

    public ScriptFactory getScriptFactory() {
        return scriptFactory;
    }

    public PolicyStore getPolicyStore() {
        return policyStore;
    }

    public ConfigurationStore getConfigurationStore() {
        return configurationStore;
    }
}
