package com.github.alexthe666.citadel.repack.jcodec.containers.imgseq;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MainUtils;
import java.io.IOException;

public class ImageSequenceMuxer implements Muxer, MuxerTrack {

    private String fileNamePattern;

    private int frameNo;

    public ImageSequenceMuxer(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    @Override
    public void addFrame(Packet packet) throws IOException {
        NIOUtils.writeTo(packet.getData(), MainUtils.tildeExpand(String.format(this.fileNamePattern, this.frameNo++)));
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        return this;
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        Logger.warn("Audio is not supported for image sequence muxer.");
        return null;
    }

    @Override
    public void finish() throws IOException {
    }
}