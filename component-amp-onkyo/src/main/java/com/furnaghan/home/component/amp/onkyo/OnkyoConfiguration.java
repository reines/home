package com.furnaghan.home.component.amp.onkyo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;

import javax.validation.constraints.NotNull;

public class OnkyoConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final HostAndPort address;

    @JsonCreator
    public OnkyoConfiguration(
            @JsonProperty("address") HostAndPort address) {
        this.address = address;
    }

    public HostAndPort getAddress() {
        return address;
    }
}
