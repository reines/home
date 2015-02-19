package com.furnaghan.home.component.storage.local;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.storage.StorageType;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.FileWriteMode;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class LocalStorageComponent extends Component<StorageType.Listener> implements StorageType {

    private final File root;

    public LocalStorageComponent(final LocalStorageConfiguration configuration) {
        root = configuration.getRoot();
    }

    private File getFile(final String path) {
        return new File(root, path);
    }

    @Override
    public boolean exists(final String path) {
        return getFile(path).exists();
    }

    @Override
    public void rename(final String source, final String destination) {
        if (!getFile(source).renameTo(getFile(destination))) {
            throw Throwables.propagate(new IOException(String.format("Failed to move %s to %s", source, destination)));
        }
    }

    @Override
    public void delete(final String path) {
        if (!getFile(path).delete()) {
            throw Throwables.propagate(new IOException(String.format("Failed to delete %s", path)));
        }
    }

    @Override
    public void mkdir(final String path) {
        if (!getFile(path).mkdir()) {
            throw Throwables.propagate(new IOException(String.format("Failed to make directory %s", path)));
        }
    }

    @Override
    public ByteSource read(final String path) {
        return Files.asByteSource(getFile(path));
    }

    @Override
    public ByteSink write(final String path, final boolean append) {
        if (append) {
            return Files.asByteSink(getFile(path), FileWriteMode.APPEND);
        } else {
            return Files.asByteSink(getFile(path));
        }
    }
}
