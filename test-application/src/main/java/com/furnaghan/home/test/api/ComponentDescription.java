package com.furnaghan.home.test.api;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;

import java.util.Collection;

public class ComponentDescription {

    public static ComponentDescription from(final Component<?> component) {
        return from(component.getClass());
    }

    public static ComponentDescription from(final Class<? extends Component> component) {
        return new ComponentDescription(
                Type.of(component),
                Collections2.transform(Components.getComponentTypes(component), Type::of),
                Components.getConfigurationType(component).transform(Type::of)
        );
    }

    private final Type name;
    private final Collection<Type> types;
    private final Optional<Type> configuration;

    public ComponentDescription(final Type name, final Collection<Type> types, final Optional<Type> configuration) {
        this.name = name;
        this.types = types;
        this.configuration = configuration;
    }

    public Type getName() {
        return name;
    }

    public Collection<Type> getTypes() {
        return types;
    }

    public Optional<Type> getConfiguration() {
        return configuration;
    }
}
