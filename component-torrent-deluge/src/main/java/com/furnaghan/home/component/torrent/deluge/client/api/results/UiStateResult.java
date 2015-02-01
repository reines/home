package com.furnaghan.home.component.torrent.deluge.client.api.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.torrent.deluge.client.api.DelugeApiError;
import com.furnaghan.home.component.torrent.deluge.client.api.Result;
import com.furnaghan.home.component.torrent.deluge.client.model.UiState;

public class UiStateResult extends Result<UiState> {

    public UiStateResult(
            @JsonProperty("id") int id,
            @JsonProperty("result") UiState result,
            @JsonProperty("error") DelugeApiError error) {
        super(id, result, error);
    }
}
