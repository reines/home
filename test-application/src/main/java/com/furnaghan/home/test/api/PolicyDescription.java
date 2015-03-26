package com.furnaghan.home.test.api;

import com.furnaghan.home.policy.Policy;
import com.furnaghan.home.util.NamedType;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

public class PolicyDescription {

    public static PolicyDescription from(final Policy policy) {
        return new PolicyDescription(
                NamedType.of(policy.getType()),
                policy.getEvent(),
                Lists.transform(Arrays.asList(policy.getParameterTypes()), NamedType::of),
                policy.getScript()
        );
    }

    private final NamedType type;
    private final String event;
    private final List<NamedType> parameterTypes;
    private final String script;

    public PolicyDescription(final NamedType type, final String event, final List<NamedType> parameterTypes, final String script) {
        this.type = type;
        this.event = event;
        this.parameterTypes = parameterTypes;
        this.script = script;
    }

    public NamedType getType() {
        return type;
    }

    public String getEvent() {
        return event;
    }

    public List<NamedType> getParameterTypes() {
        return parameterTypes;
    }

    public String getScript() {
        return script;
    }
}
