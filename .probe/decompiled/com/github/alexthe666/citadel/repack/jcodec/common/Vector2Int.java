package com.github.alexthe666.citadel.repack.jcodec.common;

public class Vector2Int {

    public static int el16_0(int packed) {
        return packed << 16 >> 16;
    }

    public static int el16_1(int packed) {
        return packed >> 16;
    }

    public static int el16(int packed, int n) {
        switch(n) {
            case 0:
                return el16_0(packed);
            default:
                return el16_1(packed);
        }
    }

    public static int set16_0(int packed, int el) {
        return packed & -65536 | el & 65535;
    }

    public static int set16_1(int packed, int el) {
        return packed & 65535 | (el & 65535) << 16;
    }

    public static int set16(int packed, int el, int n) {
        switch(n) {
            case 0:
                return set16_0(packed, el);
            default:
                return set16_1(packed, el);
        }
    }

    public static int pack16(int el0, int el1) {
        return (el1 & 65535) << 16 | el0 & 65535;
    }
}