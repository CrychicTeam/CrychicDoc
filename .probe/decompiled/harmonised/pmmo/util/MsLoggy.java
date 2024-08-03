package harmonised.pmmo.util;

import harmonised.pmmo.config.Config;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import org.apache.logging.log4j.LogManager;

public enum MsLoggy {

    INFO(code -> Config.INFO_LOGGING.get().contains(code.code), (message, args) -> LogManager.getLogger().info(message, args)), WARN(code -> Config.WARN_LOGGING.get().contains(code.code), (message, args) -> LogManager.getLogger().warn(message, args)), DEBUG(code -> Config.DEBUG_LOGGING.get().contains(code.code), (message, args) -> LogManager.getLogger().debug(message, args)), ERROR(code -> Config.ERROR_LOGGING.get().contains(code.code), (message, args) -> LogManager.getLogger().error(message, args)), FATAL(code -> Config.FATAL_LOGGING.get().contains(code.code), (message, args) -> LogManager.getLogger().fatal(message, args));

    private Predicate<MsLoggy.LOG_CODE> validator;

    private BiConsumer<String, Object[]> logExecutor;

    private MsLoggy(Predicate<MsLoggy.LOG_CODE> validator, BiConsumer<String, Object[]> logger) {
        this.validator = validator;
        this.logExecutor = logger;
    }

    public void log(MsLoggy.LOG_CODE code, String message, Object... obj) {
        if (this.validator.test(code)) {
            this.logExecutor.accept(message, obj);
        }
    }

    public <T> void log(MsLoggy.LOG_CODE code, Collection<T> array, String message, Object... obj) {
        if (this.validator.test(code)) {
            array.forEach(entry -> {
                Object[] params = new Object[obj.length + 1];
                params[0] = entry;
                for (int i = 0; i < obj.length; i++) {
                    params[i + 1] = obj[i];
                }
                this.logExecutor.accept(message, params);
            });
        }
    }

    public <K, V> void log(MsLoggy.LOG_CODE code, Map<K, V> map, String message, Object... obj) {
        if (this.validator.test(code)) {
            map.forEach((key, value) -> {
                Object[] params = new Object[obj.length + 2];
                params[0] = key;
                params[1] = value;
                for (int i = 0; i < obj.length; i++) {
                    params[i + 2] = obj[i];
                }
                this.logExecutor.accept(message, params);
            });
        }
    }

    public <VALUE> VALUE logAndReturn(VALUE value, MsLoggy.LOG_CODE code, String message, Object... obj) {
        if (this.validator.test(code)) {
            Object[] params = new Object[obj.length + 1];
            params[0] = value;
            for (int i = 0; i < obj.length; i++) {
                params[i + 1] = obj[i];
            }
            this.logExecutor.accept(message, params);
        }
        return value;
    }

    public static String mapToString(Map<?, ?> map) {
        String out = "";
        for (Entry<?, ?> entry : map.entrySet()) {
            out = out + "{" + entry.getKey().toString() + ":" + entry.getValue().toString() + "}";
        }
        return out;
    }

    public static <T> String listToString(Collection<T> list) {
        String out = "[";
        for (T entry : list) {
            out = out + entry.toString() + ", ";
        }
        return out + "]";
    }

    public static enum LOG_CODE {

        API("api"),
        AUTO_VALUES("autovalues"),
        CHUNK("chunk"),
        DATA("data"),
        EVENT("event"),
        FEATURE("feature"),
        PERKS("perks"),
        GUI("gui"),
        LOADING("loading"),
        NETWORK("network"),
        XP("xp"),
        NONE("none");

        public String code;

        private LOG_CODE(String code) {
            this.code = code;
        }
    }
}