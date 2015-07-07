package com.furnaghan.home.component.xbmc.client.types.list;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LimitsReturned {

    @JsonProperty
    private int end;

    @JsonProperty
    private int total;

    @JsonProperty
    private int start;

    public int getEnd() {
        return end;
    }

    public int getTotal() {
        return total;
    }

    public int getStart() {
        return start;
    }
}
