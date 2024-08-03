package info.journeymap.shaded.org.slf4j.helpers;

public class NOPLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = -517220405410904473L;

    public static final NOPLogger NOP_LOGGER = new NOPLogger();

    protected NOPLogger() {
    }

    @Override
    public String getName() {
        return "NOP";
    }

    @Override
    public final boolean isTraceEnabled() {
        return false;
    }

    @Override
    public final void trace(String msg) {
    }

    @Override
    public final void trace(String format, Object arg) {
    }

    @Override
    public final void trace(String format, Object arg1, Object arg2) {
    }

    @Override
    public final void trace(String format, Object... argArray) {
    }

    @Override
    public final void trace(String msg, Throwable t) {
    }

    @Override
    public final boolean isDebugEnabled() {
        return false;
    }

    @Override
    public final void debug(String msg) {
    }

    @Override
    public final void debug(String format, Object arg) {
    }

    @Override
    public final void debug(String format, Object arg1, Object arg2) {
    }

    @Override
    public final void debug(String format, Object... argArray) {
    }

    @Override
    public final void debug(String msg, Throwable t) {
    }

    @Override
    public final boolean isInfoEnabled() {
        return false;
    }

    @Override
    public final void info(String msg) {
    }

    @Override
    public final void info(String format, Object arg1) {
    }

    @Override
    public final void info(String format, Object arg1, Object arg2) {
    }

    @Override
    public final void info(String format, Object... argArray) {
    }

    @Override
    public final void info(String msg, Throwable t) {
    }

    @Override
    public final boolean isWarnEnabled() {
        return false;
    }

    @Override
    public final void warn(String msg) {
    }

    @Override
    public final void warn(String format, Object arg1) {
    }

    @Override
    public final void warn(String format, Object arg1, Object arg2) {
    }

    @Override
    public final void warn(String format, Object... argArray) {
    }

    @Override
    public final void warn(String msg, Throwable t) {
    }

    @Override
    public final boolean isErrorEnabled() {
        return false;
    }

    @Override
    public final void error(String msg) {
    }

    @Override
    public final void error(String format, Object arg1) {
    }

    @Override
    public final void error(String format, Object arg1, Object arg2) {
    }

    @Override
    public final void error(String format, Object... argArray) {
    }

    @Override
    public final void error(String msg, Throwable t) {
    }
}