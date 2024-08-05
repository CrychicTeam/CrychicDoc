package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;

public class DecoderState {

    int[] chromaQpOffset;

    int qp;

    byte[][] leftRow;

    byte[][] topLine;

    byte[][] topLeft;

    ColorSpace chromaFormat;

    H264Utils.MvList mvTop;

    H264Utils.MvList mvLeft;

    H264Utils.MvList mvTopLeft;

    public DecoderState(SliceHeader sh) {
        int mbWidth = sh.sps.picWidthInMbsMinus1 + 1;
        this.chromaQpOffset = new int[] { sh.pps.chromaQpIndexOffset, sh.pps.extended != null ? sh.pps.extended.secondChromaQpIndexOffset : sh.pps.chromaQpIndexOffset };
        this.chromaFormat = sh.sps.chromaFormatIdc;
        this.mvTop = new H264Utils.MvList((mbWidth << 2) + 1);
        this.mvLeft = new H264Utils.MvList(4);
        this.mvTopLeft = new H264Utils.MvList(1);
        this.leftRow = new byte[3][16];
        this.topLeft = new byte[3][4];
        this.topLine = new byte[3][mbWidth << 4];
        this.qp = sh.pps.picInitQpMinus26 + 26 + sh.sliceQpDelta;
    }
}