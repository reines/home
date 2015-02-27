package com.furnaghan.home.script;

import com.google.common.io.CharSource;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import static com.google.common.base.Preconditions.checkArgument;

public class JavaxScriptFactory extends ScriptFactory {

    private final ScriptEngine engine;

    public JavaxScriptFactory(final String type) {
        engine = new ScriptEngineManager().getEngineByName(type);
        checkArgument(engine != null, "Unable to find script engine: " + type);
    }

    protected ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public JavaxScript load(final CharSource script) {
        return new JavaxScript(this, script);
    }
}
