package com.furnaghan.home.component.ping;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class PingConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String address;

    @NotNull
    @JsonProperty
    private Duration frequency = Duration.seconds(3);

    @NotNull
    @JsonProperty
    private Duration timeout = Duration.seconds(1);

    public String getAddress() {
        return address;
    }

    public Duration getFrequency() {
        return frequency;
    }

    public Duration getTimeout() {
        return timeout;
    }
}
