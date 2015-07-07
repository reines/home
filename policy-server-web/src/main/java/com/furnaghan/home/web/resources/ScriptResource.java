package com.furnaghan.home.web.resources;

import com.furnaghan.home.policy.store.ScriptStore;
import com.google.common.base.Optional;
import com.google.common.base.Throwables;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/scripts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.TEXT_PLAIN)
public class ScriptResource {

    private final ScriptStore scriptStore;

    public ScriptResource(final ScriptStore scriptStore) {
        this.scriptStore = scriptStore;
    }

    @GET
    @Path("/{name}")
    public Optional<String> fetch(
            @PathParam("name") final String name) {
        return scriptStore.load(name).transform(source -> {
            try {
                return source.read();
            } catch (IOException e) {
                throw Throwables.propagate(e);
            }
        });
    }
}
