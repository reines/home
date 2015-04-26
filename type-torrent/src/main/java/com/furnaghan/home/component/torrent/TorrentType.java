package com.furnaghan.home.component.torrent;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.google.common.hash.HashCode;

import java.net.URI;
import java.util.Set;

public interface TorrentType extends ComponentType {
    interface Listener extends Component.Listener {
        void torrentAdded(final HashCode hash);
        void torrentRemoved(final HashCode hash);
        void torrentPaused(final HashCode hash);
        void torrentResumed(final HashCode hash);
        void torrentFinished(final HashCode hash);
    }

    Set<HashCode> getTorrents();
    void download(final URI path);
}
