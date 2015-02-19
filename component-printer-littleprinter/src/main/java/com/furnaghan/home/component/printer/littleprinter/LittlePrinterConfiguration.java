package com.furnaghan.home.component.printer.littleprinter;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class LittlePrinterConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private final URI root;

    @NotEmpty
    @JsonProperty
    private final String id;

    @JsonCreator
    public LittlePrinterConfiguration(
            @JsonProperty("root") final URI root,
            @JsonProperty("id") final String id) {
        this.root = root;
        this.id = id;
    }

    public URI getRoot() {
        return root;
    }

    public String getId() {
        return id;
    }
}
