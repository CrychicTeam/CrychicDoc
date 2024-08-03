package icyllis.modernui.audio;

import java.nio.ShortBuffer;

public abstract class SoundSample implements AutoCloseable {

    protected int mSampleRate;

    protected int mChannels;

    protected int mTotalSamples;

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getChannels() {
        return this.mChannels;
    }

    public int getTotalSamples() {
        return this.mTotalSamples;
    }

    public float getTotalLength() {
        return (float) this.mTotalSamples / (float) this.mSampleRate;
    }

    public abstract boolean seek(int var1);

    public abstract int getSamplesShortInterleaved(ShortBuffer var1);

    public abstract void close();
}