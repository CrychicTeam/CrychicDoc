package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.demuxer;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.mp4.AvcCBox;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.SeekableDemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.TrackType;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.model.TapeTimecode;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.MKVParser;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.MKVType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBase;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBin;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlFloat;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlMaster;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlString;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlUint;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.MkvBlock;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MKVDemuxer implements Demuxer {

    private MKVDemuxer.VideoTrack vTrack = null;

    private List<MKVDemuxer.AudioTrack> aTracks;

    private List<MKVDemuxer.SubtitlesTrack> subsTracks;

    private List<EbmlMaster> t;

    private SeekableByteChannel channel;

    int timescale = 1;

    int pictureWidth;

    int pictureHeight;

    private static Map<String, Codec> codecMapping = new HashMap();

    public MKVDemuxer(SeekableByteChannel fileChannelWrapper) throws IOException {
        this.channel = fileChannelWrapper;
        this.aTracks = new ArrayList();
        this.subsTracks = new ArrayList();
        MKVParser parser = new MKVParser(this.channel);
        this.t = parser.parse();
        this.demux();
    }

    private void demux() {
        MKVType[] path = new MKVType[] { MKVType.Segment, MKVType.Info, MKVType.TimecodeScale };
        EbmlUint ts = MKVType.findFirstTree(this.t, path);
        if (ts != null) {
            this.timescale = (int) ts.getUint();
        }
        MKVType[] path9 = new MKVType[] { MKVType.Segment, MKVType.Tracks, MKVType.TrackEntry };
        for (EbmlMaster aTrack : MKVType.findList(this.t, EbmlMaster.class, path9)) {
            MKVType[] path1 = new MKVType[] { MKVType.TrackEntry, MKVType.TrackType };
            long type = ((EbmlUint) MKVType.findFirst(aTrack, path1)).getUint();
            MKVType[] path2 = new MKVType[] { MKVType.TrackEntry, MKVType.TrackNumber };
            long id = ((EbmlUint) MKVType.findFirst(aTrack, path2)).getUint();
            if (type == 1L) {
                if (this.vTrack != null) {
                    throw new RuntimeException("More then 1 video track, can not compute...");
                }
                MKVType[] path3 = new MKVType[] { MKVType.TrackEntry, MKVType.CodecPrivate };
                MKVType[] path10 = new MKVType[] { MKVType.TrackEntry, MKVType.CodecID };
                EbmlString codecId = (EbmlString) MKVType.findFirst(aTrack, path10);
                Codec codec = (Codec) codecMapping.get(codecId.getString());
                EbmlBin videoCodecState = (EbmlBin) MKVType.findFirst(aTrack, path3);
                ByteBuffer state = null;
                if (videoCodecState != null) {
                    state = videoCodecState.data;
                }
                MKVType[] path4 = new MKVType[] { MKVType.TrackEntry, MKVType.Video, MKVType.PixelWidth };
                EbmlUint width = (EbmlUint) MKVType.findFirst(aTrack, path4);
                MKVType[] path5 = new MKVType[] { MKVType.TrackEntry, MKVType.Video, MKVType.PixelHeight };
                EbmlUint height = (EbmlUint) MKVType.findFirst(aTrack, path5);
                MKVType[] path6 = new MKVType[] { MKVType.TrackEntry, MKVType.Video, MKVType.DisplayWidth };
                EbmlUint dwidth = (EbmlUint) MKVType.findFirst(aTrack, path6);
                MKVType[] path7 = new MKVType[] { MKVType.TrackEntry, MKVType.Video, MKVType.DisplayHeight };
                EbmlUint dheight = (EbmlUint) MKVType.findFirst(aTrack, path7);
                MKVType[] path8 = new MKVType[] { MKVType.TrackEntry, MKVType.Video, MKVType.DisplayUnit };
                EbmlUint unit = (EbmlUint) MKVType.findFirst(aTrack, path8);
                if (width != null && height != null) {
                    this.pictureWidth = (int) width.getUint();
                    this.pictureHeight = (int) height.getUint();
                } else if (dwidth != null && dheight != null) {
                    if (unit != null && unit.getUint() != 0L) {
                        throw new RuntimeException("DisplayUnits other then 0 are not implemented yet");
                    }
                    this.pictureHeight = (int) dheight.getUint();
                    this.pictureWidth = (int) dwidth.getUint();
                }
                this.vTrack = new MKVDemuxer.VideoTrack(this, (int) id, state, codec);
            } else if (type == 2L) {
                MKVDemuxer.AudioTrack audioTrack = new MKVDemuxer.AudioTrack((int) id, this);
                MKVType[] path3x = new MKVType[] { MKVType.TrackEntry, MKVType.Audio, MKVType.SamplingFrequency };
                EbmlFloat sf = (EbmlFloat) MKVType.findFirst(aTrack, path3x);
                if (sf != null) {
                    audioTrack.samplingFrequency = sf.getDouble();
                }
                this.aTracks.add(audioTrack);
            } else if (type == 17L) {
                MKVDemuxer.SubtitlesTrack subsTrack = new MKVDemuxer.SubtitlesTrack((int) id, this);
                this.subsTracks.add(subsTrack);
            }
        }
        MKVType[] path2 = new MKVType[] { MKVType.Segment, MKVType.Cluster };
        for (EbmlMaster aCluster : MKVType.findList(this.t, EbmlMaster.class, path2)) {
            MKVType[] path1 = new MKVType[] { MKVType.Cluster, MKVType.Timecode };
            long baseTimecode = ((EbmlUint) MKVType.findFirst(aCluster, path1)).getUint();
            for (EbmlBase child : aCluster.children) {
                if (MKVType.SimpleBlock.equals(child.type)) {
                    MkvBlock b = (MkvBlock) child;
                    b.absoluteTimecode = (long) b.timecode + baseTimecode;
                    this.putIntoRightBasket(b);
                } else if (MKVType.BlockGroup.equals(child.type)) {
                    EbmlMaster group = (EbmlMaster) child;
                    for (EbmlBase grandChild : group.children) {
                        if (grandChild.type == MKVType.Block) {
                            MkvBlock b = (MkvBlock) grandChild;
                            b.absoluteTimecode = (long) b.timecode + baseTimecode;
                            this.putIntoRightBasket(b);
                        }
                    }
                }
            }
        }
    }

    private void putIntoRightBasket(MkvBlock b) {
        if (b.trackNumber == (long) this.vTrack.trackNo) {
            this.vTrack.blocks.add(b);
        } else {
            for (int i = 0; i < this.aTracks.size(); i++) {
                MKVDemuxer.AudioTrack audio = (MKVDemuxer.AudioTrack) this.aTracks.get(i);
                if (b.trackNumber == (long) audio.trackNo) {
                    audio.blocks.add(MKVDemuxer.IndexedBlock.make(audio.framesCount, b));
                    audio.framesCount = audio.framesCount + b.frameSizes.length;
                }
            }
            for (int ix = 0; ix < this.subsTracks.size(); ix++) {
                MKVDemuxer.SubtitlesTrack subs = (MKVDemuxer.SubtitlesTrack) this.subsTracks.get(ix);
                if (b.trackNumber == (long) subs.trackNo) {
                    subs.blocks.add(MKVDemuxer.IndexedBlock.make(subs.framesCount, b));
                    subs.framesCount = subs.framesCount + b.frameSizes.length;
                }
            }
        }
    }

    public int getPictureWidth() {
        return this.pictureWidth;
    }

    public int getPictureHeight() {
        return this.pictureHeight;
    }

    @Override
    public List<DemuxerTrack> getAudioTracks() {
        return this.aTracks;
    }

    @Override
    public List<DemuxerTrack> getTracks() {
        ArrayList<DemuxerTrack> tracks = new ArrayList(this.aTracks);
        tracks.add(this.vTrack);
        tracks.addAll(this.subsTracks);
        return tracks;
    }

    @Override
    public List<DemuxerTrack> getVideoTracks() {
        ArrayList<DemuxerTrack> tracks = new ArrayList();
        tracks.add(this.vTrack);
        return tracks;
    }

    public List<DemuxerTrack> getSubtitleTracks() {
        return this.subsTracks;
    }

    public List<? extends EbmlBase> getTree() {
        return this.t;
    }

    public void close() throws IOException {
        this.channel.close();
    }

    static {
        codecMapping.put("V_VP8", Codec.VP8);
        codecMapping.put("V_VP9", Codec.VP9);
        codecMapping.put("V_MPEG4/ISO/AVC", Codec.H264);
    }

    public static class AudioTrack extends MKVDemuxer.MkvTrack {

        public double samplingFrequency;

        public AudioTrack(int trackNo, MKVDemuxer demuxer) {
            super(trackNo, demuxer);
        }

        @Override
        public Packet nextFrame() throws IOException {
            MKVDemuxer.MkvBlockData b = this.nextBlock();
            return b == null ? null : Packet.createPacket(b.data, b.block.absoluteTimecode, (int) Math.round(this.samplingFrequency), 1L, 0L, Packet.FrameType.KEY, TapeTimecode.ZERO_TAPE_TIMECODE);
        }

        @Override
        public Packet getFrames(int count) {
            MKVDemuxer.MkvBlockData frameBlock = this.getFrameBlock(count);
            return frameBlock == null ? null : Packet.createPacket(frameBlock.data, frameBlock.block.absoluteTimecode, (int) Math.round(this.samplingFrequency), (long) frameBlock.count, 0L, Packet.FrameType.KEY, TapeTimecode.ZERO_TAPE_TIMECODE);
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            return null;
        }

        @Override
        public boolean gotoSyncFrame(long frame) {
            return this.gotoFrame(frame);
        }
    }

    public static class IndexedBlock {

        public int firstFrameNo;

        public MkvBlock block;

        public static MKVDemuxer.IndexedBlock make(int no, MkvBlock b) {
            MKVDemuxer.IndexedBlock ib = new MKVDemuxer.IndexedBlock();
            ib.firstFrameNo = no;
            ib.block = b;
            return ib;
        }
    }

    private static class MkvBlockData {

        final MkvBlock block;

        final ByteBuffer data;

        final int count;

        MkvBlockData(MkvBlock block, ByteBuffer data, int count) {
            this.block = block;
            this.data = data;
            this.count = count;
        }
    }

    public static class MkvTrack implements SeekableDemuxerTrack {

        public final int trackNo;

        List<MKVDemuxer.IndexedBlock> blocks;

        int framesCount = 0;

        private int frameIdx = 0;

        private int blockIdx = 0;

        private int frameInBlockIdx = 0;

        private MKVDemuxer demuxer;

        public MkvTrack(int trackNo, MKVDemuxer demuxer) {
            this.blocks = new ArrayList();
            this.trackNo = trackNo;
            this.demuxer = demuxer;
        }

        @Override
        public Packet nextFrame() throws IOException {
            MKVDemuxer.MkvBlockData bd = this.nextBlock();
            return bd == null ? null : Packet.createPacket(bd.data, bd.block.absoluteTimecode, this.demuxer.timescale, 1L, (long) (this.frameIdx - 1), Packet.FrameType.KEY, TapeTimecode.ZERO_TAPE_TIMECODE);
        }

        protected MKVDemuxer.MkvBlockData nextBlock() throws IOException {
            if (this.frameIdx < this.blocks.size() && this.blockIdx < this.blocks.size()) {
                MkvBlock b = ((MKVDemuxer.IndexedBlock) this.blocks.get(this.blockIdx)).block;
                if (b == null) {
                    throw new RuntimeException("Something somewhere went wrong.");
                } else {
                    if (b.frames == null || b.frames.length == 0) {
                        this.demuxer.channel.setPosition(b.dataOffset);
                        ByteBuffer data = ByteBuffer.allocate(b.dataLen);
                        this.demuxer.channel.read(data);
                        b.readFrames(data);
                    }
                    ByteBuffer data = b.frames[this.frameInBlockIdx].duplicate();
                    this.frameInBlockIdx++;
                    this.frameIdx++;
                    if (this.frameInBlockIdx >= b.frames.length) {
                        this.blockIdx++;
                        this.frameInBlockIdx = 0;
                    }
                    return new MKVDemuxer.MkvBlockData(b, data, 1);
                }
            } else {
                return null;
            }
        }

        @Override
        public boolean gotoFrame(long i) {
            if (i > 2147483647L) {
                return false;
            } else if (i > (long) this.framesCount) {
                return false;
            } else {
                int frameBlockIdx = this.findBlockIndex(i);
                if (frameBlockIdx == -1) {
                    return false;
                } else {
                    this.frameIdx = (int) i;
                    this.blockIdx = frameBlockIdx;
                    this.frameInBlockIdx = (int) i - ((MKVDemuxer.IndexedBlock) this.blocks.get(this.blockIdx)).firstFrameNo;
                    return true;
                }
            }
        }

        private int findBlockIndex(long i) {
            for (int blockIndex = 0; blockIndex < this.blocks.size(); blockIndex++) {
                if (i < (long) ((MKVDemuxer.IndexedBlock) this.blocks.get(blockIndex)).block.frameSizes.length) {
                    return blockIndex;
                }
                i -= (long) ((MKVDemuxer.IndexedBlock) this.blocks.get(blockIndex)).block.frameSizes.length;
            }
            return -1;
        }

        @Override
        public long getCurFrame() {
            return (long) this.frameIdx;
        }

        @Override
        public void seek(double second) {
            throw new RuntimeException("Not implemented yet");
        }

        public Packet getFrames(int count) {
            MKVDemuxer.MkvBlockData frameBlock = this.getFrameBlock(count);
            return frameBlock == null ? null : Packet.createPacket(frameBlock.data, frameBlock.block.absoluteTimecode, this.demuxer.timescale, (long) frameBlock.count, 0L, Packet.FrameType.KEY, TapeTimecode.ZERO_TAPE_TIMECODE);
        }

        MKVDemuxer.MkvBlockData getFrameBlock(int count) {
            if (count + this.frameIdx >= this.framesCount) {
                return null;
            } else {
                List<ByteBuffer> packetFrames = new ArrayList();
                MkvBlock firstBlockInAPacket;
                for (firstBlockInAPacket = ((MKVDemuxer.IndexedBlock) this.blocks.get(this.blockIdx)).block; count > 0; count--) {
                    MkvBlock b = ((MKVDemuxer.IndexedBlock) this.blocks.get(this.blockIdx)).block;
                    if (b.frames == null || b.frames.length == 0) {
                        try {
                            this.demuxer.channel.setPosition(b.dataOffset);
                            ByteBuffer data = ByteBuffer.allocate(b.dataLen);
                            this.demuxer.channel.read(data);
                            b.readFrames(data);
                        } catch (IOException var8) {
                            throw new RuntimeException("while reading frames of a Block at offset 0x" + Long.toHexString(b.dataOffset).toUpperCase() + ")", var8);
                        }
                    }
                    packetFrames.add(b.frames[this.frameInBlockIdx].duplicate());
                    this.frameIdx++;
                    this.frameInBlockIdx++;
                    if (this.frameInBlockIdx >= b.frames.length) {
                        this.frameInBlockIdx = 0;
                        this.blockIdx++;
                    }
                }
                int size = 0;
                for (ByteBuffer aFrame : packetFrames) {
                    size += aFrame.limit();
                }
                ByteBuffer data = ByteBuffer.allocate(size);
                for (ByteBuffer aFrame : packetFrames) {
                    data.put(aFrame);
                }
                return new MKVDemuxer.MkvBlockData(firstBlockInAPacket, data, packetFrames.size());
            }
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            return null;
        }

        @Override
        public boolean gotoSyncFrame(long frame) {
            return this.gotoFrame(frame);
        }
    }

    public static class SubtitlesTrack extends MKVDemuxer.MkvTrack {

        SubtitlesTrack(int trackNo, MKVDemuxer demuxer) {
            super(trackNo, demuxer);
        }
    }

    public static class VideoTrack implements SeekableDemuxerTrack {

        private ByteBuffer state;

        public final int trackNo;

        private int frameIdx = 0;

        List<MkvBlock> blocks = new ArrayList();

        private MKVDemuxer demuxer;

        private Codec codec;

        private AvcCBox avcC;

        public VideoTrack(MKVDemuxer demuxer, int trackNo, ByteBuffer state, Codec codec) {
            this.demuxer = demuxer;
            this.trackNo = trackNo;
            this.codec = codec;
            if (codec == Codec.H264) {
                this.avcC = H264Utils.parseAVCCFromBuffer(state);
                this.state = H264Utils.avcCToAnnexB(this.avcC);
            } else {
                this.state = state;
            }
        }

        @Override
        public Packet nextFrame() throws IOException {
            if (this.frameIdx >= this.blocks.size()) {
                return null;
            } else {
                MkvBlock b = (MkvBlock) this.blocks.get(this.frameIdx);
                if (b == null) {
                    throw new RuntimeException("Something somewhere went wrong.");
                } else {
                    this.frameIdx++;
                    this.demuxer.channel.setPosition(b.dataOffset);
                    ByteBuffer data = ByteBuffer.allocate(b.dataLen);
                    this.demuxer.channel.read(data);
                    data.flip();
                    b.readFrames(data.duplicate());
                    long duration = 1L;
                    if (this.frameIdx < this.blocks.size()) {
                        duration = ((MkvBlock) this.blocks.get(this.frameIdx)).absoluteTimecode - b.absoluteTimecode;
                    }
                    ByteBuffer result = b.frames[0].duplicate();
                    if (this.codec == Codec.H264) {
                        result = H264Utils.decodeMOVPacket(result, this.avcC);
                    }
                    return Packet.createPacket(result, b.absoluteTimecode, this.demuxer.timescale, duration, (long) (this.frameIdx - 1), b._keyFrame ? Packet.FrameType.KEY : Packet.FrameType.INTER, TapeTimecode.ZERO_TAPE_TIMECODE);
                }
            }
        }

        @Override
        public boolean gotoFrame(long i) {
            if (i > 2147483647L) {
                return false;
            } else if (i > (long) this.blocks.size()) {
                return false;
            } else {
                this.frameIdx = (int) i;
                return true;
            }
        }

        @Override
        public long getCurFrame() {
            return (long) this.frameIdx;
        }

        @Override
        public void seek(double second) {
            throw new RuntimeException("Not implemented yet");
        }

        public int getFrameCount() {
            return this.blocks.size();
        }

        public ByteBuffer getCodecState() {
            return this.state;
        }

        @Override
        public DemuxerTrackMeta getMeta() {
            return new DemuxerTrackMeta(TrackType.VIDEO, this.codec, 0.0, null, 0, this.state, VideoCodecMeta.createSimpleVideoCodecMeta(new Size(this.demuxer.pictureWidth, this.demuxer.pictureHeight), ColorSpace.YUV420), null);
        }

        @Override
        public boolean gotoSyncFrame(long i) {
            throw new RuntimeException("Unsupported");
        }
    }
}