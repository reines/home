package com.furnaghan.home.test.config;

import com.google.common.base.Function;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ConfiguredBundleWrapper<T, U> implements ConfiguredBundle<T> {

    private final ConfiguredBundle<U> delegate;
    private final Function<T, U> configurationExtractor;

    public ConfiguredBundleWrapper(final ConfiguredBundle<U> delegate, final Function<T, U> configurationExtractor) {
        this.delegate = delegate;
        this.configurationExtractor = configurationExtractor;
    }

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {
        delegate.initialize(bootstrap);
    }

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        delegate.run(configurationExtractor.apply(configuration), environment);
    }
}
