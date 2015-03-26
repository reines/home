package com.furnaghan.home.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.hash.Funnel;

public final class JsonUtils {

    private JsonUtils() {}

    public static <T> Funnel<T> jsonFunnel(final ObjectMapper json) {
        return (from, into) -> {
            try {
                into.putBytes(json.writeValueAsBytes(from));
            } catch (JsonProcessingException e) {
                throw Throwables.propagate(e);
            }
        };
    }
}
