package com.furnaghan.home.component.notifier.logging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.furnaghan.home.component.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class LoggingNotifierConfiguration implements Configuration {

    @NotEmpty
    @JsonProperty
    private String loggerName = "logging-notifier";

    @NotNull
    @JsonProperty
    private Level level = Level.INFO;

    @NotEmpty
    @JsonProperty
    private String template = "{}: {}";

    public String getLoggerName() {
        return loggerName;
    }

    public Level getLevel() {
        return level;
    }

    public String getTemplate() {
        return template;
    }
}
