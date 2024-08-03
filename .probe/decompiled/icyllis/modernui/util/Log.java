package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.config.Configurator;
import org.intellij.lang.annotations.PrintFormat;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Log {

    @Internal
    public static final Logger LOGGER = LogManager.getLogger("ModernUI");

    public static final int TRACE = 2;

    public static final int DEBUG = 3;

    public static final int INFO = 4;

    public static final int WARN = 5;

    public static final int ERROR = 6;

    public static final int FATAL = 7;

    private Log() {
    }

    public static void trace(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.trace(msg);
        } else {
            LOGGER.trace(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void trace(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.trace(msg, params);
        } else {
            LOGGER.trace(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void trace(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.trace(msg, throwable);
        } else {
            LOGGER.trace(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void debug(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.debug(msg);
        } else {
            LOGGER.debug(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void debug(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.debug(msg, params);
        } else {
            LOGGER.debug(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void debug(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.debug(msg, throwable);
        } else {
            LOGGER.debug(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void info(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.info(msg);
        } else {
            LOGGER.info(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void info(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.info(msg, params);
        } else {
            LOGGER.info(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void info(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.info(msg, throwable);
        } else {
            LOGGER.info(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void warn(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.warn(msg);
        } else {
            LOGGER.warn(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void warn(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.warn(msg, params);
        } else {
            LOGGER.warn(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void warn(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.warn(msg, throwable);
        } else {
            LOGGER.warn(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void error(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.error(msg);
        } else {
            LOGGER.error(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void error(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.error(msg, params);
        } else {
            LOGGER.error(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void error(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.error(msg, throwable);
        } else {
            LOGGER.error(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void fatal(@Nullable String tag, @NonNull String msg) {
        if (tag == null) {
            LOGGER.fatal(msg);
        } else {
            LOGGER.fatal(MarkerManager.getMarker(tag), msg);
        }
    }

    public static void fatal(@Nullable String tag, @NonNull String msg, Object... params) {
        if (tag == null) {
            LOGGER.fatal(msg, params);
        } else {
            LOGGER.fatal(MarkerManager.getMarker(tag), msg, params);
        }
    }

    public static void fatal(@Nullable String tag, @NonNull String msg, Throwable throwable) {
        if (tag == null) {
            LOGGER.fatal(msg, throwable);
        } else {
            LOGGER.fatal(MarkerManager.getMarker(tag), msg, throwable);
        }
    }

    public static void catching(Throwable throwable) {
        LOGGER.catching(throwable);
    }

    public static <T extends Throwable> T throwing(T throwable) {
        return (T) LOGGER.throwing(throwable);
    }

    public static void printf(int level, @Nullable String tag, @PrintFormat String format, Object... params) {
        org.apache.logging.log4j.Level logLevel = toLogLevel(level);
        if (tag == null) {
            LOGGER.printf(logLevel, format, params);
        } else {
            LOGGER.printf(logLevel, MarkerManager.getMarker(tag), format, params);
        }
    }

    @Internal
    public static void setLevel(int level) {
        Configurator.setLevel(LOGGER, toLogLevel(level));
    }

    private static org.apache.logging.log4j.Level toLogLevel(int level) {
        return switch(level) {
            case 2 ->
                org.apache.logging.log4j.Level.TRACE;
            case 3 ->
                org.apache.logging.log4j.Level.DEBUG;
            case 4 ->
                org.apache.logging.log4j.Level.INFO;
            case 5 ->
                org.apache.logging.log4j.Level.WARN;
            case 6 ->
                org.apache.logging.log4j.Level.ERROR;
            case 7 ->
                org.apache.logging.log4j.Level.FATAL;
            default ->
                throw new IllegalArgumentException();
        };
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Level {
    }
}