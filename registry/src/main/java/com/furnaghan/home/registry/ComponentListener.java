package com.furnaghan.home.registry;

import com.furnaghan.home.component.Component;

public interface ComponentListener {
    void onComponentLoaded(final String name, final Component<?> component);
}
