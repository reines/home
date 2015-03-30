package com.furnaghan.home.component.metrics.statsd;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;

import javax.validation.constraints.NotNull;

public class StatsDMetricsConfiguration implements Configuration {

    public static final int DEFAULT_PORT = 8125;

    @NotNull
    @JsonProperty
    private String prefix = "";

    @NotNull
    @JsonProperty
    private HostAndPort address;

    public String getPrefix() {
        return prefix;
    }

    public HostAndPort getAddress() {
        return address;
    }
}
