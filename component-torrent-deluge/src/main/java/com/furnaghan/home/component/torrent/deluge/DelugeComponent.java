package com.furnaghan.home.component.torrent.deluge;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.torrent.TorrentType;
import com.furnaghan.home.component.torrent.deluge.client.DelugeClient;
import com.furnaghan.home.component.torrent.deluge.client.StateListener;
import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;
import com.furnaghan.util.JerseyClientFactory;

import java.net.URI;

public class DelugeComponent extends Component<TorrentType.Listener> implements TorrentType {

    private final DelugeClient client;

    public DelugeComponent(final DelugeConfiguration configuration) {
        client = new DelugeClient(
                JerseyClientFactory.build("deluge-" + configuration.getRoot(), configuration),
                configuration.getRoot(),
                configuration.getPollInterval()
        );
        client.addStateListener(new StateListener() {
            @Override
            public void onTorrentAdded(final String hash, final Torrent torrent) {
                trigger((listener) -> listener.torrentAdded(hash));
            }

            @Override
            public void onTorrentRemoved(final String hash, final Torrent torrent) {
                trigger((listener) -> listener.torrentRemoved(hash));
            }

            @Override
            public void onTorrentStateChanged(final String hash, final Torrent torrent) {
                switch (torrent.getState()) {
                    case Paused:
                        trigger((listener) -> listener.torrentPaused(hash));
                        break;
                    case Seeding:
                        trigger((listener) -> listener.torrentFinished(hash));
                        break;
                    case Downloading:
                        trigger((listener) -> listener.torrentResumed(hash));
                        break;
                }
            }
        });
    }

    @Override
    public void download(final URI path) {
        client.downloadTorrent(path);
    }
}
