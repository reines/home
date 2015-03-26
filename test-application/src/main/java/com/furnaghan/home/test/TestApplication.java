package com.furnaghan.home.test;

import com.furnaghan.home.component.clock.ClockType;
import com.furnaghan.home.component.clock.system.SystemClockComponent;
import com.furnaghan.home.component.clock.system.SystemClockConfiguration;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.PolicyServerBundle;
import com.furnaghan.home.test.config.ConfiguredBundleWrapper;
import com.furnaghan.home.test.config.TestApplicationConfiguration;
import com.furnaghan.home.test.errors.IllegalArgumentExceptionMapper;
import com.furnaghan.home.test.resources.ComponentResource;
import com.furnaghan.home.test.resources.PolicyResource;
import com.google.common.base.Optional;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

import java.util.Date;

public class TestApplication extends Application<TestApplicationConfiguration> {

    public static void main(final String... args) throws Exception {
        new TestApplication().run(args);
    }

    private PolicyServerBundle policyBundle;

    @Override
    public void initialize(final Bootstrap<TestApplicationConfiguration> bootstrap) {
        policyBundle = new PolicyServerBundle();
        bootstrap.addBundle(new ConfiguredBundleWrapper<>(policyBundle, TestApplicationConfiguration::getPolicyServer));
    }

    @Override
    public void run(final TestApplicationConfiguration configuration, final Environment environment) throws Exception {
        // TODO: remove, temp testing
        policyBundle.getConfigurationStore().save(SystemClockComponent.class, "test_clock",
                Optional.of(new SystemClockConfiguration(Duration.seconds(5))));

        policyBundle.getPolicyStore().save(new Policy(ClockType.Listener.class, "tick", new Class<?>[]{Date.class}, "tick.py"));

        environment.jersey().register(new IllegalArgumentExceptionMapper());
        environment.jersey().register(new ComponentResource(policyBundle.getConfigurationStore(), policyBundle.getComponentList()));
        environment.jersey().register(new PolicyResource(policyBundle.getPolicyStore(), policyBundle.getPolicyList()));
    }
}
