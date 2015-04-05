package com.furnaghan.home.component.xbmc.client.types;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Joiner;

public class Version {

    @JsonProperty
    private int major;

    @JsonProperty
    private int minor;

    @JsonProperty
    private int patch;

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return Joiner.on('.').join(major, minor, patch);
    }
}
