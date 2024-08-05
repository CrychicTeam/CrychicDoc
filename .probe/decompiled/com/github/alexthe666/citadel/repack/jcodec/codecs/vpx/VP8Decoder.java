package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;

public class VP8Decoder extends VideoDecoder {

    private byte[][] segmentationMap;

    private int[] refLoopFilterDeltas = new int[4];

    private int[] modeLoopFilterDeltas = new int[4];

    @Override
    public Picture decodeFrame(ByteBuffer frame, byte[][] buffer) {
        byte[] firstThree = new byte[3];
        frame.get(firstThree);
        boolean keyFrame = VP8Util.getBitInBytes(firstThree, 0) == 0;
        if (!keyFrame) {
            return null;
        } else {
            int version = VP8Util.getBitsInBytes(firstThree, 1, 3);
            boolean showFrame = VP8Util.getBitInBytes(firstThree, 4) > 0;
            int partitionSize = VP8Util.getBitsInBytes(firstThree, 5, 19);
            String threeByteToken = printHexByte(frame.get()) + " " + printHexByte(frame.get()) + " " + printHexByte(frame.get());
            int twoBytesWidth = frame.get() & 255 | (frame.get() & 255) << 8;
            int twoBytesHeight = frame.get() & 255 | (frame.get() & 255) << 8;
            int width = twoBytesWidth & 16383;
            int height = twoBytesHeight & 16383;
            int numberOfMBRows = VP8Util.getMacroblockCount(height);
            int numberOfMBCols = VP8Util.getMacroblockCount(width);
            if (this.segmentationMap == null) {
                this.segmentationMap = new byte[numberOfMBRows][numberOfMBCols];
            }
            VPXMacroblock[][] mbs = new VPXMacroblock[numberOfMBRows + 2][numberOfMBCols + 2];
            for (int row = 0; row < numberOfMBRows + 2; row++) {
                for (int col = 0; col < numberOfMBCols + 2; col++) {
                    mbs[row][col] = new VPXMacroblock(row, col);
                }
            }
            int headerOffset = frame.position();
            VPXBooleanDecoder headerDecoder = new VPXBooleanDecoder(frame, 0);
            boolean isYUVColorSpace = headerDecoder.readBitEq() == 0;
            boolean clampingRequired = headerDecoder.readBitEq() == 0;
            int segmentation = headerDecoder.readBitEq();
            VP8Decoder.SegmentBasedAdjustments segmentBased = null;
            if (segmentation != 0) {
                segmentBased = this.updateSegmentation(headerDecoder);
                for (int row = 0; row < numberOfMBRows; row++) {
                    for (int col = 0; col < numberOfMBCols; col++) {
                        mbs[row + 1][col + 1].segment = this.segmentationMap[row][col];
                    }
                }
            }
            int simpleFilter = headerDecoder.readBitEq();
            int filterLevel = headerDecoder.decodeInt(6);
            int filterType = filterLevel == 0 ? 0 : (simpleFilter > 0 ? 1 : 2);
            int sharpnessLevel = headerDecoder.decodeInt(3);
            int loopFilterDeltaFlag = headerDecoder.readBitEq();
            if (loopFilterDeltaFlag == 1) {
                int loopFilterDeltaUpdate = headerDecoder.readBitEq();
                if (loopFilterDeltaUpdate == 1) {
                    for (int i = 0; i < 4; i++) {
                        if (headerDecoder.readBitEq() > 0) {
                            this.refLoopFilterDeltas[i] = headerDecoder.decodeInt(6);
                            if (headerDecoder.readBitEq() > 0) {
                                this.refLoopFilterDeltas[i] = this.refLoopFilterDeltas[i] * -1;
                            }
                        }
                    }
                    for (int ix = 0; ix < 4; ix++) {
                        if (headerDecoder.readBitEq() > 0) {
                            this.modeLoopFilterDeltas[ix] = headerDecoder.decodeInt(6);
                            if (headerDecoder.readBitEq() > 0) {
                                this.modeLoopFilterDeltas[ix] = this.modeLoopFilterDeltas[ix] * -1;
                            }
                        }
                    }
                }
            }
            int log2OfPartCnt = headerDecoder.decodeInt(2);
            Preconditions.checkState(0 == log2OfPartCnt);
            int partitionsCount = 1;
            long runningSize = 0L;
            long zSize = (long) (frame.limit() - (partitionSize + headerOffset));
            ByteBuffer tokenBuffer = frame.duplicate();
            tokenBuffer.position(partitionSize + headerOffset);
            VPXBooleanDecoder decoder = new VPXBooleanDecoder(tokenBuffer, 0);
            int yacIndex = headerDecoder.decodeInt(7);
            int ydcDelta = headerDecoder.readBitEq() > 0 ? VP8Util.delta(headerDecoder) : 0;
            int y2dcDelta = headerDecoder.readBitEq() > 0 ? VP8Util.delta(headerDecoder) : 0;
            int y2acDelta = headerDecoder.readBitEq() > 0 ? VP8Util.delta(headerDecoder) : 0;
            int chromaDCDelta = headerDecoder.readBitEq() > 0 ? VP8Util.delta(headerDecoder) : 0;
            int chromaACDelta = headerDecoder.readBitEq() > 0 ? VP8Util.delta(headerDecoder) : 0;
            boolean refreshProbs = headerDecoder.readBitEq() == 0;
            VP8Util.QuantizationParams quants = new VP8Util.QuantizationParams(yacIndex, ydcDelta, y2dcDelta, y2acDelta, chromaDCDelta, chromaACDelta);
            int[][][][] coefProbs = VP8Util.getDefaultCoefProbs();
            for (int ixx = 0; ixx < 4; ixx++) {
                for (int j = 0; j < 8; j++) {
                    for (int k = 0; k < 3; k++) {
                        for (int l = 0; l < 11; l++) {
                            if (headerDecoder.readBit(VP8Util.vp8CoefUpdateProbs[ixx][j][k][l]) > 0) {
                                int newp = headerDecoder.decodeInt(8);
                                coefProbs[ixx][j][k][l] = newp;
                            }
                        }
                    }
                }
            }
            int macroBlockNoCoeffSkip = headerDecoder.readBitEq();
            Preconditions.checkState(1 == macroBlockNoCoeffSkip);
            int probSkipFalse = headerDecoder.decodeInt(8);
            for (int mbRow = 0; mbRow < numberOfMBRows; mbRow++) {
                for (int mbCol = 0; mbCol < numberOfMBCols; mbCol++) {
                    VPXMacroblock mb = mbs[mbRow + 1][mbCol + 1];
                    if (segmentation != 0 && segmentBased != null && segmentBased.segmentProbs != null) {
                        mb.segment = headerDecoder.readTree(VP8Util.segmentTree, segmentBased.segmentProbs);
                        this.segmentationMap[mbRow][mbCol] = (byte) mb.segment;
                    }
                    if (segmentation != 0 && segmentBased != null && segmentBased.qp != null) {
                        int mbRowx;
                        if (segmentBased.abs != 0) {
                            mbRowx = segmentBased.qp[mb.segment];
                        } else {
                            mbRowx = yacIndex + segmentBased.qp[mb.segment];
                        }
                        quants = new VP8Util.QuantizationParams(mbRowx, ydcDelta, y2dcDelta, y2acDelta, chromaDCDelta, chromaACDelta);
                    }
                    mb.quants = quants;
                    if (loopFilterDeltaFlag != 0) {
                        int var72 = filterLevel + this.refLoopFilterDeltas[0];
                        var72 = MathUtil.clip(var72, 0, 63);
                        mb.filterLevel = var72;
                    } else {
                        mb.filterLevel = filterLevel;
                    }
                    if (segmentation != 0 && segmentBased != null && segmentBased.lf != null) {
                        if (segmentBased.abs != 0) {
                            mb.filterLevel = segmentBased.lf[mb.segment];
                        } else {
                            mb.filterLevel = mb.filterLevel + segmentBased.lf[mb.segment];
                            mb.filterLevel = MathUtil.clip(mb.filterLevel, 0, 63);
                        }
                    }
                    if (macroBlockNoCoeffSkip > 0) {
                        mb.skipCoeff = headerDecoder.readBit(probSkipFalse);
                    }
                    mb.lumaMode = headerDecoder.readTree(VP8Util.keyFrameYModeTree, VP8Util.keyFrameYModeProb);
                    if (mb.lumaMode == 4) {
                        for (int sbRow = 0; sbRow < 4; sbRow++) {
                            for (int sbCol = 0; sbCol < 4; sbCol++) {
                                VPXMacroblock.Subblock sb = mb.ySubblocks[sbRow][sbCol];
                                VPXMacroblock.Subblock A = sb.getAbove(VP8Util.PLANE.Y1, mbs);
                                VPXMacroblock.Subblock L = sb.getLeft(VP8Util.PLANE.Y1, mbs);
                                sb.mode = headerDecoder.readTree(VP8Util.SubblockConstants.subblockModeTree, VP8Util.SubblockConstants.keyFrameSubblockModeProb[A.mode][L.mode]);
                            }
                        }
                    } else {
                        int fixedMode;
                        switch(mb.lumaMode) {
                            case 0:
                                fixedMode = 0;
                                break;
                            case 1:
                                fixedMode = 2;
                                break;
                            case 2:
                                fixedMode = 3;
                                break;
                            case 3:
                                fixedMode = 1;
                                break;
                            default:
                                fixedMode = 0;
                        }
                        mb.lumaMode = this.edgeEmu(mb.lumaMode, mbCol, mbRow);
                        for (int x = 0; x < 4; x++) {
                            for (int y = 0; y < 4; y++) {
                                mb.ySubblocks[y][x].mode = fixedMode;
                            }
                        }
                    }
                    mb.chromaMode = headerDecoder.readTree(VP8Util.vp8UVModeTree, VP8Util.vp8KeyFrameUVModeProb);
                }
            }
            for (int mbRow = 0; mbRow < numberOfMBRows; mbRow++) {
                for (int mbCol = 0; mbCol < numberOfMBCols; mbCol++) {
                    VPXMacroblock mbx = mbs[mbRow + 1][mbCol + 1];
                    mbx.decodeMacroBlock(mbs, decoder, coefProbs);
                    mbx.dequantMacroBlock(mbs);
                }
            }
            if (filterType > 0 && filterLevel != 0) {
                if (filterType == 2) {
                    FilterUtil.loopFilterUV(mbs, sharpnessLevel, keyFrame);
                    FilterUtil.loopFilterY(mbs, sharpnessLevel, keyFrame);
                } else if (filterType == 1) {
                }
            }
            Picture p = Picture.createPicture(width, height, buffer, ColorSpace.YUV420);
            int mbWidth = VP8Util.getMacroblockCount(width);
            int mbHeight = VP8Util.getMacroblockCount(height);
            for (int mbRow = 0; mbRow < mbHeight; mbRow++) {
                for (int mbCol = 0; mbCol < mbWidth; mbCol++) {
                    VPXMacroblock mbx = mbs[mbRow + 1][mbCol + 1];
                    mbx.put(mbRow, mbCol, p);
                }
            }
            return p;
        }
    }

    private int edgeEmu(int mode, int mbCol, int mbRow) {
        switch(mode) {
            case 1:
                return mbRow == 0 ? 0 : mode;
            case 2:
                return mbCol == 0 ? 0 : mode;
            case 3:
                return this.edgeEmuTm(mode, mbCol, mbRow);
            default:
                return mode;
        }
    }

    private int edgeEmuTm(int mode, int mbCol, int mbRow) {
        if (mbCol == 0) {
            return mbRow != 0 ? 1 : 0;
        } else {
            return mbRow != 0 ? mode : 2;
        }
    }

    private VP8Decoder.SegmentBasedAdjustments updateSegmentation(VPXBooleanDecoder headerDecoder) {
        int updateMBSegmentationMap = headerDecoder.readBitEq();
        int updateSegmentFeatureData = headerDecoder.readBitEq();
        int[] qp = null;
        int[] lf = null;
        int abs = 0;
        if (updateSegmentFeatureData != 0) {
            qp = new int[4];
            lf = new int[4];
            abs = headerDecoder.readBitEq();
            for (int i = 0; i < 4; i++) {
                int quantizerUpdate = headerDecoder.readBitEq();
                if (quantizerUpdate != 0) {
                    qp[i] = headerDecoder.decodeInt(7);
                    qp[i] = headerDecoder.readBitEq() != 0 ? -qp[i] : qp[i];
                }
            }
            for (int ix = 0; ix < 4; ix++) {
                int loopFilterUpdate = headerDecoder.readBitEq();
                if (loopFilterUpdate != 0) {
                    lf[ix] = headerDecoder.decodeInt(6);
                    lf[ix] = headerDecoder.readBitEq() != 0 ? -lf[ix] : lf[ix];
                }
            }
        }
        int[] segmentProbs = new int[3];
        if (updateMBSegmentationMap != 0) {
            for (int ixx = 0; ixx < 3; ixx++) {
                int segmentProbUpdate = headerDecoder.readBitEq();
                if (segmentProbUpdate != 0) {
                    segmentProbs[ixx] = headerDecoder.decodeInt(8);
                } else {
                    segmentProbs[ixx] = 255;
                }
            }
        }
        return new VP8Decoder.SegmentBasedAdjustments(segmentProbs, qp, lf, abs);
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        return (data.get(3) & 0xFF) == 157 && (data.get(4) & 0xFF) == 1 && (data.get(5) & 0xFF) == 42 ? 100 : 0;
    }

    public static String printHexByte(byte b) {
        return "0x" + Integer.toHexString(b & 255);
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer frame) {
        NIOUtils.skip(frame, 6);
        int twoBytesWidth = frame.get() & 255 | (frame.get() & 255) << 8;
        int twoBytesHeight = frame.get() & 255 | (frame.get() & 255) << 8;
        int width = twoBytesWidth & 16383;
        int height = twoBytesHeight & 16383;
        return VideoCodecMeta.createSimpleVideoCodecMeta(new Size(width, height), ColorSpace.YUV420);
    }

    private static class SegmentBasedAdjustments {

        private int[] segmentProbs;

        private int[] qp;

        private int[] lf;

        private int abs;

        public SegmentBasedAdjustments(int[] segmentProbs, int[] qp, int[] lf, int abs) {
            this.segmentProbs = segmentProbs;
            this.qp = qp;
            this.lf = lf;
            this.abs = abs;
        }
    }
}