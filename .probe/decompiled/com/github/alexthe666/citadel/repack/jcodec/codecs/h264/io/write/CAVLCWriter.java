package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write;

import com.github.alexthe666.citadel.repack.jcodec.api.NotImplementedException;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.Debug;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class CAVLCWriter {

    private CAVLCWriter() {
    }

    public static void writeUtrace(BitWriter out, int value, int n, String message) {
        out.writeNBit(value, n);
        Debug.trace(message, value);
    }

    public static void writeUE(BitWriter out, int value) {
        int bits = 0;
        int cumul = 0;
        for (int i = 0; i < 15; i++) {
            if (value < cumul + (1 << i)) {
                bits = i;
                break;
            }
            cumul += 1 << i;
        }
        out.writeNBit(0, bits);
        out.write1Bit(1);
        out.writeNBit(value - cumul, bits);
    }

    public static void writeSE(BitWriter out, int value) {
        writeUE(out, MathUtil.golomb(value));
    }

    public static void writeUEtrace(BitWriter out, int value, String message) {
        writeUE(out, value);
        Debug.trace(message, value);
    }

    public static void writeSEtrace(BitWriter out, int value, String message) {
        writeUE(out, MathUtil.golomb(value));
        Debug.trace(message, value);
    }

    public static void writeTE(BitWriter out, int value, int max) {
        if (max > 1) {
            writeUE(out, value);
        } else {
            out.write1Bit(~value & 1);
        }
    }

    public static void writeBool(BitWriter out, boolean value, String message) {
        out.write1Bit(value ? 1 : 0);
        Debug.trace(message, value ? 1 : 0);
    }

    public static void writeU(BitWriter out, int i, int n) {
        out.writeNBit(i, n);
    }

    public static void writeNBit(BitWriter out, long value, int n, String message) {
        for (int i = 0; i < n; i++) {
            out.write1Bit((int) (value >> n - i - 1) & 1);
        }
        Debug.trace(message, value);
    }

    public static void writeTrailingBits(BitWriter out) {
        out.write1Bit(1);
        out.flush();
    }

    public static void writeSliceTrailingBits() {
        throw new NotImplementedException("todo");
    }
}