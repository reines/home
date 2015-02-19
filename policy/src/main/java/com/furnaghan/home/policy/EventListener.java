package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.Components;
import com.furnaghan.util.ReflectionUtil;
import org.slf4j.Logger;

import java.lang.reflect.Proxy;
import java.util.Collection;

public interface EventListener {

    @SuppressWarnings("unchecked")
    public static <T extends Component.Listener> T proxy(final Component<?> component, final String name, final EventListener listener) {
        final Class<? extends Component.Listener> type = Components.getListenerType(component.getClass());
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            final String event = method.getName();
            listener.onEvent(component, name, event, args);

            return null;
        });
    }

    public static EventListener logger(final Logger logger) {
        return (component, name, event, args) -> logger.trace("{}.{}", name, ReflectionUtil.toString(event, args));
    }

    public static EventListener delegate(final Collection<EventListener> delegates) {
        return (component, name, event, args) -> delegates.forEach(d -> d.onEvent(component, name, event, args));
    }

    void onEvent(final Component<?> component, final String name, final String event, final Object... args);
}
