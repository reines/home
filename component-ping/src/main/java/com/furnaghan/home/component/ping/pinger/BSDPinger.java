package com.furnaghan.home.component.ping.pinger;

import com.google.common.collect.ImmutableList;
import io.dropwizard.util.Duration;

import java.net.InetAddress;
import java.util.List;

public class BSDPinger extends UnixPinger {

    public BSDPinger(final Duration timeout) {
        super (timeout);
    }

    @Override
    protected List<String> command(final InetAddress address, final Duration timeout) {
        return ImmutableList.of(
                "ping",
                "-c", "1",
                "-t", String.valueOf(timeout.toSeconds()),
                address.getHostAddress()
        );
    }
}
