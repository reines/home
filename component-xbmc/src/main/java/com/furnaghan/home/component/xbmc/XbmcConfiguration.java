package com.furnaghan.home.component.xbmc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.net.HostAndPort;
import io.dropwizard.client.JerseyClientConfiguration;

import javax.validation.constraints.NotNull;

public class XbmcConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private HostAndPort address;

    public HostAndPort getAddress() {
        return address;
    }
}
