package com.furnaghan.home.component.xbmc.client.types.item.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Base {

    @JsonProperty
    private String label;

    public String getLabel() {
        return label;
    }
}
