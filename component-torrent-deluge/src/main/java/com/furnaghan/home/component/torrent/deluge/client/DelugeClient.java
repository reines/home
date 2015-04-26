package com.furnaghan.home.component.torrent.deluge.client;

import com.furnaghan.home.component.torrent.deluge.client.api.Command;
import com.furnaghan.home.component.torrent.deluge.client.api.DelugeApiError;
import com.furnaghan.home.component.torrent.deluge.client.api.Result;
import com.furnaghan.home.component.torrent.deluge.client.api.commands.AddTorrentCommand;
import com.furnaghan.home.component.torrent.deluge.client.api.commands.DownloadTorrentCommand;
import com.furnaghan.home.component.torrent.deluge.client.api.commands.LoginCommand;
import com.furnaghan.home.component.torrent.deluge.client.api.commands.UpdateUiCommand;
import com.furnaghan.home.component.torrent.deluge.client.api.results.StringResult;
import com.furnaghan.home.component.torrent.deluge.client.api.results.UiStateResult;
import com.furnaghan.home.component.torrent.deluge.client.filter.JsonClientFilter;
import com.furnaghan.home.component.torrent.deluge.client.model.Stats;
import com.furnaghan.home.component.torrent.deluge.client.model.Torrent;
import com.furnaghan.home.component.torrent.deluge.client.model.UiState;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.net.HostAndPort;
import com.sun.jersey.api.client.Client;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.util.Duration;
import org.apache.http.client.utils.URIBuilder;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// http://deluge-torrent.org/docs/master/modules/ui/web/json_api.html
public class DelugeClient implements Managed {

    public static final int DEFAULT_PORT = 8112;

    private static URI createUri(final String scheme, final HostAndPort address, final String path) {
        try {
            return new URIBuilder()
                    .setScheme(scheme)
                    .setHost(address.getHostText())
                    .setPort(address.getPortOrDefault(DEFAULT_PORT))
                    .setPath(path)
                    .build();
        } catch (URISyntaxException e) {
            throw Throwables.propagate(e);
        }
    }

    private final Client client;
    private final URI root;
    private final Duration pollInterval;
    private final StateManager stateManager;
    private final ScheduledExecutorService executor;

    public DelugeClient(final Client client, final HostAndPort address, final Duration pollInterval) {
        this.client = client;
        this.root = createUri("http", address, "/json");
        this.pollInterval = pollInterval;

        // Fix the returned content type so we can parse correctly
        client.addFilter(new JsonClientFilter());

        stateManager = new StateManager(this);
        executor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void start() {
        stateManager.run();
        executor.scheduleWithFixedDelay(stateManager, pollInterval.getQuantity(), pollInterval.getQuantity(), pollInterval.getUnit());
    }

    public void addStateListener(final StateListener listener) {
        stateManager.addListener(listener);
    }

    @Override
    public void stop() {
        executor.shutdown();
    }

    private void sendCommand(final Command command) {
        sendCommand(command, Result.class);
    }

    @SuppressWarnings("unchecked")
    private <T, U extends Result> T sendCommand(final Command command, final Class<U> resultType) {
        final Result<T> result = client.resource(root)
                .type(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .post(resultType, command);

        final DelugeApiError error = result.getError();
        if (error != null) {
            throw error;
        }

        return result.getResult();
    }

    public void login(final String password) {
        sendCommand(new LoginCommand(password));
    }

    public String downloadTorrent(final URI torrent) {
        return sendCommand(new DownloadTorrentCommand(torrent), StringResult.class);
    }

    public void addTorrent(final String localPath) {
        sendCommand(new AddTorrentCommand(ImmutableList.of(localPath)));
    }

    public Map<HashCode, Torrent> getTorrents() {
        return stateManager.getTorrents();
    }

    public Stats getStats() {
        return stateManager.getStats();
    }

    protected UiState getUiState() {
        return sendCommand(new UpdateUiCommand(), UiStateResult.class);
    }
}
