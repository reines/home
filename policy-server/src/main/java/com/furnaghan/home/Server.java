package com.furnaghan.home;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentRegistry;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.component.registry.store.ConfigurationStoreListener;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import com.furnaghan.home.policy.server.Context;
import com.furnaghan.home.policy.server.PolicyServer;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.PolicyStoreListener;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.JavaxScriptFactory;
import com.furnaghan.home.script.ScriptFactory;
import com.google.common.base.Optional;
import io.dropwizard.lifecycle.Managed;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Managed {

    private final ScriptStore scriptStore;
    private final PolicyStore policyStore;
    private final ComponentRegistry componentRegistry;
    private final ConfigurationStore configurationStore;
    private final PolicyServer policyServer;
    private final ExecutorService executor;

    public Server(final PolicyServerConfiguration configuration) {
        scriptStore = configuration.getScriptStore().create();
        final ScriptFactory scriptFactory = new JavaxScriptFactory();

        executor = Executors.newFixedThreadPool(configuration.getThreads());
        policyServer = new PolicyServer(executor, scriptStore, scriptFactory);

        policyStore = configuration.getPolicyStore().create();
        policyStore.addListener(new PolicyStoreListener() {
            @Override
            public void onPolicyAdded(final Policy policy) {
                policyServer.register(policy);
            }

            @Override
            public void onPolicyRemoved(final Policy policy) {
                policyServer.remove(policy);
            }
        });

        componentRegistry = new ComponentRegistry();
        componentRegistry.addListener(new ComponentRegistry.Listener() {
            @Override
            public void onComponentAdded(final String name, final Component<?> component) {
                policyServer.register(name, component);
            }

            @Override
            public void onComponentRemoved(final String name, final Component<?> component) {
                policyServer.remove(name);
            }
        });

        configurationStore = configuration.getConfigurationStore().create();
        configurationStore.addListener(new ConfigurationStoreListener() {
            @Override
            public void onConfigurationAdded(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration) {
                componentRegistry.load(type, name, configuration);
            }

            @Override
            public void onConfigurationRemoved(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration) {
                componentRegistry.remove(name);
            }
        });

        scriptFactory.setVariable("context", Context::get);
        scriptFactory.setVariable("registry", componentRegistry);
    }

    public ScriptStore getScriptStore() {
        return scriptStore;
    }

    public PolicyStore getPolicyStore() {
        return policyStore;
    }

    public ComponentRegistry getComponentRegistry() {
        return componentRegistry;
    }

    public ConfigurationStore getConfigurationStore() {
        return configurationStore;
    }

    public PolicyServer getPolicyServer() {
        return policyServer;
    }

    @Override
    public void start() {
        Components.discover().forEach(configurationStore::load);
        policyStore.load();
    }

    @Override
    public void stop() {
        executor.shutdown();
    }
}
