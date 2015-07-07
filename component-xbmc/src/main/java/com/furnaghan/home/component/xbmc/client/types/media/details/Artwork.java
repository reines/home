package com.furnaghan.home.component.xbmc.client.types.media.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artwork {

    @JsonProperty
    private String banner;

    @JsonProperty
    private String poster;

    @JsonProperty
    private String fanart;

    @JsonProperty("thumb")
    private String thumbnail;

    public String getBanner() {
        return banner;
    }

    public String getPoster() {
        return poster;
    }

    public String getFanart() {
        return fanart;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
