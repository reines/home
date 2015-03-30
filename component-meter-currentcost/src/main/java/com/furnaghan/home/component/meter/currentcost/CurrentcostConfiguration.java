package com.furnaghan.home.component.meter.currentcost;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class CurrentcostConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String port = "/dev/ttyUSB0";

    public String getPort() {
        return port;
    }
}
