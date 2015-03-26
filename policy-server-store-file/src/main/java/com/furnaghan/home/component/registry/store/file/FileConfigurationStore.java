package com.furnaghan.home.component.registry.store.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Collections;

import static com.furnaghan.home.component.Components.getConfigurationType;
import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

public class FileConfigurationStore extends ConfigurationStore {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    private final File path;

    public FileConfigurationStore(final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    private File typeDir(final Class<? extends Component> type) {
        return new File(path, getName(type));
    }

    @Override
    public void save(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        final File typeDir = typeDir(componentType);
        checkState(typeDir.isDirectory() || typeDir.mkdirs(), "Unable to create directory for type: " + typeDir.getAbsolutePath());

        final File file = new File(typeDir, name);
        save(file, configuration);
        trigger(l -> l.onConfigurationAdded(componentType, name, configuration));
    }

    @Override
    public boolean delete(final Class<? extends Component> componentType, final String name) {
        final File typeDir = typeDir(componentType);
        final File file = new File(typeDir, name);
        if (file.exists()) {
            final Optional<Configuration> configuration = load(file, componentType);
            trigger(l -> l.onConfigurationRemoved(componentType, name, configuration));
            return true;
        }

        return false;
    }

    private void save(final File file, final Optional<Configuration> configuration) {
        try {
            JSON.writeValue(file, configuration);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Collection<Optional<Configuration>> load(final Class<? extends Component> componentType) {
        final File typeDir = typeDir(componentType);
        if (!typeDir.isDirectory()) {
            return Collections.emptyList();
        }

        try {
            final ImmutableList.Builder<Optional<Configuration>> result = ImmutableList.builder();
            Files.list(typeDir.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Optional<Configuration> configuration = load(file, componentType);

                trigger(l -> l.onConfigurationAdded(componentType, file.getName(), configuration));
                result.add(configuration);
            });
            return result.build();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private Optional<Configuration> load(final File file, final Class<? extends Component> componentType) {
        try {
            final Optional<Class<Configuration>> configurationType = getConfigurationType(componentType);
            if (configurationType.isPresent()) {
                return JSON.readValue(file, ReflectionUtil.createTypeReference(Optional.class, configurationType.get()));
            }

            return Optional.absent();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
