package com.furnaghan.home.component.registry.store.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.util.JsonUtils;
import com.furnaghan.home.util.ReflectionUtil;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOG = LoggerFactory.getLogger(FileConfigurationStore.class);
    private static final ObjectMapper JSON = JsonUtils.newObjectMapper();
    private static final String FILE_EXT = "json";
    private static final Function<String, String> STRIP_FILE_EXT = name -> name.substring(0, name.length() - (FILE_EXT.length() + 1));

    private final File path;

    public FileConfigurationStore(final File path) {
        this.path = path;
        checkArgument(path.isDirectory() || path.mkdirs(), "Unable to create directory: " + path.getAbsolutePath());
    }

    private File typeDir(final Class<? extends Component> type) {
        return new File(path, getName(type));
    }

    private File file(final File typeDir, final String name) {
        return new File(typeDir, String.format("%s.%s", name, FILE_EXT));
    }

    @Override
    public void save(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        final File typeDir = typeDir(componentType);
        checkState(typeDir.isDirectory() || typeDir.mkdirs(), "Unable to create directory for type: " + typeDir.getAbsolutePath());

        final File file = file(typeDir, name);
        save(file, configuration);
        trigger(l -> l.onConfigurationAdded(componentType, name, configuration));
    }

    @Override
    public boolean delete(final Class<? extends Component> componentType, final String name) {
        final File typeDir = typeDir(componentType);
        final File file = file(typeDir, name);
        if (file.exists()) {
            final Optional<Configuration> configuration = load(componentType, name);
            file.delete();

            trigger(l -> l.onConfigurationRemoved(componentType, name, configuration));
            return true;
        }

        return false;
    }

    private void save(final File file, final Optional<Configuration> configuration) {
        try {
            JSON.writeValue(file, configuration);
        } catch (IOException e) {
            LOG.warn("Failed to save configuration", e);
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
                if (file.getName().endsWith("." + FILE_EXT)) {
                    final String name = STRIP_FILE_EXT.apply(file.getName());
                    final Optional<Configuration> configuration = load(componentType, name);

                    trigger(l -> l.onConfigurationAdded(componentType, name, configuration));
                    result.add(configuration);
                }
            });
            return result.build();
        } catch (IOException e) {
            LOG.warn("Failed to load configuration", e);
            throw Throwables.propagate(e);
        }
    }

    @Override
    public Optional<Configuration> load(final Class<? extends Component> componentType, final String name) {
        try {
            final File file = new File(typeDir(componentType), name + "." + FILE_EXT);
            if (!file.exists()) {
                return Optional.absent();
            }

            final Optional<Class<Configuration>> configurationType = getConfigurationType(componentType);
            if (configurationType.isPresent()) {
                return JSON.readValue(file, ReflectionUtil.createTypeReference(Optional.class, configurationType.get()));
            }

            return Optional.absent();
        } catch (IOException e) {
            LOG.warn("Failed to load configuration", e);
            throw Throwables.propagate(e);
        }
    }
}
