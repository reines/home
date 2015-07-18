package com.furnaghan.home.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.google.common.base.Throwables;
import com.google.common.hash.Funnel;
import io.dropwizard.jackson.Jackson;

public final class JsonUtil {

    private static final ObjectMapper JSON = JsonUtil.newObjectMapper();

    private JsonUtil() { }

    public static ObjectMapper newObjectMapper() {
        final ObjectMapper json = Jackson.newObjectMapper();
        json.enable(SerializationFeature.INDENT_OUTPUT);
        return json;
    }

    public static <T> Funnel<T> jsonFunnel(final ObjectMapper json) {
        return (from, into) -> {
            try {
                into.putBytes(json.writeValueAsBytes(from));
            } catch (JsonProcessingException e) {
                throw Throwables.propagate(e);
            }
        };
    }

    public static JsonSchema getSchema(final Class<?> target) {
        try {
            final SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
            JSON.acceptJsonFormatVisitor(JSON.constructType(target), visitor);
            return visitor.finalSchema();
        } catch (JsonMappingException e) {
            throw Throwables.propagate(e);
        }
    }
}
