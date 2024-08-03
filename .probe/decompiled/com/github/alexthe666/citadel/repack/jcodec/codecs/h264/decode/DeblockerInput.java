package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;

public class DeblockerInput {

    public int[][] nCoeff;

    public H264Utils.MvList2D mvs;

    public MBType[] mbTypes;

    public int[][] mbQps;

    public boolean[] tr8x8Used;

    public Frame[][][] refsUsed;

    public SliceHeader[] shs;

    public DeblockerInput(SeqParameterSet activeSps) {
        int picWidthInMbs = activeSps.picWidthInMbsMinus1 + 1;
        int picHeightInMbs = SeqParameterSet.getPicHeightInMbs(activeSps);
        this.nCoeff = new int[picHeightInMbs << 2][picWidthInMbs << 2];
        this.mvs = new H264Utils.MvList2D(picWidthInMbs << 2, picHeightInMbs << 2);
        this.mbTypes = new MBType[picHeightInMbs * picWidthInMbs];
        this.tr8x8Used = new boolean[picHeightInMbs * picWidthInMbs];
        this.mbQps = new int[3][picHeightInMbs * picWidthInMbs];
        this.shs = new SliceHeader[picHeightInMbs * picWidthInMbs];
        this.refsUsed = new Frame[picHeightInMbs * picWidthInMbs][][];
    }
}