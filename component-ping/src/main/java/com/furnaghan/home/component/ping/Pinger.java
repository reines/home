package com.furnaghan.home.component.ping;

import java.io.IOException;
import java.net.InetAddress;

public interface Pinger {
    PingResult ping(final InetAddress address) throws IOException, InterruptedException;
}
