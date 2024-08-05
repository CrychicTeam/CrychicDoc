package com.github.alexthe666.citadel.repack.jcodec.api.specific;

import com.github.alexthe666.citadel.repack.jcodec.api.MediaInfo;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public class GenericAdaptor implements ContainerAdaptor {

    private VideoDecoder decoder;

    public GenericAdaptor(VideoDecoder detect) {
        this.decoder = detect;
    }

    @Override
    public Picture decodeFrame(Packet packet, byte[][] data) {
        return this.decoder.decodeFrame(packet.getData(), data);
    }

    @Override
    public boolean canSeek(Packet data) {
        return true;
    }

    @Override
    public MediaInfo getMediaInfo() {
        return new MediaInfo(new Size(0, 0));
    }

    @Override
    public byte[][] allocatePicture() {
        return Picture.create(1920, 1088, ColorSpace.YUV444).getData();
    }
}