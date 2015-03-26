package com.furnaghan.home.test.api;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.util.NamedType;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

import java.util.Collection;

public class ComponentDescription {

    public static ComponentDescription from(final Component<?> component) {
        return from(component.getClass());
    }

    public static ComponentDescription from(final Class<? extends Component> component) {
        return new ComponentDescription(
                NamedType.of(component),
                Collections2.transform(Components.getComponentTypes(component), NamedType::of),
                Components.getConfigurationType(component).transform(NamedType::of)
        );
    }

    private final NamedType name;
    private final Collection<NamedType> types;
    private final Optional<NamedType> configuration;

    public ComponentDescription(final NamedType name, final Collection<NamedType> types, final Optional<NamedType> configuration) {
        this.name = name;
        this.types = types;
        this.configuration = configuration;
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
}
