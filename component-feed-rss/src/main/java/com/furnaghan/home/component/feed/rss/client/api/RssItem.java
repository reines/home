package com.furnaghan.home.component.feed.rss.client.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RssItem {

    private final LinkedHashMap<String, Object> fields;

    @JsonCreator
    public RssItem(final LinkedHashMap<String, Object> fields) {
        this.fields = fields;
    }

    public Map<String, Object> getFields() {
        return Collections.unmodifiableMap(fields);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RssItem rssItem = (RssItem) o;

        if (fields != null ? !fields.equals(rssItem.fields) : rssItem.fields != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return fields != null ? fields.hashCode() : 0;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("fields", fields)
                .toString();
    }
}
