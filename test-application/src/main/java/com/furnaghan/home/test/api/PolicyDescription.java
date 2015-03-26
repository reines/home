package com.furnaghan.home.test.api;

import com.furnaghan.home.policy.Policy;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class PolicyDescription {

    public static PolicyDescription from(final Policy policy) {
        return new PolicyDescription(
                Type.of(policy.getType()),
                policy.getEvent(),
                Lists.transform(Arrays.asList(policy.getParameterTypes()), Type::of),
                policy.getScript()
        );
    }

    private final Type type;
    private final String event;
    private final List<Type> parameterTypes;
    private final String script;

    public PolicyDescription(final Type type, final String event, final List<Type> parameterTypes, final String script) {
        this.type = type;
        this.event = event;
        this.parameterTypes = parameterTypes;
        this.script = script;
    }

    public Type getType() {
        return type;
    }

    public String getEvent() {
        return event;
    }

    public List<Type> getParameterTypes() {
        return parameterTypes;
    }

    public String getScript() {
        return script;
    }
}
