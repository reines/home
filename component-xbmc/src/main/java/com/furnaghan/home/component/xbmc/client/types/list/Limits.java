package com.furnaghan.home.component.xbmc.client.types.list;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Limits {

    private final int start;
    private final int end;

    @JsonCreator
    public Limits(
            @JsonProperty("start") final int start,
            @JsonProperty("end") final int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }
}
