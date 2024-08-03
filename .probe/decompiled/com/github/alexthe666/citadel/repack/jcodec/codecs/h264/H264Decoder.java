package com.github.alexthe666.citadel.repack.jcodec.codecs.h264;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.DeblockerInput;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.FrameReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.SliceDecoder;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.SliceHeaderReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.SliceReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.deblock.DeblockingFilter;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnit;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.NALUnitType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.RefPicMarking;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.RefPicMarkingIDR;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceHeader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.IntObjectMap;
import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class H264Decoder extends VideoDecoder {

    private Frame[] sRefs;

    private IntObjectMap<Frame> lRefs;

    private List<Frame> pictureBuffer = new ArrayList();

    private POCManager poc = new POCManager();

    private FrameReader reader;

    private ExecutorService tp;

    private boolean threaded = Runtime.getRuntime().availableProcessors() > 1;

    public H264Decoder() {
        if (this.threaded) {
            this.tp = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {

                public Thread newThread(Runnable r) {
                    Thread t = Executors.defaultThreadFactory().newThread(r);
                    t.setDaemon(true);
                    return t;
                }
            });
        }
        this.reader = new FrameReader();
    }

    public static H264Decoder createH264DecoderFromCodecPrivate(ByteBuffer codecPrivate) {
        H264Decoder d = new H264Decoder();
        for (ByteBuffer bb : H264Utils.splitFrame(codecPrivate.duplicate())) {
            NALUnit nu = NALUnit.read(bb);
            if (nu.type == NALUnitType.SPS) {
                d.reader.addSps(bb);
            } else if (nu.type == NALUnitType.PPS) {
                d.reader.addPps(bb);
            }
        }
        return d;
    }

    public Frame decodeFrame(ByteBuffer data, byte[][] buffer) {
        return this.decodeFrameFromNals(H264Utils.splitFrame(data), buffer);
    }

    public Frame decodeFrameFromNals(List<ByteBuffer> nalUnits, byte[][] buffer) {
        return new H264Decoder.FrameDecoder(this).decodeFrame(nalUnits, buffer);
    }

    public static Frame createFrame(SeqParameterSet sps, byte[][] buffer, int frameNum, SliceType frameType, H264Utils.MvList2D mvs, Frame[][][] refsUsed, int POC) {
        int width = sps.picWidthInMbsMinus1 + 1 << 4;
        int height = SeqParameterSet.getPicHeightInMbs(sps) << 4;
        Rect crop = null;
        if (sps.frameCroppingFlag) {
            int sX = sps.frameCropLeftOffset << 1;
            int sY = sps.frameCropTopOffset << 1;
            int w = width - (sps.frameCropRightOffset << 1) - sX;
            int h = height - (sps.frameCropBottomOffset << 1) - sY;
            crop = new Rect(sX, sY, w, h);
        }
        return new Frame(width, height, buffer, ColorSpace.YUV420, crop, frameNum, frameType, mvs, refsUsed, POC);
    }

    public void addSps(List<ByteBuffer> spsList) {
        this.reader.addSpsList(spsList);
    }

    public void addPps(List<ByteBuffer> ppsList) {
        this.reader.addPpsList(ppsList);
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        boolean validSps = false;
        boolean validPps = false;
        boolean validSh = false;
        for (ByteBuffer nalUnit : H264Utils.splitFrame(data.duplicate())) {
            NALUnit marker = NALUnit.read(nalUnit);
            if (marker.type == NALUnitType.IDR_SLICE || marker.type == NALUnitType.NON_IDR_SLICE) {
                BitReader reader = BitReader.createBitReader(nalUnit);
                validSh = validSh(SliceHeaderReader.readPart1(reader));
                break;
            }
            if (marker.type == NALUnitType.SPS) {
                validSps = validSps(SeqParameterSet.read(nalUnit));
            } else if (marker.type == NALUnitType.PPS) {
                validPps = validPps(PictureParameterSet.read(nalUnit));
            }
        }
        return (validSh ? 60 : 0) + (validSps ? 20 : 0) + (validPps ? 20 : 0);
    }

    private static boolean validSh(SliceHeader sh) {
        return sh.firstMbInSlice == 0 && sh.sliceType != null && sh.picParameterSetId < 2;
    }

    private static boolean validSps(SeqParameterSet sps) {
        return sps.bitDepthChromaMinus8 < 4 && sps.bitDepthLumaMinus8 < 4 && sps.chromaFormatIdc != null && sps.seqParameterSetId < 2 && sps.picOrderCntType <= 2;
    }

    private static boolean validPps(PictureParameterSet pps) {
        return pps.picInitQpMinus26 <= 26 && pps.seqParameterSetId <= 2 && pps.picParameterSetId <= 2;
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer data) {
        List<ByteBuffer> rawSPS = H264Utils.getRawSPS(data.duplicate());
        List<ByteBuffer> rawPPS = H264Utils.getRawPPS(data.duplicate());
        if (rawSPS.size() == 0) {
            Logger.warn("Can not extract metadata from the packet not containing an SPS.");
            return null;
        } else {
            SeqParameterSet sps = SeqParameterSet.read((ByteBuffer) rawSPS.get(0));
            Size size = H264Utils.getPicSize(sps);
            return VideoCodecMeta.createSimpleVideoCodecMeta(size, ColorSpace.YUV420);
        }
    }

    static class FrameDecoder {

        private SeqParameterSet activeSps;

        private DeblockingFilter filter;

        private SliceHeader firstSliceHeader;

        private NALUnit firstNu;

        private H264Decoder dec;

        private DeblockerInput di;

        public FrameDecoder(H264Decoder decoder) {
            this.dec = decoder;
        }

        public Frame decodeFrame(List<ByteBuffer> nalUnits, byte[][] buffer) {
            List<SliceReader> sliceReaders = this.dec.reader.readFrame(nalUnits);
            if (sliceReaders != null && sliceReaders.size() != 0) {
                Frame result = this.init((SliceReader) sliceReaders.get(0), buffer);
                if (this.dec.threaded && sliceReaders.size() > 1) {
                    List<Future<?>> futures = new ArrayList();
                    for (SliceReader sliceReader : sliceReaders) {
                        futures.add(this.dec.tp.submit(new H264Decoder.SliceDecoderRunnable(this, sliceReader, result)));
                    }
                    for (Future<?> future : futures) {
                        this.waitForSure(future);
                    }
                } else {
                    for (SliceReader sliceReader : sliceReaders) {
                        new SliceDecoder(this.activeSps, this.dec.sRefs, this.dec.lRefs, this.di, result).decodeFromReader(sliceReader);
                    }
                }
                this.filter.deblockFrame(result);
                this.updateReferences(result);
                return result;
            } else {
                return null;
            }
        }

        private void waitForSure(Future<?> future) {
            try {
                future.get();
            } catch (Exception var3) {
                throw new RuntimeException(var3);
            }
        }

        private void updateReferences(Frame picture) {
            if (this.firstNu.nal_ref_idc != 0) {
                if (this.firstNu.type == NALUnitType.IDR_SLICE) {
                    this.performIDRMarking(this.firstSliceHeader.refPicMarkingIDR, picture);
                } else {
                    this.performMarking(this.firstSliceHeader.refPicMarkingNonIDR, picture);
                }
            }
        }

        private Frame init(SliceReader sliceReader, byte[][] buffer) {
            this.firstNu = sliceReader.getNALUnit();
            this.firstSliceHeader = sliceReader.getSliceHeader();
            this.activeSps = this.firstSliceHeader.sps;
            this.validateSupportedFeatures(this.firstSliceHeader.sps, this.firstSliceHeader.pps);
            int picWidthInMbs = this.activeSps.picWidthInMbsMinus1 + 1;
            if (this.dec.sRefs == null) {
                this.dec.sRefs = new Frame[1 << this.firstSliceHeader.sps.log2MaxFrameNumMinus4 + 4];
                this.dec.lRefs = new IntObjectMap<>();
            }
            this.di = new DeblockerInput(this.activeSps);
            Frame result = H264Decoder.createFrame(this.activeSps, buffer, this.firstSliceHeader.frameNum, this.firstSliceHeader.sliceType, this.di.mvs, this.di.refsUsed, this.dec.poc.calcPOC(this.firstSliceHeader, this.firstNu));
            this.filter = new DeblockingFilter(picWidthInMbs, this.activeSps.bitDepthChromaMinus8 + 8, this.di);
            return result;
        }

        private void validateSupportedFeatures(SeqParameterSet sps, PictureParameterSet pps) {
            if (sps.mbAdaptiveFrameFieldFlag) {
                throw new RuntimeException("Unsupported h264 feature: MBAFF.");
            } else if (sps.bitDepthLumaMinus8 != 0 || sps.bitDepthChromaMinus8 != 0) {
                throw new RuntimeException("Unsupported h264 feature: High bit depth.");
            } else if (sps.chromaFormatIdc != ColorSpace.YUV420J) {
                throw new RuntimeException("Unsupported h264 feature: " + sps.chromaFormatIdc + " color.");
            } else if (!sps.frameMbsOnlyFlag || sps.fieldPicFlag) {
                throw new RuntimeException("Unsupported h264 feature: interlace.");
            } else if (pps.constrainedIntraPredFlag) {
                throw new RuntimeException("Unsupported h264 feature: constrained intra prediction.");
            } else if (sps.qpprimeYZeroTransformBypassFlag) {
                throw new RuntimeException("Unsupported h264 feature: qprime zero transform bypass.");
            } else if (sps.profileIdc != 66 && sps.profileIdc != 77 && sps.profileIdc != 100) {
                throw new RuntimeException("Unsupported h264 feature: " + sps.profileIdc + " profile.");
            }
        }

        public void performIDRMarking(RefPicMarkingIDR refPicMarkingIDR, Frame picture) {
            this.clearAll();
            this.dec.pictureBuffer.clear();
            Frame saved = this.saveRef(picture);
            if (refPicMarkingIDR.isUseForlongTerm()) {
                this.dec.lRefs.put(0, saved);
                saved.setShortTerm(false);
            } else {
                this.dec.sRefs[this.firstSliceHeader.frameNum] = saved;
            }
        }

        private Frame saveRef(Frame decoded) {
            Frame frame = this.dec.pictureBuffer.size() > 0 ? (Frame) this.dec.pictureBuffer.remove(0) : Frame.createFrame(decoded);
            frame.copyFromFrame(decoded);
            return frame;
        }

        private void releaseRef(Frame picture) {
            if (picture != null) {
                this.dec.pictureBuffer.add(picture);
            }
        }

        public void clearAll() {
            for (int i = 0; i < this.dec.sRefs.length; i++) {
                this.releaseRef(this.dec.sRefs[i]);
                this.dec.sRefs[i] = null;
            }
            int[] keys = this.dec.lRefs.keys();
            for (int i = 0; i < keys.length; i++) {
                this.releaseRef(this.dec.lRefs.get(keys[i]));
            }
            this.dec.lRefs.clear();
        }

        public void performMarking(RefPicMarking refPicMarking, Frame picture) {
            Frame saved = this.saveRef(picture);
            if (refPicMarking != null) {
                RefPicMarking.Instruction[] instructions = refPicMarking.getInstructions();
                for (int i = 0; i < instructions.length; i++) {
                    RefPicMarking.Instruction instr = instructions[i];
                    switch(instr.getType()) {
                        case REMOVE_SHORT:
                            this.unrefShortTerm(instr.getArg1());
                            break;
                        case REMOVE_LONG:
                            this.unrefLongTerm(instr.getArg1());
                            break;
                        case CONVERT_INTO_LONG:
                            this.convert(instr.getArg1(), instr.getArg2());
                            break;
                        case TRUNK_LONG:
                            this.truncateLongTerm(instr.getArg1() - 1);
                            break;
                        case CLEAR:
                            this.clearAll();
                            break;
                        case MARK_LONG:
                            this.saveLong(saved, instr.getArg1());
                            saved = null;
                    }
                }
            }
            if (saved != null) {
                this.saveShort(saved);
            }
            int maxFrames = 1 << this.activeSps.log2MaxFrameNumMinus4 + 4;
            if (refPicMarking == null) {
                int maxShort = Math.max(1, this.activeSps.numRefFrames - this.dec.lRefs.size());
                int min = Integer.MAX_VALUE;
                int num = 0;
                int minFn = 0;
                for (int i = 0; i < this.dec.sRefs.length; i++) {
                    if (this.dec.sRefs[i] != null) {
                        int fnWrap = this.unwrap(this.firstSliceHeader.frameNum, this.dec.sRefs[i].getFrameNo(), maxFrames);
                        if (fnWrap < min) {
                            min = fnWrap;
                            minFn = this.dec.sRefs[i].getFrameNo();
                        }
                        num++;
                    }
                }
                if (num > maxShort) {
                    this.releaseRef(this.dec.sRefs[minFn]);
                    this.dec.sRefs[minFn] = null;
                }
            }
        }

        private int unwrap(int thisFrameNo, int refFrameNo, int maxFrames) {
            return refFrameNo > thisFrameNo ? refFrameNo - maxFrames : refFrameNo;
        }

        private void saveShort(Frame saved) {
            this.dec.sRefs[this.firstSliceHeader.frameNum] = saved;
        }

        private void saveLong(Frame saved, int longNo) {
            Frame prev = this.dec.lRefs.get(longNo);
            if (prev != null) {
                this.releaseRef(prev);
            }
            saved.setShortTerm(false);
            this.dec.lRefs.put(longNo, saved);
        }

        private void truncateLongTerm(int maxLongNo) {
            int[] keys = this.dec.lRefs.keys();
            for (int i = 0; i < keys.length; i++) {
                if (keys[i] > maxLongNo) {
                    this.releaseRef(this.dec.lRefs.get(keys[i]));
                    this.dec.lRefs.remove(keys[i]);
                }
            }
        }

        private void convert(int shortNo, int longNo) {
            int ind = MathUtil.wrap(this.firstSliceHeader.frameNum - shortNo, 1 << this.firstSliceHeader.sps.log2MaxFrameNumMinus4 + 4);
            this.releaseRef(this.dec.lRefs.get(longNo));
            this.dec.lRefs.put(longNo, this.dec.sRefs[ind]);
            this.dec.sRefs[ind] = null;
            this.dec.lRefs.get(longNo).setShortTerm(false);
        }

        private void unrefLongTerm(int longNo) {
            this.releaseRef(this.dec.lRefs.get(longNo));
            this.dec.lRefs.remove(longNo);
        }

        private void unrefShortTerm(int shortNo) {
            int ind = MathUtil.wrap(this.firstSliceHeader.frameNum - shortNo, 1 << this.firstSliceHeader.sps.log2MaxFrameNumMinus4 + 4);
            this.releaseRef(this.dec.sRefs[ind]);
            this.dec.sRefs[ind] = null;
        }
    }

    private static final class SliceDecoderRunnable implements Runnable {

        private final SliceReader sliceReader;

        private final Frame result;

        private H264Decoder.FrameDecoder fdec;

        private SliceDecoderRunnable(H264Decoder.FrameDecoder fdec, SliceReader sliceReader, Frame result) {
            this.fdec = fdec;
            this.sliceReader = sliceReader;
            this.result = result;
        }

        public void run() {
            new SliceDecoder(this.fdec.activeSps, this.fdec.dec.sRefs, this.fdec.dec.lRefs, this.fdec.di, this.result).decodeFromReader(this.sliceReader);
        }
    }
}