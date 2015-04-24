package com.furnaghan.home.component.ping;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.meter.MeterType;
import com.furnaghan.home.component.ping.pinger.GNUPinger;
import com.furnaghan.home.component.ping.pinger.Pinger;

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
        this.pinger = new GNUPinger(configuration.getTimeout());

        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(this::ping, 0,
                configuration.getFrequency().getQuantity(), configuration.getFrequency().getUnit());
    }

    private void ping() {
        try {
            final PingResult result = pinger.ping(InetAddress.getByName(address));
            report(result);
            trigger(l -> l.success(result));
        } catch (Exception e) {
            report(PingResult.NO_RESPONSE);
            trigger(l -> l.failure(e));
        }
    }

    private void report(final PingResult result) {
        trigger(l -> l.receive("time", result.getTime().toMicroseconds() / 1000D));
    }
}
