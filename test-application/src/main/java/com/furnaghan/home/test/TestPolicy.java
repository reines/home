package com.furnaghan.home.test;

import com.furnaghan.home.component.clock.ClockType;
import com.furnaghan.home.component.storage.StorageType;
import com.furnaghan.home.policy.Policy;

import java.util.Date;

public class TestPolicy implements Policy, ClockType.Listener, StorageType.Listener {
    @Override
    public void tick(final Date now) {
        System.err.println("tick: " + now);
    }
}
