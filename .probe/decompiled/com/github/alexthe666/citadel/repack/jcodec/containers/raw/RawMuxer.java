package com.github.alexthe666.citadel.repack.jcodec.containers.raw;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import java.io.IOException;

public class RawMuxer implements Muxer, MuxerTrack {

    private SeekableByteChannel ch;

    private boolean hasVideo;

    private boolean hasAudio;

    public RawMuxer(SeekableByteChannel destStream) {
        this.ch = destStream;
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        if (this.hasAudio) {
            throw new RuntimeException("Raw muxer supports either video or audio track but not both.");
        } else {
            this.hasVideo = true;
            return this;
        }
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        if (this.hasVideo) {
            throw new RuntimeException("Raw muxer supports either video or audio track but not both.");
        } else {
            this.hasAudio = true;
            return this;
        }
    }

    @Override
    public void finish() throws IOException {
    }

    @Override
    public void addFrame(Packet outPacket) throws IOException {
        this.ch.write(outPacket.getData().duplicate());
    }
}