package com.furnaghan.home.test;

import com.furnaghan.home.policy.PolicyServer;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.furnaghan.home.registry.config.FileConfigurationStore;

public class TestApplication {

    public static void main(final String... args) {
        final ConfigurationStore configurationStore = new FileConfigurationStore("/tmp/home-config");
//        configurationStore.save(SystemClockComponent.class, "test-clock", Optional.absent());
//        configurationStore.save(LocalStorageComponent.class, "test-storage1", Optional.of(new LocalStorageConfiguration(new File("/"))));
//        configurationStore.save(LocalStorageComponent.class, "test-storage2", Optional.of(new LocalStorageConfiguration(new File("/"))));

        final ComponentRegistry registry = new ComponentRegistry(configurationStore);
        final PolicyServer policyServer = new PolicyServer();
        policyServer.register(new TestPolicy());

        registry.addListener(policyServer::register);
        policyServer.start();
        registry.start();
    }
}
