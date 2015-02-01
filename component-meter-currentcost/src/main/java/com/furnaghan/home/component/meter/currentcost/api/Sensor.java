package com.furnaghan.home.component.meter.currentcost.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sensor {

    private final int watts;

    @JsonCreator
    public Sensor(
            @JsonProperty("watts") final int watts) {
        this.watts = watts;
    }

    public int getWatts() {
        return watts;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("watts", watts)
                .toString();
    }
}
