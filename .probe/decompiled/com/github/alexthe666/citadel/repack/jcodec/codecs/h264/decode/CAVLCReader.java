package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.Debug;

public class CAVLCReader {

    private CAVLCReader() {
    }

    public static int readNBit(BitReader bits, int n, String message) {
        int val = bits.readNBit(n);
        Debug.trace(message, val);
        return val;
    }

    public static int readUE(BitReader bits) {
        int cnt = 0;
        while (bits.read1Bit() == 0 && cnt < 32) {
            cnt++;
        }
        int res = 0;
        if (cnt > 0) {
            long val = (long) bits.readNBit(cnt);
            res = (int) ((long) ((1 << cnt) - 1) + val);
        }
        return res;
    }

    public static int readUEtrace(BitReader bits, String message) {
        int res = readUE(bits);
        Debug.trace(message, res);
        return res;
    }

    public static int readSE(BitReader bits, String message) {
        int val = readUE(bits);
        val = H264Utils2.golomb2Signed(val);
        Debug.trace(message, val);
        return val;
    }

    public static boolean readBool(BitReader bits, String message) {
        boolean res = bits.read1Bit() != 0;
        Debug.trace(message, res ? 1 : 0);
        return res;
    }

    public static int readU(BitReader bits, int i, String string) {
        return readNBit(bits, i, string);
    }

    public static int readTE(BitReader bits, int max) {
        return max > 1 ? readUE(bits) : ~bits.read1Bit() & 1;
    }

    public static int readME(BitReader bits, String string) {
        return readUEtrace(bits, string);
    }

    public static int readZeroBitCount(BitReader bits, String message) {
        int count = 0;
        while (bits.read1Bit() == 0 && count < 32) {
            count++;
        }
        if (Debug.debug) {
            Debug.trace(message, String.valueOf(count));
        }
        return count;
    }

    public static boolean moreRBSPData(BitReader bits) {
        return bits.remaining() >= 32 || bits.checkNBit(1) != 1 || bits.checkNBit(24) << 9 != 0;
    }
}