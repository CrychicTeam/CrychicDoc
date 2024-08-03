package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;

public class SliceHeader {

    public SeqParameterSet sps;

    public PictureParameterSet pps;

    public RefPicMarking refPicMarkingNonIDR;

    public RefPicMarkingIDR refPicMarkingIDR;

    public int[][][] refPicReordering;

    public PredictionWeightTable predWeightTable;

    public int firstMbInSlice;

    public boolean fieldPicFlag;

    public SliceType sliceType;

    public boolean sliceTypeRestr;

    public int picParameterSetId;

    public int frameNum;

    public boolean bottomFieldFlag;

    public int idrPicId;

    public int picOrderCntLsb;

    public int deltaPicOrderCntBottom;

    public int[] deltaPicOrderCnt;

    public int redundantPicCnt;

    public boolean directSpatialMvPredFlag;

    public boolean numRefIdxActiveOverrideFlag;

    public int[] numRefIdxActiveMinus1 = new int[2];

    public int cabacInitIdc;

    public int sliceQpDelta;

    public boolean spForSwitchFlag;

    public int sliceQsDelta;

    public int disableDeblockingFilterIdc;

    public int sliceAlphaC0OffsetDiv2;

    public int sliceBetaOffsetDiv2;

    public int sliceGroupChangeCycle;

    public String toString() {
        return Platform.toJSON(this);
    }
}