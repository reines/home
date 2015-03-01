package com.furnaghan.home.script;

public interface Script {
    <T> T run(final ParameterMap params) throws Exception;
}
