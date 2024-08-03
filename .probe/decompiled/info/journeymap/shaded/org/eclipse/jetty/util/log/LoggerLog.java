package info.journeymap.shaded.org.eclipse.jetty.util.log;

import java.lang.reflect.Method;

public class LoggerLog extends AbstractLogger {

    private final Object _logger;

    private final Method _debugMT;

    private final Method _debugMAA;

    private final Method _infoMT;

    private final Method _infoMAA;

    private final Method _warnMT;

    private final Method _warnMAA;

    private final Method _setDebugEnabledE;

    private final Method _getLoggerN;

    private final Method _getName;

    private volatile boolean _debug;

    public LoggerLog(Object logger) {
        try {
            this._logger = logger;
            Class<?> lc = logger.getClass();
            this._debugMT = lc.getMethod("debug", String.class, Throwable.class);
            this._debugMAA = lc.getMethod("debug", String.class, Object[].class);
            this._infoMT = lc.getMethod("info", String.class, Throwable.class);
            this._infoMAA = lc.getMethod("info", String.class, Object[].class);
            this._warnMT = lc.getMethod("warn", String.class, Throwable.class);
            this._warnMAA = lc.getMethod("warn", String.class, Object[].class);
            Method _isDebugEnabled = lc.getMethod("isDebugEnabled");
            this._setDebugEnabledE = lc.getMethod("setDebugEnabled", boolean.class);
            this._getLoggerN = lc.getMethod("getLogger", String.class);
            this._getName = lc.getMethod("getName");
            this._debug = (Boolean) _isDebugEnabled.invoke(this._logger);
        } catch (Exception var4) {
            throw new IllegalStateException(var4);
        }
    }

    @Override
    public String getName() {
        try {
            return (String) this._getName.invoke(this._logger);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    @Override
    public void warn(String msg, Object... args) {
        try {
            this._warnMAA.invoke(this._logger, args);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public void warn(Throwable thrown) {
        this.warn("", thrown);
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        try {
            this._warnMT.invoke(this._logger, msg, thrown);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public void info(String msg, Object... args) {
        try {
            this._infoMAA.invoke(this._logger, args);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public void info(Throwable thrown) {
        this.info("", thrown);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        try {
            this._infoMT.invoke(this._logger, msg, thrown);
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    @Override
    public boolean isDebugEnabled() {
        return this._debug;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
        try {
            this._setDebugEnabledE.invoke(this._logger, enabled);
            this._debug = enabled;
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    @Override
    public void debug(String msg, Object... args) {
        if (this._debug) {
            try {
                this._debugMAA.invoke(this._logger, args);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }

    @Override
    public void debug(Throwable thrown) {
        this.debug("", thrown);
    }

    @Override
    public void debug(String msg, Throwable th) {
        if (this._debug) {
            try {
                this._debugMT.invoke(this._logger, msg, th);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }
    }

    @Override
    public void debug(String msg, long value) {
        if (this._debug) {
            try {
                this._debugMAA.invoke(this._logger, new Long(value));
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }

    @Override
    public void ignore(Throwable ignored) {
        if (Log.isIgnored()) {
            this.debug("IGNORED EXCEPTION ", ignored);
        }
    }

    @Override
    protected Logger newLogger(String fullname) {
        try {
            Object logger = this._getLoggerN.invoke(this._logger, fullname);
            return new LoggerLog(logger);
        } catch (Exception var3) {
            var3.printStackTrace();
            return this;
        }
    }
}