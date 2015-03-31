package com.furnaghan.home.component.router;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.router.model.Interface;
import com.furnaghan.home.component.router.model.MacAddress;

import java.util.Set;

public interface RouterType extends ComponentType {
    interface Listener extends Component.Listener {
        void deviceConnected(final MacAddress device);
        void deviceDisconnected(final MacAddress device);
    }

    Set<MacAddress> getConnectedDevices();
    Interface getWanInterface();
}
