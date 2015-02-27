package com.furnaghan.home.script;

import com.google.common.io.CharSource;

public interface ScriptLoader {
    CharSource load(final String name);
}
