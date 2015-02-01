package com.furnaghan.home.component.tv.bravia;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.tv.TvType;
import com.furnaghan.home.component.tv.model.Source;
import com.furnaghan.home.component.util.JerseyClientFactory;
import com.google.common.collect.ImmutableMap;
import com.jamierf.sony.bravia.client.BraviaClient;
import com.jamierf.sony.bravia.client.model.Command;

import java.util.Map;

public class BraviaComponent extends Component<TvType.Listener> implements TvType {

    private static final Map<Source, Command> SOURCE_MAP = ImmutableMap.of(
            Source.HDMI_1, Command.INPUT_HDMI_1,
            Source.HDMI_2, Command.INPUT_HDMI_2,
            Source.HDMI_3, Command.INPUT_HDMI_3,
            Source.HDMI_4, Command.INPUT_HDMI_4
    );

    private final BraviaClient client;

    public BraviaComponent(final BraviaConfiguration configuration) {
        client = new BraviaClient(
                JerseyClientFactory.build("bravia-" + configuration.getMacAddress(), configuration),
                configuration.getUrl(),
                configuration.getMacAddress()
        );
    }

    @Override
    public void turnOn() {
        client.turnOn();
    }

    @Override
    public void turnOff() {
        client.sendCommand(Command.POWER_OFF);
    }

    @Override
    public void setSource(final Source source) {
        final Command code = SOURCE_MAP.get(source);
        if (code == null) {
            throw new UnsupportedOperationException(String.format("TV does not support %s", source));
        }

        client.sendCommand(code);
    }
}
