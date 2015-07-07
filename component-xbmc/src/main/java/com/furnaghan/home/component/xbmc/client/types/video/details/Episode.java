package com.furnaghan.home.component.xbmc.client.types.video.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Episode extends File {

    @JsonProperty
    private int episode;

    @JsonProperty("showtitle")
    private String showTitle;

    @JsonProperty
    private int season;

    public int getEpisode() {
        return episode;
    }

    public String getShowTitle() {
        return showTitle;
    }

    public int getSeason() {
        return season;
    }
}
