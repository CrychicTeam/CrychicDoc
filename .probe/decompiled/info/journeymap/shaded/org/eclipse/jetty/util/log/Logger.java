package info.journeymap.shaded.org.eclipse.jetty.util.log;

public interface Logger {

    String getName();

    void warn(String var1, Object... var2);

    void warn(Throwable var1);

    void warn(String var1, Throwable var2);

    void info(String var1, Object... var2);

    void info(Throwable var1);

    void info(String var1, Throwable var2);

    boolean isDebugEnabled();

    void setDebugEnabled(boolean var1);

    void debug(String var1, Object... var2);

    void debug(String var1, long var2);

    void debug(Throwable var1);

    void debug(String var1, Throwable var2);

    Logger getLogger(String var1);

    void ignore(Throwable var1);
}