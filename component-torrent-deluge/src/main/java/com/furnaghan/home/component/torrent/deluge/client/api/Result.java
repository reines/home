package com.furnaghan.home.component.torrent.deluge.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Result<T> {

    @JsonProperty
    private final int id;

    @JsonProperty
    private final T result;

    @JsonProperty
    private final DelugeApiError error;

    @JsonCreator
    public Result(
            @JsonProperty("id") int id,
            @JsonProperty("result") T result,
            @JsonProperty("error") DelugeApiError error) {
        this.id = id;
        this.result = result;
        this.error = error;
    }

    public int getId() {
        return id;
    }

    public T getResult() {
        return result;
    }

    public DelugeApiError getError() {
        return error;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("result", result)
                .add("error", error)
                .toString();
    }
}
