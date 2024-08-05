package info.journeymap.shaded.org.slf4j.helpers;

import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.Marker;

public class SubstituteLogger implements Logger {

    private final String name;

    private volatile Logger _delegate;

    public SubstituteLogger(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public boolean isTraceEnabled() {
        return this.delegate().isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        this.delegate().trace(msg);
    }

    @Override
    public void trace(String format, Object arg) {
        this.delegate().trace(format, arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        this.delegate().trace(format, arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        this.delegate().trace(format, arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        this.delegate().trace(msg, t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.delegate().isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.delegate().trace(marker, msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        this.delegate().trace(marker, format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().trace(marker, format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        this.delegate().trace(marker, format, arguments);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.delegate().trace(marker, msg, t);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.delegate().isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        this.delegate().debug(msg);
    }

    @Override
    public void debug(String format, Object arg) {
        this.delegate().debug(format, arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        this.delegate().debug(format, arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        this.delegate().debug(format, arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        this.delegate().debug(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.delegate().isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.delegate().debug(marker, msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.delegate().debug(marker, format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().debug(marker, format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        this.delegate().debug(marker, format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.delegate().debug(marker, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.delegate().isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        this.delegate().info(msg);
    }

    @Override
    public void info(String format, Object arg) {
        this.delegate().info(format, arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        this.delegate().info(format, arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        this.delegate().info(format, arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        this.delegate().info(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.delegate().isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        this.delegate().info(marker, msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.delegate().info(marker, format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().info(marker, format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        this.delegate().info(marker, format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.delegate().info(marker, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.delegate().isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        this.delegate().warn(msg);
    }

    @Override
    public void warn(String format, Object arg) {
        this.delegate().warn(format, arg);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        this.delegate().warn(format, arg1, arg2);
    }

    @Override
    public void warn(String format, Object... arguments) {
        this.delegate().warn(format, arguments);
    }

    @Override
    public void warn(String msg, Throwable t) {
        this.delegate().warn(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.delegate().isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.delegate().warn(marker, msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.delegate().warn(marker, format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().warn(marker, format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        this.delegate().warn(marker, format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.delegate().warn(marker, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.delegate().isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        this.delegate().error(msg);
    }

    @Override
    public void error(String format, Object arg) {
        this.delegate().error(format, arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        this.delegate().error(format, arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        this.delegate().error(format, arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        this.delegate().error(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.delegate().isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        this.delegate().error(marker, msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.delegate().error(marker, format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.delegate().error(marker, format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        this.delegate().error(marker, format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.delegate().error(marker, msg, t);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SubstituteLogger that = (SubstituteLogger) o;
            return this.name.equals(that.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    Logger delegate() {
        return (Logger) (this._delegate != null ? this._delegate : NOPLogger.NOP_LOGGER);
    }

    public void setDelegate(Logger delegate) {
        this._delegate = delegate;
    }
}