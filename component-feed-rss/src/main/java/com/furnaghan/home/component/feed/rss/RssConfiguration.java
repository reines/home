package com.furnaghan.home.component.feed.rss;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class RssConfiguration extends JerseyClientConfiguration {

    @NotNull
    @JsonProperty
    private final URI path;

    @NotNull
    @JsonProperty
    private final Duration pollInterval;

    @JsonCreator
    public RssConfiguration(
            @JsonProperty("path") final URI path,
            @JsonProperty("pollInterval") final Duration pollInterval) {
        this.path = path;
        this.pollInterval = pollInterval;
    }

    public URI getPath() {
        return path;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
