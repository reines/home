package com.furnaghan.home.component.torrent;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.torrent.api.Torrent;
import com.google.common.hash.HashCode;

import java.net.URI;
import java.util.Map;

public interface TorrentType<T extends Torrent> extends ComponentType {
    interface Listener extends Component.Listener {
        void torrentAdded(final HashCode hash);
        void torrentRemoved(final HashCode hash);
        void torrentPaused(final HashCode hash);
        void torrentResumed(final HashCode hash);
        void torrentFinished(final HashCode hash);
    }

    Map<HashCode, T> getTorrents();
    void download(final URI path);
}
