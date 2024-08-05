package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;

public interface PacketSource {

    Packet inputVideoPacket() throws IOException;

    Packet inputAudioPacket() throws IOException;
}