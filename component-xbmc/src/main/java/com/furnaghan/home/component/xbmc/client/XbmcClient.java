package com.furnaghan.home.component.xbmc.client;

import com.furnaghan.home.component.xbmc.client.methods.remote.Application;
import com.furnaghan.home.component.xbmc.client.methods.remote.GUI;
import com.furnaghan.home.component.xbmc.client.methods.remote.JSONRPC;
import com.furnaghan.home.component.xbmc.client.methods.remote.Player;
import com.furnaghan.home.component.xbmc.client.types.Version;
import com.google.common.net.HostAndPort;
import com.jamierf.jsonrpc.JsonRpcServer;
import com.jamierf.jsonrpc.SocketTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XbmcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(XbmcClient.class);

    private final JsonRpcServer rpc;

    // Remote Interfaces
    private final JSONRPC jsonrpc;
    private final Application application;
    private final GUI gui;
    private final Player player;

    private final Version version;

    public XbmcClient(final HostAndPort address) throws IOException {
        rpc = new JsonRpcServer(new SocketTransport(address));

        jsonrpc = rpc.proxy("JSONRPC", JSONRPC.class);
        application = rpc.proxy("Application", Application.class);
        gui = rpc.proxy("GUI", GUI.class);
        player = rpc.proxy("Player", Player.class);

        version = jsonrpc.Version().get("version");
        LOGGER.info("Connected to XBMC APIv{}", version);
    }

    public Version getApiVersion() {
        return version;
    }

    public <T> void register(final T instance, final Class<T> type) {
        final String namespace = type.getSimpleName();
        LOGGER.info("Registering {} with namespace {}", instance, namespace);
        rpc.register(namespace, instance, type);
    }

    public JSONRPC JSONRPC() {
        return jsonrpc;
    }

    public Application Application() {
        return application;
    }

    public GUI GUI() {
        return gui;
    }

    public Player Player() {
        return player;
    }
}
