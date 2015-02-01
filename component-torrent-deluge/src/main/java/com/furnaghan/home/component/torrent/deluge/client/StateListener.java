package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.model.DelugeTorrent;

public interface StateListener {
    void onTorrentAdded(String hash, DelugeTorrent torrent);

    void onTorrentRemoved(String hash, DelugeTorrent torrent);

    void onTorrentStateChanged(String hash, DelugeTorrent torrent);
}
