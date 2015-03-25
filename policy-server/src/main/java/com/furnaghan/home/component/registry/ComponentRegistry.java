package com.furnaghan.home.component.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.util.Listenable;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkNotNull;

public class ComponentRegistry extends Listenable<ComponentRegistry.Listener> implements ComponentList {

    public static interface Listener {
        void onComponentAdded(final String name, final Component<?> component);
        void onComponentRemoved(final String name, final Component<?> component);
    }

    private static final Logger logger = LoggerFactory.getLogger(ComponentRegistry.class);

    private final Map<String, Component<?>> componentsByName;
    private final Multimap<Class<? extends ComponentType>, Component<?>> componentsByType;

    public ComponentRegistry() {
        componentsByName = Maps.newConcurrentMap();
        componentsByType = HashMultimap.create();
    }

    @Override
    public Map<String, Component<?>> asMap() {
        return Collections.unmodifiableMap(componentsByName);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ComponentType> Collection<T> getByType(final Class<T> type) {
        checkNotNull(type, "Component type cannot be null");
        return Collections.unmodifiableCollection(Collections2.transform(componentsByType.get(type), type::cast));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends ComponentType> Optional<T> getByName(final String name) {
        checkNotNull(name, "Component name cannot be null");
        return Optional.fromNullable((T) componentsByName.get(name));
    }

    public synchronized Optional<Component<?>> load(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        final Optional<Component<?>> existing = Optional.fromNullable(componentsByName.get(name));
        if (existing.isPresent()) {
            logger.debug("Not loading {} '{}', it is already loaded.", getName(componentType), name);
            return existing;
        }

        try {
            final Component<?> component = Components.create(componentType, configuration);
            logger.info("Loaded {} '{}'", getName(componentType), name);

            componentsByName.put(name, component);
            Components.getComponentTypes(component.getClass()).forEach(type -> componentsByType.put(type, component));
            trigger(l -> l.onComponentAdded(name, component));

            return Optional.of(component);
        }
        catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            logger.error(String.format("Unable to load %s '%s' ", getName(componentType), name), e.getCause());
            return Optional.absent();
        }
    }

    public synchronized boolean remove(final String name) {
        final Optional<Component<?>> component = Optional.fromNullable(componentsByName.remove(name));
        if (!component.isPresent()) {
            return false;
        }

        Components.getComponentTypes(component.get().getClass()).forEach(type -> componentsByType.remove(type, component));
        trigger(l -> l.onComponentRemoved(name, component.get()));
        return true;
    }
}
