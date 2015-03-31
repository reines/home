package com.furnaghan.home.component.printer;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface PrinterType extends ComponentType {
    interface Listener extends Component.Listener {
    }

    void print(final String message);
}
