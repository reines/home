package com.furnaghan.home.component.feed.rss.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RssFeed {

    private final RssChannel channel;

    @JsonCreator
    public RssFeed(
            @JsonProperty("channel") final RssChannel channel) {
        this.channel = channel;
    }

    public RssChannel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("channel", channel)
                .toString();
    }
}
