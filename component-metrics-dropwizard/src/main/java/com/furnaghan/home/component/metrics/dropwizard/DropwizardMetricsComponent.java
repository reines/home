package com.furnaghan.home.component.metrics.dropwizard;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.SharedMetricRegistries;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.metrics.MetricsType;
import com.furnaghan.home.util.AtomicGauge;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentMap;

public class DropwizardMetricsComponent extends Component<MetricsType.Listener> implements MetricsType {

    private final MetricRegistry registry;
    private final ConcurrentMap<String, AtomicGauge<Double>> gauges;

    public DropwizardMetricsComponent(final DropwizardMetricsConfiguration configuration) {
        registry = SharedMetricRegistries.getOrCreate(configuration.getNamespace());
        gauges = Maps.newConcurrentMap();
    }

    public MetricSet getMetrics() {
        return registry;
    }

    @Override
    public void send(final String name, final double value) {
        gauges.computeIfAbsent(name, key -> registry.register(key, new AtomicGauge<>())).set(value);
    }
}
