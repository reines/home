package com.furnaghan.home.component.ping.pinger;

import com.furnaghan.home.component.ping.PingResult;

import java.io.IOException;
import java.net.InetAddress;

public interface Pinger {
    PingResult ping(final InetAddress address) throws IOException, InterruptedException;
}
