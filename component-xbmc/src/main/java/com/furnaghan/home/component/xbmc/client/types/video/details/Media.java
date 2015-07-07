package com.furnaghan.home.component.xbmc.client.types.video.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media extends Base {

    @JsonProperty
    private String title;

    public String getTitle() {
        return title;
    }
}
