package com.furnaghan.home.component.xbmc.client.types.list;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Sort {

    @JsonProperty
    private final String order;

    @JsonProperty("ignorearticle")
    private final boolean ignoreArticle;

    @JsonProperty
    private final String method;

    public Sort(final String method, final boolean ignoreArticle, final String order) {
        this.method = method;
        this.ignoreArticle = ignoreArticle;
        this.order = order;
    }

    public String getOrder() {
        return order;
    }

    public boolean isIgnoreArticle() {
        return ignoreArticle;
    }

    public String getMethod() {
        return method;
    }
}
