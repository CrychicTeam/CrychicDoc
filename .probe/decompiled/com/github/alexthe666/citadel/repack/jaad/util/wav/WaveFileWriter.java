package com.github.alexthe666.citadel.repack.jaad.util.wav;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WaveFileWriter {

    private static final int HEADER_LENGTH = 44;

    private static final int RIFF = 1380533830;

    private static final long WAVE_FMT = 6287401410857104416L;

    private static final int DATA = 1684108385;

    private static final int BYTE_MASK = 255;

    private final RandomAccessFile out;

    private final int sampleRate;

    private final int channels;

    private final int bitsPerSample;

    private int bytesWritten;

    public WaveFileWriter(File output, int sampleRate, int channels, int bitsPerSample) throws IOException {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.bitsPerSample = bitsPerSample;
        this.bytesWritten = 0;
        this.out = new RandomAccessFile(output, "rw");
        this.out.write(new byte[44]);
    }

    public void write(byte[] data) throws IOException {
        this.write(data, 0, data.length);
    }

    public void write(byte[] data, int off, int len) throws IOException {
        for (int i = off; i < off + data.length; i += 2) {
            byte tmp = data[i + 1];
            data[i + 1] = data[i];
            data[i] = tmp;
        }
        this.out.write(data, off, len);
        this.bytesWritten += data.length;
    }

    public void write(short[] data) throws IOException {
        this.write(data, 0, data.length);
    }

    public void write(short[] data, int off, int len) throws IOException {
        for (int i = off; i < off + data.length; i++) {
            this.out.write(data[i] & 255);
            this.out.write(data[i] >> 8 & 0xFF);
            this.bytesWritten += 2;
        }
    }

    public void close() throws IOException {
        this.writeWaveHeader();
        this.out.close();
    }

    private void writeWaveHeader() throws IOException {
        this.out.seek(0L);
        int bytesPerSec = (this.bitsPerSample + 7) / 8;
        this.out.writeInt(1380533830);
        this.out.writeInt(Integer.reverseBytes(this.bytesWritten + 36));
        this.out.writeLong(6287401410857104416L);
        this.out.writeInt(Integer.reverseBytes(16));
        this.out.writeShort(Short.reverseBytes((short) 1));
        this.out.writeShort(Short.reverseBytes((short) this.channels));
        this.out.writeInt(Integer.reverseBytes(this.sampleRate));
        this.out.writeInt(Integer.reverseBytes(this.sampleRate * this.channels * bytesPerSec));
        this.out.writeShort(Short.reverseBytes((short) (this.channels * bytesPerSec)));
        this.out.writeShort(Short.reverseBytes((short) this.bitsPerSample));
        this.out.writeInt(1684108385);
        this.out.writeInt(Integer.reverseBytes(this.bytesWritten));
    }
}