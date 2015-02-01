package com.furnaghan.home.component.tv;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.tv.model.Source;

public interface TvType extends ComponentType {

    public static interface Listener extends Component.Listener {
    }

    void turnOn();

    void turnOff();

    void setSource(final Source source);
}
