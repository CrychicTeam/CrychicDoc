package com.github.alexthe666.citadel.repack.jcodec.api.specific;

import com.github.alexthe666.citadel.repack.jcodec.api.MediaInfo;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public interface ContainerAdaptor {

    Picture decodeFrame(Packet var1, byte[][] var2);

    boolean canSeek(Packet var1);

    byte[][] allocatePicture();

    MediaInfo getMediaInfo();
}