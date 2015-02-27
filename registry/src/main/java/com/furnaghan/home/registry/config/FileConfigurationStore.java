package com.furnaghan.home.registry.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Map;

import static com.furnaghan.home.component.Components.getConfigurationType;
import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class FileConfigurationStore implements ConfigurationStore {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    private final File dir;

    public FileConfigurationStore(final String path) {
        this (new File(path));
    }

    public FileConfigurationStore(final File dir) {
        this.dir = dir;
        checkArgument(dir.isDirectory() || dir.mkdirs(), "Unable to create configuration directory: " + dir.getAbsolutePath());
    }

    @Override
    public void start() {}

    @Override
    public void stop() { }

    private File typeDir(final Class<? extends Component> type) {
        return new File(dir, getName(type));
    }

    @Override
    public void save(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        final File typeDir = typeDir(componentType);
        checkState(typeDir.isDirectory() || typeDir.mkdirs(), "Unable to create directory for type: " + typeDir.getAbsolutePath());

        final File file = new File(typeDir, name);
        save(file, configuration);
    }

    private void save(final File file, final Optional<Configuration> configuration) {
        try {
            JSON.writeValue(file, configuration);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Map<String, Optional<Configuration>> load(final Class<? extends Component> componentType) {
        final ImmutableMap.Builder<String, Optional<Configuration>> result = ImmutableMap.builder();

        final File typeDir = typeDir(componentType);
        if (!typeDir.isDirectory()) {
            return Collections.emptyMap();
        }

        try {
            Files.list(typeDir.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Optional<Class<Configuration>> configurationType = getConfigurationType(componentType);
                final Optional<Configuration> configuration = configurationType.isPresent() ?
                        load(file, configurationType.get()) : Optional.<Configuration>absent();
                result.put(file.getName(), configuration);
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

        return result.build();
    }

    private Optional<Configuration> load(final File file, final Class<? extends Configuration> configurationType) {
        try {
            return JSON.readValue(file, ReflectionUtil.createTypeReference(Optional.class, configurationType));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
