package com.furnaghan.home.web.resources;

import com.furnaghan.home.policy.store.ScriptStore;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;
import com.google.common.io.CharSource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/scripts")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class ScriptResource {

    private final ScriptStore scriptStore;

    public ScriptResource(final ScriptStore scriptStore) {
        this.scriptStore = scriptStore;
    }

    @GET
    @Path("/{name}")
    public Optional<String> get(
            @PathParam("name") final String name) {
        return scriptStore.load(name).transform(source -> {
            try {
                return source.read();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        });
    }

    @PUT
    @Path("/{name}")
    public void put(
            @PathParam("name") final String name,
            final String script) {
        scriptStore.save(name, CharSource.wrap(script));
    }
}
