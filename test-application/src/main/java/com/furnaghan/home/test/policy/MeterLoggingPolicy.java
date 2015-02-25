package com.furnaghan.home.test.policy;

import com.codahale.metrics.MetricRegistry;
import com.furnaghan.home.component.meter.MeterType;
import com.furnaghan.home.policy.Context;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.test.metrics.AtomicGauge;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.concurrent.ExecutionException;

import static com.codahale.metrics.MetricRegistry.name;

public class MeterLoggingPolicy implements Policy, MeterType.Listener {

    private static final Logger logger = LoggerFactory.getLogger(MeterLoggingPolicy.class);

    private final LoadingCache<String, AtomicGauge<Double>> metrics;

    public MeterLoggingPolicy(final MetricRegistry metrics) {
        this.metrics = CacheBuilder.newBuilder().build(new CacheLoader<String, AtomicGauge<Double>>() {
            @Override
            public AtomicGauge<Double> load(@Nullable final String key) throws Exception {
                return metrics.register(key, new AtomicGauge<>());
            }
        });
    }

    @Override
    public void receive(final String name, final double value) {
        final String component = Context.getName();
        try {
            metrics.get(name(component, name)).set(value);
        } catch (ExecutionException e) {
            logger.warn("Failed to update gauge: " + name, e);
        }
    }
}
