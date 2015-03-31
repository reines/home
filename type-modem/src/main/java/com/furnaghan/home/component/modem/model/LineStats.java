package com.furnaghan.home.component.modem.model;

import com.furnaghan.home.util.Speed;
import com.google.common.base.Objects;

public class LineStats {
    private final Speed downstreamSpeed;
    private final double downstreamAttenuation;
    private final double downstreamNoiseMargin;
    private final Speed upstreamSpeed;
    private final double upstreamAttenuation;
    private final double upstreamNoiseMargin;

    public LineStats(Speed downstreamSpeed, double downstreamAttenuation, double downstreamNoiseMargin,
                     Speed upstreamSpeed, double upstreamAttenuation, double upstreamNoiseMargin) {
        this.downstreamSpeed = downstreamSpeed;
        this.downstreamAttenuation = downstreamAttenuation;
        this.downstreamNoiseMargin = downstreamNoiseMargin;
        this.upstreamSpeed = upstreamSpeed;
        this.upstreamAttenuation = upstreamAttenuation;
        this.upstreamNoiseMargin = upstreamNoiseMargin;
    }

    public Speed getDownstreamSpeed() {
        return downstreamSpeed;
    }

    public double getDownstreamAttenuation() {
        return downstreamAttenuation;
    }

    public double getDownstreamNoiseMargin() {
        return downstreamNoiseMargin;
    }

    public Speed getUpstreamSpeed() {
        return upstreamSpeed;
    }

    public double getUpstreamAttenuation() {
        return upstreamAttenuation;
    }

    public double getUpstreamNoiseMargin() {
        return upstreamNoiseMargin;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("downstreamSpeed", downstreamSpeed)
                .add("downstreamAttenuation", downstreamAttenuation)
                .add("downstreamNoiseMargin", downstreamNoiseMargin)
                .add("upstreamSpeed", upstreamSpeed)
                .add("upstreamAttenuation", upstreamAttenuation)
                .add("upstreamNoiseMargin", upstreamNoiseMargin)
                .toString();
    }
}
