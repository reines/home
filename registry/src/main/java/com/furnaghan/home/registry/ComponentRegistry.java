package com.furnaghan.home.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import static com.furnaghan.home.component.Components.getName;

public class ComponentRegistry implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(ComponentRegistry.class);

    private final ConfigurationStore configurationStore;
    private final Collection<ComponentListener> listeners;
    private final Map<String, Component<?>> components;

    public ComponentRegistry(ConfigurationStore configurationStore) {
        this.configurationStore = configurationStore;

        listeners = Lists.newCopyOnWriteArrayList();
        components = Maps.newConcurrentMap();
    }

    public void addListener(final ComponentListener listener) {
        listeners.add(listener);
    }

    @Override
    public void start() {
        ServiceDiscovery.discoverServices(Component.class).forEach(this::load);
    }

    @Override
    public void stop() {

    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentType> Optional<T> getComponent(final String name) {
        return Optional.fromNullable((T)components.get(name));
    }

    private void load(final Class<Component> componentType) {
        final Map<String, Optional<Configuration>> configurations = configurationStore.load(componentType);
        for (final Map.Entry<String, Optional<Configuration>> entry : configurations.entrySet()) {
            final String name = entry.getKey();
            final Optional<Configuration> configuration = entry.getValue();
            load(componentType, name, configuration);
        }
    }

    private void load(final Class<Component> componentType, final String name, final Optional<Configuration> configuration) {
        logger.debug("Loading {} '{}'", getName(componentType), name);

        try {
            final Component<?> component = Components.create(componentType, configuration);
            logger.info("Loaded {} '{}'", getName(componentType), name);
            components.put(name, component);
            listeners.forEach(l -> l.onComponentLoaded(name, component));
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error(String.format("Unable to load %s '%s' ", getName(componentType), name), e);
        }
    }
}
