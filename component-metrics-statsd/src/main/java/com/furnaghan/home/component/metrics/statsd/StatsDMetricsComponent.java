package com.furnaghan.home.component.metrics.statsd;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.metrics.MetricsType;
import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

public class StatsDMetricsComponent extends Component<MetricsType.Listener> implements MetricsType {

    private final StatsDClient statsd;

    public StatsDMetricsComponent(final StatsDMetricsConfiguration configuration) {
        statsd = new NonBlockingStatsDClient(configuration.getPrefix(), configuration.getAddress().getHostText(),
                configuration.getAddress().getPortOrDefault(StatsDMetricsConfiguration.DEFAULT_PORT));
    }

    @Override
    public void send(final String name, final double value) {
        statsd.gauge(name, value);
    }
}
