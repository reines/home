package com.furnaghan.home.component.router.tomato;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.util.Duration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class TomatoConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private URI root;

    @NotEmpty
    @JsonProperty
    private String username = "root";

    @NotEmpty
    @JsonProperty
    private String password;

    @NotNull
    @JsonProperty
    private Duration pollInterval = Duration.seconds(5);

    public URI getRoot() {
        return root;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Duration getPollInterval() {
        return pollInterval;
    }
}
