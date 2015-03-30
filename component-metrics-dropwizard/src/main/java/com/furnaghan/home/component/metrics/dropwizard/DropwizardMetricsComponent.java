package com.furnaghan.home.component.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.SharedMetricRegistries;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.metrics.MetricsType;
import com.furnaghan.home.util.AtomicGauge;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

import java.util.Map;

public class DropwizardMetricsComponent extends Component<MetricsType.Listener> implements MetricsType {

    private final MetricRegistry registry;
    private final Map<String, AtomicGauge<Double>> gauges;

    public DropwizardMetricsComponent(final DropwizardMetricsConfiguration configuration) {
        registry = SharedMetricRegistries.getOrCreate(configuration.getNamespace());

        gauges = CacheBuilder.<String, AtomicGauge<Double>>newBuilder().build(new CacheLoader<String, AtomicGauge<Double>>() {
            @Override
            public AtomicGauge<Double> load(final String name) {
                final AtomicGauge<Double> gauge = new AtomicGauge<>();
                return registry.register(name, gauge);
            }
        }).asMap();
    }

    public MetricSet getMetrics() {
        return registry;
    }

    @Override
    public void send(final String name, final double value) {
        gauges.get(name).set(value);
    }
}
