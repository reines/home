package com.furnaghan.home.component.printer.littleprinter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.net.URI;

public class LittlePrinterConfiguration extends JerseyClientConfiguration implements Configuration {

    @NotNull
    @JsonProperty
    private URI root;

    @NotEmpty
    @JsonProperty
    private String id;

    public URI getRoot() {
        return root;
    }

    public String getId() {
        return id;
    }
}
