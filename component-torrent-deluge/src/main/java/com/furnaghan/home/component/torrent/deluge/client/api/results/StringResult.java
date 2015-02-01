package com.furnaghan.home.component.torrent.deluge.client.api.results;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.torrent.deluge.client.api.DelugeApiError;
import com.furnaghan.home.component.torrent.deluge.client.api.Result;

public class StringResult extends Result<String> {
    @JsonCreator
    public StringResult(
            @JsonProperty("id") int id,
            @JsonProperty("result") String result,
            @JsonProperty("error") DelugeApiError error) {
        super(id, result, error);
    }
}
