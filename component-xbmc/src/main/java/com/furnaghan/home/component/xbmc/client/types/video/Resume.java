package com.furnaghan.home.component.xbmc.client.types.video;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Resume {

    @JsonProperty
    private int number;

    @JsonProperty
    private int total;

    public int getNumber() {
        return number;
    }

    public int getTotal() {
        return total;
    }
}
