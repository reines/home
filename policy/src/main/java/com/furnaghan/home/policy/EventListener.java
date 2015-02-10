package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.util.ReflectionUtil;
import org.slf4j.Logger;

import java.lang.reflect.Proxy;
import java.util.Collection;

public interface EventListener {

    @SuppressWarnings("unchecked")
    public static <T extends Component.Listener> T proxy(final String name, final Class<T> type, final EventListener listener) {
        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, (proxy, method, args) -> {
            final String event = method.getName();
            listener.onEvent(name, event, args);

            return null;
        });
    }

    public static EventListener logger(final Logger logger) {
        return (component, name, args) -> logger.debug("{}.{}", component, ReflectionUtil.toString(name, args));
    }

    public static EventListener delegate(final Collection<EventListener> delegates) {
        return (component, name, args) -> delegates.forEach(d -> d.onEvent(component, name, args));
    }

    void onEvent(final String component, final String name, final Object... args);
}
