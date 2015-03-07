package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentRegistry;
import com.furnaghan.home.component.registry.config.ConfigurationStore;
import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import com.furnaghan.home.policy.server.Context;
import com.furnaghan.home.policy.server.PolicyServer;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.ScriptFactory;
import com.google.common.base.Optional;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

public class PolicyServerBundle implements ConfiguredBundle<PolicyServerConfiguration> {

    private PolicyServer policyServer;
    private PolicyStore policyStore;
    private ConfigurationStore configurationStore;
    private ComponentRegistry componentRegistry;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) { }

    @Override
    public void run(final PolicyServerConfiguration configuration, final Environment environment) throws Exception {
        final ScriptStore scriptStore = configuration.getScriptStore();
        final ScriptFactory scriptFactory = configuration.getScriptFactory();

        final ExecutorService executor = environment.lifecycle().executorService("policy-%d").build();
        policyServer = new PolicyServer(executor, scriptStore, scriptFactory);

        policyStore = configuration.getPolicyStore();
        policyStore.addListener(new PolicyStore.Listener() {
            @Override
            public void onPolicyAdded(final String name, final Policy policy) {
                policyServer.register(policy);
            }

            @Override
            public void onPolicyRemoved(final String name, final Policy policy) {
                // TODO
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
                // TODO
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
                // TODO
            }
        });
        environment.lifecycle().manage(configurationStore);

        scriptFactory.setVariable("context", Context::get);
        scriptFactory.setVariable("registry", componentRegistry);
    }

    public ComponentRegistry getRegistry() {
        return checkNotNull(componentRegistry);
    }

    // TODO: Currently only handles adding a new component, not updating an existing
    public void saveComponent(final Class<? extends Component> type, final String name, final Optional<Configuration> configuration) {
        configurationStore.save(type, name, configuration);
    }

    // TODO: Currently only handles adding a new policy, not updating an existing
    public void savePolicy(final String name, final Policy policy) {
        policyStore.save(name, policy);
    }
}
