package com.furnaghan.home.component.modem.sky;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.modem.ModemType;
import com.furnaghan.home.component.modem.model.LineStats;
import com.furnaghan.home.component.modem.sky.client.SkyClient;
import com.furnaghan.home.component.util.JerseyClientFactory;

public class SkyModemComponent extends Component<ModemType.Listener> implements ModemType {
    private final SkyClient client;

    public SkyModemComponent(final SkyModemConfiguration configuration) {
        configuration.setGzipEnabledForRequests(false);
        configuration.setGzipEnabled(false);

        client = new SkyClient(
                JerseyClientFactory.build("sky-" + configuration.getRoot(), configuration),
                configuration.getRoot(),
                configuration.getUsername(),
                configuration.getPassword()
        );
    }

    @Override
    public LineStats getLineStats() {
        return client.getLineStats();
    }
}
