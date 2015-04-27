package com.furnaghan.home.component.torrent.api;

import com.google.common.base.MoreObjects;
import io.dropwizard.util.Size;

public class Torrent {

    private final String name;
    private final State state;
    private final Size size;
    private final double ratio;
    private final String path;

    public Torrent(final String name, final State state, final Size size, final double ratio, final String path) {
        this.name = name;
        this.state = state;
        this.size = size;
        this.ratio = ratio;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public Size getSize() {
        return size;
    }

    public double getRatio() {
        return ratio;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("state", state)
                .add("size", size)
                .add("ratio", ratio)
                .add("path", path)
                .toString();
    }
}
