package com.mojang.blaze3d.audio;

import com.mojang.logging.LogUtils;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC10;
import org.slf4j.Logger;

public class OpenAlUtil {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static String alErrorToString(int int0) {
        switch(int0) {
            case 40961:
                return "Invalid name parameter.";
            case 40962:
                return "Invalid enumerated parameter value.";
            case 40963:
                return "Invalid parameter parameter value.";
            case 40964:
                return "Invalid operation.";
            case 40965:
                return "Unable to allocate memory.";
            default:
                return "An unrecognized error occurred.";
        }
    }

    static boolean checkALError(String string0) {
        int $$1 = AL10.alGetError();
        if ($$1 != 0) {
            LOGGER.error("{}: {}", string0, alErrorToString($$1));
            return true;
        } else {
            return false;
        }
    }

    private static String alcErrorToString(int int0) {
        switch(int0) {
            case 40961:
                return "Invalid device.";
            case 40962:
                return "Invalid context.";
            case 40963:
                return "Illegal enum.";
            case 40964:
                return "Invalid value.";
            case 40965:
                return "Unable to allocate memory.";
            default:
                return "An unrecognized error occurred.";
        }
    }

    static boolean checkALCError(long long0, String string1) {
        int $$2 = ALC10.alcGetError(long0);
        if ($$2 != 0) {
            LOGGER.error("{}{}: {}", new Object[] { string1, long0, alcErrorToString($$2) });
            return true;
        } else {
            return false;
        }
    }

    static int audioFormatToOpenAl(AudioFormat audioFormat0) {
        Encoding $$1 = audioFormat0.getEncoding();
        int $$2 = audioFormat0.getChannels();
        int $$3 = audioFormat0.getSampleSizeInBits();
        if ($$1.equals(Encoding.PCM_UNSIGNED) || $$1.equals(Encoding.PCM_SIGNED)) {
            if ($$2 == 1) {
                if ($$3 == 8) {
                    return 4352;
                }
                if ($$3 == 16) {
                    return 4353;
                }
            } else if ($$2 == 2) {
                if ($$3 == 8) {
                    return 4354;
                }
                if ($$3 == 16) {
                    return 4355;
                }
            }
        }
        throw new IllegalArgumentException("Invalid audio format: " + audioFormat0);
    }
}