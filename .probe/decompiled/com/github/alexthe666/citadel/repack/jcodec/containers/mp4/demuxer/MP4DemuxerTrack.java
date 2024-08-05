package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.demuxer;

import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.QTTimeUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.CompositionOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SyncSamplesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MP4DemuxerTrack extends AbstractMP4DemuxerTrack {

    private int[] sizes;

    private long offInChunk;

    private int noInChunk;

    private int[] syncSamples;

    private int[] partialSync;

    private int ssOff;

    private int psOff;

    private CompositionOffsetsBox.Entry[] compOffsets;

    private int cttsInd;

    private int cttsSubInd;

    private SeekableByteChannel input;

    private MovieBox movie;

    public MP4DemuxerTrack(MovieBox mov, TrakBox trak, SeekableByteChannel input) {
        super(trak);
        this.input = input;
        this.movie = mov;
        SampleSizesBox stsz = NodeBox.findFirstPath(trak, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        SyncSamplesBox stss = NodeBox.findFirstPath(trak, SyncSamplesBox.class, Box.path("mdia.minf.stbl.stss"));
        SyncSamplesBox stps = NodeBox.findFirstPath(trak, SyncSamplesBox.class, Box.path("mdia.minf.stbl.stps"));
        CompositionOffsetsBox ctts = NodeBox.findFirstPath(trak, CompositionOffsetsBox.class, Box.path("mdia.minf.stbl.ctts"));
        this.compOffsets = ctts == null ? null : ctts.getEntries();
        if (stss != null) {
            this.syncSamples = stss.getSyncSamples();
        }
        if (stps != null) {
            this.partialSync = stps.getSyncSamples();
        }
        this.sizes = stsz.getSizes();
    }

    public synchronized MP4Packet nextFrame() throws IOException {
        if (this.curFrame >= (long) this.sizes.length) {
            return null;
        } else {
            int size = this.sizes[(int) this.curFrame];
            return this.getNextFrame(ByteBuffer.allocate(size));
        }
    }

    @Override
    public synchronized MP4Packet getNextFrame(ByteBuffer storage) throws IOException {
        if (this.curFrame >= (long) this.sizes.length) {
            return null;
        } else {
            int size = this.sizes[(int) this.curFrame];
            if (storage != null && storage.remaining() < size) {
                throw new IllegalArgumentException("Buffer size is not enough to fit a packet");
            } else {
                long pktPos = this.chunkOffsets[Math.min(this.chunkOffsets.length - 1, this.stcoInd)] + this.offInChunk;
                ByteBuffer result = this.readPacketData(this.input, storage, pktPos, size);
                if (result != null && result.remaining() < size) {
                    return null;
                } else {
                    int duration = this.timeToSamples[this.sttsInd].getSampleDuration();
                    boolean sync = this.syncSamples == null;
                    if (this.syncSamples != null && this.ssOff < this.syncSamples.length && this.curFrame + 1L == (long) this.syncSamples[this.ssOff]) {
                        sync = true;
                        this.ssOff++;
                    }
                    boolean psync = false;
                    if (this.partialSync != null && this.psOff < this.partialSync.length && this.curFrame + 1L == (long) this.partialSync[this.psOff]) {
                        psync = true;
                        this.psOff++;
                    }
                    long realPts = this.pts;
                    if (this.compOffsets != null) {
                        realPts = this.pts + (long) this.compOffsets[this.cttsInd].getOffset();
                        this.cttsSubInd++;
                        if (this.cttsInd < this.compOffsets.length - 1 && this.cttsSubInd == this.compOffsets[this.cttsInd].getCount()) {
                            this.cttsInd++;
                            this.cttsSubInd = 0;
                        }
                    }
                    ByteBuffer data = result == null ? null : this.convertPacket(result);
                    long _pts = QTTimeUtil.mediaToEdited(this.box, realPts, this.movie.getTimescale());
                    Packet.FrameType ftype = sync ? Packet.FrameType.KEY : Packet.FrameType.INTER;
                    int entryNo = this.sampleToChunks[this.stscInd].getEntry() - 1;
                    MP4Packet pkt = new MP4Packet(data, _pts, this.timescale, (long) duration, this.curFrame, ftype, null, 0, realPts, entryNo, pktPos, size, psync);
                    this.offInChunk += (long) size;
                    this.curFrame++;
                    this.noInChunk++;
                    if (this.noInChunk >= this.sampleToChunks[this.stscInd].getCount()) {
                        this.noInChunk = 0;
                        this.offInChunk = 0L;
                        this.nextChunk();
                    }
                    this.shiftPts(1L);
                    return pkt;
                }
            }
        }
    }

    @Override
    public boolean gotoSyncFrame(long frameNo) {
        if (this.syncSamples == null) {
            return this.gotoFrame(frameNo);
        } else if (frameNo < 0L) {
            throw new IllegalArgumentException("negative frame number");
        } else if (frameNo >= this.getFrameCount()) {
            return false;
        } else if (frameNo == this.curFrame) {
            return true;
        } else {
            for (int i = 0; i < this.syncSamples.length; i++) {
                if ((long) (this.syncSamples[i] - 1) > frameNo) {
                    return this.gotoFrame((long) (this.syncSamples[i - 1] - 1));
                }
            }
            return this.gotoFrame((long) (this.syncSamples[this.syncSamples.length - 1] - 1));
        }
    }

    @Override
    protected void seekPointer(long frameNo) {
        if (this.compOffsets != null) {
            this.cttsSubInd = (int) frameNo;
            for (this.cttsInd = 0; this.cttsSubInd >= this.compOffsets[this.cttsInd].getCount(); this.cttsInd++) {
                this.cttsSubInd = this.cttsSubInd - this.compOffsets[this.cttsInd].getCount();
            }
        }
        this.curFrame = (long) ((int) frameNo);
        this.stcoInd = 0;
        this.stscInd = 0;
        this.noInChunk = (int) frameNo;
        this.offInChunk = 0L;
        while (this.noInChunk >= this.sampleToChunks[this.stscInd].getCount()) {
            this.noInChunk = this.noInChunk - this.sampleToChunks[this.stscInd].getCount();
            this.nextChunk();
        }
        for (int i = 0; i < this.noInChunk; i++) {
            this.offInChunk = this.offInChunk + (long) this.sizes[(int) frameNo - this.noInChunk + i];
        }
        if (this.syncSamples != null) {
            this.ssOff = 0;
            while (this.ssOff < this.syncSamples.length && (long) this.syncSamples[this.ssOff] < this.curFrame + 1L) {
                this.ssOff++;
            }
        }
        if (this.partialSync != null) {
            this.psOff = 0;
            while (this.psOff < this.partialSync.length && (long) this.partialSync[this.psOff] < this.curFrame + 1L) {
                this.psOff++;
            }
        }
    }

    @Override
    public long getFrameCount() {
        return (long) this.sizes.length;
    }
}