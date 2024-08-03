package com.github.alexthe666.citadel.repack.jcodec.codecs.h264;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.DumbRateControl;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.EncodedMB;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.MBEncoderHelper;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.MBEncoderI16x16;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.MBEncoderP16x16;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.MotionEstimator;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode.RateControl;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.CAVLC;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnitType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.RefPicMarkingIDR;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.CAVLCWriter;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.write.SliceHeaderWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoEncoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;

public class H264Encoder extends VideoEncoder {

    private static final int KEY_INTERVAL_DEFAULT = 25;

    private static final int MOTION_SEARCH_RANGE_DEFAULT = 16;

    private CAVLC[] cavlc;

    private byte[][] leftRow;

    private byte[][] topLine;

    private RateControl rc;

    private int frameNumber;

    private int keyInterval;

    private int motionSearchRange;

    private int maxPOC;

    private int maxFrameNumber;

    private SeqParameterSet sps;

    private PictureParameterSet pps;

    private MBEncoderI16x16 mbEncoderI16x16;

    private MBEncoderP16x16 mbEncoderP16x16;

    private Picture ref;

    private Picture picOut;

    private EncodedMB[] topEncoded;

    private EncodedMB outMB;

    public static H264Encoder createH264Encoder() {
        return new H264Encoder(new DumbRateControl());
    }

    public H264Encoder(RateControl rc) {
        this.rc = rc;
        this.keyInterval = 25;
        this.motionSearchRange = 16;
    }

    public int getKeyInterval() {
        return this.keyInterval;
    }

    public void setKeyInterval(int keyInterval) {
        this.keyInterval = keyInterval;
    }

    public int getMotionSearchRange() {
        return this.motionSearchRange;
    }

    public void setMotionSearchRange(int motionSearchRange) {
        this.motionSearchRange = motionSearchRange;
    }

    @Override
    public VideoEncoder.EncodedFrame encodeFrame(Picture pic, ByteBuffer _out) {
        if (pic.getColor() != ColorSpace.YUV420J) {
            throw new IllegalArgumentException("Input picture color is not supported: " + pic.getColor());
        } else {
            if (this.frameNumber >= this.keyInterval) {
                this.frameNumber = 0;
            }
            SliceType sliceType = this.frameNumber == 0 ? SliceType.I : SliceType.P;
            boolean idr = this.frameNumber == 0;
            ByteBuffer data = this.doEncodeFrame(pic, _out, idr, this.frameNumber++, sliceType);
            return new VideoEncoder.EncodedFrame(data, idr);
        }
    }

    public ByteBuffer encodeIDRFrame(Picture pic, ByteBuffer _out) {
        this.frameNumber = 0;
        return this.doEncodeFrame(pic, _out, true, this.frameNumber, SliceType.I);
    }

    public ByteBuffer encodePFrame(Picture pic, ByteBuffer _out) {
        this.frameNumber++;
        return this.doEncodeFrame(pic, _out, true, this.frameNumber, SliceType.P);
    }

    public ByteBuffer doEncodeFrame(Picture pic, ByteBuffer _out, boolean idr, int frameNumber, SliceType frameType) {
        ByteBuffer dup = _out.duplicate();
        int maxSize = Math.min(dup.remaining(), pic.getWidth() * pic.getHeight());
        maxSize -= maxSize >>> 6;
        int qp = this.rc.startPicture(pic.getSize(), maxSize, frameType);
        if (idr) {
            this.sps = this.initSPS(new Size(pic.getCroppedWidth(), pic.getCroppedHeight()));
            this.pps = this.initPPS();
            this.maxPOC = 1 << this.sps.log2MaxPicOrderCntLsbMinus4 + 4;
            this.maxFrameNumber = 1 << this.sps.log2MaxFrameNumMinus4 + 4;
        }
        if (idr) {
            dup.putInt(1);
            new NALUnit(NALUnitType.SPS, 3).write(dup);
            this.writeSPS(dup, this.sps);
            dup.putInt(1);
            new NALUnit(NALUnitType.PPS, 3).write(dup);
            this.writePPS(dup, this.pps);
        }
        int mbWidth = this.sps.picWidthInMbsMinus1 + 1;
        int mbHeight = this.sps.picHeightInMapUnitsMinus1 + 1;
        this.leftRow = new byte[][] { new byte[16], new byte[8], new byte[8] };
        this.topLine = new byte[][] { new byte[mbWidth << 4], new byte[mbWidth << 3], new byte[mbWidth << 3] };
        this.picOut = Picture.create(mbWidth << 4, mbHeight << 4, ColorSpace.YUV420J);
        this.outMB = new EncodedMB();
        this.topEncoded = new EncodedMB[mbWidth];
        for (int i = 0; i < mbWidth; i++) {
            this.topEncoded[i] = new EncodedMB();
        }
        this.encodeSlice(this.sps, this.pps, pic, dup, idr, frameNumber, frameType, qp);
        this.putLastMBLine();
        this.ref = this.picOut;
        dup.flip();
        return dup;
    }

    private void writePPS(ByteBuffer dup, PictureParameterSet pps) {
        ByteBuffer tmp = ByteBuffer.allocate(1024);
        pps.write(tmp);
        tmp.flip();
        H264Utils.escapeNAL(tmp, dup);
    }

    private void writeSPS(ByteBuffer dup, SeqParameterSet sps) {
        ByteBuffer tmp = ByteBuffer.allocate(1024);
        sps.write(tmp);
        tmp.flip();
        H264Utils.escapeNAL(tmp, dup);
    }

    public PictureParameterSet initPPS() {
        PictureParameterSet pps = new PictureParameterSet();
        pps.picInitQpMinus26 = 0;
        return pps;
    }

    public SeqParameterSet initSPS(Size sz) {
        SeqParameterSet sps = new SeqParameterSet();
        sps.picWidthInMbsMinus1 = (sz.getWidth() + 15 >> 4) - 1;
        sps.picHeightInMapUnitsMinus1 = (sz.getHeight() + 15 >> 4) - 1;
        sps.chromaFormatIdc = ColorSpace.YUV420J;
        sps.profileIdc = 66;
        sps.levelIdc = 40;
        sps.numRefFrames = 1;
        sps.frameMbsOnlyFlag = true;
        sps.log2MaxFrameNumMinus4 = Math.max(0, MathUtil.log2(this.keyInterval) - 3);
        int codedWidth = sps.picWidthInMbsMinus1 + 1 << 4;
        int codedHeight = sps.picHeightInMapUnitsMinus1 + 1 << 4;
        sps.frameCroppingFlag = codedWidth != sz.getWidth() || codedHeight != sz.getHeight();
        sps.frameCropRightOffset = codedWidth - sz.getWidth() + 1 >> 1;
        sps.frameCropBottomOffset = codedHeight - sz.getHeight() + 1 >> 1;
        return sps;
    }

    private void encodeSlice(SeqParameterSet sps, PictureParameterSet pps, Picture pic, ByteBuffer dup, boolean idr, int frameNum, SliceType sliceType, int qp) {
        if (idr && sliceType != SliceType.I) {
            idr = false;
            Logger.warn("Illegal value of idr = true when sliceType != I");
        }
        this.cavlc = new CAVLC[] { new CAVLC(sps, pps, 2, 2), new CAVLC(sps, pps, 1, 1), new CAVLC(sps, pps, 1, 1) };
        this.mbEncoderI16x16 = new MBEncoderI16x16(this.cavlc, this.leftRow, this.topLine);
        this.mbEncoderP16x16 = new MBEncoderP16x16(sps, this.ref, this.cavlc, new MotionEstimator(this.motionSearchRange));
        dup.putInt(1);
        new NALUnit(idr ? NALUnitType.IDR_SLICE : NALUnitType.NON_IDR_SLICE, 3).write(dup);
        SliceHeader sh = new SliceHeader();
        sh.sliceType = sliceType;
        if (idr) {
            sh.refPicMarkingIDR = new RefPicMarkingIDR(false, false);
        }
        sh.pps = pps;
        sh.sps = sps;
        sh.picOrderCntLsb = (frameNum << 1) % this.maxPOC;
        sh.frameNum = frameNum % this.maxFrameNumber;
        sh.sliceQpDelta = qp - (pps.picInitQpMinus26 + 26);
        ByteBuffer buf = ByteBuffer.allocate(pic.getWidth() * pic.getHeight());
        BitWriter sliceData = new BitWriter(buf);
        SliceHeaderWriter.write(sh, idr, 2, sliceData);
        int mbY = 0;
        for (int mbAddr = 0; mbY < sps.picHeightInMapUnitsMinus1 + 1; mbY++) {
            for (int mbX = 0; mbX < sps.picWidthInMbsMinus1 + 1; mbAddr++) {
                if (sliceType == SliceType.P) {
                    CAVLCWriter.writeUE(sliceData, 0);
                }
                MBType mbType = this.selectMBType(sliceType);
                if (mbType == MBType.I_16x16) {
                    int predMode = this.mbEncoderI16x16.getPredMode(pic, mbX, mbY);
                    int cbpChroma = this.mbEncoderI16x16.getCbpChroma(pic, mbX, mbY);
                    int cbpLuma = this.mbEncoderI16x16.getCbpLuma(pic, mbX, mbY);
                    int i16x16TypeOffset = cbpLuma / 15 * 12 + cbpChroma * 4 + predMode;
                    int mbTypeOffset = sliceType == SliceType.P ? 5 : 0;
                    CAVLCWriter.writeUE(sliceData, mbTypeOffset + mbType.code() + i16x16TypeOffset);
                } else {
                    CAVLCWriter.writeUE(sliceData, mbType.code());
                }
                int totalQpDelta = 0;
                int qpDelta = this.rc.initialQpDelta();
                BitWriter candidate;
                do {
                    candidate = sliceData.fork();
                    totalQpDelta += qpDelta;
                    this.encodeMacroblock(mbType, pic, mbX, mbY, candidate, qp, totalQpDelta);
                    qpDelta = this.rc.accept(candidate.position() - sliceData.position());
                    if (qpDelta != 0) {
                        this.restoreMacroblock(mbType);
                    }
                } while (qpDelta != 0);
                sliceData = candidate;
                qp += totalQpDelta;
                this.collectPredictors(this.outMB.getPixels(), mbX);
                this.addToReference(mbX, mbY);
                mbX++;
            }
        }
        sliceData.write1Bit(1);
        sliceData.flush();
        buf = sliceData.getBuffer();
        buf.flip();
        H264Utils.escapeNAL(buf, dup);
    }

    private void encodeMacroblock(MBType mbType, Picture pic, int mbX, int mbY, BitWriter candidate, int qp, int qpDelta) {
        if (mbType == MBType.I_16x16) {
            this.mbEncoderI16x16.save();
            this.mbEncoderI16x16.encodeMacroblock(pic, mbX, mbY, candidate, this.outMB, mbX > 0 ? this.topEncoded[mbX - 1] : null, mbY > 0 ? this.topEncoded[mbX] : null, qp + qpDelta, qpDelta);
        } else {
            if (mbType != MBType.P_16x16) {
                throw new RuntimeException("Macroblock of type " + mbType + " is not supported.");
            }
            this.mbEncoderP16x16.save();
            this.mbEncoderP16x16.encodeMacroblock(pic, mbX, mbY, candidate, this.outMB, mbX > 0 ? this.topEncoded[mbX - 1] : null, mbY > 0 ? this.topEncoded[mbX] : null, qp + qpDelta, qpDelta);
        }
    }

    private void restoreMacroblock(MBType mbType) {
        if (mbType == MBType.I_16x16) {
            this.mbEncoderI16x16.restore();
        } else {
            if (mbType != MBType.P_16x16) {
                throw new RuntimeException("Macroblock of type " + mbType + " is not supported.");
            }
            this.mbEncoderP16x16.restore();
        }
    }

    private MBType selectMBType(SliceType sliceType) {
        if (sliceType == SliceType.I) {
            return MBType.I_16x16;
        } else if (sliceType == SliceType.P) {
            return MBType.P_16x16;
        } else {
            throw new RuntimeException("Unsupported slice type");
        }
    }

    private void addToReference(int mbX, int mbY) {
        if (mbY > 0) {
            MBEncoderHelper.putBlkPic(this.picOut, this.topEncoded[mbX].getPixels(), mbX << 4, mbY - 1 << 4);
        }
        EncodedMB tmp = this.topEncoded[mbX];
        this.topEncoded[mbX] = this.outMB;
        this.outMB = tmp;
    }

    private void putLastMBLine() {
        int mbWidth = this.sps.picWidthInMbsMinus1 + 1;
        int mbHeight = this.sps.picHeightInMapUnitsMinus1 + 1;
        for (int mbX = 0; mbX < mbWidth; mbX++) {
            MBEncoderHelper.putBlkPic(this.picOut, this.topEncoded[mbX].getPixels(), mbX << 4, mbHeight - 1 << 4);
        }
    }

    private void collectPredictors(Picture outMB, int mbX) {
        System.arraycopy(outMB.getPlaneData(0), 240, this.topLine[0], mbX << 4, 16);
        System.arraycopy(outMB.getPlaneData(1), 56, this.topLine[1], mbX << 3, 8);
        System.arraycopy(outMB.getPlaneData(2), 56, this.topLine[2], mbX << 3, 8);
        this.copyCol(outMB.getPlaneData(0), 15, 16, this.leftRow[0]);
        this.copyCol(outMB.getPlaneData(1), 7, 8, this.leftRow[1]);
        this.copyCol(outMB.getPlaneData(2), 7, 8, this.leftRow[2]);
    }

    private void copyCol(byte[] planeData, int off, int stride, byte[] out) {
        for (int i = 0; i < out.length; i++) {
            out[i] = planeData[off];
            off += stride;
        }
    }

    @Override
    public ColorSpace[] getSupportedColorSpaces() {
        return new ColorSpace[] { ColorSpace.YUV420J };
    }

    @Override
    public int estimateBufferSize(Picture frame) {
        return Math.max(65536, frame.getWidth() * frame.getHeight() / 2);
    }
}