package com.furnaghan.home.script;

import com.google.common.collect.ImmutableMap;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

public class ParameterMap {

    public static ParameterMap of(final Method method, final Object[] args) {
        return new ParameterMap(method.getParameters(), args);
    }

    private final Parameter[] parameters;
    private final Object[] values;

    private ParameterMap(final Parameter[] parameters, final Object[] values) {
        checkArgument(parameters.length == values.length, "Parameter length mismatch");

        this.parameters = parameters;
        this.values = values;
    }

    public Object[] values() {
        return values;
    }

    public Map<String, ?> map() {
        final ImmutableMap.Builder<String, Object> map = ImmutableMap.builder();

        for (int i = 0;i < parameters.length; i++) {
            map.put(parameters[i].getName(), values[i]);
        }

        return map.build();
    }
}
