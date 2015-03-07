package com.furnaghan.home.test.resources;

import com.furnaghan.home.component.Components;
import com.furnaghan.home.component.registry.ComponentList;
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

    private final ComponentList components;

    public ComponentResource(final ComponentList components) {
        this.components = components;
    }

    @GET
    @Path("/registered")
    public Map<String, ComponentDescription> listRegistered() {
        return Maps.transformValues(components.asMap(), ComponentDescription::from);
    }

    @GET
    @Path("/all")
    public Collection<ComponentDescription> listAll() {
        return Collections2.transform(Components.discover(), ComponentDescription::from);
    }

    @GET
    @Path("/all/types")
    public Collection<Type> listAllTypes() {
        return FluentIterable.from(Components.discover())
                .transformAndConcat(Components::getComponentTypes)
                .transform(Type::of)
                .toSet();
    }
}
