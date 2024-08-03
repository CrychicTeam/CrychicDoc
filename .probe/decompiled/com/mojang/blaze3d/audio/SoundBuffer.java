package com.mojang.blaze3d.audio;

import java.nio.ByteBuffer;
import java.util.OptionalInt;
import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import org.lwjgl.openal.AL10;

public class SoundBuffer {

    @Nullable
    private ByteBuffer data;

    private final AudioFormat format;

    private boolean hasAlBuffer;

    private int alBuffer;

    public SoundBuffer(ByteBuffer byteBuffer0, AudioFormat audioFormat1) {
        this.data = byteBuffer0;
        this.format = audioFormat1;
    }

    OptionalInt getAlBuffer() {
        if (!this.hasAlBuffer) {
            if (this.data == null) {
                return OptionalInt.empty();
            }
            int $$0 = OpenAlUtil.audioFormatToOpenAl(this.format);
            int[] $$1 = new int[1];
            AL10.alGenBuffers($$1);
            if (OpenAlUtil.checkALError("Creating buffer")) {
                return OptionalInt.empty();
            }
            AL10.alBufferData($$1[0], $$0, this.data, (int) this.format.getSampleRate());
            if (OpenAlUtil.checkALError("Assigning buffer data")) {
                return OptionalInt.empty();
            }
            this.alBuffer = $$1[0];
            this.hasAlBuffer = true;
            this.data = null;
        }
        return OptionalInt.of(this.alBuffer);
    }

    public void discardAlBuffer() {
        if (this.hasAlBuffer) {
            AL10.alDeleteBuffers(new int[] { this.alBuffer });
            if (OpenAlUtil.checkALError("Deleting stream buffers")) {
                return;
            }
        }
        this.hasAlBuffer = false;
    }

    public OptionalInt releaseAlBuffer() {
        OptionalInt $$0 = this.getAlBuffer();
        this.hasAlBuffer = false;
        return $$0;
    }
}