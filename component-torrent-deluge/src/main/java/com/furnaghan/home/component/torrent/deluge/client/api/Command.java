package com.furnaghan.home.component.torrent.deluge.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Command {

    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    @JsonProperty
    private final String method;

    @JsonProperty
    private final List<?> params;

    @JsonProperty
    private final int id;

    public Command(String method, List<?> params) {
        this(method, params, COUNTER.getAndIncrement());
    }

    @JsonCreator
    public Command(
            @JsonProperty("method") String method,
            @JsonProperty("params") List<?> params,
            @JsonProperty("id") int id) {
        this.method = method;
        this.params = params;
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public List<?> getParams() {
        return params;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("method", method)
                .add("params", params)
                .add("id", id)
                .toString();
    }
}
