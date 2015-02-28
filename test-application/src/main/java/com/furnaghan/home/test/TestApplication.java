package com.furnaghan.home.test;

import com.furnaghan.home.component.clock.ClockType;
import com.furnaghan.home.component.clock.system.SystemClockComponent;
import com.furnaghan.home.component.clock.system.SystemClockConfiguration;
import com.furnaghan.home.policy.Context;
import com.furnaghan.home.policy.PolicyServer;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.furnaghan.home.registry.config.FileConfigurationStore;
import com.furnaghan.home.script.JavaxScriptFactory;
import com.furnaghan.home.script.ScriptFactory;
import com.furnaghan.home.test.config.TestApplicationConfiguration;
import com.furnaghan.home.test.resources.ComponentResource;
import com.google.common.base.Optional;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;

import java.util.Date;
import java.util.concurrent.Executors;

public class TestApplication extends Application<TestApplicationConfiguration> {

    public static void main(String... args) throws Exception {
        new TestApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<TestApplicationConfiguration> bootstrap) { }

    @Override
    public void run(final TestApplicationConfiguration configuration, final Environment environment) throws Exception {
        final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config");
        environment.lifecycle().manage(configurationStore);

        configurationStore.save(SystemClockComponent.class, "test", Optional.of(new SystemClockConfiguration(Duration.seconds(5))));

        final ComponentRegistry components = new ComponentRegistry(configurationStore);
        environment.lifecycle().manage(components);

        final PolicyServer policyServer = new PolicyServer(Executors.newCachedThreadPool());
        environment.lifecycle().manage(policyServer);

        components.addListener(policyServer::register);

        environment.jersey().register(new ComponentResource(components));

        final ScriptFactory scriptFactory = new JavaxScriptFactory();
        scriptFactory.setVariable("context", Context::get);

        policyServer.register(ClockType.Listener.class, "tick", new Class<?>[]{Date.class},
                scriptFactory.load(TestApplication.class.getResource("/scripts/test.py"))
        );
        policyServer.register(ClockType.Listener.class, "tick", new Class<?>[]{Date.class},
                scriptFactory.load(TestApplication.class.getResource("/scripts/test.groovy"))
        );
        policyServer.register(ClockType.Listener.class, "tick", new Class<?>[]{Date.class},
                scriptFactory.load(TestApplication.class.getResource("/scripts/test.js"))
        );
    }
}
