package com.github.alexthe666.citadel.repack.jcodec.codecs.wav;

import java.io.IOException;
import java.io.InputStream;

public abstract class ReaderLE {

    public static short readShort(InputStream input) throws IOException {
        int b2 = input.read();
        int b1 = input.read();
        return b1 != -1 && b2 != -1 ? (short) ((b1 << 8) + b2) : -1;
    }

    public static int readInt(InputStream input) throws IOException {
        long b4 = (long) input.read();
        long b3 = (long) input.read();
        long b2 = (long) input.read();
        long b1 = (long) input.read();
        return b1 != -1L && b2 != -1L && b3 != -1L && b4 != -1L ? (int) ((b1 << 24) + (b2 << 16) + (b3 << 8) + b4) : -1;
    }

    public static long readLong(InputStream input) throws IOException {
        long b8 = (long) input.read();
        long b7 = (long) input.read();
        long b6 = (long) input.read();
        long b5 = (long) input.read();
        long b4 = (long) input.read();
        long b3 = (long) input.read();
        long b2 = (long) input.read();
        long b1 = (long) input.read();
        return b1 != -1L && b2 != -1L && b3 != -1L && b4 != -1L && b5 != -1L && b6 != -1L && b7 != -1L && b8 != -1L ? (long) ((int) ((b1 << 56) + (b2 << 48) + (b3 << 40) + (b4 << 32) + (b5 << 24) + (b6 << 16) + (b7 << 8) + b8)) : -1L;
    }
}