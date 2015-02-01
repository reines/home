package com.furnaghan.home.component.feed.rss;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.feed.FeedType;
import com.furnaghan.home.component.feed.rss.client.RssClient;
import com.furnaghan.home.component.util.JerseyClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RssComponent extends Component<FeedType.Listener> implements FeedType {

    private static final Logger LOG = LoggerFactory.getLogger(RssComponent.class);

    private final RssClient client;

    public RssComponent(final RssConfiguration configuration) {
        client = new RssClient(
                JerseyClientFactory.build("rss-" + configuration.getPath(), configuration),
                configuration.getPath()
        );

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                this::refresh,
                0,
                configuration.getPollInterval().toSeconds(),
                TimeUnit.SECONDS
        );
    }

    @Override
    public synchronized void refresh() {
        try {
            client.fetchNewItems().forEach((item) -> {
                trigger((listener) -> listener.newItem(item.getFields()));
            });
        } catch (Exception e) {
            LOG.warn("Failed to refresh feed", e);
        }
    }
}
