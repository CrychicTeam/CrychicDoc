package info.journeymap.shaded.org.eclipse.jetty.util.log;

import info.journeymap.shaded.org.eclipse.jetty.util.Loader;
import info.journeymap.shaded.org.eclipse.jetty.util.Uptime;
import info.journeymap.shaded.org.eclipse.jetty.util.annotation.ManagedAttribute;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Log {

    public static final String EXCEPTION = "EXCEPTION ";

    public static final String IGNORED = "IGNORED EXCEPTION ";

    protected static final Properties __props = new Properties();

    public static String __logClass;

    public static boolean __ignored;

    private static final ConcurrentMap<String, Logger> __loggers = new ConcurrentHashMap();

    private static Logger LOG;

    private static boolean __initialized = false;

    static void loadProperties(String resourceName, Properties props) {
        URL testProps = Loader.getResource(resourceName);
        if (testProps != null) {
            try {
                InputStream in = testProps.openStream();
                Throwable var4 = null;
                try {
                    Properties p = new Properties();
                    p.load(in);
                    for (Object key : p.keySet()) {
                        Object value = p.get(key);
                        if (value != null) {
                            props.put(key, value);
                        }
                    }
                } catch (Throwable var17) {
                    var4 = var17;
                    throw var17;
                } finally {
                    if (in != null) {
                        if (var4 != null) {
                            try {
                                in.close();
                            } catch (Throwable var16) {
                                var4.addSuppressed(var16);
                            }
                        } else {
                            in.close();
                        }
                    }
                }
            } catch (IOException var19) {
                System.err.println("[WARN] Error loading logging config: " + testProps);
                var19.printStackTrace(System.err);
            }
        }
    }

    public static void initialized() {
        synchronized (Log.class) {
            if (!__initialized) {
                __initialized = true;
                Boolean announce = Boolean.parseBoolean(__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.announce", "true"));
                try {
                    Class<?> log_class = __logClass == null ? null : Loader.loadClass(__logClass);
                    if (LOG == null || log_class != null && !LOG.getClass().equals(log_class)) {
                        LOG = (Logger) log_class.newInstance();
                        if (announce) {
                            LOG.debug("Logging to {} via {}", LOG, log_class.getName());
                        }
                    }
                } catch (Throwable var4) {
                    initStandardLogging(var4);
                }
                if (announce && LOG != null) {
                    LOG.info(String.format("Logging initialized @%dms to %s", Uptime.getUptime(), LOG.getClass().getName()));
                }
            }
        }
    }

    private static void initStandardLogging(Throwable e) {
        if (e != null && __ignored) {
            e.printStackTrace(System.err);
        }
        if (LOG == null) {
            Class<?> log_class = StdErrLog.class;
            LOG = new StdErrLog();
            Boolean announce = Boolean.parseBoolean(__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.announce", "true"));
            if (announce) {
                LOG.debug("Logging to {} via {}", LOG, log_class.getName());
            }
        }
    }

    public static Logger getLog() {
        initialized();
        return LOG;
    }

    public static void setLog(Logger log) {
        LOG = log;
        __logClass = null;
    }

    public static Logger getRootLogger() {
        initialized();
        return LOG;
    }

    static boolean isIgnored() {
        return __ignored;
    }

    public static void setLogToParent(String name) {
        ClassLoader loader = Log.class.getClassLoader();
        if (loader != null && loader.getParent() != null) {
            try {
                Class<?> uberlog = loader.getParent().loadClass("info.journeymap.shaded.org.eclipse.jetty.util.log.Log");
                Method getLogger = uberlog.getMethod("getLogger", String.class);
                Object logger = getLogger.invoke(null, name);
                setLog(new LoggerLog(logger));
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        } else {
            setLog(getLogger(name));
        }
    }

    public static Logger getLogger(Class<?> clazz) {
        return getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        initialized();
        if (name == null) {
            return LOG;
        } else {
            Logger logger = (Logger) __loggers.get(name);
            if (logger == null) {
                logger = LOG.getLogger(name);
            }
            return logger;
        }
    }

    static ConcurrentMap<String, Logger> getMutableLoggers() {
        return __loggers;
    }

    @ManagedAttribute("list of all instantiated loggers")
    public static Map<String, Logger> getLoggers() {
        return Collections.unmodifiableMap(__loggers);
    }

    public static Properties getProperties() {
        return __props;
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {

            public Object run() {
                Log.loadProperties("jetty-logging.properties", Log.__props);
                String osName = System.getProperty("os.name");
                if (osName != null && osName.length() > 0) {
                    osName = osName.toLowerCase(Locale.ENGLISH).replace(' ', '-');
                    Log.loadProperties("jetty-logging-" + osName + ".properties", Log.__props);
                }
                Enumeration<String> systemKeyEnum = System.getProperties().propertyNames();
                while (systemKeyEnum.hasMoreElements()) {
                    String key = (String) systemKeyEnum.nextElement();
                    String val = System.getProperty(key);
                    if (val != null) {
                        Log.__props.setProperty(key, val);
                    }
                }
                Log.__logClass = Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.class", "info.journeymap.shaded.org.eclipse.jetty.util.log.Slf4jLog");
                Log.__ignored = Boolean.parseBoolean(Log.__props.getProperty("info.journeymap.shaded.org.eclipse.jetty.util.log.IGNORED", "false"));
                return null;
            }
        });
    }
}