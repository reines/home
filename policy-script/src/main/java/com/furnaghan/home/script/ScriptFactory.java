package com.furnaghan.home.script;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.common.io.CharSource;

import java.util.Collections;
import java.util.Map;

public abstract class ScriptFactory {

    private final Map<String, Supplier<?>> variables = Maps.newConcurrentMap();

    public Map<String, ?> getVariables() {
        return Collections.unmodifiableMap(Maps.transformValues(variables, new Function<Supplier<?>, Object>() {
            @Override
            public Object apply(final Supplier<?> supplier) {
                return supplier.get();
            }
        }));
    }

    public void setVariable(final String name, final Object value) {
        variables.put(name, Suppliers.ofInstance(value));
    }

    public void setVariable(final String name, final Supplier<?> value) {
        variables.put(name, value);
    }

    public abstract Script load(final CharSource script, final String type);
}
