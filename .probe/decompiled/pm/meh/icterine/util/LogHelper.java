package pm.meh.icterine.util;

import java.util.function.Supplier;
import pm.meh.icterine.Common;

public class LogHelper {

    private static boolean initialized = false;

    private static boolean debugEnabled = false;

    private static void init() {
        if (!initialized && Common.config != null) {
            initialized = true;
            debugEnabled = Common.config.DEBUG_MODE;
        }
    }

    public static void debug(String msg) {
        init();
        if (debugEnabled) {
            Common.LOG.info(msg);
        }
    }

    public static void debug(Supplier<String> msg) {
        init();
        if (debugEnabled) {
            Common.LOG.info((String) msg.get());
        }
    }
}