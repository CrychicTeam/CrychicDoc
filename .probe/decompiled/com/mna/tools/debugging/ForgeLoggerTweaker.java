package com.mna.tools.debugging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.AbstractFilter;

public class ForgeLoggerTweaker {

    private static ForgeLoggerTweaker.MBELogFilter mbeLogFilter = new ForgeLoggerTweaker.MBELogFilter();

    public static void applyLoggerFilter() {
        List<LoggerConfig> foundLog4JLoggers = new ArrayList();
        LoggerContext logContext = (LoggerContext) LogManager.getContext(false);
        Map<String, LoggerConfig> map = logContext.getConfiguration().getLoggers();
        for (LoggerConfig logger : map.values()) {
            if (!foundLog4JLoggers.contains(logger)) {
                logger.addFilter(mbeLogFilter);
                foundLog4JLoggers.add(logger);
            }
        }
    }

    public static void setMinimumLevel(Level minimumLevel) {
        mbeLogFilter.minimumLevel = minimumLevel;
    }

    public static class MBELogFilter extends AbstractFilter implements Filter {

        private Level minimumLevel = Level.DEBUG;

        public boolean isLoggable(LogRecord record) {
            return true;
        }

        public Result filter(LogEvent event) {
            return this.minimumLevel.compareTo(event.getLevel()) < 0 ? Result.DENY : Result.NEUTRAL;
        }
    }
}