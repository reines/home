package com.furnaghan.home.component.meter.currentcost.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Reading {

    private final float temperature;
    private final Optional<Sensor> channel1;
    private final Optional<Sensor> channel2;
    private final Optional<Sensor> channel3;

    @JsonCreator
    public Reading(
            @JsonProperty("tmpr") final float temperature,
            @JsonProperty("ch1") final Optional<Sensor> channel1,
            @JsonProperty("ch2") final Optional<Sensor> channel2,
            @JsonProperty("ch3") final Optional<Sensor> channel3) {
        this.temperature = temperature;
        this.channel1 = channel1;
        this.channel2 = channel2;
        this.channel3 = channel3;
    }

    public float getTemperature() {
        return temperature;
    }

    public Optional<Sensor> getChannel1() {
        return channel1;
    }

    public Optional<Sensor> getChannel2() {
        return channel2;
    }

    public Optional<Sensor> getChannel3() {
        return channel3;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("temperature", temperature)
                .add("channel1", channel1)
                .add("channel2", channel2)
                .add("channel3", channel3)
                .toString();
    }
}
