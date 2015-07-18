package com.furnaghan.home.web.api;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.util.NamedType;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ComponentDescription {

    public static ComponentDescription from(final Component<?> component) {
        return from(component.getClass());
    }

    public static ComponentDescription from(final Class<? extends Component> component) {
        final Collection<String> events = Components.getListenerTypes(component).stream()
                .flatMap(l -> Arrays.stream(l.getDeclaredMethods()))
                .map(Method::getName)
                .collect(Collectors.toSet());

        final Collection<String> actions = Arrays.stream(component.getDeclaredMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()))
                .map(Method::getName)
                .collect(Collectors.toSet());

        return new ComponentDescription(
                NamedType.of(component),
                Collections2.transform(Components.getComponentTypes(component), NamedType::of),
                Components.getConfigurationType(component).transform(NamedType::of),
                events,
                actions
        );
    }

    private final NamedType name;
    private final Collection<NamedType> types;
    private final Optional<NamedType> configuration;
    private final Collection<String> events;
    private final Collection<String> actions;

    public ComponentDescription(final NamedType name, final Collection<NamedType> types,
                                final Optional<NamedType> configuration, final Collection<String> events,
                                final Collection<String> actions) {
        this.name = name;
        this.types = types;
        this.configuration = configuration;
        this.events = events;
        this.actions = actions;
    }

    public NamedType getName() {
        return name;
    }

    public Collection<NamedType> getTypes() {
        return types;
    }

    public Optional<NamedType> getConfiguration() {
        return configuration;
    }

    public Collection<String> getEvents() {
        return events;
    }

    public Collection<String> getActions() {
        return actions;
    }
}
