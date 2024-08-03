package de.keksuccino.melody;

import de.keksuccino.melody.platform.Services;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Melody {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final String VERSION = "1.0.2";

    public static final String MOD_LOADER = Services.PLATFORM.getPlatformName();

    public static final String MOD_ID = "melody";

    public static void init() {
        if (Services.PLATFORM.isOnClient()) {
            LOGGER.info("[MELODY] Loading Melody background audio library..");
        } else {
            LOGGER.info("[MELODY] Disabling Melody since it's loaded server-side.");
        }
    }
}