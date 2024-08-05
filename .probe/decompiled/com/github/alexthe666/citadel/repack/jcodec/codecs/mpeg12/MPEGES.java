package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12.bitstream.PictureHeader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MPEGPacket;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

public class MPEGES extends SegmentReader {

    private int frameNo;

    public long lastKnownDuration;

    public MPEGES(ReadableByteChannel channel, int fetchSize) throws IOException {
        super(channel, fetchSize);
    }

    public MPEGPacket frame(ByteBuffer buffer) throws IOException {
        ByteBuffer dup = buffer.duplicate();
        while (this.curMarker != 256 && this.curMarker != 435 && this.skipToMarker()) {
        }
        while (this.curMarker != 256 && this.readToNextMarker(dup)) {
        }
        this.readToNextMarker(dup);
        while (this.curMarker != 256 && this.curMarker != 435 && this.readToNextMarker(dup)) {
        }
        dup.flip();
        PictureHeader ph = MPEGDecoder.getPictureHeader(dup.duplicate());
        return dup.hasRemaining() ? new MPEGPacket(dup, 0L, 90000, 0L, (long) (this.frameNo++), ph.picture_coding_type <= 1 ? Packet.FrameType.KEY : Packet.FrameType.INTER, null) : null;
    }

    public MPEGPacket getFrame() throws IOException {
        while (this.curMarker != 256 && this.curMarker != 435 && this.skipToMarker()) {
        }
        List<ByteBuffer> buffers = new ArrayList();
        while (this.curMarker != 256 && !this.done) {
            this.readToNextMarkerBuffers(buffers);
        }
        this.readToNextMarkerBuffers(buffers);
        while (this.curMarker != 256 && this.curMarker != 435 && !this.done) {
            this.readToNextMarkerBuffers(buffers);
        }
        ByteBuffer dup = NIOUtils.combineBuffers(buffers);
        PictureHeader ph = MPEGDecoder.getPictureHeader(dup.duplicate());
        return dup.hasRemaining() ? new MPEGPacket(dup, 0L, 90000, 0L, (long) (this.frameNo++), ph.picture_coding_type <= 1 ? Packet.FrameType.KEY : Packet.FrameType.INTER, null) : null;
    }
}