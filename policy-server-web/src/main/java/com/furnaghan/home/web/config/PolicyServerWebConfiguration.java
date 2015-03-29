package com.furnaghan.home.web.config;

import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class PolicyServerWebConfiguration extends Configuration {

    @Valid
    @NotNull
    private final PolicyServerConfiguration policyServer = new PolicyServerConfiguration();

    public PolicyServerConfiguration getPolicyServer() {
        return policyServer;
    }
}
