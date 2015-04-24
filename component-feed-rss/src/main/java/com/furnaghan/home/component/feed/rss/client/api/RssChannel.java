package com.furnaghan.home.component.feed.rss.client.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RssChannel {

    @JsonProperty
    private String title;

    @JsonProperty
    private String description;

    @JsonProperty("item")
    @JacksonXmlElementWrapper(useWrapping = false)
    private Set<RssItem> items;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Set<RssItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("description", description)
                .add("items", items)
                .toString();
    }
}
