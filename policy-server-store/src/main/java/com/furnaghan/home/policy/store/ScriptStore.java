package com.furnaghan.home.policy.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.common.base.Optional;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import io.dropwizard.jackson.Discoverable;

import java.nio.charset.StandardCharsets;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class ScriptStore implements Discoverable {

    protected abstract Optional<ByteSource> open(final String name);

    public Optional<CharSource> load(final String name) {
        return open(name).transform(source -> source.asCharSource(StandardCharsets.UTF_8));
    }
}
