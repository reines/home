package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;

public interface StateListener {
    void onTorrentAdded(String hash, Torrent torrent);

    void onTorrentRemoved(String hash, Torrent torrent);

    void onTorrentStateChanged(String hash, Torrent torrent);
}
