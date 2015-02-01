package com.furnaghan.home.component.torrent.deluge.client.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UiState {

    @JsonProperty
    private final boolean connected;

    @JsonProperty
    private final Stats stats;

    @JsonProperty
    private final Map<String, DelugeTorrent> torrents;

    @JsonCreator
    public UiState(
            @JsonProperty("connected") boolean connected,
            @JsonProperty("stats") Stats stats,
            @JsonProperty("torrents") Map<String, DelugeTorrent> torrents) {
        this.connected = connected;
        this.stats = stats;
        this.torrents = torrents;
    }

    public boolean isConnected() {
        return connected;
    }

    public Stats getStats() {
        return stats;
    }

    public Map<String, DelugeTorrent> getTorrents() {
        return torrents;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("connected", connected)
                .add("stats", stats)
                .add("torrents", torrents)
                .toString();
    }
}
