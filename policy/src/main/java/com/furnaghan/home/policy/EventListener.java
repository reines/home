package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.home.script.ParameterMap;
import com.furnaghan.util.ReflectionUtil;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface EventListener {

    public static <T extends Component.Listener> T proxy(final Component<?> component, final String name, final EventListener listener) {
        final Class<T> type = Iterables.getFirst(Components.getListenerTypes(component.getClass()), null);
        if (type == null) {
            throw new IllegalStateException("Component has no listener");
        }

        return ReflectionUtil.proxy(type, (proxy, method, args) -> {
            final String event = method.getName();
            final ParameterMap params = ParameterMap.of(method, args);

            listener.onEvent(component, name, event, params);
            return null;
        });
    }

    public static EventListener logger(final Logger logger) {
        return (component, name, event, params) -> logger.trace("{}.{}", name, ReflectionUtil.toString(event, params.values()));
    }

    public static EventListener delegate(final Collection<EventListener> delegates) {
        return (component, name, event, params) -> delegates.forEach(d -> d.onEvent(component, name, event, params));
    }

    void onEvent(final Component<?> component, final String name, final String event, final ParameterMap params);
}
