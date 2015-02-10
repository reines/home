package com.furnaghan.home.component.clock.system;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.clock.ClockType;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SystemClockComponent extends Component<ClockType.Listener> implements ClockType {

    public SystemClockComponent() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(this::tick, 0, 1, TimeUnit.SECONDS);
    }

    private void tick() {
        final Date now = new Date();
        trigger((listener) -> listener.tick(now));
    }
}
