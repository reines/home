package com.furnaghan.home.component.xbmc.client.types;

public class PlayRequest {

    private final String path;
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
