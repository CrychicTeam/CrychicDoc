package de.keksuccino.melody.resources.audio.openal;

import de.keksuccino.melody.mixin.mixins.common.client.IMixinSoundEngine;
import de.keksuccino.melody.mixin.mixins.common.client.IMixinSoundManager;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public class ALUtils {

    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isOpenAlReady() {
        SoundManager manager = Minecraft.getInstance().getSoundManager();
        SoundEngine engine = ((IMixinSoundManager) manager).getSoundEngineMelody();
        return ((IMixinSoundEngine) engine).getLoadedMelody();
    }

    public static int getAudioFormatAsOpenAL(@NotNull AudioFormat audioFormat) throws ALException {
        Encoding encoding = audioFormat.getEncoding();
        int channels = audioFormat.getChannels();
        int sampleSize = audioFormat.getSampleSizeInBits();
        if (encoding.equals(Encoding.PCM_UNSIGNED) || encoding.equals(Encoding.PCM_SIGNED)) {
            if (channels == 1) {
                if (sampleSize == 8) {
                    return 4352;
                }
                if (sampleSize == 16) {
                    return 4353;
                }
            } else if (channels == 2) {
                if (sampleSize == 8) {
                    return 4354;
                }
                if (sampleSize == 16) {
                    return 4355;
                }
            }
        }
        throw new ALException("Failed to convert AudioFormat to OpenAL! Unsupported format: " + audioFormat);
    }

    @NotNull
    public static ByteBuffer readStreamIntoBuffer(@NotNull InputStream audioInputStream) throws Exception {
        byte[] array = audioInputStream.readAllBytes();
        ByteBuffer buffer = ByteBuffer.allocateDirect(array.length);
        buffer.put(array);
        buffer.flip();
        return buffer;
    }
}