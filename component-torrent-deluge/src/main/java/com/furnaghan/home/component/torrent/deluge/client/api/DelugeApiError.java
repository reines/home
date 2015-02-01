package com.furnaghan.home.component.torrent.deluge.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DelugeApiError extends RuntimeException {

    private final int code;

    @JsonCreator
    public DelugeApiError(
            @JsonProperty("message") String message,
            @JsonProperty("code") int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
