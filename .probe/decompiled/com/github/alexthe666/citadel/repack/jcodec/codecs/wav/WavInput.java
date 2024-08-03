package com.github.alexthe666.citadel.repack.jcodec.codecs.wav;

import com.github.alexthe666.citadel.repack.jcodec.audio.AudioSource;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioFormat;
import com.github.alexthe666.citadel.repack.jcodec.common.AudioUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.channels.ReadableByteChannel;

public class WavInput implements Closeable {

    protected WavHeader header;

    protected byte[] prevBuf;

    protected ReadableByteChannel _in;

    protected AudioFormat format;

    public WavInput(ReadableByteChannel _in) throws IOException {
        this.header = WavHeader.readChannel(_in);
        this.format = this.header.getFormat();
        this._in = _in;
    }

    public int read(ByteBuffer buf) throws IOException {
        int maxRead = this.format.framesToBytes(this.format.bytesToFrames(buf.remaining()));
        return NIOUtils.readL(this._in, buf, maxRead);
    }

    public void close() throws IOException {
        this._in.close();
    }

    public WavHeader getHeader() {
        return this.header;
    }

    public AudioFormat getFormat() {
        return this.format;
    }

    public static class Source implements AudioSource, Closeable {

        private WavInput src;

        private AudioFormat format;

        private int pos;

        public Source(WavInput src) {
            this.src = src;
            this.format = src.getFormat();
        }

        @Override
        public AudioFormat getFormat() {
            return this.src.getFormat();
        }

        public void close() throws IOException {
            this.src.close();
        }

        public int read(int[] samples, int max) throws IOException {
            max = Math.min(max, samples.length);
            ByteBuffer bb = ByteBuffer.allocate(this.format.samplesToBytes(max));
            int read = this.src.read(bb);
            bb.flip();
            AudioUtil.toInt(this.format, bb, samples);
            return this.format.bytesToFrames(read);
        }

        @Override
        public int readFloat(FloatBuffer samples) throws IOException {
            ByteBuffer bb = ByteBuffer.allocate(this.format.samplesToBytes(samples.remaining()));
            int i = this.src.read(bb);
            if (i == -1) {
                return -1;
            } else {
                bb.flip();
                AudioUtil.toFloat(this.format, bb, samples);
                int read = this.format.bytesToFrames(i);
                this.pos += read;
                return read;
            }
        }
    }

    public static class WavFile extends WavInput {

        public WavFile(File f) throws IOException {
            super(NIOUtils.readableChannel(f));
        }

        @Override
        public void close() throws IOException {
            super.close();
            this._in.close();
        }
    }
}