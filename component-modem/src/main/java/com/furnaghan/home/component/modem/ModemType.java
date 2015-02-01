package com.furnaghan.home.component.modem;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.modem.model.LineStats;

public interface ModemType extends ComponentType {
    public static interface Listener extends Component.Listener {
    }

    LineStats getLineStats();
}
