package com.furnaghan.home.component.amp.onkyo;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.amp.AmpType;
import com.furnaghan.home.component.amp.model.Source;
import com.furnaghan.home.component.amp.onkyo.client.OnkyoClient;
import com.furnaghan.home.component.amp.onkyo.client.model.Input;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.google.common.net.HostAndPort;
import de.csmp.jeiscp.eiscp.EiscpCommmandsConstants;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

public class OnkyoComponent extends Component<AmpType.Listener> implements AmpType {
    private static final Map<Source, Input> SOURCE_MAP = ImmutableMap.of(
            Source.HDMI_1, Input.CBL_SAT,
            Source.HDMI_2, Input.GAME,
            Source.HDMI_3, Input.PC,
            Source.HDMI_4, Input.BD_DVD
    );

    private final OnkyoClient client;

    public OnkyoComponent(final OnkyoConfiguration configuration) {
        final Optional<HostAndPort> address = configuration.getAddress();
        client = address.isPresent() ? new OnkyoClient(address.get()) : OnkyoClient.autodiscover();
        client.addListener((command, value) -> {
            switch (command) {
                case EiscpCommmandsConstants.MASTER_VOLUME_ISCP: {
                    trigger((listener) -> listener.volumeChanged(value));
                    break;
                }

                case EiscpCommmandsConstants.SYSTEM_POWER_ISCP: {
                    trigger(value == 0x00 ? AmpType.Listener::turnedOff : AmpType.Listener::turnedOn);
                    break;
                }

                case EiscpCommmandsConstants.AUDIO_MUTING_ISCP: {
                    trigger((listener) -> listener.muteChanged(value == 0x01));
                    break;
                }
            }
        });
    }

    @Override
    public void turnOn() {
        client.sendCommand(EiscpCommmandsConstants.SYSTEM_POWER_ON_ISCP);
    }

    @Override
    public void turnOff() {
        client.sendCommand(EiscpCommmandsConstants.SYSTEM_POWER_STANDBY_ISCP);
    }

    @Override
    public void setMute(final boolean mute) {
        client.sendCommand(mute ? EiscpCommmandsConstants.AUDIO_MUTING_ON_ISCP : EiscpCommmandsConstants.AUDIO_MUTING_OFF_ISCP);
    }

    @Override
    public void setVolume(final int volume) {
        final String code = Integer.toHexString(volume);
        client.sendCommand(String.format("MVL%s", StringUtils.leftPad(code, 2, '0')));
    }

    @Override
    public void setSource(final Source source) {
        final Input input = SOURCE_MAP.get(source);
        if (input == null) {
            throw new UnsupportedOperationException(String.format("Amp does not support %s", source));
        }

        final String code = Integer.toHexString(input.getCode());
        client.sendCommand(String.format("SLI%s", StringUtils.leftPad(code, 2, '0')));
    }
}
