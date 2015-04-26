package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;
import com.google.common.hash.HashCode;

public interface StateListener {
    void onTorrentAdded(HashCode hash, Torrent torrent);
    void onTorrentRemoved(HashCode hash, Torrent torrent);
    void onTorrentStateChanged(HashCode hash, Torrent torrent);
}
