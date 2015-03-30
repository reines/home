package com.furnaghan.home.component.amp.onkyo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;
import com.google.common.net.HostAndPort;

import javax.validation.constraints.NotNull;

public class OnkyoConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private Optional<HostAndPort> address = Optional.absent();

    public Optional<HostAndPort> getAddress() {
        return address;
    }
}
