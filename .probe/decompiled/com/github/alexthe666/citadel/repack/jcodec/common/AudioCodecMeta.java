package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Label;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AudioCodecMeta extends CodecMeta {

    private int sampleSize;

    private int channelCount;

    private int sampleRate;

    private ByteOrder endian;

    private int samplesPerPacket;

    private int bytesPerPacket;

    private int bytesPerFrame;

    private boolean pcm;

    private Label[] labels;

    public static AudioCodecMeta createAudioCodecMeta(String fourcc, int sampleSize, int channelCount, int sampleRate, ByteOrder endian, boolean pcm, Label[] labels, ByteBuffer codecPrivate) {
        AudioCodecMeta self = new AudioCodecMeta(fourcc, codecPrivate);
        self.sampleSize = sampleSize;
        self.channelCount = channelCount;
        self.sampleRate = sampleRate;
        self.endian = endian;
        self.pcm = pcm;
        self.labels = labels;
        return self;
    }

    public static AudioCodecMeta createAudioCodecMeta2(String fourcc, int sampleSize, int channelCount, int sampleRate, ByteOrder endian, boolean pcm, Label[] labels, int samplesPerPacket, int bytesPerPacket, int bytesPerFrame, ByteBuffer codecPrivate) {
        AudioCodecMeta self = new AudioCodecMeta(fourcc, codecPrivate);
        self.sampleSize = sampleSize;
        self.channelCount = channelCount;
        self.sampleRate = sampleRate;
        self.endian = endian;
        self.samplesPerPacket = samplesPerPacket;
        self.bytesPerPacket = bytesPerPacket;
        self.bytesPerFrame = bytesPerFrame;
        self.pcm = pcm;
        self.labels = labels;
        return self;
    }

    public static AudioCodecMeta createAudioCodecMeta3(String fourcc, ByteBuffer codecPrivate, AudioFormat format, boolean pcm, Label[] labels) {
        AudioCodecMeta self = new AudioCodecMeta(fourcc, codecPrivate);
        self.sampleSize = format.getSampleSizeInBits() >> 3;
        self.channelCount = format.getChannels();
        self.sampleRate = format.getSampleRate();
        self.endian = format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        self.pcm = pcm;
        self.labels = labels;
        return self;
    }

    public AudioCodecMeta(String fourcc, ByteBuffer codecPrivate) {
        super(fourcc, codecPrivate);
    }

    public int getFrameSize() {
        return this.sampleSize * this.channelCount;
    }

    public int getSampleRate() {
        return this.sampleRate;
    }

    public int getSampleSize() {
        return this.sampleSize;
    }

    public int getChannelCount() {
        return this.channelCount;
    }

    public int getSamplesPerPacket() {
        return this.samplesPerPacket;
    }

    public int getBytesPerPacket() {
        return this.bytesPerPacket;
    }

    public int getBytesPerFrame() {
        return this.bytesPerFrame;
    }

    public ByteOrder getEndian() {
        return this.endian;
    }

    public boolean isPCM() {
        return this.pcm;
    }

    public AudioFormat getFormat() {
        return new AudioFormat(this.sampleRate, this.sampleSize << 3, this.channelCount, true, this.endian == ByteOrder.BIG_ENDIAN);
    }

    public Label[] getChannelLabels() {
        return this.labels;
    }

    public static AudioCodecMeta fromAudioFormat(AudioFormat format) {
        AudioCodecMeta self = new AudioCodecMeta(null, null);
        self.sampleSize = format.getSampleSizeInBits() >> 3;
        self.channelCount = format.getChannels();
        self.sampleRate = format.getSampleRate();
        self.endian = format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
        self.pcm = false;
        return self;
    }
}