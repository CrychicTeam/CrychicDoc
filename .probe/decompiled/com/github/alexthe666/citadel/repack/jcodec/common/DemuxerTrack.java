package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;

public interface DemuxerTrack {

    Packet nextFrame() throws IOException;

    DemuxerTrackMeta getMeta();
}