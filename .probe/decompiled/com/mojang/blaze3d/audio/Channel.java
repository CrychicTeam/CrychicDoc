package com.mojang.blaze3d.audio;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Nullable;
import javax.sound.sampled.AudioFormat;
import net.minecraft.client.sounds.AudioStream;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.openal.AL10;
import org.slf4j.Logger;

public class Channel {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int QUEUED_BUFFER_COUNT = 4;

    public static final int BUFFER_DURATION_SECONDS = 1;

    private final int source;

    private final AtomicBoolean initialized = new AtomicBoolean(true);

    private int streamingBufferSize = 16384;

    @Nullable
    private AudioStream stream;

    @Nullable
    static Channel create() {
        int[] $$0 = new int[1];
        AL10.alGenSources($$0);
        return OpenAlUtil.checkALError("Allocate new source") ? null : new Channel($$0[0]);
    }

    private Channel(int int0) {
        this.source = int0;
    }

    public void destroy() {
        if (this.initialized.compareAndSet(true, false)) {
            AL10.alSourceStop(this.source);
            OpenAlUtil.checkALError("Stop");
            if (this.stream != null) {
                try {
                    this.stream.close();
                } catch (IOException var2) {
                    LOGGER.error("Failed to close audio stream", var2);
                }
                this.removeProcessedBuffers();
                this.stream = null;
            }
            AL10.alDeleteSources(new int[] { this.source });
            OpenAlUtil.checkALError("Cleanup");
        }
    }

    public void play() {
        AL10.alSourcePlay(this.source);
    }

    private int getState() {
        return !this.initialized.get() ? 4116 : AL10.alGetSourcei(this.source, 4112);
    }

    public void pause() {
        if (this.getState() == 4114) {
            AL10.alSourcePause(this.source);
        }
    }

    public void unpause() {
        if (this.getState() == 4115) {
            AL10.alSourcePlay(this.source);
        }
    }

    public void stop() {
        if (this.initialized.get()) {
            AL10.alSourceStop(this.source);
            OpenAlUtil.checkALError("Stop");
        }
    }

    public boolean playing() {
        return this.getState() == 4114;
    }

    public boolean stopped() {
        return this.getState() == 4116;
    }

    public void setSelfPosition(Vec3 vec0) {
        AL10.alSourcefv(this.source, 4100, new float[] { (float) vec0.x, (float) vec0.y, (float) vec0.z });
    }

    public void setPitch(float float0) {
        AL10.alSourcef(this.source, 4099, float0);
    }

    public void setLooping(boolean boolean0) {
        AL10.alSourcei(this.source, 4103, boolean0 ? 1 : 0);
    }

    public void setVolume(float float0) {
        AL10.alSourcef(this.source, 4106, float0);
    }

    public void disableAttenuation() {
        AL10.alSourcei(this.source, 53248, 0);
    }

    public void linearAttenuation(float float0) {
        AL10.alSourcei(this.source, 53248, 53251);
        AL10.alSourcef(this.source, 4131, float0);
        AL10.alSourcef(this.source, 4129, 1.0F);
        AL10.alSourcef(this.source, 4128, 0.0F);
    }

    public void setRelative(boolean boolean0) {
        AL10.alSourcei(this.source, 514, boolean0 ? 1 : 0);
    }

    public void attachStaticBuffer(SoundBuffer soundBuffer0) {
        soundBuffer0.getAlBuffer().ifPresent(p_83676_ -> AL10.alSourcei(this.source, 4105, p_83676_));
    }

    public void attachBufferStream(AudioStream audioStream0) {
        this.stream = audioStream0;
        AudioFormat $$1 = audioStream0.getFormat();
        this.streamingBufferSize = calculateBufferSize($$1, 1);
        this.pumpBuffers(4);
    }

    private static int calculateBufferSize(AudioFormat audioFormat0, int int1) {
        return (int) ((float) (int1 * audioFormat0.getSampleSizeInBits()) / 8.0F * (float) audioFormat0.getChannels() * audioFormat0.getSampleRate());
    }

    private void pumpBuffers(int int0) {
        if (this.stream != null) {
            try {
                for (int $$1 = 0; $$1 < int0; $$1++) {
                    ByteBuffer $$2 = this.stream.read(this.streamingBufferSize);
                    if ($$2 != null) {
                        new SoundBuffer($$2, this.stream.getFormat()).releaseAlBuffer().ifPresent(p_83669_ -> AL10.alSourceQueueBuffers(this.source, new int[] { p_83669_ }));
                    }
                }
            } catch (IOException var4) {
                LOGGER.error("Failed to read from audio stream", var4);
            }
        }
    }

    public void updateStream() {
        if (this.stream != null) {
            int $$0 = this.removeProcessedBuffers();
            this.pumpBuffers($$0);
        }
    }

    private int removeProcessedBuffers() {
        int $$0 = AL10.alGetSourcei(this.source, 4118);
        if ($$0 > 0) {
            int[] $$1 = new int[$$0];
            AL10.alSourceUnqueueBuffers(this.source, $$1);
            OpenAlUtil.checkALError("Unqueue buffers");
            AL10.alDeleteBuffers($$1);
            OpenAlUtil.checkALError("Remove processed buffers");
        }
        return $$0;
    }
}