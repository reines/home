package com.furnaghan.home.test.resources;

import com.furnaghan.home.component.Components;
import com.furnaghan.home.registry.ComponentRegistry;
import com.furnaghan.home.test.api.ComponentDescription;
import com.furnaghan.home.test.api.Type;
import com.google.common.collect.Collections2;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.Map;

@Path("/components")
@Produces(MediaType.APPLICATION_JSON)
public class ComponentResource {

    private final ComponentRegistry registry;

    public ComponentResource(final ComponentRegistry registry) {
        this.registry = registry;
    }

    @GET
    @Path("/registered")
    public Map<String, ComponentDescription> listRegistered() {
        return Maps.transformValues(registry.getRegisteredComponents(), ComponentDescription::from);
    }

    @GET
    @Path("/all")
    public Collection<ComponentDescription> listAll() {
        return Collections2.transform(registry.getComponents(), ComponentDescription::from);
    }

    @GET
    @Path("/all/types")
    public Collection<Type> listAllTypes() {
        return FluentIterable.from(registry.getComponents())
                .transformAndConcat(Components::getComponentTypes)
                .transform(Type::of)
                .toSet();
    }
}
