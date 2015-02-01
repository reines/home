package com.furnaghan.home.component.heating;

import com.furnaghan.home.component.meter.MeterType;

public interface HeatingType extends MeterType {
    public static interface Listener extends MeterType.Listener {
    }

    void setAutomatic();

    void turnOff();

    void setTemperature(final String zone, final float temperature);
}
