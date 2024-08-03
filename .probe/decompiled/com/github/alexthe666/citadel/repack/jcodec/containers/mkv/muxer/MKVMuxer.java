package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.muxer;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.CuesFactory;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.MKVType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.SeekHeadFactory;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBase;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBin;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlDate;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlFloat;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlMaster;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlString;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlUint;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.MkvBlock;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MKVMuxer implements Muxer {

    private List<MKVMuxerTrack> tracks;

    private MKVMuxerTrack audioTrack;

    private MKVMuxerTrack videoTrack;

    private EbmlMaster mkvInfo;

    private EbmlMaster mkvTracks;

    private EbmlMaster mkvCues;

    private EbmlMaster mkvSeekHead;

    private List<EbmlMaster> clusterList;

    private SeekableByteChannel sink;

    private static Map<Codec, String> codec2mkv = new HashMap();

    public MKVMuxer(SeekableByteChannel s) {
        this.sink = s;
        this.tracks = new ArrayList();
        this.clusterList = new LinkedList();
    }

    public MKVMuxerTrack createVideoTrack(VideoCodecMeta meta, String codecId) {
        if (this.videoTrack == null) {
            this.videoTrack = new MKVMuxerTrack();
            this.tracks.add(this.videoTrack);
            this.videoTrack.codecId = codecId;
            this.videoTrack.videoMeta = meta;
            this.videoTrack.trackNo = this.tracks.size();
        }
        return this.videoTrack;
    }

    @Override
    public void finish() throws IOException {
        List<EbmlMaster> mkvFile = new ArrayList();
        EbmlMaster ebmlHeader = this.defaultEbmlHeader();
        mkvFile.add(ebmlHeader);
        EbmlMaster segmentElem = MKVType.createByType(MKVType.Segment);
        this.mkvInfo = this.muxInfo();
        this.mkvTracks = this.muxTracks();
        this.mkvCues = MKVType.createByType(MKVType.Cues);
        this.mkvSeekHead = this.muxSeekHead();
        this.muxCues();
        segmentElem.add(this.mkvSeekHead);
        segmentElem.add(this.mkvInfo);
        segmentElem.add(this.mkvTracks);
        segmentElem.add(this.mkvCues);
        for (EbmlMaster aCluster : this.clusterList) {
            segmentElem.add(aCluster);
        }
        mkvFile.add(segmentElem);
        for (EbmlMaster el : mkvFile) {
            el.mux(this.sink);
        }
    }

    private EbmlMaster defaultEbmlHeader() {
        EbmlMaster master = MKVType.createByType(MKVType.EBML);
        createLong(master, MKVType.EBMLVersion, 1L);
        createLong(master, MKVType.EBMLReadVersion, 1L);
        createLong(master, MKVType.EBMLMaxIDLength, 4L);
        createLong(master, MKVType.EBMLMaxSizeLength, 8L);
        createString(master, MKVType.DocType, "webm");
        createLong(master, MKVType.DocTypeVersion, 2L);
        createLong(master, MKVType.DocTypeReadVersion, 2L);
        return master;
    }

    private EbmlMaster muxInfo() {
        EbmlMaster master = MKVType.createByType(MKVType.Info);
        int frameDurationInNanoseconds = 40000000;
        createLong(master, MKVType.TimecodeScale, (long) frameDurationInNanoseconds);
        createString(master, MKVType.WritingApp, "JCodec");
        createString(master, MKVType.MuxingApp, "JCodec");
        List<MKVMuxerTrack> tracks2 = this.tracks;
        long max = 0L;
        for (MKVMuxerTrack track : tracks2) {
            MkvBlock lastBlock = (MkvBlock) track.trackBlocks.get(track.trackBlocks.size() - 1);
            if (lastBlock.absoluteTimecode > max) {
                max = lastBlock.absoluteTimecode;
            }
        }
        createDouble(master, MKVType.Duration, (double) ((max + 1L) * (long) frameDurationInNanoseconds) * 1.0);
        createDate(master, MKVType.DateUTC, new Date());
        return master;
    }

    private EbmlMaster muxTracks() {
        EbmlMaster master = MKVType.createByType(MKVType.Tracks);
        for (int i = 0; i < this.tracks.size(); i++) {
            MKVMuxerTrack track = (MKVMuxerTrack) this.tracks.get(i);
            EbmlMaster trackEntryElem = MKVType.createByType(MKVType.TrackEntry);
            createLong(trackEntryElem, MKVType.TrackNumber, (long) track.trackNo);
            createLong(trackEntryElem, MKVType.TrackUID, (long) track.trackNo);
            if (MKVMuxerTrack.MKVMuxerTrackType.VIDEO.equals(track.type)) {
                createLong(trackEntryElem, MKVType.TrackType, 1L);
                createString(trackEntryElem, MKVType.Name, "Track " + (i + 1) + " Video");
                createString(trackEntryElem, MKVType.CodecID, track.codecId);
                EbmlMaster trackVideoElem = MKVType.createByType(MKVType.Video);
                createLong(trackVideoElem, MKVType.PixelWidth, (long) track.videoMeta.getSize().getWidth());
                createLong(trackVideoElem, MKVType.PixelHeight, (long) track.videoMeta.getSize().getHeight());
                trackEntryElem.add(trackVideoElem);
            } else {
                createLong(trackEntryElem, MKVType.TrackType, 2L);
                createString(trackEntryElem, MKVType.Name, "Track " + (i + 1) + " Audio");
                createString(trackEntryElem, MKVType.CodecID, track.codecId);
            }
            master.add(trackEntryElem);
        }
        return master;
    }

    private void muxCues() {
        CuesFactory cf = new CuesFactory(this.mkvSeekHead.size() + this.mkvInfo.size() + this.mkvTracks.size(), (long) this.videoTrack.trackNo);
        for (MkvBlock aBlock : this.videoTrack.trackBlocks) {
            EbmlMaster mkvCluster = this.singleBlockedCluster(aBlock);
            this.clusterList.add(mkvCluster);
            cf.add(CuesFactory.CuePointMock.make(mkvCluster));
        }
        EbmlMaster indexedCues = cf.createCues();
        for (EbmlBase aCuePoint : indexedCues.children) {
            this.mkvCues.add(aCuePoint);
        }
    }

    private EbmlMaster singleBlockedCluster(MkvBlock aBlock) {
        EbmlMaster mkvCluster = MKVType.createByType(MKVType.Cluster);
        createLong(mkvCluster, MKVType.Timecode, aBlock.absoluteTimecode - (long) aBlock.timecode);
        mkvCluster.add(aBlock);
        return mkvCluster;
    }

    private EbmlMaster muxSeekHead() {
        SeekHeadFactory shi = new SeekHeadFactory();
        shi.add(this.mkvInfo);
        shi.add(this.mkvTracks);
        shi.add(this.mkvCues);
        return shi.indexSeekHead();
    }

    public static void createLong(EbmlMaster parent, MKVType type, long value) {
        EbmlUint se = MKVType.createByType(type);
        se.setUint(value);
        parent.add(se);
    }

    public static void createString(EbmlMaster parent, MKVType type, String value) {
        EbmlString se = MKVType.createByType(type);
        se.setString(value);
        parent.add(se);
    }

    public static void createDate(EbmlMaster parent, MKVType type, Date value) {
        EbmlDate se = MKVType.createByType(type);
        se.setDate(value);
        parent.add(se);
    }

    public static void createBuffer(EbmlMaster parent, MKVType type, ByteBuffer value) {
        EbmlBin se = MKVType.createByType(type);
        se.setBuf(value);
        parent.add(se);
    }

    public static void createDouble(EbmlMaster parent, MKVType type, double value) {
        try {
            EbmlFloat se = MKVType.createByType(type);
            se.setDouble(value);
            parent.add(se);
        } catch (ClassCastException var5) {
            throw new RuntimeException("Element of type " + type + " can't be cast to EbmlFloat", var5);
        }
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        return this.createVideoTrack(meta, (String) codec2mkv.get(codec));
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        this.audioTrack = new MKVMuxerTrack();
        this.tracks.add(this.audioTrack);
        this.audioTrack.codecId = (String) codec2mkv.get(codec);
        this.audioTrack.trackNo = this.tracks.size();
        return this.audioTrack;
    }

    static {
        codec2mkv.put(Codec.H264, "V_MPEG4/ISO/AVC");
        codec2mkv.put(Codec.VP8, "V_VP8");
        codec2mkv.put(Codec.VP9, "V_VP9");
    }
}