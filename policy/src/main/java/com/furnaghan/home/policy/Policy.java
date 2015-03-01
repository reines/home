package com.furnaghan.home.policy;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.script.Script;

public abstract class Policy implements Script {

    private final Class<? extends Component.Listener> type;
    private final String event;
    private final Class<?>[] parameterTypes;

    public Policy(final Class<? extends Component.Listener> type, final String event, final Class<?>[] parameterTypes) {
        this.type = type;
        this.event = event;
        this.parameterTypes = parameterTypes;
    }

    public Class<? extends Component.Listener> getType() {
        return type;
    }

    public String getEvent() {
        return event;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
}
