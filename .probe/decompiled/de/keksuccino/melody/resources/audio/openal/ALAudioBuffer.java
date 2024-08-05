package de.keksuccino.melody.resources.audio.openal;

import java.nio.ByteBuffer;
import java.util.Objects;
import javax.sound.sampled.AudioFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.openal.AL10;

public class ALAudioBuffer {

    private ByteBuffer dataBuffer;

    private final AudioFormat audioFormat;

    private boolean bufferPrepared;

    private Integer source;

    public ALAudioBuffer(@NotNull ByteBuffer dataBuffer, @NotNull AudioFormat audioFormat) {
        this.dataBuffer = (ByteBuffer) Objects.requireNonNull(dataBuffer);
        this.audioFormat = (AudioFormat) Objects.requireNonNull(audioFormat);
    }

    @Nullable
    public Integer getSource() {
        return this.prepare() ? this.source : null;
    }

    public boolean prepare() {
        try {
            if (!this.bufferPrepared) {
                this.bufferPrepared = true;
                int audioFormat = ALUtils.getAudioFormatAsOpenAL(this.audioFormat);
                int[] bufferSource = new int[1];
                AL10.alGenBuffers(bufferSource);
                ALErrorHandler.checkOpenAlError();
                this.source = bufferSource[0];
                AL10.alBufferData(bufferSource[0], audioFormat, this.dataBuffer, (int) this.audioFormat.getSampleRate());
                ALErrorHandler.checkOpenAlError();
                this.dataBuffer = null;
            }
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            this.dataBuffer = null;
            return false;
        }
    }

    public void delete() throws ALException {
        this.bufferPrepared = false;
        if (this.isValidOpenAlSource()) {
            AL10.alDeleteBuffers(new int[] { this.source });
            ALErrorHandler.checkOpenAlError();
        }
    }

    public void deleteQuietly() {
        this.bufferPrepared = false;
        if (this.isValidOpenAlSource()) {
            AL10.alDeleteBuffers(new int[] { this.source });
            ALErrorHandler.getOpenAlError();
        }
    }

    public boolean isValidOpenAlSource() {
        return this.source == null ? false : this.bufferPrepared && AL10.alIsBuffer(this.source);
    }
}