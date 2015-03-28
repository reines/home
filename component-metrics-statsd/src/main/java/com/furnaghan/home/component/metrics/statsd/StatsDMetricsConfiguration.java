package com.furnaghan.home.component.metrics.statsd;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;

import javax.validation.constraints.NotNull;

public class StatsDMetricsConfiguration implements Configuration {

    public static final int DEFAULT_PORT = 8125;

    @NotNull
    @JsonProperty
    private final String prefix;

    @NotNull
    @JsonProperty
    private final HostAndPort address;

    @JsonCreator
    public StatsDMetricsConfiguration(
            @JsonProperty("prefix") final String prefix,
            @JsonProperty("address") final HostAndPort address) {
        this.prefix = prefix;
        this.address = address;
    }

    public String getPrefix() {
        return prefix;
    }

    public HostAndPort getAddress() {
        return address;
    }
}
