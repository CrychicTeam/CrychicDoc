package info.journeymap.shaded.org.eclipse.jetty.util.log;

import info.journeymap.shaded.org.eclipse.jetty.util.DateCache;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedObject;
import java.io.PrintStream;
import java.security.AccessControlException;
import java.util.Properties;

@ManagedObject("Jetty StdErr Logging Implementation")
public class StdErrLog extends AbstractLogger {

    private static final String EOL = System.getProperty("line.separator");

    private static int __tagpad = Integer.parseInt(Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.StdErrLog.TAG_PAD", "0"));

    private static DateCache _dateCache;

    private static final boolean __source = Boolean.parseBoolean(Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.SOURCE", Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.stderr.SOURCE", "false")));

    private static final boolean __long = Boolean.parseBoolean(Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.stderr.LONG", "false"));

    private static final boolean __escape = Boolean.parseBoolean(Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.stderr.ESCAPE", "true"));

    private int _level = 2;

    private int _configuredLevel;

    private PrintStream _stderr = null;

    private boolean _source = __source;

    private boolean _printLongNames = __long;

    private final String _name;

    protected final String _abbrevname;

    private boolean _hideStacks = false;

    public static void setTagPad(int pad) {
        __tagpad = pad;
    }

    public static int getLoggingLevel(Properties props, String name) {
        int level = lookupLoggingLevel(props, name);
        if (level == -1) {
            level = lookupLoggingLevel(props, "log");
            if (level == -1) {
                level = 2;
            }
        }
        return level;
    }

    public static StdErrLog getLogger(Class<?> clazz) {
        Logger log = Log.getLogger(clazz);
        if (log instanceof StdErrLog) {
            return (StdErrLog) log;
        } else {
            throw new RuntimeException("Logger for " + clazz + " is not of type StdErrLog");
        }
    }

    public StdErrLog() {
        this(null);
    }

    public StdErrLog(String name) {
        this(name, null);
    }

    public StdErrLog(String name, Properties props) {
        if (props != null && props != Log.__props) {
            Log.__props.putAll(props);
        }
        this._name = name == null ? "" : name;
        this._abbrevname = condensePackageString(this._name);
        this._level = getLoggingLevel(Log.__props, this._name);
        this._configuredLevel = this._level;
        try {
            String source = getLoggingProperty(Log.__props, this._name, "SOURCE");
            this._source = source == null ? __source : Boolean.parseBoolean(source);
        } catch (AccessControlException var5) {
            this._source = __source;
        }
        try {
            String stacks = getLoggingProperty(Log.__props, this._name, "STACKS");
            this._hideStacks = stacks == null ? false : !Boolean.parseBoolean(stacks);
        } catch (AccessControlException var4) {
        }
    }

    @Override
    public String getName() {
        return this._name;
    }

    public void setPrintLongNames(boolean printLongNames) {
        this._printLongNames = printLongNames;
    }

    public boolean isPrintLongNames() {
        return this._printLongNames;
    }

    public boolean isHideStacks() {
        return this._hideStacks;
    }

    public void setHideStacks(boolean hideStacks) {
        this._hideStacks = hideStacks;
    }

    public boolean isSource() {
        return this._source;
    }

    public void setSource(boolean source) {
        this._source = source;
    }

    @Override
    public void warn(String msg, Object... args) {
        if (this._level <= 3) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":WARN:", msg, args);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @Override
    public void warn(Throwable thrown) {
        this.warn("", thrown);
    }

    @Override
    public void warn(String msg, Throwable thrown) {
        if (this._level <= 3) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":WARN:", msg, thrown);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @Override
    public void info(String msg, Object... args) {
        if (this._level <= 2) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":INFO:", msg, args);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @Override
    public void info(Throwable thrown) {
        this.info("", thrown);
    }

    @Override
    public void info(String msg, Throwable thrown) {
        if (this._level <= 2) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":INFO:", msg, thrown);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @ManagedAttribute("is debug enabled for root logger Log.LOG")
    @Override
    public boolean isDebugEnabled() {
        return this._level <= 1;
    }

    @Override
    public void setDebugEnabled(boolean enabled) {
        if (enabled) {
            this._level = 1;
            for (Logger log : Log.getLoggers().values()) {
                if (log.getName().startsWith(this.getName()) && log instanceof StdErrLog) {
                    ((StdErrLog) log).setLevel(1);
                }
            }
        } else {
            this._level = this._configuredLevel;
            for (Logger logx : Log.getLoggers().values()) {
                if (logx.getName().startsWith(this.getName()) && logx instanceof StdErrLog) {
                    ((StdErrLog) logx).setLevel(((StdErrLog) logx)._configuredLevel);
                }
            }
        }
    }

    public int getLevel() {
        return this._level;
    }

    public void setLevel(int level) {
        this._level = level;
    }

    public void setStdErrStream(PrintStream stream) {
        this._stderr = stream == System.err ? null : stream;
    }

    @Override
    public void debug(String msg, Object... args) {
        if (this._level <= 1) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":DBUG:", msg, args);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @Override
    public void debug(String msg, long arg) {
        if (this.isDebugEnabled()) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":DBUG:", msg, arg);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    @Override
    public void debug(Throwable thrown) {
        this.debug("", thrown);
    }

    @Override
    public void debug(String msg, Throwable thrown) {
        if (this._level <= 1) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":DBUG:", msg, thrown);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    private void format(StringBuilder buffer, String level, String msg, Object... args) {
        long now = System.currentTimeMillis();
        int ms = (int) (now % 1000L);
        String d = _dateCache.formatNow(now);
        this.tag(buffer, d, ms, level);
        this.format(buffer, msg, args);
    }

    private void format(StringBuilder buffer, String level, String msg, Throwable thrown) {
        this.format(buffer, level, msg);
        if (this.isHideStacks()) {
            this.format(buffer, ": " + String.valueOf(thrown));
        } else {
            this.format(buffer, thrown);
        }
    }

    private void tag(StringBuilder buffer, String d, int ms, String tag) {
        buffer.setLength(0);
        buffer.append(d);
        if (ms > 99) {
            buffer.append('.');
        } else if (ms > 9) {
            buffer.append(".0");
        } else {
            buffer.append(".00");
        }
        buffer.append(ms).append(tag);
        String name = this._printLongNames ? this._name : this._abbrevname;
        String tname = Thread.currentThread().getName();
        int p = __tagpad > 0 ? name.length() + tname.length() - __tagpad : 0;
        if (p < 0) {
            buffer.append(name).append(':').append("                                                  ", 0, -p).append(tname);
        } else if (p == 0) {
            buffer.append(name).append(':').append(tname);
        }
        buffer.append(':');
        if (this._source) {
            Throwable source = new Throwable();
            StackTraceElement[] frames = source.getStackTrace();
            for (int i = 0; i < frames.length; i++) {
                StackTraceElement frame = frames[i];
                String clazz = frame.getClassName();
                if (!clazz.equals(StdErrLog.class.getName()) && !clazz.equals(Log.class.getName())) {
                    if (!this._printLongNames && clazz.startsWith("info.journeymap.shaded.org.eclipse.jetty.")) {
                        buffer.append(condensePackageString(clazz));
                    } else {
                        buffer.append(clazz);
                    }
                    buffer.append('#').append(frame.getMethodName());
                    if (frame.getFileName() != null) {
                        buffer.append('(').append(frame.getFileName()).append(':').append(frame.getLineNumber()).append(')');
                    }
                    buffer.append(':');
                    break;
                }
            }
        }
        buffer.append(' ');
    }

    private void format(StringBuilder builder, String msg, Object... args) {
        if (msg == null) {
            msg = "";
            for (int i = 0; i < args.length; i++) {
                msg = msg + "{} ";
            }
        }
        String braces = "{}";
        int start = 0;
        for (Object arg : args) {
            int bracesIndex = msg.indexOf(braces, start);
            if (bracesIndex < 0) {
                this.escape(builder, msg.substring(start));
                builder.append(" ");
                builder.append(arg);
                start = msg.length();
            } else {
                this.escape(builder, msg.substring(start, bracesIndex));
                builder.append(String.valueOf(arg));
                start = bracesIndex + braces.length();
            }
        }
        this.escape(builder, msg.substring(start));
    }

    private void escape(StringBuilder builder, String string) {
        if (__escape) {
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (Character.isISOControl(c)) {
                    if (c == '\n') {
                        builder.append('|');
                    } else if (c == '\r') {
                        builder.append('<');
                    } else {
                        builder.append('?');
                    }
                } else {
                    builder.append(c);
                }
            }
        } else {
            builder.append(string);
        }
    }

    protected void format(StringBuilder buffer, Throwable thrown) {
        this.format(buffer, thrown, "");
    }

    protected void format(StringBuilder buffer, Throwable thrown, String indent) {
        if (thrown == null) {
            buffer.append("null");
        } else {
            buffer.append(EOL).append(indent);
            this.format(buffer, thrown.toString());
            StackTraceElement[] elements = thrown.getStackTrace();
            for (int i = 0; elements != null && i < elements.length; i++) {
                buffer.append(EOL).append(indent).append("\tat ");
                this.format(buffer, elements[i].toString());
            }
            for (Throwable suppressed : thrown.getSuppressed()) {
                buffer.append(EOL).append(indent).append("Suppressed: ");
                this.format(buffer, suppressed, "\t|" + indent);
            }
            Throwable cause = thrown.getCause();
            if (cause != null && cause != thrown) {
                buffer.append(EOL).append(indent).append("Caused by: ");
                this.format(buffer, cause, indent);
            }
        }
    }

    @Override
    protected Logger newLogger(String fullname) {
        StdErrLog logger = new StdErrLog(fullname);
        logger.setPrintLongNames(this._printLongNames);
        logger._stderr = this._stderr;
        if (this._level != this._configuredLevel) {
            logger._level = this._level;
        }
        return logger;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("StdErrLog:");
        s.append(this._name);
        s.append(":LEVEL=");
        switch(this._level) {
            case 0:
                s.append("ALL");
                break;
            case 1:
                s.append("DEBUG");
                break;
            case 2:
                s.append("INFO");
                break;
            case 3:
                s.append("WARN");
                break;
            default:
                s.append("?");
        }
        return s.toString();
    }

    @Override
    public void ignore(Throwable ignored) {
        if (this._level <= 0) {
            StringBuilder buffer = new StringBuilder(64);
            this.format(buffer, ":IGNORED:", "", ignored);
            (this._stderr == null ? System.err : this._stderr).println(buffer);
        }
    }

    static {
        String[] deprecatedProperties = new String[] { "DEBUG", "info.journeymap.shaded.org.eclipse.jetty.util.log.DEBUG", "info.journeymap.shaded.org.eclipse.jetty.util.log.stderr.DEBUG" };
        for (String deprecatedProp : deprecatedProperties) {
            if (System.getProperty(deprecatedProp) != null) {
                System.err.printf("System Property [%s] has been deprecated! (Use org.eclipse.jetty.LEVEL=DEBUG instead)%n", deprecatedProp);
            }
        }
        try {
            _dateCache = new DateCache("yyyy-MM-dd HH:mm:ss");
        } catch (Exception var5) {
            var5.printStackTrace(System.err);
        }
    }
}