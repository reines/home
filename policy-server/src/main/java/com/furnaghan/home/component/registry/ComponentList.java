package com.furnaghan.home.component.registry;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.google.common.base.Optional;

import java.util.Collection;
import java.util.Map;

public interface ComponentList {
    Map<String, Component<?>> asMap();
    <T extends ComponentType> Collection<T> getByType(final Class<T> type);
    <T extends ComponentType> Optional<T> getByName(final String name);
}
