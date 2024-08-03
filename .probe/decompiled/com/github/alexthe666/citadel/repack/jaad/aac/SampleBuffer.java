package com.github.alexthe666.citadel.repack.jaad.aac;

public class SampleBuffer {

    private int sampleRate;

    private int channels;

    private int bitsPerSample;

    private double length;

    private double bitrate;

    private double encodedBitrate;

    private byte[] data = new byte[0];

    private boolean bigEndian;

    public SampleBuffer() {
        this.sampleRate = 0;
        this.channels = 0;
        this.bitsPerSample = 0;
        this.bigEndian = true;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public int getChannels() {
        return this.channels;
    }

    public int getBitsPerSample() {
        return this.bitsPerSample;
    }

    public double getLength() {
        return this.length;
    }

    public double getBitrate() {
        return this.bitrate;
    }

    public double getEncodedBitrate() {
        return this.encodedBitrate;
    }

    public boolean isBigEndian() {
        return this.bigEndian;
    }

    public void setBigEndian(boolean bigEndian) {
        if (bigEndian != this.bigEndian) {
            for (int i = 0; i < this.data.length; i += 2) {
                byte tmp = this.data[i];
                this.data[i] = this.data[i + 1];
                this.data[i + 1] = tmp;
            }
            this.bigEndian = bigEndian;
        }
    }

    public void setData(byte[] data, int sampleRate, int channels, int bitsPerSample, int bitsRead) {
        this.data = data;
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitsPerSample = bitsPerSample;
        if (sampleRate == 0) {
            this.length = 0.0;
            this.bitrate = 0.0;
            this.encodedBitrate = 0.0;
        } else {
            int bytesPerSample = bitsPerSample / 8;
            int samplesPerChannel = data.length / (bytesPerSample * channels);
            this.length = (double) samplesPerChannel / (double) sampleRate;
            this.bitrate = (double) (samplesPerChannel * bitsPerSample * channels) / this.length;
            this.encodedBitrate = (double) bitsRead / this.length;
        }
    }
}