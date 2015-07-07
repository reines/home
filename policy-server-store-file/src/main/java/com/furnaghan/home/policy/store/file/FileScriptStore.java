package com.furnaghan.home.policy.store.file;

import com.furnaghan.home.policy.store.ScriptStore;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import static com.google.common.base.Preconditions.checkArgument;

public class FileScriptStore extends ScriptStore {

    private final File path;

    public FileScriptStore(final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    @Override
    protected Optional<ByteSource> open(final String name) {
        final File file = new File(path, name);
        return file.exists() ? Optional.of(Files.asByteSource(file)) :
                Optional.<ByteSource>absent();
    }

    @Override
    public void save(final String name, final CharSource source) {
        final File file = new File(path, name);
        try (final Reader reader = source.openStream()) {
            Files.asCharSink(file, StandardCharsets.UTF_8).writeFrom(reader);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
