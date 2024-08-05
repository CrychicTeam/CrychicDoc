package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer;

import com.github.alexthe666.citadel.repack.jcodec.common.IntArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.Ints;
import com.github.alexthe666.citadel.repack.jcodec.common.LongArrayList;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Unit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ChunkOffsets64Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.CompositionOffsetsBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.HandlerBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleDescriptionBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleSizesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SyncSamplesBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimeToSampleBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrackHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MP4MuxerTrack extends AbstractMP4MuxerTrack {

    private List<TimeToSampleBox.TimeToSampleEntry> sampleDurations;

    private long sameDurCount = 0L;

    private long curDuration = -1L;

    private LongArrayList chunkOffsets;

    private IntArrayList sampleSizes;

    private IntArrayList iframes;

    private List<CompositionOffsetsBox.LongEntry> compositionOffsets;

    private long lastCompositionOffset = 0L;

    private long lastCompositionSamples = 0L;

    private long ptsEstimate = 0L;

    private int lastEntry = -1;

    private long trackTotalDuration;

    private int curFrame;

    private boolean allIframes = true;

    private TimecodeMP4MuxerTrack timecodeTrack;

    public MP4MuxerTrack(int trackId, MP4TrackType type) {
        super(trackId, type);
        this.sampleDurations = new ArrayList();
        this.chunkOffsets = LongArrayList.createLongArrayList();
        this.sampleSizes = IntArrayList.createIntArrayList();
        this.iframes = IntArrayList.createIntArrayList();
        this.compositionOffsets = new ArrayList();
        this.setTgtChunkDuration(new Rational(1, 1), Unit.FRAME);
    }

    @Override
    public void addFrame(Packet pkt) throws IOException {
        this.addFrameInternal(pkt, 1);
        this.processTimecode(pkt);
    }

    public void addFrameInternal(Packet pkt, int entryNo) throws IOException {
        if (this.finished) {
            throw new IllegalStateException("The muxer track has finished muxing");
        } else {
            if (this._timescale == -1) {
                this._timescale = pkt.getTimescale();
            }
            if (this._timescale != pkt.getTimescale()) {
                pkt.setPts(pkt.getPts() * (long) this._timescale / (long) pkt.getTimescale());
                pkt.setDuration(pkt.getPts() * (long) this._timescale / pkt.getDuration());
            }
            if (this.type == MP4TrackType.VIDEO) {
                long compositionOffset = pkt.getPts() - this.ptsEstimate;
                if (compositionOffset != this.lastCompositionOffset) {
                    if (this.lastCompositionSamples > 0L) {
                        this.compositionOffsets.add(new CompositionOffsetsBox.LongEntry(this.lastCompositionSamples, this.lastCompositionOffset));
                    }
                    this.lastCompositionOffset = compositionOffset;
                    this.lastCompositionSamples = 0L;
                }
                this.lastCompositionSamples++;
                this.ptsEstimate = this.ptsEstimate + pkt.getDuration();
            }
            if (this.lastEntry != -1 && this.lastEntry != entryNo) {
                this.outChunk(this.lastEntry);
                this.samplesInLastChunk = -1;
            }
            this.curChunk.add(pkt.getData());
            if (pkt.isKeyFrame()) {
                this.iframes.add(this.curFrame + 1);
            } else {
                this.allIframes = false;
            }
            this.curFrame++;
            this.chunkDuration = this.chunkDuration + pkt.getDuration();
            if (this.curDuration != -1L && pkt.getDuration() != this.curDuration) {
                this.sampleDurations.add(new TimeToSampleBox.TimeToSampleEntry((int) this.sameDurCount, (int) this.curDuration));
                this.sameDurCount = 0L;
            }
            this.curDuration = pkt.getDuration();
            this.sameDurCount++;
            this.trackTotalDuration = this.trackTotalDuration + pkt.getDuration();
            this.outChunkIfNeeded(entryNo);
            this.lastEntry = entryNo;
        }
    }

    private void processTimecode(Packet pkt) throws IOException {
        if (this.timecodeTrack != null) {
            this.timecodeTrack.addTimecode(pkt);
        }
    }

    private void outChunkIfNeeded(int entryNo) throws IOException {
        Preconditions.checkState(this.tgtChunkDurationUnit == Unit.FRAME || this.tgtChunkDurationUnit == Unit.SEC);
        if (this.tgtChunkDurationUnit == Unit.FRAME && this.curChunk.size() * this.tgtChunkDuration.getDen() == this.tgtChunkDuration.getNum()) {
            this.outChunk(entryNo);
        } else if (this.tgtChunkDurationUnit == Unit.SEC && this.chunkDuration > 0L && this.chunkDuration * (long) this.tgtChunkDuration.getDen() >= (long) (this.tgtChunkDuration.getNum() * this._timescale)) {
            this.outChunk(entryNo);
        }
    }

    void outChunk(int entryNo) throws IOException {
        if (this.curChunk.size() != 0) {
            this.chunkOffsets.add(this.out.position());
            for (ByteBuffer bs : this.curChunk) {
                this.sampleSizes.add(bs.remaining());
                this.out.write(bs);
            }
            if (this.samplesInLastChunk == -1 || this.samplesInLastChunk != this.curChunk.size()) {
                this.samplesInChunks.add(new SampleToChunkBox.SampleToChunkEntry((long) (this.chunkNo + 1), this.curChunk.size(), entryNo));
            }
            this.samplesInLastChunk = this.curChunk.size();
            this.chunkNo++;
            this.chunkDuration = 0L;
            this.curChunk.clear();
        }
    }

    @Override
    protected Box finish(MovieHeaderBox mvhd) throws IOException {
        Preconditions.checkState(!this.finished, "The muxer track has finished muxing");
        this.outChunk(this.lastEntry);
        if (this.sameDurCount > 0L) {
            this.sampleDurations.add(new TimeToSampleBox.TimeToSampleEntry((int) this.sameDurCount, (int) this.curDuration));
        }
        this.finished = true;
        TrakBox trak = TrakBox.createTrakBox();
        Size dd = this.getDisplayDimensions();
        TrackHeaderBox tkhd = TrackHeaderBox.createTrackHeaderBox(this.trackId, (long) mvhd.getTimescale() * this.trackTotalDuration / (long) this._timescale, (float) dd.getWidth(), (float) dd.getHeight(), new Date().getTime(), new Date().getTime(), 1.0F, (short) 0, 0L, new int[] { 65536, 0, 0, 0, 65536, 0, 0, 0, 1073741824 });
        tkhd.setFlags(15);
        trak.add(tkhd);
        this.tapt(trak);
        MediaBox media = MediaBox.createMediaBox();
        trak.add(media);
        media.add(MediaHeaderBox.createMediaHeaderBox(this._timescale, this.trackTotalDuration, 0, new Date().getTime(), new Date().getTime(), 0));
        HandlerBox hdlr = HandlerBox.createHandlerBox("mhlr", this.type.getHandler(), "appl", 0, 0);
        media.add(hdlr);
        MediaInfoBox minf = MediaInfoBox.createMediaInfoBox();
        media.add(minf);
        this.mediaHeader(minf, this.type);
        minf.add(HandlerBox.createHandlerBox("dhlr", "url ", "appl", 0, 0));
        this.addDref(minf);
        NodeBox stbl = new NodeBox(new Header("stbl"));
        minf.add(stbl);
        this.putCompositionOffsets(stbl);
        this.putEdits(trak);
        this.putName(trak);
        stbl.add(SampleDescriptionBox.createSampleDescriptionBox((SampleEntry[]) this.sampleEntries.toArray(new SampleEntry[0])));
        stbl.add(SampleToChunkBox.createSampleToChunkBox((SampleToChunkBox.SampleToChunkEntry[]) this.samplesInChunks.toArray(new SampleToChunkBox.SampleToChunkEntry[0])));
        stbl.add(SampleSizesBox.createSampleSizesBox2(this.sampleSizes.toArray()));
        stbl.add(TimeToSampleBox.createTimeToSampleBox((TimeToSampleBox.TimeToSampleEntry[]) this.sampleDurations.toArray(new TimeToSampleBox.TimeToSampleEntry[0])));
        stbl.add(ChunkOffsets64Box.createChunkOffsets64Box(this.chunkOffsets.toArray()));
        if (!this.allIframes && this.iframes.size() > 0) {
            stbl.add(SyncSamplesBox.createSyncSamplesBox(this.iframes.toArray()));
        }
        return trak;
    }

    private void putCompositionOffsets(NodeBox stbl) {
        if (this.compositionOffsets.size() > 0) {
            this.compositionOffsets.add(new CompositionOffsetsBox.LongEntry(this.lastCompositionSamples, this.lastCompositionOffset));
            long min = minLongOffset(this.compositionOffsets);
            if (min > 0L) {
                for (CompositionOffsetsBox.LongEntry entry : this.compositionOffsets) {
                    entry.offset -= min;
                }
            }
            CompositionOffsetsBox.LongEntry first = (CompositionOffsetsBox.LongEntry) this.compositionOffsets.get(0);
            if (first.getOffset() > 0L) {
                if (this.edits == null) {
                    this.edits = new ArrayList();
                    this.edits.add(new Edit(this.trackTotalDuration, first.getOffset(), 1.0F));
                } else {
                    for (Edit edit : this.edits) {
                        edit.setMediaTime(edit.getMediaTime() + first.getOffset());
                    }
                }
            }
            CompositionOffsetsBox.Entry[] intEntries = new CompositionOffsetsBox.Entry[this.compositionOffsets.size()];
            for (int i = 0; i < this.compositionOffsets.size(); i++) {
                CompositionOffsetsBox.LongEntry longEntry = (CompositionOffsetsBox.LongEntry) this.compositionOffsets.get(i);
                intEntries[i] = new CompositionOffsetsBox.Entry(Ints.checkedCast(longEntry.count), Ints.checkedCast(longEntry.offset));
            }
            stbl.add(CompositionOffsetsBox.createCompositionOffsetsBox(intEntries));
        }
    }

    public static long minLongOffset(List<CompositionOffsetsBox.LongEntry> offs) {
        long min = Long.MAX_VALUE;
        for (CompositionOffsetsBox.LongEntry entry : offs) {
            min = Math.min(min, entry.getOffset());
        }
        return min;
    }

    public static int minOffset(List<CompositionOffsetsBox.Entry> offs) {
        int min = Integer.MAX_VALUE;
        for (CompositionOffsetsBox.Entry entry : offs) {
            min = Math.min(min, entry.getOffset());
        }
        return min;
    }

    @Override
    public long getTrackTotalDuration() {
        return this.trackTotalDuration;
    }

    public TimecodeMP4MuxerTrack getTimecodeTrack() {
        return this.timecodeTrack;
    }

    public void setTimecode(TimecodeMP4MuxerTrack timecodeTrack) {
        this.timecodeTrack = timecodeTrack;
    }
}