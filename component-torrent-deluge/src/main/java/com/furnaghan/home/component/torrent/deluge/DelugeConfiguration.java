package com.furnaghan.home.component.torrent.deluge;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class DelugeConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private HostAndPort address;

    @NotEmpty
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.seconds(5);

    public HostAndPort getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
