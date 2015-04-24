package com.furnaghan.home.component.ping;

import com.google.common.base.MoreObjects;
import io.dropwizard.util.Duration;
import io.dropwizard.util.Size;

public class PingResult {

    public static final PingResult NO_RESPONSE = new PingResult(Duration.milliseconds(0), 0, Size.bytes(0));

    private final Duration time;
    private final int ttl;
    private final Size size;

    public PingResult(final Duration time, final int ttl, final Size size) {
        this.time = time;
        this.ttl = ttl;
        this.size = size;
    }

    public Duration getTime() {
        return time;
    }

    public int getTtl() {
        return ttl;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("time", time)
                .add("ttl", ttl)
                .add("size", size)
                .toString();
    }
}
