package com.furnaghan.home.policy.store;

import com.google.common.base.Optional;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

import java.io.File;

import static com.google.common.base.Preconditions.checkArgument;

public class FileScriptStore extends ScriptStore {

    private final File dir;

    public FileScriptStore(final String path) {
        this (new File(path));
    }

    public FileScriptStore(final File dir) {
        this.dir = dir;
        checkArgument(dir.isDirectory(), "No such directory: " + dir.getAbsolutePath());
    }

    @Override
    protected Optional<ByteSource> open(final String name) {
        final File file = new File(dir, name);
        return file.exists() ? Optional.of(Files.asByteSource(file)) :
                Optional.<ByteSource>absent();
    }
}
