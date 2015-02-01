package com.furnaghan.home.component.amp.onkyo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.net.HostAndPort;

import javax.validation.constraints.NotNull;

public class OnkyoConfiguration {

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
