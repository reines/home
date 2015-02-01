package com.furnaghan.home.component.router.tomato;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.router.RouterType;
import com.furnaghan.home.component.router.model.Interface;
import com.furnaghan.home.component.router.model.MacAddress;
import com.furnaghan.home.component.router.tomato.client.TomatoClient;
import com.furnaghan.home.component.util.JerseyClientFactory;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TomatoComponent extends Component<RouterType.Listener> implements RouterType {

    private static final Logger LOG = LoggerFactory.getLogger(TomatoComponent.class);
    private static final String WAN_INTERFACE = "vlan2";

    private final TomatoClient client;
    private final Set<MacAddress> connectedDevices;

    public TomatoComponent(final TomatoConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);
        configuration.setGzipEnabled(false);

        client = new TomatoClient(
                JerseyClientFactory.build("tomato-" + configuration.getRoot(), configuration),
                configuration.getRoot(),
                configuration.getUsername(),
                configuration.getPassword()
        );
        connectedDevices = Sets.newHashSet();

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                this::refresh,
                0,
                configuration.getPollInterval().toSeconds(),
                TimeUnit.SECONDS
        );
    }

    private synchronized void refresh() {
        try {
            final Set<MacAddress> devices = client.getConnectedDevices();

            final Set<MacAddress> added = ImmutableSet.copyOf(Sets.difference(devices, connectedDevices));
            connectedDevices.addAll(added);
            added.forEach((device) -> {
                trigger((listener) -> listener.deviceConnected(device));
            });

            final Set<MacAddress> removed = ImmutableSet.copyOf(Sets.difference(connectedDevices, devices));
            connectedDevices.removeAll(removed);
            removed.forEach((device) -> {
                trigger((listener) -> listener.deviceDisconnected(device));
            });
        } catch (Exception e) {
            LOG.warn("Failed to refresh {}", client);
        }
    }

    @Override
    public Set<MacAddress> getConnectedDevices() {
        try {
            return client.getConnectedDevices();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Interface getWanInterface() {
        try {
            final Map<String, Interface> interfaces = client.getInterfaces();
            return interfaces.get(WAN_INTERFACE);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
