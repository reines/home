package com.furnaghan.home.component.meter.currentcost;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class CurrentcostConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private final String port;

    @JsonCreator
    public CurrentcostConfiguration(
            @JsonProperty("port") final String port) {
        this.port = port;
    }

    public String getPort() {
        return port;
    }
}
