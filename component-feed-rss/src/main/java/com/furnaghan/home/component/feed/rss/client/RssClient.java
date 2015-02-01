package com.furnaghan.home.component.feed.rss.client;

import com.furnaghan.home.component.feed.rss.client.api.RssChannel;
import com.furnaghan.home.component.feed.rss.client.api.RssFeed;
import com.furnaghan.home.component.feed.rss.client.api.RssItem;
import com.furnaghan.home.component.feed.rss.client.filter.SeenItemFilter;
import com.google.common.collect.Sets;
import com.sun.jersey.api.client.Client;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class RssClient {

    private static final int MAX_SEEN_ITEMS_HISTORY = 10_000;

    private final Client client;
    private final URI path;
    private final SeenItemFilter<RssItem> seenFilter;

    public RssClient(final Client client, final URI path) {
        this.client = client;
        this.path = path;

        seenFilter = new SeenItemFilter<>(MAX_SEEN_ITEMS_HISTORY);
    }

    public Set<RssItem> fetchNewItems() {
        // Fetch the feed
        final RssChannel rss = client.resource(path).get(RssFeed.class).getChannel();

        // Filter out seen items
        final Set<RssItem> items = Sets.newHashSet(rss.getItems());
        items.removeIf(seenFilter);

        return Collections.unmodifiableSet(items);
    }
}
