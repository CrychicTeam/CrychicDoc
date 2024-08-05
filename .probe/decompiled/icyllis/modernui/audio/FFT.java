package icyllis.modernui.audio;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FFT {

    public static final int NONE = 0;

    public static final int BARTLETT = 1;

    public static final int HAMMING = 2;

    public static final int HANN = 3;

    public static final int BLACKMAN = 4;

    private static final int LINEAR = 1;

    private static final int LOG = 2;

    private final int mTimeSize;

    private final int mSampleRate;

    private final float mBandWidth;

    private final float[] mReal;

    private final float[] mImag;

    private final float[] mSpectrum;

    private final int[] mReverse;

    @Nullable
    private float[] mWindow;

    @Nullable
    private float[] mAverages;

    private int mAverageMode;

    private int mOctaves;

    private int mAvgPerOctave;

    private FFT(int timeSize, int sampleRate) {
        this.mTimeSize = timeSize;
        this.mSampleRate = sampleRate;
        this.mBandWidth = (float) sampleRate / (float) timeSize;
        this.mReal = new float[timeSize];
        this.mImag = new float[timeSize];
        this.mSpectrum = new float[(timeSize >> 1) + 1];
        int[] reverse = new int[timeSize];
        int limit = 1;
        for (int bit = timeSize >> 1; limit < timeSize; bit >>= 1) {
            for (int i = 0; i < limit; i++) {
                reverse[i + limit] = reverse[i] + bit;
            }
            limit <<= 1;
        }
        this.mReverse = reverse;
    }

    @Nonnull
    public static FFT create(int timeSize, int sampleRate) {
        if (timeSize > 0 & (timeSize & timeSize - 1) != 0) {
            throw new IllegalArgumentException("timeSize must be a power of two");
        } else {
            return new FFT(timeSize, sampleRate);
        }
    }

    public void forward(@Nonnull float[] samples, int offset) {
        if (offset >= 0 && offset < samples.length) {
            for (int i = 0; i < this.mTimeSize; i++) {
                int j = this.mReverse[i];
                if (j + offset >= samples.length) {
                    this.mReal[i] = 0.0F;
                } else {
                    float sample = samples[j + offset];
                    if (this.mWindow != null) {
                        sample *= this.mWindow[j];
                    }
                    this.mReal[i] = sample;
                }
                this.mImag[i] = 0.0F;
            }
            this.fft();
            this.fillSpectrum();
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void forward(@Nonnull float[] real, @Nonnull float[] imag, int offset) {
        if (offset >= 0 && real.length == imag.length && offset < real.length) {
            for (int i = 0; i < this.mTimeSize; i++) {
                int j = this.mReverse[i];
                if (j + offset >= real.length) {
                    this.mReal[i] = 0.0F;
                    this.mImag[i] = 0.0F;
                } else {
                    this.mReal[i] = real[j + offset];
                    this.mImag[i] = imag[j + offset];
                }
            }
            this.fft();
            this.fillSpectrum();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void fft() {
        for (int halfSize = 1; halfSize < this.mTimeSize; halfSize <<= 1) {
            double k = -Math.PI / (double) halfSize;
            float phaseShiftStepR = (float) Math.cos(k);
            float phaseShiftStepI = (float) Math.sin(k);
            float currentPhaseShiftR = 1.0F;
            float currentPhaseShiftI = 0.0F;
            for (int fftStep = 0; fftStep < halfSize; fftStep++) {
                for (int i = fftStep; i < this.mTimeSize; i += halfSize << 1) {
                    int off = i + halfSize;
                    float tr = currentPhaseShiftR * this.mReal[off] - currentPhaseShiftI * this.mImag[off];
                    float ti = currentPhaseShiftR * this.mImag[off] + currentPhaseShiftI * this.mReal[off];
                    this.mReal[off] = this.mReal[i] - tr;
                    this.mImag[off] = this.mImag[i] - ti;
                    this.mReal[i] = this.mReal[i] + tr;
                    this.mImag[i] = this.mImag[i] + ti;
                }
                float tmpR = currentPhaseShiftR;
                currentPhaseShiftR = currentPhaseShiftR * phaseShiftStepR - currentPhaseShiftI * phaseShiftStepI;
                currentPhaseShiftI = tmpR * phaseShiftStepI + currentPhaseShiftI * phaseShiftStepR;
            }
        }
    }

    private void fillSpectrum() {
        for (int i = 0; i < this.mSpectrum.length; i++) {
            this.mSpectrum[i] = (float) Math.sqrt((double) (this.mReal[i] * this.mReal[i] + this.mImag[i] * this.mImag[i]));
        }
        if (this.mAverages != null) {
            if (this.mAverageMode == 1) {
                int avgWidth = this.mSpectrum.length / this.mAverages.length;
                for (int i = 0; i < this.mAverages.length; i++) {
                    float avg = 0.0F;
                    int j;
                    for (j = 0; j < avgWidth; j++) {
                        int offset = j + i * avgWidth;
                        if (offset >= this.mSpectrum.length) {
                            break;
                        }
                        avg += this.mSpectrum[offset];
                    }
                    avg /= (float) (j + 1);
                    this.mAverages[i] = avg;
                }
            } else if (this.mAverageMode == 2) {
                for (int i = 0; i < this.mOctaves; i++) {
                    float lowFreq;
                    if (i == 0) {
                        lowFreq = 0.0F;
                    } else {
                        lowFreq = (float) this.mSampleRate / 2.0F / (float) (1L << this.mOctaves - i);
                    }
                    float hiFreq = (float) this.mSampleRate / 2.0F / (float) (1L << this.mOctaves - i - 1);
                    float freqStep = (hiFreq - lowFreq) / (float) this.mAvgPerOctave;
                    float f = lowFreq;
                    for (int j = 0; j < this.mAvgPerOctave; j++) {
                        int offset = j + i * this.mAvgPerOctave;
                        this.mAverages[offset] = this.getAverage(f, f + freqStep);
                        f += freqStep;
                    }
                }
            }
        }
    }

    public void setNoAverages() {
        this.mAverages = null;
        this.mAverageMode = 0;
    }

    public void setLinearAverages(int num) {
        if (num > this.mSpectrum.length / 2) {
            throw new IllegalArgumentException("The number of averages for this transform can be at most " + this.mSpectrum.length / 2 + ".");
        } else {
            this.mAverages = new float[num];
            this.mAverageMode = 1;
        }
    }

    public void setLogAverages(int minBandwidth, int bandsPerOctave) {
        float nyq = (float) this.mSampleRate / 2.0F;
        this.mOctaves = 1;
        while ((nyq /= 2.0F) > (float) minBandwidth) {
            this.mOctaves++;
        }
        this.mAvgPerOctave = bandsPerOctave;
        this.mAverages = new float[this.mOctaves * bandsPerOctave];
        this.mAverageMode = 2;
    }

    public void setWindowFunc(int func) {
        if (func == 0) {
            this.mWindow = null;
        } else {
            float[] window = this.mWindow;
            if (window == null) {
                window = new float[this.mTimeSize];
            }
            int n = window.length - 1;
            switch(func) {
                case 1:
                    for (int i = 0; i <= n; i++) {
                        window[i] = (float) (1 - Math.abs((i * 2 - n) / n));
                    }
                    break;
                case 2:
                    for (int i = 0; i <= n; i++) {
                        window[i] = (float) (0.54F - 0.46F * Math.cos((Math.PI * 2) * (double) i / (double) n));
                    }
                    break;
                case 3:
                    for (int i = 0; i <= n; i++) {
                        float sin = (float) Math.sin(Math.PI * (double) i / (double) n);
                        window[i] = sin * sin;
                    }
                    break;
                case 4:
                    for (int i = 0; i <= n; i++) {
                        window[i] = (float) (0.42F - 0.5 * Math.cos((Math.PI * 2) * (double) i / (double) n) + 0.08F * Math.cos((Math.PI * 4) * (double) i / (double) n));
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized window function " + func);
            }
            this.mWindow = window;
        }
    }

    public int getTimeSize() {
        return this.mTimeSize;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getBandSize() {
        return this.mSpectrum.length;
    }

    public float getBandWidth() {
        return this.mBandWidth;
    }

    public float getBand(int i) {
        return this.mSpectrum[i];
    }

    public void setBand(int band, float amplitude) {
        if (amplitude < 0.0F) {
            throw new IllegalArgumentException("Can't set a frequency band to a negative value.");
        } else {
            if (this.mReal[band] == 0.0F && this.mImag[band] == 0.0F) {
                this.mReal[band] = amplitude;
                this.mSpectrum[band] = amplitude;
            } else {
                this.mReal[band] = this.mReal[band] / this.mSpectrum[band];
                this.mImag[band] = this.mImag[band] / this.mSpectrum[band];
                this.mSpectrum[band] = amplitude;
                this.mReal[band] = this.mReal[band] * this.mSpectrum[band];
                this.mImag[band] = this.mImag[band] * this.mSpectrum[band];
            }
            if (band != 0 && band != this.mTimeSize / 2) {
                this.mReal[this.mTimeSize - band] = this.mReal[band];
                this.mImag[this.mTimeSize - band] = -this.mImag[band];
            }
        }
    }

    public void scaleBand(int band, float scale) {
        if (scale < 0.0F) {
            throw new IllegalArgumentException("Can't scale a frequency band by a negative value.");
        } else {
            if (this.mSpectrum[band] != 0.0F) {
                this.mReal[band] = this.mReal[band] / this.mSpectrum[band];
                this.mImag[band] = this.mImag[band] / this.mSpectrum[band];
                this.mSpectrum[band] = this.mSpectrum[band] * scale;
                this.mReal[band] = this.mReal[band] * this.mSpectrum[band];
                this.mImag[band] = this.mImag[band] * this.mSpectrum[band];
            }
            if (band != 0 && band != this.mTimeSize / 2) {
                this.mReal[this.mTimeSize - band] = this.mReal[band];
                this.mImag[this.mTimeSize - band] = -this.mImag[band];
            }
        }
    }

    public int freqToIndex(float freq) {
        if (freq < this.getBandWidth() / 2.0F) {
            return 0;
        } else if (freq > ((float) this.mSampleRate - this.getBandWidth()) / 2.0F) {
            return this.mSpectrum.length - 1;
        } else {
            float fraction = freq / (float) this.mSampleRate;
            return Math.round((float) this.mTimeSize * fraction);
        }
    }

    public float indexToFreq(int i) {
        float bw = this.getBandWidth();
        if (i == 0) {
            return bw * 0.25F;
        } else if (i == this.mSpectrum.length - 1) {
            float lastBinBeginFreq = ((float) this.mSampleRate - bw) / 2.0F;
            float binHalfWidth = bw * 0.25F;
            return lastBinBeginFreq + binHalfWidth;
        } else {
            return (float) i * bw;
        }
    }

    public float getAverageCenterFrequency(int i) {
        if (this.mAverages == null) {
            return 0.0F;
        } else if (this.mAverageMode == 1) {
            int avgWidth = this.mSpectrum.length / this.mAverages.length;
            int centerBinIndex = i * avgWidth + avgWidth / 2;
            return this.indexToFreq(centerBinIndex);
        } else if (this.mAverageMode == 2) {
            int octave = i / this.mAvgPerOctave;
            int offset = i % this.mAvgPerOctave;
            float lowFreq;
            if (octave == 0) {
                lowFreq = 0.0F;
            } else {
                lowFreq = (float) this.mSampleRate / 2.0F / (float) (1L << this.mOctaves - octave);
            }
            float hiFreq = (float) this.mSampleRate / 2.0F / (float) (1L << this.mOctaves - octave - 1);
            float freqStep = (hiFreq - lowFreq) / (float) this.mAvgPerOctave;
            float f = lowFreq + (float) offset * freqStep;
            return f + freqStep / 2.0F;
        } else {
            return 0.0F;
        }
    }

    public float getFrequency(float freq) {
        return this.getBand(this.freqToIndex(freq));
    }

    public void setFrequency(float freq, float amplitude) {
        this.setBand(this.freqToIndex(freq), amplitude);
    }

    public void scaleFrequency(float freq, float scale) {
        this.scaleBand(this.freqToIndex(freq), scale);
    }

    public int getAverageSize() {
        return this.mAverages == null ? 0 : this.mAverages.length;
    }

    public float getAverage(int i) {
        return this.mAverages == null ? 0.0F : this.mAverages[i];
    }

    public float getAverage(float lowFreq, float hiFreq) {
        int lowBound = this.freqToIndex(lowFreq);
        int hiBound = this.freqToIndex(hiFreq);
        float avg = 0.0F;
        for (int i = lowBound; i <= hiBound; i++) {
            avg += this.mSpectrum[i];
        }
        return avg / (float) (hiBound - lowBound + 1);
    }
}