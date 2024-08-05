package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.muxer;

import com.github.alexthe666.citadel.repack.jcodec.api.UnhandledStateException;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Unit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ClearApertureBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.DataRefBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Edit;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.EditListBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.EncodedPixelBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.GenericMediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.Header;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NameBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.NodeBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.PixelAspectExt;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.ProductionApertureBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleEntry;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.SoundMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TimecodeMediaInfoBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.TrakBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoMediaHeaderBox;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.VideoSampleEntry;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMP4MuxerTrack implements MuxerTrack {

    protected static final int NO_TIMESCALE_SET = -1;

    protected int trackId;

    protected MP4TrackType type;

    protected int _timescale;

    protected Rational tgtChunkDuration;

    protected Unit tgtChunkDurationUnit;

    protected long chunkDuration;

    protected List<ByteBuffer> curChunk;

    protected List<SampleToChunkBox.SampleToChunkEntry> samplesInChunks;

    protected int samplesInLastChunk = -1;

    protected int chunkNo = 0;

    protected boolean finished;

    protected List<SampleEntry> sampleEntries;

    protected List<Edit> edits;

    private String name;

    protected SeekableByteChannel out;

    public AbstractMP4MuxerTrack(int trackId, MP4TrackType type) {
        this.curChunk = new ArrayList();
        this.samplesInChunks = new ArrayList();
        this.sampleEntries = new ArrayList();
        this.trackId = trackId;
        this.type = type;
        this._timescale = -1;
    }

    AbstractMP4MuxerTrack setOut(SeekableByteChannel out) {
        this.out = out;
        return this;
    }

    public void setTgtChunkDuration(Rational duration, Unit unit) {
        this.tgtChunkDuration = duration;
        this.tgtChunkDurationUnit = unit;
    }

    public abstract long getTrackTotalDuration();

    protected abstract Box finish(MovieHeaderBox var1) throws IOException;

    public boolean isVideo() {
        return this.type == MP4TrackType.VIDEO;
    }

    public boolean isTimecode() {
        return this.type == MP4TrackType.TIMECODE;
    }

    public boolean isAudio() {
        return this.type == MP4TrackType.SOUND;
    }

    public MP4TrackType getType() {
        return this.type;
    }

    public int getTrackId() {
        return this.trackId;
    }

    public Size getDisplayDimensions() {
        int width = 0;
        int height = 0;
        if (this.sampleEntries != null && !this.sampleEntries.isEmpty() && this.sampleEntries.get(0) instanceof VideoSampleEntry) {
            VideoSampleEntry vse = (VideoSampleEntry) this.sampleEntries.get(0);
            PixelAspectExt paspBox = NodeBox.findFirst(vse, PixelAspectExt.class, PixelAspectExt.fourcc());
            Rational pasp = paspBox != null ? paspBox.getRational() : new Rational(1, 1);
            width = pasp.getNum() * vse.getWidth() / pasp.getDen();
            height = vse.getHeight();
        }
        return new Size(width, height);
    }

    public void tapt(TrakBox trak) {
        Size dd = this.getDisplayDimensions();
        if (this.type == MP4TrackType.VIDEO) {
            NodeBox tapt = new NodeBox(new Header("tapt"));
            tapt.add(ClearApertureBox.createClearApertureBox(dd.getWidth(), dd.getHeight()));
            tapt.add(ProductionApertureBox.createProductionApertureBox(dd.getWidth(), dd.getHeight()));
            tapt.add(EncodedPixelBox.createEncodedPixelBox(dd.getWidth(), dd.getHeight()));
            trak.add(tapt);
        }
    }

    public AbstractMP4MuxerTrack addSampleEntry(SampleEntry se) {
        Preconditions.checkState(!this.finished, "The muxer track has finished muxing");
        this.sampleEntries.add(se);
        return this;
    }

    public List<SampleEntry> getEntries() {
        return this.sampleEntries;
    }

    public void setEdits(List<Edit> edits) {
        this.edits = edits;
    }

    protected void putEdits(TrakBox trak) {
        if (this.edits != null) {
            NodeBox edts = new NodeBox(new Header("edts"));
            edts.add(EditListBox.createEditListBox(this.edits));
            trak.add(edts);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    protected void putName(TrakBox trak) {
        if (this.name != null) {
            NodeBox udta = new NodeBox(new Header("udta"));
            udta.add(NameBox.createNameBox(this.name));
            trak.add(udta);
        }
    }

    protected void mediaHeader(MediaInfoBox minf, MP4TrackType type) {
        if (MP4TrackType.VIDEO == type) {
            VideoMediaHeaderBox vmhd = VideoMediaHeaderBox.createVideoMediaHeaderBox(0, 0, 0, 0);
            vmhd.setFlags(1);
            minf.add(vmhd);
        } else if (MP4TrackType.SOUND == type) {
            SoundMediaHeaderBox smhd = SoundMediaHeaderBox.createSoundMediaHeaderBox();
            smhd.setFlags(1);
            minf.add(smhd);
        } else if (MP4TrackType.TIMECODE == type) {
            NodeBox gmhd = new NodeBox(new Header("gmhd"));
            gmhd.add(GenericMediaInfoBox.createGenericMediaInfoBox());
            NodeBox tmcd = new NodeBox(new Header("tmcd"));
            gmhd.add(tmcd);
            tmcd.add(TimecodeMediaInfoBox.createTimecodeMediaInfoBox((short) 0, (short) 0, (short) 12, new short[] { 0, 0, 0 }, new short[] { 255, 255, 255 }, "Lucida Grande"));
            minf.add(gmhd);
        } else if (MP4TrackType.DATA != type) {
            throw new UnhandledStateException("Handler " + type.getHandler() + " not supported");
        }
    }

    protected void addDref(NodeBox minf) {
        DataInfoBox dinf = DataInfoBox.createDataInfoBox();
        minf.add(dinf);
        DataRefBox dref = DataRefBox.createDataRefBox();
        dinf.add(dref);
        dref.add(Box.LeafBox.createLeafBox(Header.createHeader("alis", 0L), ByteBuffer.wrap(new byte[] { 0, 0, 0, 1 })));
    }

    protected int getTimescale() {
        return this._timescale;
    }
}