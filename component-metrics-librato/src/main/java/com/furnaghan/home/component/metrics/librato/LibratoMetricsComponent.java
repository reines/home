package com.furnaghan.home.component.metrics.librato;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.metrics.MetricsType;
import com.librato.metrics.HttpPoster;
import com.librato.metrics.LibratoBatch;
import com.librato.metrics.NingHttpPoster;
import com.librato.metrics.Sanitizer;
import io.dropwizard.util.Duration;

public class LibratoMetricsComponent extends Component<MetricsType.Listener> implements MetricsType {

    private final String source;
    private final int batchSize;
    private final Duration timeout;
    private final String userAgent;
    private final HttpPoster httpPoster;

    public LibratoMetricsComponent(final LibratoMetricsConfiguration configuration) {
        source = configuration.getSource().orNull();
        batchSize = configuration.getBatchSize();
        timeout = configuration.getTimeout();
        userAgent = configuration.getUserAgent();

        httpPoster = NingHttpPoster.newPoster(
                configuration.getUsername(),
                configuration.getApiToken(),
                configuration.getApiUrl()
        );
    }

    private LibratoBatch batch() {
        return new LibratoBatch( batchSize, Sanitizer.LAST_PASS, timeout.getQuantity(),
                timeout.getUnit(), userAgent, httpPoster );
    }

    @Override
    public void send(final String name, final double value) {
        final LibratoBatch batch = batch();
        batch.addGaugeMeasurement(name, value);
        batch.post(source, System.currentTimeMillis() / 1000);
    }
}
