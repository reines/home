package com.furnaghan.home.component.clock.system;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.clock.ClockType;
import io.dropwizard.util.Duration;

import java.util.Date;
import java.util.concurrent.Executors;

public class SystemClockComponent extends Component<ClockType.Listener> implements ClockType {

    public SystemClockComponent(final SystemClockConfiguration configuration) {
        final Duration frequency = configuration.getFrequency();
        Executors.newSingleThreadScheduledExecutor()
                .scheduleAtFixedRate(this::tick, 0, frequency.getQuantity(), frequency.getUnit());
    }

    private void tick() {
        trigger((listener) -> listener.tick(getTime()));
    }

    @Override
    public Date getTime() {
        return new Date();
    }
}
