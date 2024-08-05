package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DictionaryCompressor {

    protected VLC buildCodes(int[] counts, int esc) {
        int[] codes = new int[counts.length];
        int[] codeSizes = new int[counts.length];
        for (int code = 0; code < Math.min(codes.length, esc); code++) {
            int max = 0;
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > counts[max]) {
                    max = i;
                }
            }
            codes[max] = code;
            codeSizes[max] = Math.max(1, MathUtil.log2(code));
            counts[max] = Integer.MIN_VALUE;
        }
        int escSize = MathUtil.log2(esc);
        for (int ix = 0; ix < counts.length; ix++) {
            if (counts[ix] >= 0) {
                codes[ix] = esc;
                codeSizes[ix] = escSize;
            }
        }
        return new VLC(codes, codeSizes);
    }

    public static class Int extends DictionaryCompressor {

        public void compress(int[] values, ByteBuffer bb) {
            RunLength.Integer rl = this.getValueStats(values);
            int[] counts = rl.getCounts();
            int[] keys = rl.getValues();
            int esc = Math.max(1, (1 << MathUtil.log2(counts.length) - 2) - 1);
            VLC vlc = this.buildCodes(counts, esc);
            int[] codes = vlc.getCodes();
            int[] codeSizes = vlc.getCodeSizes();
            bb.putInt(codes.length);
            for (int i = 0; i < codes.length; i++) {
                bb.put((byte) codeSizes[i]);
                bb.putShort((short) (codes[i] >>> 16));
                bb.putInt(keys[i]);
            }
            BitWriter br = new BitWriter(bb);
            for (int j = 0; j < values.length; j++) {
                int l = values[j];
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] == l) {
                        vlc.writeVLC(br, i);
                        if (codes[i] == esc) {
                            br.writeNBit(i, 16);
                        }
                    }
                }
            }
            br.flush();
        }

        private RunLength.Integer getValueStats(int[] values) {
            int[] copy = Platform.copyOfInt(values, values.length);
            Arrays.sort(copy);
            RunLength.Integer rl = new RunLength.Integer();
            for (int i = 0; i < copy.length; i++) {
                int l = copy[i];
                rl.add(l);
            }
            return rl;
        }
    }

    public static class Long extends DictionaryCompressor {

        public void compress(long[] values, ByteBuffer bb) {
            RunLength.Long rl = this.getValueStats(values);
            int[] counts = rl.getCounts();
            long[] keys = rl.getValues();
            VLC vlc = this.buildCodes(counts, values.length / 10);
            int[] codes = vlc.getCodes();
            int[] codeSizes = vlc.getCodeSizes();
            bb.putInt(codes.length);
            for (int i = 0; i < codes.length; i++) {
                bb.put((byte) codeSizes[i]);
                bb.putShort((short) (codes[i] >>> 16));
                bb.putLong(keys[i]);
            }
            BitWriter br = new BitWriter(bb);
            for (int j = 0; j < values.length; j++) {
                long l = values[j];
                for (int i = 0; i < keys.length; i++) {
                    if (keys[i] == l) {
                        vlc.writeVLC(br, i);
                        if (codes[i] == 15) {
                            br.writeNBit(16, i);
                        }
                    }
                }
            }
            br.flush();
        }

        private RunLength.Long getValueStats(long[] values) {
            long[] copy = Platform.copyOfLong(values, values.length);
            Arrays.sort(copy);
            RunLength.Long rl = new RunLength.Long();
            for (int i = 0; i < copy.length; i++) {
                long l = copy[i];
                rl.add(l);
            }
            return rl;
        }
    }
}