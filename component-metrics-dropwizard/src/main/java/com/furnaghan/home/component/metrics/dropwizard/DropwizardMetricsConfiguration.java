package com.furnaghan.home.component.metrics.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class DropwizardMetricsConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String namespace = "home";

    public String getNamespace() {
        return namespace;
    }
}
