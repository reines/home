package com.furnaghan.home.component.calendar.google.event;

import com.google.api.client.http.HttpResponseException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventSink implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(EventSink.class);

    private final Calendar client;
    private final String calendarId;

    private transient String syncToken;

    protected EventSink(final Calendar client, final String calendarId) {
        this.client = client;
        this.calendarId = calendarId;

        syncToken = null;
    }

    protected String getCalendarId() {
        return calendarId;
    }

    @Override
    public synchronized void run() {
        try {
            final Calendar.Events.List request = client.events()
                    .list(calendarId);

            if (syncToken == null) {
                // Sync events from 1 day ago onwards
                request.setTimeMin(new DateTime(System.currentTimeMillis()));
            } else {
                request.setSyncToken(syncToken);
            }

            String pageToken = null;
            do {
                request.setPageToken(pageToken);

                try {
                    final Events events = request.execute();
                    events.getItems().forEach(this::syncEvent);

                    pageToken = events.getNextPageToken();
                    syncToken = events.getNextSyncToken();
                } catch (HttpResponseException e) {
                    if (e.getStatusCode() == HttpStatus.SC_GONE) {
                        LOG.warn("Invalid sync token, re-syncing");
                        syncToken = null;
                    } else {
                        throw e;
                    }
                }
            }
            while (pageToken != null);
        } catch (Exception e) {
            LOG.warn("Error syncing events", e);
        }
    }

    protected abstract void syncEvent(final Event event);
}
