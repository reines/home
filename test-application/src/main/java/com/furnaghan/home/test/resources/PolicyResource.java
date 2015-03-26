package com.furnaghan.home.test.resources;

import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.server.PolicyList;
import com.furnaghan.home.policy.store.PolicyStore;
import com.furnaghan.home.test.api.PolicyDescription;
import com.google.common.collect.Collections2;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

@Path("/policies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PolicyResource {

    private final PolicyStore store;
    private final PolicyList server;

    public PolicyResource(final PolicyStore store, final PolicyList server) {
        this.store = store;
        this.server = server;
    }

    @POST
    public void create(final Policy policy) {
        store.save(policy);
    }

    @GET
    public Collection<PolicyDescription> list() {
        return Collections2.transform(server.list(), PolicyDescription::from);
    }
}
