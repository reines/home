package com.furnaghan.home.component.xbmc.client;

import com.furnaghan.home.component.xbmc.client.methods.remote.*;
import com.furnaghan.home.component.xbmc.client.types.api.jsonrpc.Version;
import com.google.common.net.HostAndPort;
import com.jamierf.jsonrpc.JsonRpcClient;
import com.jamierf.jsonrpc.codec.jackson.JacksonCodecFactory;
import com.jamierf.jsonrpc.transport.socket.SocketTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class XbmcClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(XbmcClient.class);

    private final JsonRpcClient rpc;

    // Remote Interfaces
    private final JSONRPC jsonrpc;
    private final Application application;
    private final GUI gui;
    private final Player player;
    private final VideoLibrary videoLibrary;

    private final Version version;

    public XbmcClient(final HostAndPort address) throws IOException {
        rpc = JsonRpcClient.builder(
                SocketTransport.withAddress(address).build(),
                new JacksonCodecFactory()
        ).build();

        jsonrpc = rpc.proxy("JSONRPC", JSONRPC.class);
        application = rpc.proxy("Application", Application.class);
        gui = rpc.proxy("GUI", GUI.class);
        player = rpc.proxy("Player", Player.class);
        videoLibrary = rpc.proxy("VideoLibrary", VideoLibrary.class);

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

    public JSONRPC jsonRpc() {
        return jsonrpc;
    }

    public Application application() {
        return application;
    }

    public GUI gui() {
        return gui;
    }

    public Player player() {
        return player;
    }

    public VideoLibrary videoLibrary() {
        return videoLibrary;
    }
}
