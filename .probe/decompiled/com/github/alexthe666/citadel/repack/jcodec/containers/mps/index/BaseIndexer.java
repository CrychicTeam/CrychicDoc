package com.github.alexthe666.citadel.repack.jcodec.containers.mps.index;

import com.github.alexthe666.citadel.repack.jcodec.common.ArrayUtil;
import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.LongArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.RunLength;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPSUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.PESPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BaseIndexer extends MPSUtils.PESReader {

    private Map<Integer, BaseIndexer.BaseAnalyser> analyzers = new HashMap();

    private LongArrayList tokens = LongArrayList.createLongArrayList();

    private RunLength.Integer streams = new RunLength.Integer();

    public int estimateSize() {
        int sizeEstimate = (this.tokens.size() << 3) + this.streams.estimateSize() + 128;
        for (Integer stream : this.analyzers.keySet()) {
            sizeEstimate += ((BaseIndexer.BaseAnalyser) this.analyzers.get(stream)).estimateSize();
        }
        return sizeEstimate;
    }

    protected BaseIndexer.BaseAnalyser getAnalyser(int stream) {
        BaseIndexer.BaseAnalyser analizer = (BaseIndexer.BaseAnalyser) this.analyzers.get(stream);
        if (analizer == null) {
            analizer = (BaseIndexer.BaseAnalyser) (stream >= 224 && stream <= 239 ? new BaseIndexer.MPEGVideoAnalyser() : new BaseIndexer.GenericAnalyser());
            this.analyzers.put(stream, analizer);
        }
        return (BaseIndexer.BaseAnalyser) this.analyzers.get(stream);
    }

    public MPSIndex serialize() {
        List<MPSIndex.MPSStreamIndex> streamsIndices = new ArrayList();
        for (Entry<Integer, BaseIndexer.BaseAnalyser> entry : this.analyzers.entrySet()) {
            streamsIndices.add(((BaseIndexer.BaseAnalyser) entry.getValue()).serialize((Integer) entry.getKey()));
        }
        return new MPSIndex(this.tokens.toArray(), this.streams, (MPSIndex.MPSStreamIndex[]) streamsIndices.toArray(new MPSIndex.MPSStreamIndex[0]));
    }

    protected void savePESMeta(int stream, long token) {
        this.tokens.add(token);
        this.streams.add(stream);
    }

    void finishAnalyse() {
        super.finishRead();
        for (BaseIndexer.BaseAnalyser baseAnalyser : this.analyzers.values()) {
            baseAnalyser.finishAnalyse();
        }
    }

    protected abstract static class BaseAnalyser {

        protected IntArrayList pts = new IntArrayList(250000);

        protected IntArrayList dur = new IntArrayList(250000);

        public BaseAnalyser() {
        }

        public abstract void pkt(ByteBuffer var1, PESPacket var2);

        public abstract void finishAnalyse();

        public int estimateSize() {
            return (this.pts.size() << 2) + 4;
        }

        public abstract MPSIndex.MPSStreamIndex serialize(int var1);
    }

    private static class GenericAnalyser extends BaseIndexer.BaseAnalyser {

        private IntArrayList sizes = new IntArrayList(250000);

        private int knownDuration;

        private long lastPts;

        public GenericAnalyser() {
        }

        @Override
        public void pkt(ByteBuffer pkt, PESPacket pesHeader) {
            this.sizes.add(pkt.remaining());
            if (pesHeader.pts == -1L) {
                pesHeader.pts = this.lastPts + (long) this.knownDuration;
            } else {
                this.knownDuration = (int) (pesHeader.pts - this.lastPts);
                this.lastPts = pesHeader.pts;
            }
            this.pts.add((int) pesHeader.pts);
            this.dur.add(this.knownDuration);
        }

        @Override
        public MPSIndex.MPSStreamIndex serialize(int streamId) {
            return new MPSIndex.MPSStreamIndex(streamId, this.sizes.toArray(), this.pts.toArray(), this.dur.toArray(), new int[0]);
        }

        @Override
        public int estimateSize() {
            return super.estimateSize() + (this.sizes.size() << 2) + 32;
        }

        @Override
        public void finishAnalyse() {
        }
    }

    private static class MPEGVideoAnalyser extends BaseIndexer.BaseAnalyser {

        private int marker = -1;

        private long position;

        private IntArrayList sizes;

        private IntArrayList keyFrames;

        private int frameNo;

        private boolean inFrameData;

        private BaseIndexer.MPEGVideoAnalyser.Frame lastFrame;

        private List<BaseIndexer.MPEGVideoAnalyser.Frame> curGop;

        private long phPos = -1L;

        private BaseIndexer.MPEGVideoAnalyser.Frame lastFrameOfLastGop;

        public MPEGVideoAnalyser() {
            this.sizes = new IntArrayList(250000);
            this.keyFrames = new IntArrayList(20000);
            this.curGop = new ArrayList();
        }

        @Override
        public void pkt(ByteBuffer pkt, PESPacket pesHeader) {
            while (pkt.hasRemaining()) {
                int b = pkt.get() & 255;
                this.position++;
                this.marker = this.marker << 8 | b;
                if (this.phPos != -1L) {
                    long phOffset = this.position - this.phPos;
                    if (phOffset == 5L) {
                        this.lastFrame.tempRef = b << 2;
                    } else if (phOffset == 6L) {
                        int picCodingType = b >> 3 & 7;
                        this.lastFrame.tempRef |= b >> 6;
                        if (picCodingType == 1) {
                            this.keyFrames.add(this.frameNo - 1);
                            if (this.curGop.size() > 0) {
                                this.outGop();
                            }
                        }
                    }
                }
                if ((this.marker & -256) == 256) {
                    if (!this.inFrameData || this.marker != 256 && this.marker <= 431) {
                        if (!this.inFrameData && this.marker > 256 && this.marker <= 431) {
                            this.inFrameData = true;
                        }
                    } else {
                        this.lastFrame.size = (int) (this.position - 4L - this.lastFrame.offset);
                        this.curGop.add(this.lastFrame);
                        this.lastFrame = null;
                        this.inFrameData = false;
                    }
                    if (this.lastFrame == null && (this.marker == 435 || this.marker == 440 || this.marker == 256)) {
                        BaseIndexer.MPEGVideoAnalyser.Frame frame = new BaseIndexer.MPEGVideoAnalyser.Frame();
                        frame.pts = (int) pesHeader.pts;
                        frame.offset = this.position - 4L;
                        Logger.info(String.format("FRAME[%d]: %012x, %d", this.frameNo, pesHeader.pos + (long) pkt.position() - 4L, pesHeader.pts));
                        this.frameNo++;
                        this.lastFrame = frame;
                    }
                    if (this.lastFrame != null && this.lastFrame.pts == -1 && this.marker == 256) {
                        this.lastFrame.pts = (int) pesHeader.pts;
                    }
                    this.phPos = this.marker == 256 ? this.position - 4L : -1L;
                }
            }
        }

        private void outGop() {
            this.fixPts(this.curGop);
            for (BaseIndexer.MPEGVideoAnalyser.Frame frame : this.curGop) {
                this.sizes.add(frame.size);
                this.pts.add(frame.pts);
            }
            this.curGop.clear();
        }

        private void fixPts(List<BaseIndexer.MPEGVideoAnalyser.Frame> curGop) {
            BaseIndexer.MPEGVideoAnalyser.Frame[] frames = (BaseIndexer.MPEGVideoAnalyser.Frame[]) curGop.toArray(new BaseIndexer.MPEGVideoAnalyser.Frame[0]);
            Arrays.sort(frames, new Comparator<BaseIndexer.MPEGVideoAnalyser.Frame>() {

                public int compare(BaseIndexer.MPEGVideoAnalyser.Frame o1, BaseIndexer.MPEGVideoAnalyser.Frame o2) {
                    return o1.tempRef > o2.tempRef ? 1 : (o1.tempRef == o2.tempRef ? 0 : -1);
                }
            });
            for (int dir = 0; dir < 3; dir++) {
                int i = 0;
                int lastPts = -1;
                int secondLastPts = -1;
                int lastTref = -1;
                for (int secondLastTref = -1; i < frames.length; i++) {
                    if (frames[i].pts == -1 && lastPts != -1 && secondLastPts != -1) {
                        frames[i].pts = lastPts + (lastPts - secondLastPts) / MathUtil.abs(lastTref - secondLastTref);
                    }
                    if (frames[i].pts != -1) {
                        secondLastPts = lastPts;
                        secondLastTref = lastTref;
                        lastPts = frames[i].pts;
                        lastTref = frames[i].tempRef;
                    }
                }
                ArrayUtil.reverse(frames);
            }
            if (this.lastFrameOfLastGop != null) {
                this.dur.add(frames[0].pts - this.lastFrameOfLastGop.pts);
            }
            for (int i = 1; i < frames.length; i++) {
                this.dur.add(frames[i].pts - frames[i - 1].pts);
            }
            this.lastFrameOfLastGop = frames[frames.length - 1];
        }

        @Override
        public void finishAnalyse() {
            if (this.lastFrame != null) {
                this.lastFrame.size = (int) (this.position - this.lastFrame.offset);
                this.curGop.add(this.lastFrame);
                this.outGop();
            }
        }

        @Override
        public MPSIndex.MPSStreamIndex serialize(int streamId) {
            return new MPSIndex.MPSStreamIndex(streamId, this.sizes.toArray(), this.pts.toArray(), this.dur.toArray(), this.keyFrames.toArray());
        }

        private static class Frame {

            long offset;

            int size;

            int pts;

            int tempRef;

            private Frame() {
            }
        }
    }
}