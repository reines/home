package com.furnaghan.home.component.clock.system;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.util.Duration;

import javax.validation.constraints.NotNull;

public class SystemClockConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final Duration frequency;

    @JsonCreator
    public SystemClockConfiguration(
            @JsonProperty("frequency") final Duration frequency) {
        this.frequency = frequency;
    }

    public Duration getFrequency() {
        return frequency;
    }
}
