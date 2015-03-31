package com.furnaghan.home.component.notifier.logging;

import com.furnaghan.home.component.Component;
import com.furnaghan.home.component.notifier.NotifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingNotifierComponent extends Component<NotifierType.Listener> implements NotifierType {

    private final Logger logger;
    private final Level level;
    private final String template;

    public LoggingNotifierComponent(final LoggingNotifierConfiguration configuration) {
        logger = LoggerFactory.getLogger(configuration.getLoggerName());
        level = configuration.getLevel();
        template = configuration.getTemplate();
    }

    @Override
    public void send(final String title, final String message) {
        switch (level) {
            case TRACE:
                logger.trace(template, title, message);
                break;
            case DEBUG:
                logger.debug(template, title, message);
                break;
            case INFO:
                logger.info(template, title, message);
                break;
            case WARN:
                logger.warn(template, title, message);
                break;
            case ERROR:
                logger.error(template, title, message);
                break;
        }
    }
}
