package com.furnaghan.home.component.torrent.deluge;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class DelugeConfiguration extends JerseyClientConfiguration {

    @NotNull
    @JsonProperty
    private final URI root;

    @NotNull
    @JsonProperty
    private final Duration pollInterval;

    @JsonCreator
    public DelugeConfiguration(
            @JsonProperty("root") final URI root,
            @JsonProperty("pollInterval") final Duration pollInterval) {
        this.root = root;
        this.pollInterval = pollInterval;
    }

    public URI getRoot() {
        return root;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
