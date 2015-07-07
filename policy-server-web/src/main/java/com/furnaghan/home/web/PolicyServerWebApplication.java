package com.furnaghan.home.web;

import com.furnaghan.home.Server;
import com.furnaghan.home.web.config.PolicyServerWebConfiguration;
import com.furnaghan.home.web.errors.IllegalArgumentExceptionMapper;
import com.furnaghan.home.web.resources.ComponentResource;
import com.furnaghan.home.web.resources.PolicyResource;
import com.furnaghan.home.web.resources.ScriptResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PolicyServerWebApplication extends Application<PolicyServerWebConfiguration> {

    public static void main(final String... args) throws Exception {
        new PolicyServerWebApplication().run(args);
    }

    @Override
    public void initialize(final Bootstrap<PolicyServerWebConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets", "/assets/", "index.html", "assets"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/lib/", "index.html", "webjars"));
    }

    @Override
    public void run(final PolicyServerWebConfiguration configuration, final Environment environment) {
        final Server server = new Server(configuration.getPolicyServer());
        environment.lifecycle().manage(server);

        environment.jersey().register(new IllegalArgumentExceptionMapper());

        environment.jersey().register(new ComponentResource(server.getConfigurationStore(), server.getComponentRegistry()));
        environment.jersey().register(new PolicyResource(server.getPolicyStore(), server.getPolicyServer()));
        environment.jersey().register(new ScriptResource(server.getScriptStore()));
    }
}
