package com.furnaghan.home.component.modem.sky;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class SkyModemConfiguration extends JerseyClientConfiguration implements Configuration {
    @NotNull
    @JsonProperty
    private URI root = URI.create("http://192.168.1.1");

    @NotEmpty
    @JsonProperty
    private String username = "admin";

    @NotEmpty
    @JsonProperty
    private String password = "sky";

    public URI getRoot() {
        return root;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
