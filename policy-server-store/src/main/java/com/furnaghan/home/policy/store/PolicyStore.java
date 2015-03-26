package com.furnaghan.home.policy.store;

import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.util.Listenable;

import java.util.Collection;

public abstract class PolicyStore extends Listenable<PolicyStoreListener> {
    public abstract void save(final Policy policy);
    public abstract boolean delete(final Policy policy);
    public abstract Collection<Policy> load();
}
