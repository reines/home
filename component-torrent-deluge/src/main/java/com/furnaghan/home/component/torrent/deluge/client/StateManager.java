package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.model.Stats;
import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;
import com.furnaghan.home.component.torrent.deluge.client.model.UiState;
import com.furnaghan.home.util.Listenable;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class StateManager extends Listenable<StateListener> implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DelugeClient.class);

    private final DelugeClient client;

    private Stats stats;
    private Map<HashCode, Torrent> torrents;

    public StateManager(DelugeClient client) {
        this.client = client;

        stats = Stats.DEFAULT;
        torrents = Collections.emptyMap();
    }

    @Override
    public synchronized void run() {
        try {
            final UiState latestState = client.getUiState();

            LOG.trace("Received server state update, connected={}", latestState.isConnected());

            // Torrents which have been added
            final Set<HashCode> added = Sets.difference(latestState.getTorrents().keySet(), torrents.keySet());
            for (HashCode hash : added) {
                onTorrentAdded(hash, latestState.getTorrents().get(hash));
            }

            // Torrents which have been updated
            final Set<HashCode> updated = Sets.intersection(latestState.getTorrents().keySet(), torrents.keySet());
            for (HashCode hash : updated) {
                onTorrentUpdated(hash, torrents.get(hash), latestState.getTorrents().get(hash));
            }

            // Torrents which have been removed
            final Set<HashCode> removed = Sets.difference(torrents.keySet(), latestState.getTorrents().keySet());
            for (HashCode hash : removed) {
                onTorrentRemoved(hash, torrents.get(hash));
            }

            // Update the stored state
            stats = latestState.getStats();
            torrents = latestState.getTorrents();
        } catch (Exception e) {
            LOG.warn("Error fetching server state", e);
        }
    }

    public Stats getStats() {
        return stats;
    }

    public Map<HashCode, Torrent> getTorrents() {
        return Collections.unmodifiableMap(torrents);
    }

    private synchronized void onTorrentAdded(HashCode hash, Torrent torrent) {
        LOG.trace("Torrent {} added", torrent);

        // It was added
        trigger(l -> l.onTorrentAdded(hash, torrent));

        // The state was changed
        onTorrentStateChanged(hash, torrent);
    }

    private synchronized void onTorrentUpdated(HashCode hash, Torrent previous, Torrent updated) {
        LOG.trace("Torrent {} updated", updated);

        // The state was changed
        if (!Objects.equals(previous.getState(), updated.getState())) {
            onTorrentStateChanged(hash, updated);
        }
    }

    private synchronized void onTorrentStateChanged(HashCode hash, Torrent torrent) {
        LOG.trace("Torrent {} state changed", torrent);

        trigger(l -> l.onTorrentStateChanged(hash, torrent));
    }

    private synchronized void onTorrentRemoved(HashCode hash, Torrent torrent) {
        LOG.trace("Torrent {} removed", torrent);

        // It was removed
        trigger(l -> l.onTorrentRemoved(hash, torrent));
    }
}
