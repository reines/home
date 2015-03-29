package com.furnaghan.home.web.resources;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentList;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.util.NamedType;
import com.furnaghan.home.web.api.ComponentDescription;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@Path("/components")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ComponentResource {

    private final ConfigurationStore store;
    private final ComponentList registry;

    public ComponentResource(ConfigurationStore store, final ComponentList registry) {
        this.store = store;
        this.registry = registry;
    }

    @PUT
    @Path("/{type}/{name}")
    @SuppressWarnings("unchecked")
    public void create(
            @PathParam("type") final NamedType type,
            @PathParam("name") final String name,
            final Optional<Configuration> configuration) {
        checkArgument(Component.class.isAssignableFrom(type.getType()), "Invalid component type.");
        store.save((Class<Component>) type.getType(), name, configuration);
    }

    @GET
    @Path("/{type}/{name}")
    public ComponentDescription get(
            @PathParam("type") final NamedType type,
            @PathParam("name") final String name) {
        final Component<?> component = registry.asMap().get(name);
        if (component == null || !component.getClass().equals(type.getType())) {
            throw new NotFoundException("No such component.");
        }

        return ComponentDescription.from(component);
    }

    @GET
    public Map<String, ComponentDescription> list() {
        return Maps.transformValues(registry.asMap(), ComponentDescription::from);
    }
}
