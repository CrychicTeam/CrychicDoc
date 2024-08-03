package com.github.alexthe666.citadel.repack.jcodec.codecs.common.biari;

import com.github.alexthe666.citadel.repack.jcodec.platform.BaseOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitIO {

    public static BitIO.InputBits inputFromStream(InputStream is) {
        return new BitIO.StreamInputBits(is);
    }

    public static BitIO.OutputBits outputFromStream(OutputStream out) {
        return new BitIO.StreamOutputBits(out);
    }

    public static BitIO.InputBits inputFromArray(byte[] bytes) {
        return new BitIO.StreamInputBits(new ByteArrayInputStream(bytes));
    }

    public static BitIO.OutputBits outputFromArray(final byte[] bytes) {
        return new BitIO.StreamOutputBits(new BaseOutputStream() {

            int ptr;

            @Override
            protected void writeByte(int b) throws IOException {
                if (this.ptr >= bytes.length) {
                    throw new IOException("Buffer is full");
                } else {
                    bytes[this.ptr++] = (byte) b;
                }
            }
        });
    }

    public static byte[] compressBits(int[] decompressed) {
        byte[] compressed = new byte[(decompressed.length >> 3) + 1];
        BitIO.OutputBits out = outputFromArray(compressed);
        try {
            for (int i = 0; i < decompressed.length; i++) {
                int bit = decompressed[i];
                out.putBit(bit);
            }
        } catch (IOException var5) {
        }
        return compressed;
    }

    public static int[] decompressBits(byte[] compressed) {
        int[] decompressed = new int[compressed.length << 3];
        BitIO.InputBits inputFromArray = inputFromArray(compressed);
        int read;
        try {
            for (int i = 0; (read = inputFromArray.getBit()) != -1; i++) {
                decompressed[i] = read;
            }
        } catch (IOException var5) {
        }
        return decompressed;
    }

    public interface InputBits {

        int getBit() throws IOException;
    }

    public interface OutputBits {

        void putBit(int var1) throws IOException;

        void flush() throws IOException;
    }

    public static class StreamInputBits implements BitIO.InputBits {

        private InputStream _in;

        private int cur;

        private int bit;

        public StreamInputBits(InputStream _in) {
            this._in = _in;
            this.bit = 8;
        }

        @Override
        public int getBit() throws IOException {
            if (this.bit > 7) {
                this.cur = this._in.read();
                if (this.cur == -1) {
                    return -1;
                }
                this.bit = 0;
            }
            return this.cur >> 7 - this.bit++ & 1;
        }
    }

    public static class StreamOutputBits implements BitIO.OutputBits {

        private OutputStream out;

        private int cur;

        private int bit;

        public StreamOutputBits(OutputStream out) {
            this.out = out;
        }

        @Override
        public void putBit(int symbol) throws IOException {
            if (this.bit > 7) {
                this.out.write(this.cur);
                this.cur = 0;
                this.bit = 0;
            }
            this.cur = this.cur | (symbol & 1) << 7 - this.bit++;
        }

        @Override
        public void flush() throws IOException {
            if (this.bit > 0) {
                this.out.write(this.cur);
            }
        }
    }
}