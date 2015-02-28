package com.furnaghan.home.script;

import com.google.common.io.CharSource;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static com.google.common.base.Preconditions.checkArgument;

public class JavaxScriptFactory extends ScriptFactory {

    private final ScriptEngineManager manager;

    public JavaxScriptFactory() {
        manager = new ScriptEngineManager();
    }

    @Override
    public JavaxScript load(final CharSource script, final String type) {
        final ScriptEngine engine = manager.getEngineByExtension(type);
        checkArgument(engine != null, "Unable to find script engine: " + type);
        return new JavaxScript(this, engine, script);
    }
}
