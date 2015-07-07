package com.furnaghan.home.component.xbmc.client.types.api.videolibrary;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.xbmc.client.types.list.LimitsReturned;
import com.furnaghan.home.component.xbmc.client.types.video.details.Episode;

import java.util.List;

public class Episodes {

    @JsonProperty
    private List<Episode> episodes;

    @JsonProperty
    private LimitsReturned limits;

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public LimitsReturned getLimits() {
        return limits;
    }
}
