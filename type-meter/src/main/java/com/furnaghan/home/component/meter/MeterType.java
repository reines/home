package com.furnaghan.home.component.meter;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface MeterType extends ComponentType {
    interface Listener extends Component.Listener {
        void receive(final String name, final double value);
    }
}
