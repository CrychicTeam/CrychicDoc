package icyllis.modernui.audio;

import java.io.IOException;
import java.nio.FloatBuffer;
import javax.annotation.Nullable;

public abstract class SoundStream implements AutoCloseable {

    protected int mSampleRate;

    protected int mChannels;

    protected int mSampleOffset;

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public int getSampleOffset() {
        return this.mSampleOffset;
    }

    @Nullable
    public abstract FloatBuffer decodeFrame(@Nullable FloatBuffer var1) throws IOException;

    public abstract void close() throws IOException;

    public static short f_to_s16(float s) {
        return (short) ((int) (s * 32767.5F - 0.5F));
    }

    public static float s16_to_f(short s) {
        return ((float) s + 0.5F) / 32767.5F;
    }
}