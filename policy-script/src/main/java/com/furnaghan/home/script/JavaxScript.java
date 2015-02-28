package com.furnaghan.home.script;

import com.google.common.io.CharSource;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.SimpleBindings;
import java.io.Reader;

public class JavaxScript implements Script {

    private final JavaxScriptFactory factory;
    private final ScriptEngine engine;
    private final CharSource script;

    public JavaxScript(final JavaxScriptFactory factory, final ScriptEngine engine, final CharSource script) {
        this.factory = factory;
        this.engine = engine;
        this.script = script;
    }

    private Bindings bind(final Object[] args) {
        final SimpleBindings binding = new SimpleBindings();

        binding.putAll(factory.getVariables());
        binding.put("args", args);

        return binding;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T run(final Object... args) throws Exception {
        try (final Reader reader = script.openStream()) {
            return (T) engine.eval(reader, bind(args));
        }
    }
}
