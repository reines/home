package com.furnaghan.home.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.registry.config.ConfigurationStore;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import io.dropwizard.lifecycle.Managed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkNotNull;

public class ComponentRegistry implements Managed {

    private static final Logger logger = LoggerFactory.getLogger(ComponentRegistry.class);

    private final ConfigurationStore configurationStore;
    private final Collection<ComponentListener> listeners;
    private final Map<String, Component<?>> componentsByName;
    private final Multimap<Class<? extends ComponentType>, Component<?>> componentsByType;

    public ComponentRegistry(final ConfigurationStore configurationStore) {
        this.configurationStore = configurationStore;

        listeners = Lists.newCopyOnWriteArrayList();
        componentsByName = Maps.newHashMap();
        componentsByType = HashMultimap.create();

        listeners.add((name, component) -> {
            componentsByName.put(name, component);

            final Set<Class<ComponentType>> types = Components.getComponentTypes(component.getClass());
            types.forEach(type -> componentsByType.put(type, component));
        });
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
    public synchronized <T extends ComponentType> Collection<T> getComponents(final Class<T> type) {
        checkNotNull(type, "Component type cannot be null");
        return Collections2.transform(componentsByType.get(type), new Function<Component<?>, T>() {
            @Nullable
            @Override
            public T apply(final Component<?> input) {
                return (T) input;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public synchronized <T extends ComponentType> Optional<T> getComponent(final String name) {
        checkNotNull(name, "Component name cannot be null");
        return Optional.fromNullable((T) componentsByName.get(name));
    }

    public synchronized int size() {
        return componentsByName.size();
    }

    public void load(final Class<? extends Component> componentType) {
        checkNotNull(componentType, "Component type cannot be null");
        final Map<String, Optional<Configuration>> configurations = configurationStore.load(componentType);
        for (final Map.Entry<String, Optional<Configuration>> entry : configurations.entrySet()) {
            final String name = entry.getKey();
            final Optional<Configuration> configuration = entry.getValue();
            load(componentType, name, configuration);
        }
    }

    private synchronized void load(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        logger.debug("Loading {} '{}'", getName(componentType), name);

        try {
            final Component<?> component = Components.create(componentType, configuration);
            logger.info("Loaded {} '{}'", getName(componentType), name);
            listeners.forEach(l -> l.onComponentLoaded(name, component));
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error(String.format("Unable to load %s '%s' ", getName(componentType), name), e.getCause());
        }
    }
}
