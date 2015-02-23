package com.furnaghan.home.component.calendar.google;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.calendar.CalendarType;
import com.furnaghan.home.component.calendar.google.event.EventSink;
import com.furnaghan.home.component.calendar.google.event.SchedulingEventSink;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Throwables;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.common.base.Optional;
import io.dropwizard.util.Duration;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GoogleCalendarComponent extends Component<CalendarType.Listener> implements CalendarType {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCalendarComponent.class);
    private static final String APPLICATION_NAME = "Furnaghan Home";

    private final Calendar client;
    private final String calendarId;

    public GoogleCalendarComponent(final GoogleCalendarConfiguration configuration) {
        try {
            final HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            final JsonFactory json = JacksonFactory.getDefaultInstance();

            final GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(transport)
                    .setJsonFactory(json)
                    .setServiceAccountId(configuration.getEmail())
                    .setServiceAccountScopes(CalendarScopes.all())
                    .setServiceAccountPrivateKey(configuration.getPrivateKey())
                    .build();

            client = new Calendar.Builder(transport, json, credential).setApplicationName(APPLICATION_NAME).build();

            calendarId = configuration.getCalendarId();
            ensureAccessToCalendar(calendarId);

            final EventSink eventSink = new SchedulingEventSink(client, calendarId, new SchedulingEventSink.Callback() {
                @Override
                public void notify(final String id, final long start, final long end, final String summary, final String description) {
                    final Date date = new Date(start);
                    final Duration duration = Duration.milliseconds(end - start);

                    LOG.info("Triggering {}", id);
                    trigger((listener) -> listener.notify(date, duration, summary, Optional.fromNullable(description)));
                }
            });

            Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
                    eventSink,
                    0,
                    configuration.getPollInterval().toSeconds(),
                    TimeUnit.SECONDS
            );
        } catch (GeneralSecurityException | IOException | SchedulerException e) {
            throw Throwables.propagate(e);
        }
    }

    private void ensureAccessToCalendar(final String id) {
        try {
            client.calendars().get(id).execute();
        } catch (IOException e) {
            LOG.debug("Failed to access calendar: " + id, e);
            throw new IllegalArgumentException(String.format("Unable to access calendar '%s'", id));
        }
    }

    @Override
    public void addEvent(final Date start, final Duration duration, final String title, final Optional<String> description) {
        try {
            client.events().insert(calendarId, new Event()
                    .setSummary(title)
                    .setDescription(description.orNull())
                    .setStart(new EventDateTime().setDateTime(new DateTime(start)))
                    .setEnd(new EventDateTime().setDateTime(new DateTime(start.getTime() + duration.toMilliseconds()))))
                    .execute();
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }
}
