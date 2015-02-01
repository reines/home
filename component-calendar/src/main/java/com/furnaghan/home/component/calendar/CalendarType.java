package com.furnaghan.home.component.calendar;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.ComponentType;
import com.furnaghan.home.component.calendar.model.CalendarEvent;

public interface CalendarType extends ComponentType {
    public static interface Listener extends Component.Listener {
        void notify(final CalendarEvent event);
    }

    void addEvent(final CalendarEvent event);
}
