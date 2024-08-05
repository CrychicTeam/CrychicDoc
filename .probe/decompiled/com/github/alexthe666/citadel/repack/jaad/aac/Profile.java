package com.github.alexthe666.citadel.repack.jaad.aac;

public enum Profile {

    UNKNOWN(-1, "unknown", false),
    AAC_MAIN(1, "AAC Main Profile", true),
    AAC_LC(2, "AAC Low Complexity", true),
    AAC_SSR(3, "AAC Scalable Sample Rate", false),
    AAC_LTP(4, "AAC Long Term Prediction", true),
    AAC_SBR(5, "AAC SBR", true),
    AAC_SCALABLE(6, "Scalable AAC", false),
    TWIN_VQ(7, "TwinVQ", false),
    AAC_LD(11, "AAC Low Delay", false),
    ER_AAC_LC(17, "Error Resilient AAC Low Complexity", true),
    ER_AAC_SSR(18, "Error Resilient AAC SSR", false),
    ER_AAC_LTP(19, "Error Resilient AAC Long Term Prediction", true),
    ER_AAC_SCALABLE(20, "Error Resilient Scalable AAC", false),
    ER_TWIN_VQ(21, "Error Resilient TwinVQ", false),
    ER_BSAC(22, "Error Resilient BSAC", false),
    ER_AAC_LD(23, "Error Resilient AAC Low Delay", false);

    private static final Profile[] ALL = new Profile[] { AAC_MAIN, AAC_LC, AAC_SSR, AAC_LTP, AAC_SBR, AAC_SCALABLE, TWIN_VQ, null, null, null, AAC_LD, null, null, null, null, null, ER_AAC_LC, ER_AAC_SSR, ER_AAC_LTP, ER_AAC_SCALABLE, ER_TWIN_VQ, ER_BSAC, ER_AAC_LD };

    private final int num;

    private final String descr;

    private final boolean supported;

    public static Profile forInt(int i) {
        Profile p;
        if (i > 0 && i <= ALL.length) {
            p = ALL[i - 1];
        } else {
            p = UNKNOWN;
        }
        return p;
    }

    private Profile(int num, String descr, boolean supported) {
        this.num = num;
        this.descr = descr;
        this.supported = supported;
    }

    public int getIndex() {
        return this.num;
    }

    public String getDescription() {
        return this.descr;
    }

    public String toString() {
        return this.descr;
    }

    public boolean isDecodingSupported() {
        return this.supported;
    }

    public boolean isErrorResilientProfile() {
        return this.num > 16;
    }
}