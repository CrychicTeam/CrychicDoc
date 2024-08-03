package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.MapManager;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.aso.Mapper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.IntObjectMap;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class SliceDecoder {

    private Mapper mapper;

    private MBlockDecoderIntra16x16 decoderIntra16x16;

    private MBlockDecoderIntraNxN decoderIntraNxN;

    private MBlockDecoderInter decoderInter;

    private MBlockDecoderInter8x8 decoderInter8x8;

    private MBlockSkipDecoder skipDecoder;

    private MBlockDecoderBDirect decoderBDirect;

    private RefListManager refListManager;

    private MBlockDecoderIPCM decoderIPCM;

    private SliceReader parser;

    private SeqParameterSet activeSps;

    private Frame frameOut;

    private DecoderState decoderState;

    private DeblockerInput di;

    private IntObjectMap<Frame> lRefs;

    private Frame[] sRefs;

    public SliceDecoder(SeqParameterSet activeSps, Frame[] sRefs, IntObjectMap<Frame> lRefs, DeblockerInput di, Frame result) {
        this.di = di;
        this.activeSps = activeSps;
        this.frameOut = result;
        this.sRefs = sRefs;
        this.lRefs = lRefs;
    }

    public void decodeFromReader(SliceReader sliceReader) {
        this.parser = sliceReader;
        this.initContext();
        MBlockDecoderUtils.debugPrint("============%d============= ", this.frameOut.getPOC());
        Frame[][] refList = this.refListManager.getRefList();
        this.decodeMacroblocks(refList);
    }

    private void initContext() {
        SliceHeader sh = this.parser.getSliceHeader();
        this.decoderState = new DecoderState(sh);
        this.mapper = new MapManager(sh.sps, sh.pps).getMapper(sh);
        this.decoderIntra16x16 = new MBlockDecoderIntra16x16(this.mapper, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.decoderIntraNxN = new MBlockDecoderIntraNxN(this.mapper, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.decoderInter = new MBlockDecoderInter(this.mapper, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.decoderBDirect = new MBlockDecoderBDirect(this.mapper, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.decoderInter8x8 = new MBlockDecoderInter8x8(this.mapper, this.decoderBDirect, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.skipDecoder = new MBlockSkipDecoder(this.mapper, this.decoderBDirect, sh, this.di, this.frameOut.getPOC(), this.decoderState);
        this.decoderIPCM = new MBlockDecoderIPCM(this.mapper, this.decoderState);
        this.refListManager = new RefListManager(sh, this.sRefs, this.lRefs, this.frameOut);
    }

    private void decodeMacroblocks(Frame[][] refList) {
        Picture mb = Picture.create(16, 16, this.activeSps.chromaFormatIdc);
        int mbWidth = this.activeSps.picWidthInMbsMinus1 + 1;
        MBlock mBlock = new MBlock(this.activeSps.chromaFormatIdc);
        while (this.parser.readMacroblock(mBlock)) {
            this.decode(mBlock, this.parser.getSliceHeader().sliceType, mb, refList);
            int mbAddr = this.mapper.getAddress(mBlock.mbIdx);
            int mbX = mbAddr % mbWidth;
            int mbY = mbAddr / mbWidth;
            putMacroblock(this.frameOut, mb, mbX, mbY);
            this.di.shs[mbAddr] = this.parser.getSliceHeader();
            this.di.refsUsed[mbAddr] = refList;
            this.fillCoeff(mBlock, mbX, mbY);
            mb.fill(0);
            mBlock.clear();
        }
    }

    private void fillCoeff(MBlock mBlock, int mbX, int mbY) {
        for (int i = 0; i < 16; i++) {
            int blkOffLeft = H264Const.MB_BLK_OFF_LEFT[i];
            int blkOffTop = H264Const.MB_BLK_OFF_TOP[i];
            int blkX = (mbX << 2) + blkOffLeft;
            int blkY = (mbY << 2) + blkOffTop;
            this.di.nCoeff[blkY][blkX] = mBlock.nCoeff[i];
        }
    }

    public void decode(MBlock mBlock, SliceType sliceType, Picture mb, Frame[][] references) {
        if (mBlock.skipped) {
            this.skipDecoder.decodeSkip(mBlock, references, mb, sliceType);
        } else if (sliceType == SliceType.I) {
            this.decodeMBlockI(mBlock, mb);
        } else if (sliceType == SliceType.P) {
            this.decodeMBlockP(mBlock, mb, references);
        } else {
            this.decodeMBlockB(mBlock, mb, references);
        }
    }

    private void decodeMBlockI(MBlock mBlock, Picture mb) {
        this.decodeMBlockIInt(mBlock, mb);
    }

    private void decodeMBlockIInt(MBlock mBlock, Picture mb) {
        if (mBlock.curMbType == MBType.I_NxN) {
            this.decoderIntraNxN.decode(mBlock, mb);
        } else if (mBlock.curMbType == MBType.I_16x16) {
            this.decoderIntra16x16.decode(mBlock, mb);
        } else {
            Logger.warn("IPCM macroblock found. Not tested, may cause unpredictable behavior.");
            this.decoderIPCM.decode(mBlock, mb);
        }
    }

    private void decodeMBlockP(MBlock mBlock, Picture mb, Frame[][] references) {
        if (MBType.P_16x16 == mBlock.curMbType) {
            this.decoderInter.decode16x16(mBlock, mb, references, H264Const.PartPred.L0);
        } else if (MBType.P_16x8 == mBlock.curMbType) {
            this.decoderInter.decode16x8(mBlock, mb, references, H264Const.PartPred.L0, H264Const.PartPred.L0);
        } else if (MBType.P_8x16 == mBlock.curMbType) {
            this.decoderInter.decode8x16(mBlock, mb, references, H264Const.PartPred.L0, H264Const.PartPred.L0);
        } else if (MBType.P_8x8 == mBlock.curMbType) {
            this.decoderInter8x8.decode(mBlock, references, mb, SliceType.P, false);
        } else if (MBType.P_8x8ref0 == mBlock.curMbType) {
            this.decoderInter8x8.decode(mBlock, references, mb, SliceType.P, true);
        } else {
            this.decodeMBlockIInt(mBlock, mb);
        }
    }

    private void decodeMBlockB(MBlock mBlock, Picture mb, Frame[][] references) {
        if (mBlock.curMbType.isIntra()) {
            this.decodeMBlockIInt(mBlock, mb);
        } else if (mBlock.curMbType == MBType.B_Direct_16x16) {
            this.decoderBDirect.decode(mBlock, mb, references);
        } else if (mBlock.mbType <= 3) {
            this.decoderInter.decode16x16(mBlock, mb, references, H264Const.bPredModes[mBlock.mbType][0]);
        } else if (mBlock.mbType == 22) {
            this.decoderInter8x8.decode(mBlock, references, mb, SliceType.B, false);
        } else if ((mBlock.mbType & 1) == 0) {
            this.decoderInter.decode16x8(mBlock, mb, references, H264Const.bPredModes[mBlock.mbType][0], H264Const.bPredModes[mBlock.mbType][1]);
        } else {
            this.decoderInter.decode8x16(mBlock, mb, references, H264Const.bPredModes[mBlock.mbType][0], H264Const.bPredModes[mBlock.mbType][1]);
        }
    }

    private static void putMacroblock(Picture tgt, Picture decoded, int mbX, int mbY) {
        byte[] luma = tgt.getPlaneData(0);
        int stride = tgt.getPlaneWidth(0);
        byte[] cb = tgt.getPlaneData(1);
        byte[] cr = tgt.getPlaneData(2);
        int strideChroma = tgt.getPlaneWidth(1);
        int dOff = 0;
        int mbx16 = mbX * 16;
        int mby16 = mbY * 16;
        byte[] decodedY = decoded.getPlaneData(0);
        for (int i = 0; i < 16; i++) {
            System.arraycopy(decodedY, dOff, luma, (mby16 + i) * stride + mbx16, 16);
            dOff += 16;
        }
        int mbx8 = mbX * 8;
        int mby8 = mbY * 8;
        byte[] decodedCb = decoded.getPlaneData(1);
        byte[] decodedCr = decoded.getPlaneData(2);
        for (int i = 0; i < 8; i++) {
            int decodePos = i << 3;
            int chromaPos = (mby8 + i) * strideChroma + mbx8;
            System.arraycopy(decodedCb, decodePos, cb, chromaPos, 8);
            System.arraycopy(decodedCr, decodePos, cr, chromaPos, 8);
        }
    }
}