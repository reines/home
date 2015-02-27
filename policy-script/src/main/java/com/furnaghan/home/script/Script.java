package com.furnaghan.home.script;

public interface Script {
    <T> T run(final Object... args) throws Exception;
}
