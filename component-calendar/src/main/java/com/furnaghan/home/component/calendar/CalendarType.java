package com.furnaghan.home.component.calendar;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.google.common.base.Optional;
import io.dropwizard.util.Duration;

import java.util.Date;

public interface CalendarType extends ComponentType {
    public static interface Listener extends Component.Listener {
        void notify(final Date start, final Duration duration, final String title, final Optional<String> description);
    }

    void addEvent(final Date start, final Duration duration, final String title, final Optional<String> description);
}
