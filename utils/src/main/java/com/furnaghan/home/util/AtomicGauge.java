package com.furnaghan.home.util;

import com.codahale.metrics.Gauge;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicGauge<T> implements Gauge<T> {

    private final AtomicReference<T> reference = new AtomicReference<>();

    public void set(final T value) {
        reference.set(value);
    }

    @Override
    public T getValue() {
        return reference.get();
    }
}
