package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.muxer;

import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.MkvBlock;
import java.util.ArrayList;
import java.util.List;

public class MKVMuxerTrack implements MuxerTrack {

    public MKVMuxerTrack.MKVMuxerTrackType type;

    public VideoCodecMeta videoMeta;

    public String codecId;

    public int trackNo;

    private int frameDuration;

    List<MkvBlock> trackBlocks = new ArrayList();

    static final int DEFAULT_TIMESCALE = 1000000000;

    static final int NANOSECONDS_IN_A_MILISECOND = 1000000;

    static final int MULTIPLIER = 1000;

    public MKVMuxerTrack() {
        this.type = MKVMuxerTrack.MKVMuxerTrackType.VIDEO;
    }

    public int getTimescale() {
        return 1000000;
    }

    @Override
    public void addFrame(Packet outPacket) {
        MkvBlock frame = MkvBlock.keyFrame((long) this.trackNo, 0, outPacket.getData());
        frame.absoluteTimecode = outPacket.getPts() - 1L;
        this.trackBlocks.add(frame);
    }

    public long getTrackNo() {
        return (long) this.trackNo;
    }

    public static enum MKVMuxerTrackType {

        VIDEO
    }
}