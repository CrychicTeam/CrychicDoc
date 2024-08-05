package info.journeymap.shaded.org.slf4j.helpers;

import info.journeymap.shaded.org.slf4j.Logger;
import info.journeymap.shaded.org.slf4j.Marker;

public abstract class MarkerIgnoringBase extends NamedLoggerBase implements Logger {

    private static final long serialVersionUID = 9044267456635152283L;

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return this.isTraceEnabled();
    }

    @Override
    public void trace(Marker marker, String msg) {
        this.trace(msg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        this.trace(format, arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        this.trace(format, arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... arguments) {
        this.trace(format, arguments);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        this.trace(msg, t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return this.isDebugEnabled();
    }

    @Override
    public void debug(Marker marker, String msg) {
        this.debug(msg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        this.debug(format, arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        this.debug(format, arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        this.debug(format, arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        this.debug(msg, t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return this.isInfoEnabled();
    }

    @Override
    public void info(Marker marker, String msg) {
        this.info(msg);
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        this.info(format, arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        this.info(format, arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        this.info(format, arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        this.info(msg, t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return this.isWarnEnabled();
    }

    @Override
    public void warn(Marker marker, String msg) {
        this.warn(msg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        this.warn(format, arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        this.warn(format, arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        this.warn(format, arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        this.warn(msg, t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return this.isErrorEnabled();
    }

    @Override
    public void error(Marker marker, String msg) {
        this.error(msg);
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        this.error(format, arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        this.error(format, arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        this.error(format, arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        this.error(msg, t);
    }

    public String toString() {
        return this.getClass().getName() + "(" + this.getName() + ")";
    }
}