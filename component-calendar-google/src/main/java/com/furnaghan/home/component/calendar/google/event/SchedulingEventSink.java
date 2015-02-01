package com.furnaghan.home.component.calendar.google.event;

import com.google.api.client.repackaged.com.google.common.base.Throwables;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.PropertySettingJobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SchedulingEventSink extends EventSink {

    private static final String STATUS_CANCELLED = "cancelled";
    private static final String STATUS_CONFIRMED = "confirmed";

    public static interface Callback {
        void notify(final String id, final long start, final long end, final String summary, final String description);
    }

    private static final Logger LOG = LoggerFactory.getLogger(SchedulingEventSink.class);

    public class EventJob implements Job {

        private String id;
        private long start;
        private long end;
        private String summary;
        private String description;

        @SuppressWarnings("unused")
        public void setId(final String id) {
            this.id = id;
        }

        @SuppressWarnings("unused")
        public void setStart(final long start) {
            this.start = start;
        }

        @SuppressWarnings("unused")
        public void setEnd(final long end) {
            this.end = end;
        }

        @SuppressWarnings("unused")
        public void setSummary(final String summary) {
            this.summary = summary;
        }

        @SuppressWarnings("unused")
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public void execute(final JobExecutionContext context) throws JobExecutionException {
            callback.notify(id, start, end, summary, description);
        }
    }

    private final Callback callback;
    private final Scheduler scheduler;

    public SchedulingEventSink(final Calendar client, final String calendarId, final Callback callback) throws SchedulerException {
        super(client, calendarId);

        this.callback = callback;

        scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(new PropertySettingJobFactory() {
            @Override
            public EventJob newJob(final TriggerFiredBundle bundle, final Scheduler scheduler) throws SchedulerException {
                final EventJob job = new EventJob();

                JobDataMap jobDataMap = new JobDataMap();
                jobDataMap.putAll(scheduler.getContext());
                jobDataMap.putAll(bundle.getJobDetail().getJobDataMap());
                jobDataMap.putAll(bundle.getTrigger().getJobDataMap());

                setBeanProps(job, jobDataMap);

                return job;
            }
        });
        scheduler.start();
    }

    @Override
    protected void syncEvent(final Event event) {
        try {
            final JobKey key = JobKey.jobKey(event.getId(), getCalendarId());

            // TODO: Is this a new event or just a change to one? If it's a change, modify the old job rather than create a new one

            // Ignore tentative events
            switch (event.getStatus()) {
                case STATUS_CANCELLED: {
                    LOG.trace("Cancelling {}", event.getId());
                    scheduler.deleteJob(key);
                    break;
                }

                case STATUS_CONFIRMED: {
                    final JobDetail job = JobBuilder.newJob(EventJob.class)
                            .withIdentity(key)
                            .usingJobData("id", event.getId())
                            .usingJobData("start", event.getStart().getDateTime().getValue())
                            .usingJobData("end", event.getEnd().getDateTime().getValue())
                            .usingJobData("summary", event.getSummary())
                            .usingJobData("description", event.getDescription())
                            .build();

                    final Trigger trigger = TriggerBuilder.newTrigger()
                            .withIdentity(TriggerKey.triggerKey(event.getId(), getCalendarId()))
                            .startAt(new Date(event.getStart().getDateTime().getValue()))
                            .build();

                    LOG.trace("Scheduling {} at {}", event.getId(), event.getStart());
                    scheduler.scheduleJob(job, trigger);
                    break;
                }
            }
        } catch (SchedulerException e) {
            throw Throwables.propagate(e);
        }
    }
}
