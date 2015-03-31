package com.furnaghan.home.component.notificationsource;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface NotificationSourceType extends ComponentType {
    interface Listener extends Component.Listener {
        void receive(final String title, final String message);
    }
}
