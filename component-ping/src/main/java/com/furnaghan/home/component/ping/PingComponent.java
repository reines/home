package com.furnaghan.home.component.ping;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.meter.MeterType;

import java.net.InetAddress;
import java.util.concurrent.Executors;

public class PingComponent extends Component<PingComponent.Listener> implements MeterType {

    public interface Listener extends MeterType.Listener {
        void success(final PingResult result);
        void failure(final Throwable cause);
    }

    private final String address;
    private final Pinger pinger;

    public PingComponent(final PingConfiguration configuration) {
        this.address = configuration.getAddress();
        this.pinger = new UnixPinger(configuration.getTimeout());

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this::ping, 0,
                configuration.getFrequency().getQuantity(), configuration.getFrequency().getUnit());
    }

    private void ping() {
        try {
            final PingResult result = pinger.ping(InetAddress.getByName(address));
            trigger(l -> l.receive("time", result.getTime().toMicroseconds() / 1000D));
            trigger(l -> l.success(result));
        } catch (Exception e) {
            trigger(l -> l.failure(e));
        }
    }
}
