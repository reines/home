package com.furnaghan.home.component.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

import static com.furnaghan.home.component.Components.getName;
import static com.google.common.base.Preconditions.checkNotNull;

public class ComponentRegistry {

    public static interface Listener {
        void onComponentAdded(final String name, final Component<?> component);
        void onComponentRemoved(final String name, final Component<?> component);
    }

    private static final Logger logger = LoggerFactory.getLogger(ComponentRegistry.class);

    private final Map<String, Component<?>> componentsByName;
    private final Multimap<Class<? extends ComponentType>, Component<?>> componentsByType;
    private final Collection<Listener> listeners = Lists.newCopyOnWriteArrayList();

    public ComponentRegistry() {
        componentsByName = Maps.newConcurrentMap();
        componentsByType = HashMultimap.create();
    }

    public void addListener(final Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(final Listener listener) {
        listeners.remove(listener);
    }

    protected final void trigger(final Consumer<Listener> action) {
        listeners.forEach(action);
    }

    public Map<String, Component<?>> getComponents() {
        return Collections.unmodifiableMap(componentsByName);
    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentType> Collection<T> getComponents(final Class<T> type) {
        checkNotNull(type, "Component type cannot be null");
        return Collections.unmodifiableCollection(Collections2.transform(componentsByType.get(type), type::cast));
    }

    @SuppressWarnings("unchecked")
    public <T extends ComponentType> Optional<T> getComponent(final String name) {
        checkNotNull(name, "Component name cannot be null");
        return Optional.fromNullable((T) componentsByName.get(name));
    }

    public synchronized Optional<Component<?>> load(final Class<? extends Component> componentType, final String name, final Optional<Configuration> configuration) {
        final Optional<Component<?>> existing = Optional.fromNullable(componentsByName.get(name));
        if (existing.isPresent()) {
            logger.debug("Not loading {} '{}', it is already loaded.", getName(componentType), name);
            return existing;
        }

        logger.debug("Loading {} '{}'", getName(componentType), name);

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
}
