package com.furnaghan.home.component.metrics.librato;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import com.google.common.base.Optional;
import com.librato.metrics.LibratoBatch;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class LibratoMetricsConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private Optional<String> source = Optional.absent();

    @Min(1)
    @JsonProperty
    private int batchSize = LibratoBatch.DEFAULT_BATCH_SIZE;

    @NotNull
    @JsonProperty
    private Duration timeout = Duration.seconds(5);

    @NotEmpty
    @JsonProperty
    private String userAgent = "furnaghan-home";

    @NotEmpty
    @JsonProperty
    private String username;

    @NotEmpty
    @JsonProperty
    private String apiToken;

    @NotEmpty
    @JsonProperty
    private String apiUrl = "https://metrics-api.librato.com/v1/metrics";

    public Optional<String> getSource() {
        return source;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getUsername() {
        return username;
    }

    public String getApiToken() {
        return apiToken;
    }

    public String getApiUrl() {
        return apiUrl;
    }
}
