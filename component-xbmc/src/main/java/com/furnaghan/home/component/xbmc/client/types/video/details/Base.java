package com.furnaghan.home.component.xbmc.client.types.video.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.xbmc.client.types.media.details.Artwork;

public class Base extends com.furnaghan.home.component.xbmc.client.types.media.details.Base {

    @JsonProperty
    private Artwork art;

    @JsonProperty("playcount")
    private int playCount;

    public Artwork getArt() {
        return art;
    }

    public int getPlayCount() {
        return playCount;
    }
}
