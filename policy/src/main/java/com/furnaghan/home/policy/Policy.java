package com.furnaghan.home.policy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Component;
import com.furnaghan.home.script.Script;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class Policy {

    @Valid
    @NotNull
    @JsonProperty
    private final Class<? extends Component.Listener> type;

    @Valid
    @NotNull
    @JsonProperty
    private final String event;

    @Valid
    @NotNull
    @JsonProperty
    private final Class<?>[] parameterTypes;

    @Valid
    @NotNull
    @JsonProperty
    private final String script;

    @JsonCreator
    public Policy(
            @JsonProperty("type") final Class<? extends Component.Listener> type,
            @JsonProperty("event") final String event,
            @JsonProperty("parameterTypes") final Class<?>[] parameterTypes,
            @JsonProperty("script") final String script) {
        this.type = type;
        this.event = event;
        this.parameterTypes = parameterTypes;
        this.script = script;
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

    public String getScript() {
        return script;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Policy policy = (Policy) o;

        if (!event.equals(policy.event)) return false;
        if (!Arrays.equals(parameterTypes, policy.parameterTypes)) return false;
        if (!script.equals(policy.script)) return false;
        if (!type.equals(policy.type)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + event.hashCode();
        result = 31 * result + Arrays.hashCode(parameterTypes);
        result = 31 * result + script.hashCode();
        return result;
    }
}
