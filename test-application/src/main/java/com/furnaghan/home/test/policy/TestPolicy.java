package com.furnaghan.home.test.policy;

import com.furnaghan.home.component.clock.ClockType;
import com.furnaghan.home.component.storage.StorageType;
import com.furnaghan.home.policy.Context;
import com.furnaghan.home.policy.Policy;
import com.google.common.base.Optional;

import java.util.Collection;
import java.util.Date;

public class TestPolicy implements Policy, ClockType.Listener, StorageType.Listener {
    @Override
    public void tick(final Date now) {
        final ClockType clock = Context.getComponent();
        System.err.println(clock + " -> tick: " + now);

        final Optional<StorageType> localStorage = Context.findByName("test-storage1");
        System.err.println(localStorage);

        final Collection<StorageType> localStorages = Context.findByType(StorageType.class);
        System.err.println(localStorages);
    }
}
