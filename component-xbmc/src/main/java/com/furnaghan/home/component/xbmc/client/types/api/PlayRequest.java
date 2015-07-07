package com.furnaghan.home.component.xbmc.client.types.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayRequest {

    @JsonProperty
    private final String path;

    @JsonProperty
    private final boolean recursive;

    public PlayRequest(final String path, final boolean recursive) {
        this.path = path;
        this.recursive = recursive;
    }

    public String getPath() {
        return path;
    }

    public boolean isRecursive() {
        return recursive;
    }
}
