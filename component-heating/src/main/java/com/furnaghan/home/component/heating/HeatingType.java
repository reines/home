package com.furnaghan.home.component.heating;

import com.furnaghan.home.component.meter.MeterType;

public interface HeatingType extends MeterType {
    interface Listener extends MeterType.Listener {
    }

    void setAutomatic();
    void turnOff();
    void setTemperature(final String zone, final double temperature);
}
