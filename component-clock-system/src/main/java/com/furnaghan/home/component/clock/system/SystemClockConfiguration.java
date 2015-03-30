package com.furnaghan.home.component.clock.system;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;

public class SystemClockConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private Duration frequency = Duration.seconds(1);

    public Duration getFrequency() {
        return frequency;
    }
}
