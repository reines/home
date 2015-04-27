package com.furnaghan.home.component.torrent.deluge;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.torrent.TorrentType;
import com.furnaghan.home.component.torrent.deluge.client.DelugeClient;
import com.furnaghan.home.component.torrent.deluge.client.StateListener;
import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;
import com.furnaghan.util.JerseyClientFactory;
import com.google.common.hash.HashCode;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class DelugeComponent extends Component<TorrentType.Listener> implements TorrentType<Torrent> {

    private final DelugeClient client;

    public DelugeComponent(final DelugeConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);
        configuration.setCookiesEnabled(true);

        client = new DelugeClient(
                JerseyClientFactory.build("deluge-" + configuration.getAddress(), configuration),
                configuration.getAddress(),
                configuration.getPollInterval()
        );
        client.addStateListener(new StateListener() {
            @Override
            public void onTorrentAdded(final HashCode hash, final Torrent torrent) {
                trigger((listener) -> listener.torrentAdded(hash));
            }

            @Override
            public void onTorrentRemoved(final HashCode hash, final Torrent torrent) {
                trigger((listener) -> listener.torrentRemoved(hash));
            }

            @Override
            public void onTorrentStateChanged(final HashCode hash, final Torrent torrent) {
                switch (torrent.getState()) {
                    case PAUSED:
                        trigger((listener) -> listener.torrentPaused(hash));
                        break;
                    case SEEDING:
                        trigger((listener) -> listener.torrentFinished(hash));
                        break;
                    case DOWNLOADING:
                        trigger((listener) -> listener.torrentResumed(hash));
                        break;
                }
            }
        });
        client.login(configuration.getPassword());
        client.start();
    }

    @Override
    public Map<HashCode, Torrent> getTorrents() {
        return Collections.unmodifiableMap(client.getTorrents());
    }

    @Override
    public void download(final URI path) {
        client.downloadTorrent(path);
    }
}
