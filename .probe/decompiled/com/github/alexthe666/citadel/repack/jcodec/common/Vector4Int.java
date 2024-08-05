package com.github.alexthe666.citadel.repack.jcodec.common;

public class Vector4Int {

    public static int el8_0(int packed) {
        return packed << 24 >> 24;
    }

    public static int el8_1(int packed) {
        return packed << 16 >> 24;
    }

    public static int el8_2(int packed) {
        return packed << 8 >> 24;
    }

    public static int el8_3(int packed) {
        return packed >> 24;
    }

    public static int el8(int packed, int n) {
        switch(n) {
            case 0:
                return el8_0(packed);
            case 1:
                return el8_1(packed);
            case 2:
                return el8_2(packed);
            default:
                return el8_3(packed);
        }
    }

    public static int set8_0(int packed, int el) {
        return packed & -256 | el & 0xFF;
    }

    public static int set8_1(int packed, int el) {
        return packed & -65281 | (el & 0xFF) << 8;
    }

    public static int set8_2(int packed, int el) {
        return packed & -16711681 | (el & 0xFF) << 16;
    }

    public static int set8_3(int packed, int el) {
        return packed & -16711681 | (el & 0xFF) << 24;
    }

    public static int set8(int packed, int el, int n) {
        switch(n) {
            case 0:
                return set8_0(packed, el);
            case 1:
                return set8_1(packed, el);
            case 2:
                return set8_2(packed, el);
            default:
                return set8_3(packed, el);
        }
    }

    public static int pack8(int el0, int el1, int el2, int el3) {
        return (el3 & 0xFF) << 24 | (el2 & 0xFF) << 16 | (el1 & 0xFF) << 8 | el0 & 0xFF;
    }
}