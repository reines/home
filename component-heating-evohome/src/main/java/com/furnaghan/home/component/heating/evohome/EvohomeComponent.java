package com.furnaghan.home.component.heating.evohome;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.heating.HeatingType;
import com.furnaghan.util.JerseyClientFactory;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.jamierf.evohome.EvohomeClient;
import com.jamierf.evohome.model.Device;
import com.jamierf.evohome.model.QuickAction;
import com.jamierf.evohome.model.Temperature;
import com.sun.jersey.api.client.Client;
import io.dropwizard.client.JerseyClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

public class EvohomeComponent extends Component<HeatingType.Listener> implements HeatingType {
    private static final Logger LOG = LoggerFactory.getLogger(EvohomeComponent.class);

    // Work around for https://bugs.openjdk.java.net/browse/JDK-8030936
    private static Client buildCompatibleJerseyClient(final String name, final JerseyClientConfiguration configuration) {
        final String httpsProtocols = System.getProperty("https.protocols");

        try {
            System.setProperty("https.protocols", "TLSv1");
            return JerseyClientFactory.build(name, configuration);
        } finally {
            if (httpsProtocols == null) {
                System.clearProperty("https.protocols");
            } else {
                System.setProperty("https.protocols", httpsProtocols);
            }
        }
    }

    private final EvohomeClient client;

    public EvohomeComponent(final EvohomeConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);

        client = new EvohomeClient(
                buildCompatibleJerseyClient("evohome-" + configuration.getUsername(), configuration),
                configuration.getUsername(),
                configuration.getPassword()
        );
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                final Collection<Device> devices = client.getDevices().values();
                LOG.trace("Received readings for {} devices", devices.size());

                devices.forEach((device) -> {
                    final String name = device.getName();
                    final double value = device.getTemperature().toCelsius();
                    trigger((listener) -> listener.receive(name, value));
                });
            } catch (RuntimeException e) {
                LOG.warn("Failed to poll devices", e);
            }
        }, 0, configuration.getPollInterval().toMilliseconds(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void setAutomatic() {
        try {
            client.setQuickAction(QuickAction.Auto, Optional.absent()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void turnOff() {
        try {
            client.setQuickAction(QuickAction.HeatingOff, Optional.absent()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void setTemperature(final String zone, final double temperature) {
        try {
            final Optional<Device> device = client.getDevice(zone);
            checkArgument(device.isPresent(), "Device not found");

            client.setTemperature(device.get(), Temperature.celsius(temperature), Optional.absent()).get();
        } catch (InterruptedException | ExecutionException e) {
            throw Throwables.propagate(e);
        }
    }
}
