package com.furnaghan.home.policy.store;

import com.furnaghan.home.policy.Policy;

public interface PolicyStoreListener {
    void onPolicyAdded(final Policy policy);
    void onPolicyRemoved(final Policy policy);
}
