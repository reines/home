package com.furnaghan.home.component.metrics;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface MetricsType extends ComponentType {

    interface Listener extends Component.Listener {
    }

    void send(final String name, final double value);
    void mark(final String name);
}
