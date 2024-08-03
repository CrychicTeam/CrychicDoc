package com.github.alexthe666.citadel.repack.jcodec.codecs.wav;

import com.github.alexthe666.citadel.repack.jcodec.audio.AudioSink;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class WavOutput implements Closeable {

    protected SeekableByteChannel out;

    protected WavHeader header;

    protected int written;

    protected AudioFormat format;

    public WavOutput(SeekableByteChannel out, AudioFormat format) throws IOException {
        this.out = out;
        this.format = format;
        this.header = WavHeader.createWavHeader(format, 0);
        this.header.write(out);
    }

    public void write(ByteBuffer samples) throws IOException {
        this.written = this.written + this.out.write(samples);
    }

    public void close() throws IOException {
        this.out.setPosition(0L);
        WavHeader.createWavHeader(this.format, this.format.bytesToFrames(this.written)).write(this.out);
        NIOUtils.closeQuietly(this.out);
    }

    public static class Sink implements AudioSink, Closeable {

        private WavOutput out;

        public Sink(WavOutput out) {
            this.out = out;
        }

        @Override
        public void writeFloat(FloatBuffer data) throws IOException {
            ByteBuffer buf = ByteBuffer.allocate(this.out.format.samplesToBytes(data.remaining()));
            AudioUtil.fromFloat(data, this.out.format, buf);
            buf.flip();
            this.out.write(buf);
        }

        public void write(int[] data, int len) throws IOException {
            len = Math.min(data.length, len);
            ByteBuffer buf = ByteBuffer.allocate(this.out.format.samplesToBytes(len));
            AudioUtil.fromInt(data, len, this.out.format, buf);
            buf.flip();
            this.out.write(buf);
        }

        public void close() throws IOException {
            this.out.close();
        }
    }

    public static class WavOutFile extends WavOutput {

        public WavOutFile(File f, AudioFormat format) throws IOException {
            super(NIOUtils.writableChannel(f), format);
        }

        @Override
        public void close() throws IOException {
            super.close();
            NIOUtils.closeQuietly(this.out);
        }
    }
}