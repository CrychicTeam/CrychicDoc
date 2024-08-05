package journeymap.client.log;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.feature.FeatureManager;
import journeymap.client.io.FileHandler;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.RandomAccessFileAppender;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.message.SimpleMessage;

public class JMLogger {

    public static final String DEPRECATED_LOG_FILE = "journeyMap.log";

    public static final String LOG_FILE = "journeymap.log";

    private static final HashSet<Integer> singletonErrors = new HashSet();

    private static final AtomicInteger singletonErrorsCounter = new AtomicInteger(0);

    private static RandomAccessFileAppender fileAppender;

    public static Logger init() {
        Logger logger = LogManager.getLogger("journeymap");
        if (!logger.isInfoEnabled()) {
            logger.warn("Forge is surpressing INFO-level logging. If you need technical support for JourneyMap, you must return logging to INFO.");
        }
        try {
            File deprecatedLog = new File(FileHandler.getJourneyMapDir(), "journeyMap.log");
            if (deprecatedLog.exists()) {
                deprecatedLog.delete();
            }
        } catch (Exception var5) {
            logger.error("Error removing deprecated logfile: " + var5.getMessage());
        }
        try {
            File logFile = getLogFile();
            if (logFile.exists()) {
                logFile.delete();
            } else {
                logFile.getParentFile().mkdirs();
            }
            PatternLayout layout = PatternLayout.createLayout("[%d{HH:mm:ss}] [%t/%level] [%C{1}] %msg%n", null, null, null, null, true, false, null, null);
            fileAppender = RandomAccessFileAppender.createAppender(logFile.getAbsolutePath(), "treu", "journeymap-logfile", "true", null, "true", layout, null, "false", null, null);
            ((org.apache.logging.log4j.core.Logger) logger).addAppender(fileAppender);
            if (!fileAppender.isStarted()) {
                fileAppender.start();
            }
            logger.info("JourneyMap log initialized.");
        } catch (SecurityException var3) {
            logger.error("Error adding file handler: " + LogFormatter.toString(var3));
        } catch (Throwable var4) {
            logger.error("Error adding file handler: " + LogFormatter.toString(var4));
        }
        return logger;
    }

    public static void setLevelFromProperties() {
        try {
            Logger logger = LogManager.getLogger("journeymap");
            ((org.apache.logging.log4j.core.Logger) logger).setLevel(Level.toLevel(JourneymapClient.getInstance().getCoreProperties().logLevel.get(), Level.INFO));
        } catch (Throwable var1) {
            var1.printStackTrace();
        }
    }

    public static void logProperties() {
        LogEvent record = new Log4jLogEvent(JourneymapClient.MOD_NAME, MarkerManager.getMarker(JourneymapClient.MOD_NAME), null, Level.INFO, new SimpleMessage(getPropertiesSummary()), null);
        if (fileAppender != null) {
            fileAppender.append(record);
        }
    }

    public static String getPropertiesSummary() {
        LinkedHashMap<String, String> props = new LinkedHashMap();
        props.put("Version", JourneymapClient.MOD_NAME + ", built with " + Journeymap.LOADER_NAME + " " + Journeymap.LOADER_VERSION);
        props.put(Journeymap.LOADER_NAME, LoaderHooks.getLoaderVersion());
        List<String> envProps = Arrays.asList("os.name, os.arch, java.version, user.country, user.language");
        StringBuilder sb = new StringBuilder();
        for (String env : envProps) {
            sb.append(env).append("=").append(System.getProperty(env)).append(", ");
        }
        sb.append("game language=").append(Minecraft.getInstance().options.languageCode).append(", ");
        sb.append("locale=").append(Constants.getLocale());
        props.put("Environment", sb.toString());
        sb = new StringBuilder();
        for (Entry<String, String> prop : props.entrySet()) {
            if (sb.length() > 0) {
                sb.append(LogFormatter.LINEBREAK);
            }
            sb.append((String) prop.getKey()).append(": ").append((String) prop.getValue());
        }
        sb.append(LogFormatter.LINEBREAK).append(FeatureManager.getInstance().getPolicyDetails());
        JourneymapClient jm = JourneymapClient.getInstance();
        for (PropertiesBase config : Arrays.asList(jm.getMiniMapProperties1(), jm.getMiniMapProperties2(), jm.getFullMapProperties(), jm.getWaypointProperties(), jm.getWebMapProperties(), jm.getCoreProperties())) {
            sb.append(LogFormatter.LINEBREAK).append(config);
        }
        return sb.toString();
    }

    public static File getLogFile() {
        return new File(FileHandler.getJourneyMapDir(), "journeymap.log");
    }

    public static void logOnce(String text) {
        throwLogOnce(text, null, false);
    }

    public static void throwLogOnce(String text, Throwable throwable) {
        throwLogOnce(text, throwable, true);
    }

    private static void throwLogOnce(String text, Throwable throwable, boolean shouldThrow) {
        if (!singletonErrors.contains(text.hashCode())) {
            singletonErrors.add(text.hashCode());
            if (throwable != null) {
                Journeymap.getLogger().error(LogFormatter.toString(throwable));
            } else if (shouldThrow) {
                Journeymap.getLogger().warn(new Throwable(text));
            } else {
                Journeymap.getLogger().warn(text);
            }
        } else {
            int count = singletonErrorsCounter.incrementAndGet();
            if (count > 1000) {
                singletonErrors.clear();
                singletonErrorsCounter.set(0);
            }
        }
    }

    public static class LogLevelStringProvider implements StringField.ValuesProvider {

        @Override
        public List<String> getStrings() {
            Level[] levels = Level.values();
            String[] levelStrings = new String[levels.length];
            for (int i = 0; i < levels.length; i++) {
                levelStrings[i] = levels[i].toString();
            }
            return Arrays.asList(levelStrings);
        }

        @Override
        public String getDefaultString() {
            return Level.INFO.toString();
        }
    }
}