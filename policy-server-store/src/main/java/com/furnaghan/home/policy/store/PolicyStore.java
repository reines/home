package com.furnaghan.home.policy.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.util.Listenable;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.lifecycle.Managed;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public abstract class PolicyStore extends Listenable<PolicyStore.Listener> implements Managed, Discoverable {

    public static interface Listener {
        void onPolicyAdded(final Policy policy);
        void onPolicyRemoved(final Policy policy);
    }

    @Override
    public void start() throws Exception { }

    @Override
    public void stop() throws Exception { }

    public abstract void save(final Policy policy);
}
