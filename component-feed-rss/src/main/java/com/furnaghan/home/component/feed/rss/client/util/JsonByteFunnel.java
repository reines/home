package com.furnaghan.home.component.feed.rss.client.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import io.dropwizard.jackson.Jackson;

public class JsonByteFunnel<T> implements Funnel<T> {

    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    @Override
    public void funnel(final T from, final PrimitiveSink into) {
        try {
            final byte[] bytes = JSON.writeValueAsBytes(from);
            into.putBytes(bytes);
        } catch (JsonProcessingException e) {
            throw Throwables.propagate(e);
        }
    }
}
