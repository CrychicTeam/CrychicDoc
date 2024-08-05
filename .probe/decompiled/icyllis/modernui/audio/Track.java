package icyllis.modernui.audio;

import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryUtil;

public class Track implements AutoCloseable {

    private static final int BUFFER_COUNT = 2;

    private int mSource;

    private final SoundSample mSample;

    private int mBaseSampleOffset;

    private FFT mFFT;

    private Consumer<FFT> mFFTCallback;

    private float[] mMixedSamples;

    private int mMixedSampleCount;

    private int[] mBuffers = new int[2];

    private ShortBuffer mClientBuffer;

    private static final int STATE_IDLE = 0;

    private static final int STATE_PLAYING = 1;

    private static final int STATE_PAUSED = 2;

    private int mClientState = 0;

    public Track(@Nonnull SoundSample sample) {
        this.mSample = sample;
        this.mSource = AL11.alGenSources();
        AL11.alSourcef(this.mSource, 4106, 1.0F);
        AL11.alSourcei(this.mSource, 4147, 1);
        int targetSamples = this.mSample.getChannels() * this.mSample.getSampleRate() / 2;
        this.mClientBuffer = MemoryUtil.memAllocShort(targetSamples);
        AL11.alGenBuffers(this.mBuffers);
        AudioManager.getInstance().addTrack(this);
    }

    public boolean isPlaying() {
        return this.mClientState == 1;
    }

    public void play() {
        if (this.mSource != 0 && AL11.alGetSourcei(this.mSource, 4112) != 4114) {
            if (this.mClientState == 0) {
                if (this.mBaseSampleOffset != 0) {
                    this.mSample.seek(0);
                    this.mBaseSampleOffset = 0;
                    int count = AL11.alGetSourcei(this.mSource, 4117);
                    while (count-- != 0) {
                        AL11.alSourceUnqueueBuffers(this.mSource);
                    }
                    this.mMixedSampleCount = 0;
                }
                for (int i = 0; i < 2; i++) {
                    this.forward(this.mBuffers[i]);
                }
            }
            AL11.alSourcePlay(this.mSource);
            this.mClientState = 1;
        }
    }

    public void pause() {
        if (this.mSource != 0) {
            AL11.alSourcePause(this.mSource);
            this.mClientState = 2;
        }
    }

    public void setPosition(float x, float y, float z) {
        AL11.alSourcefv(this.mSource, 4100, new float[] { x, y, z });
    }

    public void setGain(float gain) {
        AL11.alSourcef(this.mSource, 4106, gain);
    }

    public float getTime() {
        return this.mSource == 0 ? 0.0F : (float) this.mBaseSampleOffset / (float) this.mSample.getSampleRate() + AL11.alGetSourcef(this.mSource, 4132);
    }

    public float getLength() {
        return this.mSample.getTotalLength();
    }

    public int getSampleRate() {
        return this.mSample.getSampleRate();
    }

    public boolean seek(int sampleOffset) {
        if (this.mSample.seek(sampleOffset)) {
            AL11.alSourceStop(this.mSource);
            this.swapBuffers(false);
            this.mBaseSampleOffset = sampleOffset;
            if (this.mClientState == 1) {
                this.play();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean seekToSeconds(float seconds) {
        return this.seek((int) (seconds * (float) this.getSampleRate()));
    }

    private int swapBuffers(boolean onlyProcessed) {
        int count = AL11.alGetSourcei(this.mSource, onlyProcessed ? 4118 : 4117);
        for (int i = 0; i < count; i++) {
            int buf = AL11.alSourceUnqueueBuffers(this.mSource);
            int samplesPerChannel = AL11.alGetBufferi(buf, 8196) / 2 / this.mSample.getChannels();
            this.mBaseSampleOffset += samplesPerChannel;
            System.arraycopy(this.mMixedSamples, samplesPerChannel, this.mMixedSamples, 0, this.mMixedSampleCount - samplesPerChannel);
            this.mMixedSampleCount -= samplesPerChannel;
            int forwardedSamples = this.forward(buf);
            if (forwardedSamples == 0) {
                if (i == 0 && count == 1) {
                    this.mClientState = 0;
                }
                return i + 1;
            }
        }
        return count;
    }

    public void tick() {
        if (this.mClientState == 1) {
            int count = this.swapBuffers(true);
            if (count == 2) {
                this.play();
            }
            if (this.mFFT != null) {
                int offset = AL11.alGetSourcei(this.mSource, 4133);
                this.mFFT.forward(this.mMixedSamples, offset);
                if (this.mFFTCallback != null) {
                    this.mFFTCallback.accept(this.mFFT);
                }
            }
        }
    }

    public void setAnalyzer(@Nullable FFT fft, @Nullable Consumer<FFT> callback) {
        if (fft != null && fft.getSampleRate() != this.mSample.getSampleRate()) {
            throw new IllegalArgumentException("Mismatched sample rate");
        } else {
            this.mFFT = fft;
            this.mFFTCallback = callback;
        }
    }

    private int forward(int buf) {
        ShortBuffer buffer = this.mClientBuffer;
        int channels = this.mSample.getChannels();
        int samples = 0;
        int maxSamples = buffer.capacity();
        while (samples < maxSamples) {
            buffer.position(samples);
            int samplesPerChannel = this.mSample.getSamplesShortInterleaved(buffer);
            if (samplesPerChannel == 0) {
                break;
            }
            samples += samplesPerChannel * channels;
        }
        if (samples != 0) {
            buffer.position(0);
            buffer.limit(samples);
            AL11.alBufferData(buf, channels == 1 ? 4353 : 4355, buffer, this.mSample.getSampleRate());
            buffer.limit(maxSamples);
            AL11.alSourceQueueBuffers(this.mSource, buf);
            int samplesPerChannel = samples / channels;
            if (this.mMixedSamples == null) {
                this.mMixedSamples = new float[this.mMixedSampleCount + samplesPerChannel];
            } else if (this.mMixedSamples.length < this.mMixedSampleCount + samplesPerChannel) {
                this.mMixedSamples = Arrays.copyOf(this.mMixedSamples, this.mMixedSampleCount + samplesPerChannel);
            }
            for (int j = this.mMixedSampleCount; j < this.mMixedSampleCount + samplesPerChannel; j++) {
                float mixedSample = 0.0F;
                for (int k = 0; k < channels; k++) {
                    mixedSample += SoundStream.s16_to_f(buffer.get());
                }
                mixedSample /= (float) channels;
                this.mMixedSamples[j] = mixedSample;
            }
            this.mMixedSampleCount += samplesPerChannel;
        }
        return samples;
    }

    public void close() {
        AudioManager.getInstance().removeTrack(this);
        MemoryUtil.memFree(this.mClientBuffer);
        this.mClientBuffer = null;
        if (this.mBuffers != null) {
            AL11.alDeleteBuffers(this.mBuffers);
            this.mBuffers = null;
        }
        if (this.mSource != 0) {
            AL11.alDeleteSources(this.mSource);
            this.mSource = 0;
        }
        this.mSample.close();
    }
}