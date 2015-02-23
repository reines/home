package com.furnaghan.home.test;

import com.furnaghan.home.component.clock.system.SystemClockComponent;
import com.furnaghan.home.component.clock.system.SystemClockConfiguration;
import com.furnaghan.home.component.storage.local.LocalStorageComponent;
import com.furnaghan.home.component.storage.local.LocalStorageConfiguration;
import com.furnaghan.home.policy.PolicyServer;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.furnaghan.home.registry.config.FileConfigurationStore;
import com.furnaghan.home.test.policy.TestPolicy;
import com.google.common.base.Optional;
import io.dropwizard.util.Duration;

import java.io.File;
import java.util.concurrent.Executors;

public class TestApplication {

    public static void main(final String... args) {
        final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config");
        configurationStore.save(SystemClockComponent.class, "test-clock", Optional.of(new SystemClockConfiguration(Duration.seconds(2))));
        configurationStore.save(LocalStorageComponent.class, "test-storage1", Optional.of(new LocalStorageConfiguration(new File("/"))));
        configurationStore.save(LocalStorageComponent.class, "test-storage2", Optional.of(new LocalStorageConfiguration(new File("/"))));

        final ComponentRegistry registry = new ComponentRegistry(configurationStore);
        final PolicyServer policyServer = new PolicyServer(registry, Executors.newCachedThreadPool());
        policyServer.register(new TestPolicy());

        policyServer.start();
        registry.start();
    }
}
