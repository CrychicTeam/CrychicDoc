package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public final class MBType {

    public static final MBType I_NxN = new MBType(true, 0);

    public static final MBType I_16x16 = new MBType(true, 1);

    public static final MBType I_PCM = new MBType(true, 25);

    public static final MBType P_16x16 = new MBType(false, 0);

    public static final MBType P_16x8 = new MBType(false, 1);

    public static final MBType P_8x16 = new MBType(false, 2);

    public static final MBType P_8x8 = new MBType(false, 3);

    public static final MBType P_8x8ref0 = new MBType(false, 4);

    public static final MBType B_Direct_16x16 = new MBType(false, 0);

    public static final MBType B_L0_16x16 = new MBType(false, 1);

    public static final MBType B_L1_16x16 = new MBType(false, 2);

    public static final MBType B_Bi_16x16 = new MBType(false, 3);

    public static final MBType B_L0_L0_16x8 = new MBType(false, 4);

    public static final MBType B_L0_L0_8x16 = new MBType(false, 5);

    public static final MBType B_L1_L1_16x8 = new MBType(false, 6);

    public static final MBType B_L1_L1_8x16 = new MBType(false, 7);

    public static final MBType B_L0_L1_16x8 = new MBType(false, 8);

    public static final MBType B_L0_L1_8x16 = new MBType(false, 9);

    public static final MBType B_L1_L0_16x8 = new MBType(false, 10);

    public static final MBType B_L1_L0_8x16 = new MBType(false, 11);

    public static final MBType B_L0_Bi_16x8 = new MBType(false, 12);

    public static final MBType B_L0_Bi_8x16 = new MBType(false, 13);

    public static final MBType B_L1_Bi_16x8 = new MBType(false, 14);

    public static final MBType B_L1_Bi_8x16 = new MBType(false, 15);

    public static final MBType B_Bi_L0_16x8 = new MBType(false, 16);

    public static final MBType B_Bi_L0_8x16 = new MBType(false, 17);

    public static final MBType B_Bi_L1_16x8 = new MBType(false, 18);

    public static final MBType B_Bi_L1_8x16 = new MBType(false, 19);

    public static final MBType B_Bi_Bi_16x8 = new MBType(false, 20);

    public static final MBType B_Bi_Bi_8x16 = new MBType(false, 21);

    public static final MBType B_8x8 = new MBType(false, 22);

    public boolean intra;

    public int _code;

    private MBType(boolean intra, int code) {
        this.intra = intra;
        this._code = code;
    }

    public boolean isIntra() {
        return this.intra;
    }

    public int code() {
        return this._code;
    }
}