package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.model.DelugeTorrent;
import com.furnaghan.home.component.torrent.deluge.client.model.Stats;
import com.furnaghan.home.component.torrent.deluge.client.model.UiState;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class StateManager implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(DelugeClient.class);

    private final DelugeClient client;
    private final Collection<StateListener> listeners;

    private Stats stats;
    private Map<String, DelugeTorrent> torrents;

    public StateManager(DelugeClient client) {
        this.client = client;

        listeners = Lists.newLinkedList();

        stats = Stats.DEFAULT;
        torrents = Collections.emptyMap();
    }

    public synchronized void addListener(StateListener listener) {
        listeners.add(listener);
    }

    @Override
    public void run() {
        try {
            final UiState latestState = client.getUiState();

            LOG.trace("Received server state update, connected={}", latestState.isConnected());

            // Torrents which have been added
            final Set<String> added = Sets.difference(latestState.getTorrents().keySet(), torrents.keySet());
            for (String hash : added) {
                onTorrentAdded(hash, latestState.getTorrents().get(hash));
            }

            // Torrents which have been updated
            final Set<String> updated = Sets.intersection(latestState.getTorrents().keySet(), torrents.keySet());
            for (String hash : updated) {
                onTorrentUpdated(hash, torrents.get(hash), latestState.getTorrents().get(hash));
            }

            // Torrents which have been removed
            final Set<String> removed = Sets.difference(torrents.keySet(), latestState.getTorrents().keySet());
            for (String hash : removed) {
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

    public Map<String, DelugeTorrent> getTorrents() {
        return Collections.unmodifiableMap(torrents);
    }

    private synchronized void onTorrentAdded(String hash, DelugeTorrent torrent) {
        LOG.trace("Torrent {} added", torrent);

        // It was added
        for (StateListener listener : listeners) {
            listener.onTorrentAdded(hash, torrent);
        }

        // The state was changed
        onTorrentStateChanged(hash, torrent);
    }

    private synchronized void onTorrentUpdated(String hash, DelugeTorrent previous, DelugeTorrent updated) {
        LOG.trace("Torrent {} updated", updated);

        // The state was changed
        if (!Objects.equals(previous.getState(), updated.getState())) {
            onTorrentStateChanged(hash, updated);
        }
    }

    private synchronized void onTorrentStateChanged(String hash, DelugeTorrent torrent) {
        LOG.trace("Torrent {} state changed", torrent);

        for (StateListener listener : listeners) {
            listener.onTorrentStateChanged(hash, torrent);
        }
    }

    private synchronized void onTorrentRemoved(String hash, DelugeTorrent torrent) {
        LOG.trace("Torrent {} removed", torrent);

        // It was removed
        for (StateListener listener : listeners) {
            listener.onTorrentRemoved(hash, torrent);
        }
    }
}
