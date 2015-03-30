package com.furnaghan.home.component.torrent;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

import java.net.URI;

public interface TorrentType extends ComponentType {

    public static interface Listener extends Component.Listener {
        void torrentAdded(final String hash);
        void torrentRemoved(final String hash);

        void torrentPaused(final String hash);
        void torrentResumed(final String hash);
        void torrentFinished(final String hash);
    }

    void download(final URI path);
}
