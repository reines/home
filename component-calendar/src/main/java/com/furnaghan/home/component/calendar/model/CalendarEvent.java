package com.furnaghan.home.component.calendar.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import io.dropwizard.util.Duration;

import java.util.Date;

public class CalendarEvent {

    private final Date start;
    private final Duration duration;
    private final String title;
    private final Optional<String> description;

    public CalendarEvent(final Date start, final Duration duration, final String title, final Optional<String> description) {
        this.start = start;
        this.duration = duration;
        this.title = title;
        this.description = description;
    }

    public Date getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    public Date getEnd() {
        return new Date(start.getTime() + duration.toMilliseconds());
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("start", start)
                .add("duration", duration)
                .add("title", title)
                .add("description", description)
                .toString();
    }
}
