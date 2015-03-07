package com.furnaghan.home.test.config;

import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import io.dropwizard.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class TestApplicationConfiguration extends Configuration {

    @Valid
    @NotNull
    private final PolicyServerConfiguration policyServer = new PolicyServerConfiguration();

    public PolicyServerConfiguration getPolicyServer() {
        return policyServer;
    }
}
