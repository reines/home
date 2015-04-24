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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

// Works through the API, though would be nice to use: https://github.com/domoticz/domoticz/blob/master/hardware/evohome.cpp
public class EvohomeComponent extends Component<HeatingType.Listener> implements HeatingType {
    private static final Logger LOG = LoggerFactory.getLogger(EvohomeComponent.class);

    private final EvohomeClient client;

    public EvohomeComponent(final EvohomeConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);

        client = EvohomeClient.builder(JerseyClientFactory.build("evohome-" + configuration.getUsername(), configuration))
                .build(configuration.getUsername(), configuration.getPassword());
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
