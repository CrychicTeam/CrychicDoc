package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import com.github.alexthe666.citadel.repack.jcodec.common.AudioCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.common.Muxer;
import com.github.alexthe666.citadel.repack.jcodec.common.MuxerTrack;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class IVFMuxer implements Muxer, MuxerTrack {

    private SeekableByteChannel ch;

    private int nFrames;

    private Size dim;

    private int frameRate;

    private boolean headerWritten;

    public IVFMuxer(SeekableByteChannel ch) throws IOException {
        this.ch = ch;
    }

    @Override
    public void addFrame(Packet pkt) throws IOException {
        if (!this.headerWritten) {
            this.frameRate = pkt.getTimescale();
            this.writeHeader();
            this.headerWritten = true;
        }
        ByteBuffer fh = ByteBuffer.allocate(12);
        fh.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer frame = pkt.getData();
        fh.putInt(frame.remaining());
        fh.putLong((long) this.nFrames);
        fh.clear();
        this.ch.write(fh);
        this.ch.write(frame);
        this.nFrames++;
    }

    public void close() throws IOException {
        this.ch.setPosition(24L);
        NIOUtils.writeIntLE(this.ch, this.nFrames);
    }

    @Override
    public MuxerTrack addVideoTrack(Codec codec, VideoCodecMeta meta) {
        if (this.dim != null) {
            throw new RuntimeException("IVF can not have multiple video tracks.");
        } else {
            this.dim = meta.getSize();
            return this;
        }
    }

    private void writeHeader() throws IOException {
        ByteBuffer ivf = ByteBuffer.allocate(32);
        ivf.order(ByteOrder.LITTLE_ENDIAN);
        ivf.put((byte) 68);
        ivf.put((byte) 75);
        ivf.put((byte) 73);
        ivf.put((byte) 70);
        ivf.putShort((short) 0);
        ivf.putShort((short) 32);
        ivf.putInt(808996950);
        ivf.putShort((short) this.dim.getWidth());
        ivf.putShort((short) this.dim.getHeight());
        ivf.putInt(this.frameRate);
        ivf.putInt(1);
        ivf.putInt(1);
        ivf.clear();
        this.ch.write(ivf);
    }

    @Override
    public MuxerTrack addAudioTrack(Codec codec, AudioCodecMeta meta) {
        throw new RuntimeException("Video-only container");
    }

    @Override
    public void finish() throws IOException {
    }
}