package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.MP4TrackType;
import java.util.List;
import java.util.ListIterator;

public class TrakBox extends NodeBox {

    public static String fourcc() {
        return "trak";
    }

    public static TrakBox createTrakBox() {
        return new TrakBox(new Header(fourcc()));
    }

    public TrakBox(Header atom) {
        super(atom);
    }

    public void setDataRef(String url) {
        MediaInfoBox minf = this.getMdia().getMinf();
        DataInfoBox dinf = minf.getDinf();
        if (dinf == null) {
            dinf = DataInfoBox.createDataInfoBox();
            minf.add(dinf);
        }
        DataRefBox dref = dinf.getDref();
        UrlBox urlBox = UrlBox.createUrlBox(url);
        if (dref == null) {
            dref = DataRefBox.createDataRefBox();
            dinf.add(dref);
            dref.add(urlBox);
        } else {
            ListIterator<Box> lit = dref.boxes.listIterator();
            while (lit.hasNext()) {
                FullBox box = (FullBox) lit.next();
                if ((box.getFlags() & 1) != 0) {
                    lit.set(urlBox);
                }
            }
        }
    }

    public MediaBox getMdia() {
        return NodeBox.findFirst(this, MediaBox.class, "mdia");
    }

    public TrackHeaderBox getTrackHeader() {
        return NodeBox.findFirst(this, TrackHeaderBox.class, "tkhd");
    }

    public List<Edit> getEdits() {
        EditListBox elst = NodeBox.findFirstPath(this, EditListBox.class, Box.path("edts.elst"));
        return elst == null ? null : elst.getEdits();
    }

    public void setEdits(List<Edit> edits) {
        NodeBox edts = NodeBox.findFirst(this, NodeBox.class, "edts");
        if (edts == null) {
            edts = new NodeBox(new Header("edts"));
            this.add(edts);
        }
        edts.removeChildren(new String[] { "elst" });
        edts.add(EditListBox.createEditListBox(edits));
        this.getTrackHeader().setDuration(getEditedDuration(this));
    }

    public boolean isVideo() {
        return "vide".equals(this.getHandlerType());
    }

    public boolean isTimecode() {
        return "tmcd".equals(this.getHandlerType());
    }

    public String getHandlerType() {
        HandlerBox handlerBox = NodeBox.findFirstPath(this, HandlerBox.class, Box.path("mdia.hdlr"));
        return handlerBox == null ? null : handlerBox.getComponentSubType();
    }

    public boolean isAudio() {
        return "soun".equals(this.getHandlerType());
    }

    public int getTimescale() {
        return NodeBox.<MediaHeaderBox>findFirstPath(this, MediaHeaderBox.class, Box.path("mdia.mdhd")).getTimescale();
    }

    public void setTimescale(int timescale) {
        NodeBox.<MediaHeaderBox>findFirstPath(this, MediaHeaderBox.class, Box.path("mdia.mdhd")).setTimescale(timescale);
    }

    public long rescale(long tv, long ts) {
        return tv * (long) this.getTimescale() / ts;
    }

    public void setDuration(long duration) {
        this.getTrackHeader().setDuration(duration);
    }

    public long getDuration() {
        return this.getTrackHeader().getDuration();
    }

    public long getMediaDuration() {
        return NodeBox.<MediaHeaderBox>findFirstPath(this, MediaHeaderBox.class, Box.path("mdia.mdhd")).getDuration();
    }

    public boolean isPureRef() {
        MediaInfoBox minf = this.getMdia().getMinf();
        DataInfoBox dinf = minf.getDinf();
        if (dinf == null) {
            return false;
        } else {
            DataRefBox dref = dinf.getDref();
            if (dref == null) {
                return false;
            } else {
                for (Box box : dref.boxes) {
                    if ((((FullBox) box).getFlags() & 1) != 0) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public boolean hasDataRef() {
        DataInfoBox dinf = this.getMdia().getMinf().getDinf();
        if (dinf == null) {
            return false;
        } else {
            DataRefBox dref = dinf.getDref();
            if (dref == null) {
                return false;
            } else {
                boolean result = false;
                for (Box box : dref.boxes) {
                    result |= (((FullBox) box).getFlags() & 1) != 1;
                }
                return result;
            }
        }
    }

    public Rational getPAR() {
        PixelAspectExt pasp = NodeBox.findFirstPath(this, PixelAspectExt.class, new String[] { "mdia", "minf", "stbl", "stsd", null, "pasp" });
        return pasp == null ? new Rational(1, 1) : pasp.getRational();
    }

    public void setPAR(Rational par) {
        SampleEntry[] sampleEntries = this.getSampleEntries();
        for (int i = 0; i < sampleEntries.length; i++) {
            SampleEntry sampleEntry = sampleEntries[i];
            sampleEntry.removeChildren(new String[] { "pasp" });
            sampleEntry.add(PixelAspectExt.createPixelAspectExt(par));
        }
    }

    public SampleEntry[] getSampleEntries() {
        return NodeBox.findAllPath(this, SampleEntry.class, new String[] { "mdia", "minf", "stbl", "stsd", null });
    }

    public void setClipRect(short x, short y, short width, short height) {
        NodeBox clip = NodeBox.findFirst(this, NodeBox.class, "clip");
        if (clip == null) {
            clip = new NodeBox(new Header("clip"));
            this.add(clip);
        }
        clip.replace("crgn", ClipRegionBox.createClipRegionBox(x, y, width, height));
    }

    public long getSampleCount() {
        return (long) NodeBox.<SampleSizesBox>findFirstPath(this, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz")).getCount();
    }

    public void setAperture(Size sar, Size dar) {
        this.removeChildren(new String[] { "tapt" });
        NodeBox tapt = new NodeBox(new Header("tapt"));
        tapt.add(ClearApertureBox.createClearApertureBox(dar.getWidth(), dar.getHeight()));
        tapt.add(ProductionApertureBox.createProductionApertureBox(dar.getWidth(), dar.getHeight()));
        tapt.add(EncodedPixelBox.createEncodedPixelBox(sar.getWidth(), sar.getHeight()));
        this.add(tapt);
    }

    public void setDimensions(Size dd) {
        this.getTrackHeader().setWidth((float) dd.getWidth());
        this.getTrackHeader().setHeight((float) dd.getHeight());
    }

    public int getFrameCount() {
        SampleSizesBox stsz = NodeBox.findFirstPath(this, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
        return stsz.getDefaultSize() != 0 ? stsz.getCount() : stsz.getSizes().length;
    }

    public String getName() {
        NameBox nb = NodeBox.findFirstPath(this, NameBox.class, Box.path("udta.name"));
        return nb == null ? null : nb.getName();
    }

    public void fixMediaTimescale(int ts) {
        MediaHeaderBox mdhd = NodeBox.findFirstPath(this, MediaHeaderBox.class, Box.path("mdia.mdhd"));
        int oldTs = mdhd.getTimescale();
        mdhd.setTimescale(ts);
        mdhd.setDuration((long) ts * mdhd.getDuration() / (long) oldTs);
        List<Edit> edits = this.getEdits();
        if (edits != null) {
            for (Edit edit : edits) {
                edit.setMediaTime((long) ts * edit.getMediaTime() / (long) oldTs);
            }
        }
        TimeToSampleBox tts = NodeBox.findFirstPath(this, TimeToSampleBox.class, Box.path("mdia.minf.stbl.stts"));
        TimeToSampleBox.TimeToSampleEntry[] entries = tts.getEntries();
        for (int i = 0; i < entries.length; i++) {
            TimeToSampleBox.TimeToSampleEntry tte = entries[i];
            tte.setSampleDuration(ts * tte.getSampleDuration() / oldTs);
        }
    }

    public void setName(String string) {
        NodeBox udta = NodeBox.findFirst(this, NodeBox.class, "udta");
        if (udta == null) {
            udta = new NodeBox(new Header("udta"));
            this.add(udta);
        }
        udta.removeChildren(new String[] { "name" });
        udta.add(NameBox.createNameBox(string));
    }

    public Size getCodedSize() {
        SampleEntry se = this.getSampleEntries()[0];
        if (!(se instanceof VideoSampleEntry)) {
            throw new IllegalArgumentException("Not a video track");
        } else {
            VideoSampleEntry vse = (VideoSampleEntry) se;
            return new Size(vse.getWidth(), vse.getHeight());
        }
    }

    public TimeToSampleBox getStts() {
        return NodeBox.findFirstPath(this, TimeToSampleBox.class, Box.path("mdia.minf.stbl.stts"));
    }

    public ChunkOffsetsBox getStco() {
        return NodeBox.findFirstPath(this, ChunkOffsetsBox.class, Box.path("mdia.minf.stbl.stco"));
    }

    public ChunkOffsets64Box getCo64() {
        return NodeBox.findFirstPath(this, ChunkOffsets64Box.class, Box.path("mdia.minf.stbl.co64"));
    }

    public SampleSizesBox getStsz() {
        return NodeBox.findFirstPath(this, SampleSizesBox.class, Box.path("mdia.minf.stbl.stsz"));
    }

    public SampleToChunkBox getStsc() {
        return NodeBox.findFirstPath(this, SampleToChunkBox.class, Box.path("mdia.minf.stbl.stsc"));
    }

    public SampleDescriptionBox getStsd() {
        return NodeBox.findFirstPath(this, SampleDescriptionBox.class, Box.path("mdia.minf.stbl.stsd"));
    }

    public SyncSamplesBox getStss() {
        return NodeBox.findFirstPath(this, SyncSamplesBox.class, Box.path("mdia.minf.stbl.stss"));
    }

    public CompositionOffsetsBox getCtts() {
        return NodeBox.findFirstPath(this, CompositionOffsetsBox.class, Box.path("mdia.minf.stbl.ctts"));
    }

    public static MP4TrackType getTrackType(TrakBox trak) {
        HandlerBox handler = NodeBox.findFirstPath(trak, HandlerBox.class, Box.path("mdia.hdlr"));
        return handler == null ? null : MP4TrackType.fromHandler(handler.getComponentSubType());
    }

    public static long getEditedDuration(TrakBox track) {
        List<Edit> edits = track.getEdits();
        if (edits == null) {
            return track.getDuration();
        } else {
            long duration = 0L;
            for (Edit edit : edits) {
                duration += edit.getDuration();
            }
            return duration;
        }
    }
}