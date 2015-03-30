package com.furnaghan.home.component.feed.rss;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class RssConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private URI path;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.minutes(15);

    public URI getPath() {
        return path;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
