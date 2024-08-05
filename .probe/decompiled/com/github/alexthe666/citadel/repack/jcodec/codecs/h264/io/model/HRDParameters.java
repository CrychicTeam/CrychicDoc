package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

public class HRDParameters {

    public int cpbCntMinus1;

    public int bitRateScale;

    public int cpbSizeScale;

    public int[] bitRateValueMinus1;

    public int[] cpbSizeValueMinus1;

    public boolean[] cbrFlag;

    public int initialCpbRemovalDelayLengthMinus1;

    public int cpbRemovalDelayLengthMinus1;

    public int dpbOutputDelayLengthMinus1;

    public int timeOffsetLength;
}