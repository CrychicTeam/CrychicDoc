package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.ChunkOffsetBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.DataEntryUrlBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.DataReferenceBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.DecodingTimeToSampleBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.ESDBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.MediaHeaderBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.SampleSizeBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.SampleToChunkBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.TrackHeaderBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.DecoderSpecificInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.od.Descriptor;
import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Track {

    private final MP4InputStream in;

    protected final TrackHeaderBox tkhd;

    private final MediaHeaderBox mdhd;

    private final boolean inFile;

    private final List<Frame> frames;

    private URL location;

    private int currentFrame;

    protected DecoderSpecificInfo decoderSpecificInfo;

    protected DecoderInfo decoderInfo;

    protected Protection protection;

    Track(Box trak, MP4InputStream in) {
        this.in = in;
        this.tkhd = (TrackHeaderBox) trak.getChild(1953196132L);
        Box mdia = trak.getChild(1835297121L);
        this.mdhd = (MediaHeaderBox) mdia.getChild(1835296868L);
        Box minf = mdia.getChild(1835626086L);
        Box dinf = minf.getChild(1684631142L);
        DataReferenceBox dref = (DataReferenceBox) dinf.getChild(1685218662L);
        if (dref.hasChild(1970433056L)) {
            DataEntryUrlBox url = (DataEntryUrlBox) dref.getChild(1970433056L);
            this.inFile = url.isInFile();
            if (!this.inFile) {
                try {
                    this.location = new URL(url.getLocation());
                } catch (MalformedURLException var9) {
                    Logger.getLogger("MP4 API").log(Level.WARNING, "Parsing URL-Box failed: {0}, url: {1}", new String[] { var9.toString(), url.getLocation() });
                    this.location = null;
                }
            }
        } else {
            this.inFile = true;
            this.location = null;
        }
        Box stbl = minf.getChild(1937007212L);
        if (stbl.hasChildren()) {
            this.frames = new ArrayList();
            this.parseSampleTable(stbl);
        } else {
            this.frames = Collections.emptyList();
        }
        this.currentFrame = 0;
    }

    private void parseSampleTable(Box stbl) {
        double timeScale = (double) this.mdhd.getTimeScale();
        Type type = this.getType();
        long[] sampleSizes = ((SampleSizeBox) stbl.getChild(1937011578L)).getSampleSizes();
        ChunkOffsetBox stco;
        if (stbl.hasChild(1937007471L)) {
            stco = (ChunkOffsetBox) stbl.getChild(1937007471L);
        } else {
            stco = (ChunkOffsetBox) stbl.getChild(1668232756L);
        }
        long[] chunkOffsets = stco.getChunks();
        SampleToChunkBox stsc = (SampleToChunkBox) stbl.getChild(1937011555L);
        long[] firstChunks = stsc.getFirstChunks();
        long[] samplesPerChunk = stsc.getSamplesPerChunk();
        DecodingTimeToSampleBox stts = (DecodingTimeToSampleBox) stbl.getChild(1937011827L);
        long[] sampleCounts = stts.getSampleCounts();
        long[] sampleDeltas = stts.getSampleDeltas();
        long[] timeOffsets = new long[sampleSizes.length];
        long tmp = 0L;
        int off = 0;
        for (int i = 0; i < sampleCounts.length; i++) {
            for (int j = 0; (long) j < sampleCounts[i]; j++) {
                timeOffsets[off + j] = tmp;
                tmp += sampleDeltas[i];
            }
            off = (int) ((long) off + sampleCounts[i]);
        }
        int current = 0;
        long offset = 0L;
        for (int i = 0; i < firstChunks.length; i++) {
            int lastChunk;
            if (i < firstChunks.length - 1) {
                lastChunk = (int) firstChunks[i + 1] - 1;
            } else {
                lastChunk = chunkOffsets.length;
            }
            for (int j = (int) firstChunks[i] - 1; j < lastChunk; j++) {
                offset = chunkOffsets[j];
                for (int k = 0; (long) k < samplesPerChunk[i]; k++) {
                    double timeStamp = (double) timeOffsets[current] / timeScale;
                    this.frames.add(new Frame(type, offset, sampleSizes[current], timeStamp));
                    offset += sampleSizes[current];
                    current++;
                }
            }
        }
        Collections.sort(this.frames);
    }

    protected void findDecoderSpecificInfo(ESDBox esds) {
        Descriptor ed = esds.getEntryDescriptor();
        for (Descriptor e : ed.getChildren()) {
            for (Descriptor e2 : e.getChildren()) {
                switch(e2.getType()) {
                    case 5:
                        this.decoderSpecificInfo = (DecoderSpecificInfo) e2;
                }
            }
        }
    }

    protected <T> void parseSampleEntry(Box sampleEntry, Class<T> clazz) {
        try {
            T type = (T) clazz.newInstance();
            if (sampleEntry.getClass().isInstance(type)) {
                System.out.println("true");
            }
        } catch (InstantiationException var5) {
            var5.printStackTrace();
        } catch (IllegalAccessException var6) {
            var6.printStackTrace();
        }
    }

    public abstract Type getType();

    public abstract Track.Codec getCodec();

    public boolean isEnabled() {
        return this.tkhd.isTrackEnabled();
    }

    public boolean isUsed() {
        return this.tkhd.isTrackInMovie();
    }

    public boolean isUsedForPreview() {
        return this.tkhd.isTrackInPreview();
    }

    public Date getCreationTime() {
        return Utils.getDate(this.tkhd.getCreationTime());
    }

    public Date getModificationTime() {
        return Utils.getDate(this.tkhd.getModificationTime());
    }

    public Locale getLanguage() {
        return new Locale(this.mdhd.getLanguage());
    }

    public boolean isInFile() {
        return this.inFile;
    }

    public URL getLocation() {
        return this.location;
    }

    public byte[] getDecoderSpecificInfo() {
        return this.decoderSpecificInfo.getData();
    }

    public DecoderInfo getDecoderInfo() {
        return this.decoderInfo;
    }

    public Protection getProtection() {
        return this.protection;
    }

    public boolean hasMoreFrames() {
        return this.currentFrame < this.frames.size();
    }

    public Frame readNextFrame() throws IOException {
        Frame frame = null;
        if (this.hasMoreFrames()) {
            frame = (Frame) this.frames.get(this.currentFrame);
            long diff = frame.getOffset() - this.in.getOffset();
            if (diff > 0L) {
                this.in.skipBytes(diff);
            } else if (diff < 0L) {
                if (!this.in.hasRandomAccess()) {
                    Logger.getLogger("MP4 API").log(Level.WARNING, "readNextFrame failed: frame {0} already skipped, offset:{1}, stream:{2}", new Object[] { this.currentFrame, frame.getOffset(), this.in.getOffset() });
                    throw new IOException("frame already skipped and no random access");
                }
                this.in.seek(frame.getOffset());
            }
            byte[] b = new byte[(int) frame.getSize()];
            try {
                this.in.readBytes(b);
            } catch (EOFException var6) {
                Logger.getLogger("MP4 API").log(Level.WARNING, "readNextFrame failed: tried to read {0} bytes at {1}", new Long[] { frame.getSize(), this.in.getOffset() });
                throw var6;
            }
            frame.setData(b);
            this.currentFrame++;
        }
        return frame;
    }

    public double seek(double timestamp) {
        Frame frame = null;
        for (int i = 0; i < this.frames.size(); i++) {
            frame = (Frame) this.frames.get(i++);
            if (frame.getTime() > timestamp) {
                this.currentFrame = i;
                break;
            }
        }
        return frame == null ? -1.0 : frame.getTime();
    }

    double getNextTimeStamp() {
        return ((Frame) this.frames.get(this.currentFrame)).getTime();
    }

    public interface Codec {
    }
}