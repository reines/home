package com.furnaghan.home.component.registry.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.furnaghan.home.component.Components.getConfigurationType;
import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

@JsonTypeName("file")
public class FileConfigurationStore extends ConfigurationStore {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    private final File path;

    @JsonCreator
    public FileConfigurationStore(@JsonProperty("path") final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    @Override
    public void start() {
        Components.discover().forEach(this::load);
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

    private void save(final File file, final Optional<Configuration> configuration) {
        try {
            JSON.writeValue(file, configuration);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private void load(final Class<? extends Component> componentType) {
        final File typeDir = typeDir(componentType);
        if (!typeDir.isDirectory()) {
            return;
        }

        try {
            Files.list(typeDir.toPath()).forEach(path -> {
                final File file = path.toFile();
                final Optional<Class<Configuration>> configurationType = getConfigurationType(componentType);
                final Optional<Configuration> configuration = configurationType.isPresent() ?
                        load(file, configurationType.get()) : Optional.<Configuration>absent();
                trigger(l -> l.onConfigurationAdded(componentType, file.getName(), configuration));
            });
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    private Optional<Configuration> load(final File file, final Class<? extends Configuration> configurationType) {
        try {
            return JSON.readValue(file, ReflectionUtil.createTypeReference(Optional.class, configurationType));
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
