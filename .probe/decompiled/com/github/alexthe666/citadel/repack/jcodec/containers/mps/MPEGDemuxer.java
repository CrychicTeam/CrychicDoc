package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import com.github.alexthe666.citadel.repack.jcodec.common.Demuxer;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public interface MPEGDemuxer extends Demuxer {

    @Override
    List<? extends MPEGDemuxer.MPEGDemuxerTrack> getTracks();

    @Override
    List<? extends MPEGDemuxer.MPEGDemuxerTrack> getVideoTracks();

    @Override
    List<? extends MPEGDemuxer.MPEGDemuxerTrack> getAudioTracks();

    public interface MPEGDemuxerTrack extends DemuxerTrack {

        Packet nextFrameWithBuffer(ByteBuffer var1) throws IOException;

        @Override
        DemuxerTrackMeta getMeta();

        int getSid();

        List<PESPacket> getPending();

        void ignore();
    }
}