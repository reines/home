package com.furnaghan.home.component.storage;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;

public interface StorageType extends ComponentType {
    interface Listener extends Component.Listener {
    }

    boolean exists(final String path);
    void rename(final String source, final String destination);
    void delete(final String path);
    void mkdir(final String path);
    ByteSource read(final String path);
    ByteSink write(final String path, final boolean append);
}
