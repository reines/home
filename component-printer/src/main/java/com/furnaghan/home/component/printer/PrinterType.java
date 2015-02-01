package com.furnaghan.home.component.printer;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;

public interface PrinterType extends ComponentType {
    public static interface Listener extends Component.Listener {
    }

    void print(final String message);
}
