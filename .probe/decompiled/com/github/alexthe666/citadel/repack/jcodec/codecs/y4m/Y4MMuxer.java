package com.github.alexthe666.citadel.repack.jcodec.codecs.y4m;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

public class Y4MMuxer implements Muxer, MuxerTrack {

    private WritableByteChannel ch;

    private boolean headerWritten;

    private VideoCodecMeta meta;

    public static final byte[] frameTag = "FRAME\n".getBytes();

    public Y4MMuxer(WritableByteChannel ch) {
        this.ch = ch;
    }

    protected void writeHeader() throws IOException {
        Size size = this.meta.getSize();
        byte[] bytes = String.format("YUV4MPEG2 W%d H%d F25:1 Ip A0:0 C420jpeg XYSCSS=420JPEG\n", size.getWidth(), size.getHeight()).getBytes();
        this.ch.write(ByteBuffer.wrap(bytes));
    }

    @Override
    public void addFrame(Packet outPacket) throws IOException {
        if (!this.headerWritten) {
            this.writeHeader();
            this.headerWritten = true;
        }
        this.ch.write(ByteBuffer.wrap(frameTag));
        this.ch.write(outPacket.data.duplicate());
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        this.meta = meta;
        return this;
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        throw new RuntimeException("Y4M doesn't support audio");
    }

    @Override
    public void finish() throws IOException {
    }
}