package com.furnaghan.home.component.meter;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface MeterType extends ComponentType {
    public static interface Listener extends Component.Listener {
        void receive(final String name, final float value);
    }
}
