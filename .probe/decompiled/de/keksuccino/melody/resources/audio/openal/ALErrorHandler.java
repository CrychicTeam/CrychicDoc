package de.keksuccino.melody.resources.audio.openal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL10;

public class ALErrorHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    @Nullable
    public static String getOpenAlError() {
        int errorResult = AL10.alGetError();
        return errorResult != 0 ? AL10.alGetString(errorResult) : null;
    }

    public static void checkOpenAlError() throws ALException {
        String error = getOpenAlError();
        if (error != null) {
            throw new ALException(error);
        }
    }

    public static boolean checkAndPrintOpenAlError() {
        String error = getOpenAlError();
        if (error != null) {
            LOGGER.error("Error while handling OpenAL audio!", new ALException(error));
            return true;
        } else {
            return false;
        }
    }
}