package com.furnaghan.home.component.xbmc.client.types.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VolumeData {

    @JsonProperty
    private boolean muted;

    @JsonProperty
    private int volume;

    public boolean isMuted() {
        return muted;
    }

    public int getVolume() {
        return volume;
    }
}
