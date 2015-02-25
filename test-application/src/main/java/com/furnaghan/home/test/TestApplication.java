package com.furnaghan.home.test;

import com.codahale.metrics.MetricRegistry;
import com.furnaghan.home.component.heating.evohome.EvohomeComponent;
import com.furnaghan.home.component.heating.evohome.EvohomeConfiguration;
import com.furnaghan.home.component.meter.currentcost.CurrentcostComponent;
import com.furnaghan.home.component.meter.currentcost.CurrentcostConfiguration;
import com.furnaghan.home.policy.PolicyServer;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.furnaghan.home.registry.config.FileConfigurationStore;
import com.furnaghan.home.test.policy.MeterLoggingPolicy;
import com.google.common.base.Optional;
import io.dropwizard.metrics.Slf4jReporterFactory;
import io.dropwizard.util.Duration;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestApplication {

    public static void main(final String... args) {
        final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config");
        final ComponentRegistry registry = new ComponentRegistry(configurationStore);
        final PolicyServer policyServer = new PolicyServer(registry, Executors.newCachedThreadPool());

        // Evohome
        final EvohomeConfiguration evohomeConfiguration = new EvohomeConfiguration("user", "pass", Duration.seconds(10));
        evohomeConfiguration.setConnectionTimeout(Duration.seconds(5));
        evohomeConfiguration.setTimeout(Duration.seconds(5));
        configurationStore.save(EvohomeComponent.class, "heating", Optional.of(evohomeConfiguration));

        // Currentcost
        configurationStore.save(CurrentcostComponent.class, "electricity", Optional.of(new CurrentcostConfiguration("/dev/ttyUSB0")));

        final MetricRegistry metrics = new MetricRegistry();
        new Slf4jReporterFactory().build(metrics).start(5, TimeUnit.SECONDS);

        // Meter logging
        policyServer.register(new MeterLoggingPolicy(metrics));

        policyServer.start();
        registry.start();
    }
}
