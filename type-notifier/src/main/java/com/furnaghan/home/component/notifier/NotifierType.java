package com.furnaghan.home.component.notifier;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface NotifierType extends ComponentType {
    interface Listener extends Component.Listener {}

    void send(final String title, final String message);
}
