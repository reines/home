package com.furnaghan.home.component.clock;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

import java.util.Date;

public interface ClockType extends ComponentType {
    public static interface Listener extends Component.Listener {
        void tick(final Date now);
    }
}
