package com.furnaghan.home.web.resources;

import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.Configuration;
import com.furnaghan.home.component.registry.ComponentList;
import com.furnaghan.home.component.registry.store.ConfigurationStore;
import com.furnaghan.home.util.JsonUtil;
import com.furnaghan.home.util.NamedType;
import com.furnaghan.home.web.api.ComponentDescription;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.common.collect.Maps;
import com.sun.jersey.api.NotFoundException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
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
    public void put(
            @PathParam("type") final NamedType type,
            @PathParam("name") final String name,
            final Optional<Configuration> configuration) {
        checkArgument(type.isTypeOf(Component.class), "Invalid component type.");
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
    @Path("/{type}/{name}/config.json")
    @SuppressWarnings("unchecked")
    public Optional<Configuration> getConfiguration(
            @PathParam("type") final NamedType type,
            @PathParam("name") final String name) {
        checkArgument(type.isTypeOf(Component.class), "Invalid component type.");
        return store.load((Class<Component>) type.getType(), name);
    }

    @GET
    @Path("/{type}/config_schema.json")
    @SuppressWarnings("unchecked")
    public Optional<JsonSchema> getConfigurationSchema(
            @PathParam("type") final NamedType type) {
        checkArgument(type.isTypeOf(Component.class), "Invalid component type.");
        return Components.getConfigurationType((Class<Component>) type.getType())
                .transform(JsonUtil::getSchema);
    }

    @GET
    public Map<String, ComponentDescription> list() {
        return Maps.transformValues(registry.asMap(), ComponentDescription::from);
    }

    @GET
    @Path("/types")
    public Collection<ComponentDescription> listAvailableTypes() {
        return Collections2.transform(Components.discover(), ComponentDescription::from);
    }
}
