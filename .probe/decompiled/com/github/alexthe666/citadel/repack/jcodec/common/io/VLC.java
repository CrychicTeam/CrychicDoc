package com.github.alexthe666.citadel.repack.jcodec.common.io;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.PrintStream;

public class VLC {

    private int[] codes;

    private int[] codeSizes;

    private int[] values;

    private int[] valueSizes;

    public static VLC createVLC(String[] codes) {
        IntArrayList _codes = IntArrayList.createIntArrayList();
        IntArrayList _codeSizes = IntArrayList.createIntArrayList();
        for (int i = 0; i < codes.length; i++) {
            String string = codes[i];
            _codes.add(Integer.parseInt(string, 2) << 32 - string.length());
            _codeSizes.add(string.length());
        }
        return new VLC(_codes.toArray(), _codeSizes.toArray());
    }

    public VLC(int[] codes, int[] codeSizes) {
        this.codes = codes;
        this.codeSizes = codeSizes;
        this._invert();
    }

    private void _invert() {
        IntArrayList values = IntArrayList.createIntArrayList();
        IntArrayList valueSizes = IntArrayList.createIntArrayList();
        this.invert(0, 0, 0, values, valueSizes);
        this.values = values.toArray();
        this.valueSizes = valueSizes.toArray();
    }

    private int invert(int startOff, int level, int prefix, IntArrayList values, IntArrayList valueSizes) {
        int tableEnd = startOff + 256;
        values.fill(startOff, tableEnd, -1);
        valueSizes.fill(startOff, tableEnd, 0);
        int prefLen = level << 3;
        for (int i = 0; i < this.codeSizes.length; i++) {
            if (this.codeSizes[i] > prefLen && (level <= 0 || this.codes[i] >>> 32 - prefLen == prefix)) {
                int pref = this.codes[i] >>> 32 - prefLen - 8;
                int code = pref & 0xFF;
                int len = this.codeSizes[i] - prefLen;
                if (len <= 8) {
                    for (int k = 0; k < 1 << 8 - len; k++) {
                        values.set(startOff + code + k, i);
                        valueSizes.set(startOff + code + k, len);
                    }
                } else if (values.get(startOff + code) == -1) {
                    values.set(startOff + code, tableEnd);
                    tableEnd = this.invert(tableEnd, level + 1, pref, values, valueSizes);
                }
            }
        }
        return tableEnd;
    }

    public int readVLC16(BitReader _in) {
        int string = _in.check16Bits();
        int b = string >>> 8;
        int code = this.values[b];
        int len = this.valueSizes[b];
        if (len == 0) {
            b = (string & 0xFF) + code;
            code = this.values[b];
            _in.skipFast(8 + this.valueSizes[b]);
        } else {
            _in.skipFast(len);
        }
        return code;
    }

    public int readVLC(BitReader _in) {
        int code = 0;
        int len = 0;
        int overall = 0;
        int total = 0;
        for (int i = 0; len == 0; i++) {
            int string = _in.checkNBit(8);
            int ind = string + code;
            code = this.values[ind];
            len = this.valueSizes[ind];
            int bits = len != 0 ? len : 8;
            total += bits;
            overall = overall << bits | string >> 8 - bits;
            _in.skip(bits);
            if (code == -1) {
                throw new RuntimeException("Invalid code prefix " + binary(overall, (i << 3) + bits));
            }
        }
        return code;
    }

    private static String binary(int string, int len) {
        char[] symb = new char[len];
        for (int i = 0; i < len; i++) {
            symb[i] = (char) ((string & 1 << len - i - 1) != 0 ? 49 : 48);
        }
        return Platform.stringFromChars(symb);
    }

    public void writeVLC(BitWriter out, int code) {
        out.writeNBit(this.codes[code] >>> 32 - this.codeSizes[code], this.codeSizes[code]);
    }

    public void printTable(PrintStream ps) {
        for (int i = 0; i < this.values.length; i++) {
            ps.println(i + ": " + extracted(i) + " (" + this.valueSizes[i] + ") -> " + this.values[i]);
        }
    }

    private static String extracted(int num) {
        String str = Integer.toString(num & 0xFF, 2);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8 - str.length(); i++) {
            builder.append("0");
        }
        builder.append(str);
        return builder.toString();
    }

    public int[] getCodes() {
        return this.codes;
    }

    public int[] getCodeSizes() {
        return this.codeSizes;
    }
}