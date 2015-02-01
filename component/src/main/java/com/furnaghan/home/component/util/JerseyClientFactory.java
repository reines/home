package com.furnaghan.home.component.util;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.SharedMetricRegistries;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.client.apache4.config.ApacheHttpClient4Config;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.jackson.Jackson;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JerseyClientFactory {

    private static final MetricRegistry METRICS_REGISTRY = SharedMetricRegistries.getOrCreate("furnaghan-home");
    private static final ObjectMapper JSON = Jackson.newObjectMapper();

    public static Client build(final String name, final JerseyClientConfiguration configuration) {
        final ExecutorService executor = new ThreadPoolExecutor(configuration.getMinThreads(),
                configuration.getMaxThreads(), 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        return new JerseyClientBuilder(METRICS_REGISTRY)
                .using(configuration)
                .withProperty(ApacheHttpClient4Config.PROPERTY_ENABLE_BUFFERING, true)
                .using(executor, JSON)
                .build(name);
    }
}
