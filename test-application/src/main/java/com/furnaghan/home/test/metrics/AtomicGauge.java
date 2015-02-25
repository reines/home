package com.furnaghan.home.test.metrics;

import com.codahale.metrics.Gauge;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicGauge<T> implements Gauge<T> {

    private final AtomicReference<T> delegate;

    public AtomicGauge() {
        this (null);
    }

    public AtomicGauge(final T value) {
        delegate = new AtomicReference<>(value);
    }

    public void set(final T value) {
        delegate.set(value);
    }

    @Override
    public T getValue() {
        return delegate.get();
    }
}
