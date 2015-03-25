package com.furnaghan.home.policy.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.policy.Policy;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@JsonTypeName("file")
public class FilePolicyStore extends PolicyStore {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    private final File path;

    @JsonCreator
    public FilePolicyStore(@JsonProperty("path") final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    private File randomFile() {
        return new File(path, UUID.randomUUID().toString());
    }

    @Override
    public void start() {
        try {
            Files.list(path.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Policy policy = load(file);
                trigger(l -> l.onPolicyAdded(policy));
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public void save(final Policy policy) {
        final File file = randomFile();
        try {
            JSON.writeValue(file, policy);
            trigger(l -> l.onPolicyAdded(policy));
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
