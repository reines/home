package com.furnaghan.home.policy.store;

import com.google.common.base.Optional;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;

import java.nio.charset.StandardCharsets;

public abstract class ScriptStore {

    protected abstract Optional<ByteSource> open(final String name);

    public Optional<CharSource> load(final String name) {
        return open(name).transform(source -> source.asCharSource(StandardCharsets.UTF_8));
    }

    public abstract void save(final String name, final CharSource source);
}
