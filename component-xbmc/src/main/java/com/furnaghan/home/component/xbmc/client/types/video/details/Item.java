package com.furnaghan.home.component.xbmc.client.types.video.details;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Item extends Media {

    @JsonProperty("dateadded")
    private Date added;

    @JsonProperty
    private String file;

    @JsonProperty("lastplayed")
    private Date lastPlayed;

    @JsonProperty
    private String plot;

    public Date getAdded() {
        return added;
    }

    public String getFile() {
        return file;
    }

    public Date getLastPlayed() {
        return lastPlayed;
    }

    public String getPlot() {
        return plot;
    }
}
