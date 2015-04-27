package com.furnaghan.home.component.torrent.api;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum State {
    QUEUED, DOWNLOADING, PAUSED, SEEDING, ERROR;

    @JsonCreator
    public static State fromString(final String input) {
        return State.valueOf(input.toUpperCase());
    }
}
