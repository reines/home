package com.furnaghan.home.policy.store;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.policy.Policy;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.google.common.base.Preconditions.checkArgument;

public class FilePolicyStore extends PolicyStore {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    private final File dir;

    public FilePolicyStore(final String path) {
        this (new File(path));
    }

    public FilePolicyStore(final File dir) {
        this.dir = dir;
        checkArgument(dir.isDirectory() || dir.mkdirs(), "Unable to create directory: " + dir.getAbsolutePath());
    }

    @Override
    public void start() {
        try {
            Files.list(dir.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Policy policy = load(file);
                trigger(l -> l.onPolicyAdded(file.getName(), policy));
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void save(final String name, final Policy policy) {
        final File file = new File(dir, name);
        try {
            JSON.writeValue(file, policy);
            trigger(l -> l.onPolicyAdded(name, policy));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private Policy load(final File file) {
        try {
            return JSON.readValue(file, Policy.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
