package com.github.alexthe666.citadel.repack.jaad.aac;

public enum SampleFrequency {

    SAMPLE_FREQUENCY_96000(0, 96000, new int[] { 33, 512 }, new int[] { 31, 9 }),
    SAMPLE_FREQUENCY_88200(1, 88200, new int[] { 33, 512 }, new int[] { 31, 9 }),
    SAMPLE_FREQUENCY_64000(2, 64000, new int[] { 38, 664 }, new int[] { 34, 10 }),
    SAMPLE_FREQUENCY_48000(3, 48000, new int[] { 40, 672 }, new int[] { 40, 14 }),
    SAMPLE_FREQUENCY_44100(4, 44100, new int[] { 40, 672 }, new int[] { 42, 14 }),
    SAMPLE_FREQUENCY_32000(5, 32000, new int[] { 40, 672 }, new int[] { 51, 14 }),
    SAMPLE_FREQUENCY_24000(6, 24000, new int[] { 41, 652 }, new int[] { 46, 14 }),
    SAMPLE_FREQUENCY_22050(7, 22050, new int[] { 41, 652 }, new int[] { 46, 14 }),
    SAMPLE_FREQUENCY_16000(8, 16000, new int[] { 37, 664 }, new int[] { 42, 14 }),
    SAMPLE_FREQUENCY_12000(9, 12000, new int[] { 37, 664 }, new int[] { 42, 14 }),
    SAMPLE_FREQUENCY_11025(10, 11025, new int[] { 37, 664 }, new int[] { 42, 14 }),
    SAMPLE_FREQUENCY_8000(11, 8000, new int[] { 34, 664 }, new int[] { 39, 14 }),
    SAMPLE_FREQUENCY_NONE(-1, 0, new int[] { 0, 0 }, new int[] { 0, 0 });

    private final int index;

    private final int frequency;

    private final int[] prediction;

    private final int[] maxTNS_SFB;

    public static SampleFrequency forInt(int i) {
        SampleFrequency freq;
        if (i >= 0 && i < 12) {
            freq = values()[i];
        } else {
            freq = SAMPLE_FREQUENCY_NONE;
        }
        return freq;
    }

    public static SampleFrequency forFrequency(int i) {
        SampleFrequency[] all = values();
        SampleFrequency freq = null;
        for (int j = 0; freq == null && j < 12; j++) {
            if (i == all[j].frequency) {
                freq = all[j];
            }
        }
        if (freq == null) {
            freq = SAMPLE_FREQUENCY_NONE;
        }
        return freq;
    }

    private SampleFrequency(int index, int freqency, int[] prediction, int[] maxTNS_SFB) {
        this.index = index;
        this.frequency = freqency;
        this.prediction = prediction;
        this.maxTNS_SFB = maxTNS_SFB;
    }

    public int getIndex() {
        return this.index;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public int getMaximalPredictionSFB() {
        return this.prediction[0];
    }

    public int getPredictorCount() {
        return this.prediction[1];
    }

    public int getMaximalTNS_SFB(boolean shortWindow) {
        return this.maxTNS_SFB[shortWindow ? 1 : 0];
    }

    public String toString() {
        return Integer.toString(this.frequency);
    }
}