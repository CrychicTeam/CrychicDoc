package com.github.alexthe666.citadel.repack.jcodec.containers.mxf;

import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.TrackType;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.FileDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericPictureEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.GenericSoundEssenceDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.IndexSegment;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.KLV;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MXFMetadata;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MXFPartition;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.MXFUtil;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.SourceClip;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.TimecodeComponent;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.TimelineTrack;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.UL;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.WaveAudioDescriptor;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MXFDemuxer {

    protected List<MXFMetadata> metadata;

    protected MXFPartition header;

    protected List<MXFPartition> partitions;

    protected List<IndexSegment> indexSegments;

    protected SeekableByteChannel ch;

    protected MXFDemuxer.MXFDemuxerTrack[] tracks;

    protected int totalFrames;

    protected double duration;

    protected TimecodeComponent timecode;

    public MXFDemuxer(SeekableByteChannel ch) throws IOException {
        this.ch = ch;
        ch.setPosition(0L);
        this.parseHeader(ch);
        this.findIndex();
        this.tracks = this.findTracks();
        this.timecode = MXFUtil.findMeta(this.metadata, TimecodeComponent.class);
    }

    public MXFDemuxer.OP getOp() {
        UL op = this.header.getPack().getOp();
        MXFDemuxer.OP[] values = MXFDemuxer.OP.values();
        for (int i = 0; i < values.length; i++) {
            MXFDemuxer.OP op2 = values[i];
            if (op.get(12) == op2.major && op.get(13) == op2.minor) {
                return op2;
            }
        }
        return MXFDemuxer.OP.OPAtom;
    }

    public MXFDemuxer.MXFDemuxerTrack[] findTracks() throws IOException {
        List<TimelineTrack> _tracks = MXFUtil.findAllMeta(this.metadata, TimelineTrack.class);
        List<FileDescriptor> descriptors = MXFUtil.findAllMeta(this.metadata, FileDescriptor.class);
        Map<Integer, MXFDemuxer.MXFDemuxerTrack> tracks = new LinkedHashMap();
        for (TimelineTrack track : _tracks) {
            if (track.getTrackId() != 0 && track.getTrackNumber() != 0) {
                int trackId = track.getTrackId();
                if (tracks.containsKey(trackId)) {
                    Logger.warn("duplicate trackId " + trackId);
                } else {
                    FileDescriptor descriptor = findDescriptor(descriptors, track.getTrackId());
                    if (descriptor == null) {
                        Logger.warn("No generic descriptor for track: " + track.getTrackId());
                        if (descriptors.size() == 1 && ((FileDescriptor) descriptors.get(0)).getLinkedTrackId() == 0) {
                            descriptor = (FileDescriptor) descriptors.get(0);
                        }
                    }
                    if (descriptor == null) {
                        Logger.warn("Track without descriptor: " + track.getTrackId());
                    } else {
                        int trackNumber = track.getTrackNumber();
                        UL ul = UL.newULFromInts(new int[] { 6, 14, 43, 52, 1, 2, 1, 1, 13, 1, 3, 1, trackNumber >>> 24 & 0xFF, trackNumber >>> 16 & 0xFF, trackNumber >>> 8 & 0xFF, trackNumber & 0xFF });
                        MXFDemuxer.MXFDemuxerTrack dt = this.createTrack(ul, track, descriptor);
                        if (dt.getCodec() != null || descriptor instanceof WaveAudioDescriptor) {
                            tracks.put(trackId, dt);
                        }
                    }
                }
            } else {
                Logger.warn("trackId == 0 || trackNumber == 0");
            }
        }
        return (MXFDemuxer.MXFDemuxerTrack[]) tracks.values().toArray(new MXFDemuxer.MXFDemuxerTrack[tracks.size()]);
    }

    public static FileDescriptor findDescriptor(List<FileDescriptor> descriptors, int trackId) {
        for (FileDescriptor descriptor : descriptors) {
            if (descriptor.getLinkedTrackId() == trackId) {
                return descriptor;
            }
        }
        return null;
    }

    protected MXFDemuxer.MXFDemuxerTrack createTrack(UL ul, TimelineTrack track, GenericDescriptor descriptor) throws IOException {
        return new MXFDemuxer.MXFDemuxerTrack(this, ul, track, descriptor);
    }

    public List<IndexSegment> getIndexes() {
        return this.indexSegments;
    }

    public List<MXFPartition> getEssencePartitions() {
        return this.partitions;
    }

    public TimecodeComponent getTimecode() {
        return this.timecode;
    }

    public void parseHeader(SeekableByteChannel ff) throws IOException {
        this.header = readHeaderPartition(ff);
        this.metadata = new ArrayList();
        this.partitions = new ArrayList();
        long nextPartition = ff.size();
        ff.setPosition(this.header.getPack().getFooterPartition());
        do {
            long thisPartition = ff.position();
            KLV kl = KLV.readKL(ff);
            ByteBuffer fetchFrom = NIOUtils.fetchFromChannel(ff, (int) kl.len);
            this.header = MXFPartition.read(kl.key, fetchFrom, ff.position() - kl.offset, nextPartition);
            if (this.header.getPack().getNbEssenceContainers() > 0) {
                this.partitions.add(0, this.header);
            }
            this.metadata.addAll(0, readPartitionMeta(ff, this.header));
            ff.setPosition(this.header.getPack().getPrevPartition());
            nextPartition = thisPartition;
        } while (this.header.getPack().getThisPartition() != 0L);
    }

    public static List<MXFMetadata> readPartitionMeta(SeekableByteChannel ff, MXFPartition header) throws IOException {
        long basePos = ff.position();
        List<MXFMetadata> local = new ArrayList();
        ByteBuffer metaBuffer = NIOUtils.fetchFromChannel(ff, (int) Math.max(0L, header.getEssenceFilePos() - basePos));
        KLV kl;
        while (metaBuffer.hasRemaining() && (kl = KLV.readKLFromBuffer(metaBuffer, basePos)) != null && (long) metaBuffer.remaining() >= kl.len) {
            MXFMetadata meta = parseMeta(kl.key, NIOUtils.read(metaBuffer, (int) kl.len));
            if (meta != null) {
                local.add(meta);
            }
        }
        return local;
    }

    public static MXFPartition readHeaderPartition(SeekableByteChannel ff) throws IOException {
        MXFPartition header = null;
        KLV kl;
        while ((kl = KLV.readKL(ff)) != null) {
            if (MXFConst.HEADER_PARTITION_KLV.equals(kl.key)) {
                ByteBuffer data = NIOUtils.fetchFromChannel(ff, (int) kl.len);
                header = MXFPartition.read(kl.key, data, ff.position() - kl.offset, 0L);
                break;
            }
            ff.setPosition(ff.position() + kl.len);
        }
        return header;
    }

    private static MXFMetadata parseMeta(UL ul, ByteBuffer _bb) {
        Class<? extends MXFMetadata> class1 = (Class<? extends MXFMetadata>) MXFConst.klMetadata.get(ul);
        if (class1 == null) {
            Logger.warn("Unknown metadata piece: " + ul);
            return null;
        } else {
            try {
                MXFMetadata meta = Platform.newInstance(class1, new Object[] { ul });
                meta.readBuf(_bb);
                return meta;
            } catch (Exception var4) {
                Logger.warn("Unknown metadata piece: " + ul);
                return null;
            }
        }
    }

    private void findIndex() {
        this.indexSegments = new ArrayList();
        for (MXFMetadata meta : this.metadata) {
            if (meta instanceof IndexSegment) {
                IndexSegment is = (IndexSegment) meta;
                this.indexSegments.add(is);
                this.totalFrames = (int) ((long) this.totalFrames + is.getIndexDuration());
                this.duration = this.duration + (double) is.getIndexEditRateDen() * (double) is.getIndexDuration() / (double) is.getIndexEditRateNum();
            }
        }
    }

    public MXFDemuxer.MXFDemuxerTrack[] getTracks() {
        return this.tracks;
    }

    public MXFDemuxer.MXFDemuxerTrack getVideoTrack() {
        for (MXFDemuxer.MXFDemuxerTrack track : this.tracks) {
            if (track.isVideo()) {
                return track;
            }
        }
        return null;
    }

    public MXFDemuxer.MXFDemuxerTrack[] getAudioTracks() {
        List<MXFDemuxer.MXFDemuxerTrack> audio = new ArrayList();
        for (MXFDemuxer.MXFDemuxerTrack track : this.tracks) {
            if (track.isAudio()) {
                audio.add(track);
            }
        }
        return (MXFDemuxer.MXFDemuxerTrack[]) audio.toArray(new MXFDemuxer.MXFDemuxerTrack[0]);
    }

    public List<SourceClip> getSourceClips(int trackId) {
        boolean trackFound = true;
        List<SourceClip> clips = new ArrayList();
        for (MXFMetadata m : this.metadata) {
            if (m instanceof TimelineTrack) {
                TimelineTrack tt = (TimelineTrack) m;
                int trackId2 = tt.getTrackId();
                trackFound = trackId2 == trackId;
            }
            if (trackFound && m instanceof SourceClip) {
                SourceClip clip = (SourceClip) m;
                if (clip.getSourceTrackId() == trackId) {
                    clips.add(clip);
                }
            }
        }
        return clips;
    }

    public static TapeTimecode readTapeTimecode(File mxf) throws IOException {
        FileChannelWrapper read = NIOUtils.readableChannel(mxf);
        TapeTimecode var15;
        try {
            MXFDemuxer.Fast fast = new MXFDemuxer.Fast(read);
            MXFDemuxer.MXFDemuxerTrack track = fast.getVideoTrack();
            TimecodeComponent timecode = fast.getTimecode();
            List<SourceClip> sourceClips = fast.getSourceClips(track.getTrackId());
            long tc = 0L;
            boolean dropFrame = false;
            Rational editRate = null;
            if (timecode != null) {
                editRate = track.getEditRate();
                dropFrame = timecode.getDropFrame() != 0;
                tc = timecode.getStart();
            }
            for (SourceClip sourceClip : sourceClips) {
                tc += sourceClip.getStartPosition();
            }
            if (editRate == null) {
                return null;
            }
            var15 = TapeTimecode.tapeTimecode(tc, dropFrame, (int) Math.ceil(editRate.toDouble()));
        } finally {
            read.close();
        }
        return var15;
    }

    public List<MXFMetadata> getMetadata() {
        return Collections.unmodifiableList(this.metadata);
    }

    public static class Fast extends MXFDemuxer {

        public Fast(SeekableByteChannel ch) throws IOException {
            super(ch);
        }

        @Override
        public void parseHeader(SeekableByteChannel ff) throws IOException {
            this.partitions = new ArrayList();
            this.metadata = new ArrayList();
            this.header = readHeaderPartition(ff);
            this.metadata.addAll(readPartitionMeta(ff, this.header));
            this.partitions.add(this.header);
            ff.setPosition(this.header.getPack().getFooterPartition());
            KLV kl = KLV.readKL(ff);
            if (kl != null) {
                ByteBuffer fetchFrom = NIOUtils.fetchFromChannel(ff, (int) kl.len);
                MXFPartition footer = MXFPartition.read(kl.key, fetchFrom, ff.position() - kl.offset, ff.size());
                this.metadata.addAll(readPartitionMeta(ff, footer));
            }
        }
    }

    public static class MXFDemuxerTrack implements SeekableDemuxerTrack {

        private UL essenceUL;

        private int dataLen;

        private int indexSegmentIdx;

        private int indexSegmentSubIdx;

        private int frameNo;

        private long pts;

        private int partIdx;

        private long partEssenceOffset;

        private GenericDescriptor descriptor;

        private TimelineTrack track;

        private boolean video;

        private boolean audio;

        private MXFCodec codec;

        private int audioFrameDuration;

        private int audioTimescale;

        private MXFDemuxer demuxer;

        public MXFDemuxerTrack(MXFDemuxer demuxer, UL essenceUL, TimelineTrack track, GenericDescriptor descriptor) throws IOException {
            this.demuxer = demuxer;
            this.essenceUL = essenceUL;
            this.track = track;
            this.descriptor = descriptor;
            if (descriptor instanceof GenericPictureEssenceDescriptor) {
                this.video = true;
            } else if (descriptor instanceof GenericSoundEssenceDescriptor) {
                this.audio = true;
            }
            this.codec = this.resolveCodec();
            if (this.codec != null || descriptor instanceof WaveAudioDescriptor) {
                Logger.warn("Track type: " + this.video + ", " + this.audio);
                if (this.audio && descriptor instanceof WaveAudioDescriptor) {
                    WaveAudioDescriptor wave = (WaveAudioDescriptor) descriptor;
                    this.cacheAudioFrameSizes(demuxer.ch);
                    this.audioFrameDuration = this.dataLen / ((wave.getQuantizationBits() >> 3) * wave.getChannelCount());
                    this.audioTimescale = (int) wave.getAudioSamplingRate().scalar();
                }
            }
        }

        public boolean isAudio() {
            return this.audio;
        }

        public boolean isVideo() {
            return this.video;
        }

        public double getDuration() {
            return this.demuxer.duration;
        }

        public int getNumFrames() {
            return this.demuxer.totalFrames;
        }

        public String getName() {
            return this.track.getName();
        }

        private void cacheAudioFrameSizes(SeekableByteChannel ch) throws IOException {
            for (MXFPartition mxfPartition : this.demuxer.partitions) {
                if (mxfPartition.getEssenceLength() > 0L) {
                    ch.setPosition(mxfPartition.getEssenceFilePos());
                    KLV kl;
                    do {
                        kl = KLV.readKL(ch);
                        if (kl == null) {
                            break;
                        }
                        ch.setPosition(ch.position() + kl.len);
                    } while (!this.essenceUL.equals(kl.key));
                    if (kl != null && this.essenceUL.equals(kl.key)) {
                        this.dataLen = (int) kl.len;
                        break;
                    }
                }
            }
        }

        @Override
        public Packet nextFrame() throws IOException {
            if (this.indexSegmentIdx >= this.demuxer.indexSegments.size()) {
                return null;
            } else {
                IndexSegment seg = (IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx);
                long[] off = seg.getIe().getFileOff();
                int erDen = seg.getIndexEditRateNum();
                int erNum = seg.getIndexEditRateDen();
                long frameEssenceOffset = off[this.indexSegmentSubIdx];
                byte toff = seg.getIe().getDisplayOff()[this.indexSegmentSubIdx];
                boolean kf;
                for (kf = seg.getIe().getKeyFrameOff()[this.indexSegmentSubIdx] == 0; frameEssenceOffset >= this.partEssenceOffset + ((MXFPartition) this.demuxer.partitions.get(this.partIdx)).getEssenceLength() && this.partIdx < this.demuxer.partitions.size() - 1; this.partIdx++) {
                    this.partEssenceOffset = this.partEssenceOffset + ((MXFPartition) this.demuxer.partitions.get(this.partIdx)).getEssenceLength();
                }
                long frameFileOffset = frameEssenceOffset - this.partEssenceOffset + ((MXFPartition) this.demuxer.partitions.get(this.partIdx)).getEssenceFilePos();
                Packet result;
                if (!this.audio) {
                    result = this.readPacket(frameFileOffset, this.dataLen, this.pts + (long) (erNum * toff), erDen, erNum, this.frameNo++, kf);
                    this.pts += (long) erNum;
                } else {
                    result = this.readPacket(frameFileOffset, this.dataLen, this.pts, this.audioTimescale, this.audioFrameDuration, this.frameNo++, kf);
                    this.pts = this.pts + (long) this.audioFrameDuration;
                }
                this.indexSegmentSubIdx++;
                if (this.indexSegmentSubIdx >= off.length) {
                    this.indexSegmentIdx++;
                    this.indexSegmentSubIdx = 0;
                    if (this.dataLen == 0 && this.indexSegmentIdx < this.demuxer.indexSegments.size()) {
                        IndexSegment nseg = (IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx);
                        this.pts = this.pts * (long) nseg.getIndexEditRateNum() / (long) erDen;
                    }
                }
                return result;
            }
        }

        public MXFDemuxer.MXFPacket readPacket(long off, int len, long pts, int timescale, int duration, int frameNo, boolean kf) throws IOException {
            SeekableByteChannel ch = this.demuxer.ch;
            synchronized (ch) {
                ch.setPosition(off);
                KLV kl;
                for (kl = KLV.readKL(ch); kl != null && !this.essenceUL.equals(kl.key); kl = KLV.readKL(ch)) {
                    ch.setPosition(ch.position() + kl.len);
                }
                return kl != null && this.essenceUL.equals(kl.key) ? new MXFDemuxer.MXFPacket(NIOUtils.fetchFromChannel(ch, (int) kl.len), pts, timescale, (long) duration, (long) frameNo, kf ? Packet.FrameType.KEY : Packet.FrameType.INTER, null, off, len) : null;
            }
        }

        @Override
        public boolean gotoFrame(long frameNo) {
            if (frameNo == (long) this.frameNo) {
                return true;
            } else {
                this.indexSegmentSubIdx = (int) frameNo;
                for (this.indexSegmentIdx = 0; this.indexSegmentIdx < this.demuxer.indexSegments.size() && (long) this.indexSegmentSubIdx >= ((IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx)).getIndexDuration(); this.indexSegmentIdx++) {
                    this.indexSegmentSubIdx = (int) ((long) this.indexSegmentSubIdx - ((IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx)).getIndexDuration());
                }
                this.indexSegmentSubIdx = Math.min(this.indexSegmentSubIdx, (int) ((IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx)).getIndexDuration());
                return true;
            }
        }

        @Override
        public boolean gotoSyncFrame(long frameNo) {
            if (!this.gotoFrame(frameNo)) {
                return false;
            } else {
                IndexSegment seg = (IndexSegment) this.demuxer.indexSegments.get(this.indexSegmentIdx);
                byte kfOff = seg.getIe().getKeyFrameOff()[this.indexSegmentSubIdx];
                return this.gotoFrame(frameNo + (long) kfOff);
            }
        }

        @Override
        public long getCurFrame() {
            return (long) this.frameNo;
        }

        @Override
        public void seek(double second) {
            throw new NotSupportedException("");
        }

        public UL getEssenceUL() {
            return this.essenceUL;
        }

        public GenericDescriptor getDescriptor() {
            return this.descriptor;
        }

        public MXFCodec getCodec() {
            return this.codec;
        }

        private MXFCodec resolveCodec() {
            UL codecUL;
            if (this.video) {
                codecUL = ((GenericPictureEssenceDescriptor) this.descriptor).getPictureEssenceCoding();
            } else {
                if (!this.audio) {
                    return null;
                }
                codecUL = ((GenericSoundEssenceDescriptor) this.descriptor).getSoundEssenceCompression();
            }
            MXFCodec[] values = MXFCodec.values();
            for (int i = 0; i < values.length; i++) {
                MXFCodec codec = values[i];
                if (codec.getUl().equals(codecUL)) {
                    return codec;
                }
            }
            Logger.warn("Unknown codec: " + codecUL);
            return null;
        }

        public int getTrackId() {
            return this.track.getTrackId();
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            Size size = null;
            if (this.video) {
                GenericPictureEssenceDescriptor pd = (GenericPictureEssenceDescriptor) this.descriptor;
                size = new Size(pd.getStoredWidth(), pd.getStoredHeight());
            }
            TrackType t = this.video ? TrackType.VIDEO : (this.audio ? TrackType.AUDIO : TrackType.OTHER);
            return new DemuxerTrackMeta(t, this.getCodec().getCodec(), this.demuxer.duration, null, this.demuxer.totalFrames, null, VideoCodecMeta.createSimpleVideoCodecMeta(size, ColorSpace.YUV420), null);
        }

        public Rational getEditRate() {
            return this.track.getEditRate();
        }
    }

    public static class MXFPacket extends Packet {

        private long offset;

        private int len;

        public MXFPacket(ByteBuffer data, long pts, int timescale, long duration, long frameNo, Packet.FrameType frameType, TapeTimecode tapeTimecode, long offset, int len) {
            super(data, pts, timescale, duration, frameNo, frameType, tapeTimecode, 0);
            this.offset = offset;
            this.len = len;
        }

        public long getOffset() {
            return this.offset;
        }

        public int getLen() {
            return this.len;
        }
    }

    public static final class OP {

        public static final MXFDemuxer.OP OP1a = new MXFDemuxer.OP(1, 1);

        public static final MXFDemuxer.OP OP1b = new MXFDemuxer.OP(1, 2);

        public static final MXFDemuxer.OP OP1c = new MXFDemuxer.OP(1, 3);

        public static final MXFDemuxer.OP OP2a = new MXFDemuxer.OP(2, 1);

        public static final MXFDemuxer.OP OP2b = new MXFDemuxer.OP(2, 2);

        public static final MXFDemuxer.OP OP2c = new MXFDemuxer.OP(2, 3);

        public static final MXFDemuxer.OP OP3a = new MXFDemuxer.OP(3, 1);

        public static final MXFDemuxer.OP OP3b = new MXFDemuxer.OP(3, 2);

        public static final MXFDemuxer.OP OP3c = new MXFDemuxer.OP(3, 3);

        public static final MXFDemuxer.OP OPAtom = new MXFDemuxer.OP(16, 0);

        private static final MXFDemuxer.OP[] _values = new MXFDemuxer.OP[] { OP1a, OP1b, OP1c, OP2a, OP2b, OP2c, OP3a, OP3b, OP3c, OPAtom };

        public int major;

        public int minor;

        private OP(int major, int minor) {
            this.major = major;
            this.minor = minor;
        }

        public static MXFDemuxer.OP[] values() {
            return _values;
        }
    }
}