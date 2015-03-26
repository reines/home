package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentList;
import com.furnaghan.home.component.registry.ComponentRegistry;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.component.registry.store.ConfigurationStoreListener;
import com.furnaghan.home.policy.config.PolicyServerConfiguration;
import com.furnaghan.home.policy.server.Context;
import com.furnaghan.home.policy.server.PolicyList;
import com.furnaghan.home.policy.server.PolicyServer;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.policy.store.PolicyStoreListener;
import com.furnaghan.home.policy.store.ScriptStore;
import com.furnaghan.home.script.JavaxScriptFactory;
import com.furnaghan.home.script.ScriptFactory;
import com.google.common.base.Optional;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;

import java.util.concurrent.ExecutorService;

import static com.google.common.base.Preconditions.checkNotNull;

public class PolicyServerBundle implements ConfiguredBundle<PolicyServerConfiguration> {

    private ScriptStore scriptStore;
    private PolicyStore policyStore;
    private ComponentRegistry componentRegistry;
    private ConfigurationStore configurationStore;
    private PolicyServer policyServer;

    @Override
    public void initialize(final Bootstrap<?> bootstrap) { }

    @Override
    public void run(final PolicyServerConfiguration configuration, final Environment environment) throws Exception {
        scriptStore = configuration.getScriptStore().create();
        final ScriptFactory scriptFactory = new JavaxScriptFactory();

        final ExecutorService policyExecutor = environment.lifecycle().executorService("policy-%d")
                .minThreads(configuration.getThreads())
                .maxThreads(configuration.getThreads())
                .build();
        policyServer = new PolicyServer(policyExecutor, scriptStore, scriptFactory);

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

        // Add a hook to load policies and components once everything else has started
        environment.lifecycle().addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
            @Override
            public void lifeCycleStarted(LifeCycle lifeCycle) {
                Components.discover().forEach(configurationStore::load);
                policyStore.load();
            }
        });
    }

    public ScriptStore getScriptStore() {
        return checkNotNull(scriptStore);
    }

    public PolicyStore getPolicyStore() {
        return checkNotNull(policyStore);
    }

    public ComponentList getComponentList() {
        return checkNotNull(componentRegistry);
    }

    public ConfigurationStore getConfigurationStore() {
        return checkNotNull(configurationStore);
    }

    public PolicyList getPolicyList() {
        return checkNotNull(policyServer);
    }
}
