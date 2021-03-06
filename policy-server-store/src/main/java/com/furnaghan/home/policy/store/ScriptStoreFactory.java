package com.furnaghan.home.policy.store;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.dropwizard.jackson.Discoverable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface ScriptStoreFactory extends Discoverable {
    ScriptStore create();
}
