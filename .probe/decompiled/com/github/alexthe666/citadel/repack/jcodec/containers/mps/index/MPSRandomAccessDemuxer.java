package com.github.alexthe666.citadel.repack.jcodec.containers.mps.index;

import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPSUtils;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class MPSRandomAccessDemuxer {

    private MPSRandomAccessDemuxer.Stream[] streams;

    private long[] pesTokens;

    private int[] pesStreamIds;

    public MPSRandomAccessDemuxer(SeekableByteChannel ch, MPSIndex mpsIndex) throws IOException {
        this.pesTokens = mpsIndex.getPesTokens();
        this.pesStreamIds = mpsIndex.getPesStreamIds().flattern();
        MPSIndex.MPSStreamIndex[] streamIndices = mpsIndex.getStreams();
        this.streams = new MPSRandomAccessDemuxer.Stream[streamIndices.length];
        for (int i = 0; i < streamIndices.length; i++) {
            this.streams[i] = this.newStream(ch, streamIndices[i]);
        }
    }

    protected MPSRandomAccessDemuxer.Stream newStream(SeekableByteChannel ch, MPSIndex.MPSStreamIndex streamIndex) throws IOException {
        return new MPSRandomAccessDemuxer.Stream(this, streamIndex, ch);
    }

    public MPSRandomAccessDemuxer.Stream[] getStreams() {
        return this.streams;
    }

    public static class Stream extends MPSIndex.MPSStreamIndex implements SeekableDemuxerTrack {

        private static final int MPEG_TIMESCALE = 90000;

        private int curPesIdx;

        private int curFrame;

        private ByteBuffer pesBuf;

        private int _seekToFrame = -1;

        protected SeekableByteChannel source;

        private long[] foffs;

        private MPSRandomAccessDemuxer demuxer;

        public Stream(MPSRandomAccessDemuxer demuxer, MPSIndex.MPSStreamIndex streamIndex, SeekableByteChannel source) throws IOException {
            super(streamIndex.streamId, streamIndex.fsizes, streamIndex.fpts, streamIndex.fdur, streamIndex.sync);
            this.demuxer = demuxer;
            this.source = source;
            this.foffs = new long[this.fsizes.length];
            long curOff = 0L;
            for (int i = 0; i < this.fsizes.length; i++) {
                this.foffs[i] = curOff;
                curOff += (long) this.fsizes[i];
            }
            int[] seg = Platform.copyOfInt(streamIndex.getFpts(), 100);
            Arrays.sort(seg);
            this._seekToFrame = 0;
            this.seekToFrame();
        }

        @Override
        public Packet nextFrame() throws IOException {
            this.seekToFrame();
            if (this.curFrame >= this.fsizes.length) {
                return null;
            } else {
                int fs = this.fsizes[this.curFrame];
                ByteBuffer result = ByteBuffer.allocate(fs);
                return this._nextFrame(result);
            }
        }

        private Packet _nextFrame(ByteBuffer buf) throws IOException {
            this.seekToFrame();
            if (this.curFrame >= this.fsizes.length) {
                return null;
            } else {
                int fs = this.fsizes[this.curFrame];
                ByteBuffer result = buf.duplicate();
                result.limit(result.position() + fs);
                while (result.hasRemaining()) {
                    if (this.pesBuf.hasRemaining()) {
                        result.put(NIOUtils.read(this.pesBuf, Math.min(this.pesBuf.remaining(), result.remaining())));
                    } else {
                        this.curPesIdx++;
                        long posShift;
                        for (posShift = 0L; this.demuxer.pesStreamIds[this.curPesIdx] != this.streamId; this.curPesIdx++) {
                            posShift += (long) (MPSIndex.pesLen(this.demuxer.pesTokens[this.curPesIdx]) + MPSIndex.leadingSize(this.demuxer.pesTokens[this.curPesIdx]));
                        }
                        this.skip(posShift + (long) MPSIndex.leadingSize(this.demuxer.pesTokens[this.curPesIdx]));
                        int pesLen = MPSIndex.pesLen(this.demuxer.pesTokens[this.curPesIdx]);
                        this.pesBuf = this.fetch(pesLen);
                        MPSUtils.readPESHeader(this.pesBuf, 0L);
                    }
                }
                result.flip();
                Packet pkt = Packet.createPacket(result, (long) this.fpts[this.curFrame], 90000, (long) this.fdur[this.curFrame], (long) this.curFrame, this.sync.length != 0 && Arrays.binarySearch(this.sync, this.curFrame) < 0 ? Packet.FrameType.INTER : Packet.FrameType.KEY, null);
                this.curFrame++;
                return pkt;
            }
        }

        protected ByteBuffer fetch(int pesLen) throws IOException {
            return NIOUtils.fetchFromChannel(this.source, pesLen);
        }

        protected void skip(long leadingSize) throws IOException {
            this.source.setPosition(this.source.position() + leadingSize);
        }

        protected void reset() throws IOException {
            this.source.setPosition(0L);
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            return null;
        }

        @Override
        public boolean gotoFrame(long frameNo) {
            this._seekToFrame = (int) frameNo;
            return true;
        }

        @Override
        public boolean gotoSyncFrame(long frameNo) {
            for (int i = 0; i < this.sync.length; i++) {
                if ((long) this.sync[i] > frameNo) {
                    this._seekToFrame = this.sync[i - 1];
                    return true;
                }
            }
            this._seekToFrame = this.sync[this.sync.length - 1];
            return true;
        }

        private void seekToFrame() throws IOException {
            if (this._seekToFrame != -1) {
                this.curFrame = this._seekToFrame;
                long payloadOff = this.foffs[this.curFrame];
                long posShift = 0L;
                this.reset();
                this.curPesIdx = 0;
                while (true) {
                    if (this.demuxer.pesStreamIds[this.curPesIdx] == this.streamId) {
                        int payloadSize = MPSIndex.payLoadSize(this.demuxer.pesTokens[this.curPesIdx]);
                        if (payloadOff < (long) payloadSize) {
                            this.skip(posShift + (long) MPSIndex.leadingSize(this.demuxer.pesTokens[this.curPesIdx]));
                            this.pesBuf = this.fetch(MPSIndex.pesLen(this.demuxer.pesTokens[this.curPesIdx]));
                            MPSUtils.readPESHeader(this.pesBuf, 0L);
                            NIOUtils.skip(this.pesBuf, (int) payloadOff);
                            this._seekToFrame = -1;
                            return;
                        }
                        payloadOff -= (long) payloadSize;
                    }
                    posShift += (long) (MPSIndex.pesLen(this.demuxer.pesTokens[this.curPesIdx]) + MPSIndex.leadingSize(this.demuxer.pesTokens[this.curPesIdx]));
                    this.curPesIdx++;
                }
            }
        }

        @Override
        public long getCurFrame() {
            return (long) this.curFrame;
        }

        @Override
        public void seek(double second) {
            throw new NotSupportedException("");
        }
    }
}