package info.journeymap.shaded.org.slf4j;

import info.journeymap.shaded.org.slf4j.helpers.NOPLoggerFactory;
import info.journeymap.shaded.org.slf4j.helpers.SubstituteLogger;
import info.journeymap.shaded.org.slf4j.helpers.SubstituteLoggerFactory;
import info.journeymap.shaded.org.slf4j.helpers.Util;
import info.journeymap.shaded.org.slf4j.impl.StaticLoggerBinder;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class LoggerFactory {

    static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";

    static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";

    static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";

    static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";

    static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";

    static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";

    static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";

    static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";

    static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";

    static final int UNINITIALIZED = 0;

    static final int ONGOING_INITIALIZATION = 1;

    static final int FAILED_INITIALIZATION = 2;

    static final int SUCCESSFUL_INITIALIZATION = 3;

    static final int NOP_FALLBACK_INITIALIZATION = 4;

    static int INITIALIZATION_STATE = 0;

    static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();

    static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();

    static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";

    static boolean DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");

    private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.6", "1.7" };

    private static String STATIC_LOGGER_BINDER_PATH = "info/journeymap/shaded/org/slf4j/impl/StaticLoggerBinder.class";

    private LoggerFactory() {
    }

    static void reset() {
        INITIALIZATION_STATE = 0;
        TEMP_FACTORY = new SubstituteLoggerFactory();
    }

    private static final void performInitialization() {
        bind();
        if (INITIALIZATION_STATE == 3) {
            versionSanityCheck();
        }
    }

    private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
        if (msg == null) {
            return false;
        } else {
            return msg.contains("info/journeymap/shaded/org/slf4j/impl/StaticLoggerBinder") ? true : msg.contains("info.journeymap.shaded.org.slf4j.impl.StaticLoggerBinder");
        }
    }

    private static final void bind() {
        try {
            Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
            reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
            StaticLoggerBinder.getSingleton();
            INITIALIZATION_STATE = 3;
            reportActualBinding(staticLoggerBinderPathSet);
            fixSubstitutedLoggers();
        } catch (NoClassDefFoundError var2) {
            String msg = var2.getMessage();
            if (!messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
                failedBinding(var2);
                throw var2;
            }
            INITIALIZATION_STATE = 4;
            Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
            Util.report("Defaulting to no-operation (NOP) logger implementation");
            Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
        } catch (NoSuchMethodError var3) {
            String msg = var3.getMessage();
            if (msg != null && msg.contains("info.journeymap.shaded.org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
                INITIALIZATION_STATE = 2;
                Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
                Util.report("Your binding is version 1.5.5 or earlier.");
                Util.report("Upgrade your binding to version 1.6.x.");
            }
            throw var3;
        } catch (Exception var4) {
            failedBinding(var4);
            throw new IllegalStateException("Unexpected initialization failure", var4);
        }
    }

    static void failedBinding(Throwable t) {
        INITIALIZATION_STATE = 2;
        Util.report("Failed to instantiate SLF4J LoggerFactory", t);
    }

    private static final void fixSubstitutedLoggers() {
        List<SubstituteLogger> loggers = TEMP_FACTORY.getLoggers();
        if (!loggers.isEmpty()) {
            Util.report("The following set of substitute loggers may have been accessed");
            Util.report("during the initialization phase. Logging calls during this");
            Util.report("phase were not honored. However, subsequent logging calls to these");
            Util.report("loggers will work as normally expected.");
            Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
            for (SubstituteLogger subLogger : loggers) {
                subLogger.setDelegate(getLogger(subLogger.getName()));
                Util.report(subLogger.getName());
            }
            TEMP_FACTORY.clear();
        }
    }

    private static final void versionSanityCheck() {
        try {
            String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
            boolean match = false;
            for (String aAPI_COMPATIBILITY_LIST : API_COMPATIBILITY_LIST) {
                if (requested.startsWith(aAPI_COMPATIBILITY_LIST)) {
                    match = true;
                }
            }
            if (!match) {
                Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.asList(API_COMPATIBILITY_LIST).toString());
                Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
            }
        } catch (NoSuchFieldError var6) {
        } catch (Throwable var7) {
            Util.report("Unexpected problem occured during version sanity check", var7);
        }
    }

    private static Set<URL> findPossibleStaticLoggerBinderPathSet() {
        Set<URL> staticLoggerBinderPathSet = new LinkedHashSet();
        try {
            ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
            Enumeration<URL> paths;
            if (loggerFactoryClassLoader == null) {
                paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
            } else {
                paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
            }
            while (paths.hasMoreElements()) {
                URL path = (URL) paths.nextElement();
                staticLoggerBinderPathSet.add(path);
            }
        } catch (IOException var4) {
            Util.report("Error getting resources from path", var4);
        }
        return staticLoggerBinderPathSet;
    }

    private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> staticLoggerBinderPathSet) {
        return staticLoggerBinderPathSet.size() > 1;
    }

    private static void reportMultipleBindingAmbiguity(Set<URL> staticLoggerBinderPathSet) {
        if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
            Util.report("Class path contains multiple SLF4J bindings.");
            for (URL path : staticLoggerBinderPathSet) {
                Util.report("Found binding in [" + path + "]");
            }
            Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
        }
    }

    private static void reportActualBinding(Set<URL> staticLoggerBinderPathSet) {
        if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
            Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
        }
    }

    public static Logger getLogger(String name) {
        ILoggerFactory iLoggerFactory = getILoggerFactory();
        return iLoggerFactory.getLogger(name);
    }

    public static Logger getLogger(Class<?> clazz) {
        Logger logger = getLogger(clazz.getName());
        if (DETECT_LOGGER_NAME_MISMATCH) {
            Class<?> autoComputedCallingClass = Util.getCallingClass();
            if (autoComputedCallingClass != null && nonMatchingClasses(clazz, autoComputedCallingClass)) {
                Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", logger.getName(), autoComputedCallingClass.getName()));
                Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
            }
        }
        return logger;
    }

    private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
        return !autoComputedCallingClass.isAssignableFrom(clazz);
    }

    public static ILoggerFactory getILoggerFactory() {
        if (INITIALIZATION_STATE == 0) {
            INITIALIZATION_STATE = 1;
            performInitialization();
        }
        switch(INITIALIZATION_STATE) {
            case 1:
                return TEMP_FACTORY;
            case 2:
                throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
            case 3:
                return StaticLoggerBinder.getSingleton().getLoggerFactory();
            case 4:
                return NOP_FALLBACK_FACTORY;
            default:
                throw new IllegalStateException("Unreachable code");
        }
    }
}