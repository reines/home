package com.furnaghan.home.component.torrent.deluge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class DelugeConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private URI root;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.seconds(5);

    public URI getRoot() {
        return root;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
