package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentList;
import com.furnaghan.home.component.registry.ComponentRegistry;
import com.furnaghan.home.component.registry.config.ConfigurationStore;
import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import com.furnaghan.home.policy.server.Context;
import com.furnaghan.home.policy.server.PolicyServer;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.JavaxScriptFactory;
import com.furnaghan.home.script.ScriptFactory;
import com.google.common.base.Optional;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

public class PolicyServerBundle implements ConfiguredBundle<PolicyServerConfiguration> {

    private PolicyStore policyStore;
    private ConfigurationStore configurationStore;
    private ComponentRegistry componentRegistry;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) { }

    @Override
    public void run(final PolicyServerConfiguration configuration, final Environment environment) throws Exception {
        final ScriptStore scriptStore = configuration.getScriptStore();
        final ScriptFactory scriptFactory = new JavaxScriptFactory();

        final ExecutorService policyExecutor = environment.lifecycle().executorService("policy-%d")
                .minThreads(configuration.getThreads())
                .maxThreads(configuration.getThreads())
                .build();
        final PolicyServer policyServer = new PolicyServer(policyExecutor, scriptStore, scriptFactory);

        policyStore = configuration.getPolicyStore();
        policyStore.addListener(new PolicyStore.Listener() {
            @Override
            public void onPolicyAdded(final String name, final Policy policy) {
                policyServer.register(policy);
            }

            @Override
            public void onPolicyRemoved(final String name, final Policy policy) {
                policyServer.remove(policy);
            }
        });
        environment.lifecycle().manage(policyStore);

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

        configurationStore = configuration.getConfigurationStore();
        configurationStore.addListener(new ConfigurationStore.Listener() {
            @Override
            public void onConfigurationAdded(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration) {
                componentRegistry.load(type, name, configuration);
            }

            @Override
            public void onConfigurationRemoved(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration) {
                componentRegistry.remove(name);
            }
        });
        environment.lifecycle().manage(configurationStore);

        scriptFactory.setVariable("context", Context::get);
        scriptFactory.setVariable("registry", componentRegistry);
    }

    public PolicyStore getPolicyStore() {
        return checkNotNull(policyStore);
    }

    public ConfigurationStore getConfigurationStore() {
        return checkNotNull(configurationStore);
    }

    public ComponentList getComponentList() {
        return checkNotNull(componentRegistry);
    }
}
