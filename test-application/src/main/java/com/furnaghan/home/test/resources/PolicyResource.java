package com.furnaghan.home.test.resources;

import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.policy.store.PolicyStore;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/policies")
public class PolicyResource {

    private final PolicyStore store;

    public PolicyResource(final PolicyStore store) {
        this.store = store;
    }

    @POST
    public void create(final Policy policy) {
        store.save(policy);
    }
}
