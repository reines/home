package com.furnaghan.home.component.xbmc.client.types.media.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Base extends com.furnaghan.home.component.xbmc.client.types.item.details.Base {

    @JsonProperty
    private String fanart;

    @JsonProperty
    private String thumbnail;

    public String getFanart() {
        return fanart;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
