package com.github.alexthe666.citadel.repack.jcodec.containers.flv;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.IOException;
import java.nio.ByteBuffer;

public class FLVWriter {

    private static final int WRITE_BUFFER_SIZE = 1048576;

    private int startOfLastPacket = 9;

    private SeekableByteChannel out;

    private ByteBuffer writeBuf;

    public FLVWriter(SeekableByteChannel out) {
        this.out = out;
        this.writeBuf = ByteBuffer.allocate(1048576);
        writeHeader(this.writeBuf);
    }

    public void addPacket(FLVTag pkt) throws IOException {
        if (!this.writePacket(this.writeBuf, pkt)) {
            this.writeBuf.flip();
            this.startOfLastPacket = this.startOfLastPacket - this.out.write(this.writeBuf);
            this.writeBuf.clear();
            if (!this.writePacket(this.writeBuf, pkt)) {
                throw new RuntimeException("Unexpected");
            }
        }
    }

    public void finish() throws IOException {
        this.writeBuf.flip();
        this.out.write(this.writeBuf);
    }

    private boolean writePacket(ByteBuffer writeBuf, FLVTag pkt) {
        int pktType = pkt.getType() == FLVTag.Type.VIDEO ? 9 : (pkt.getType() == FLVTag.Type.SCRIPT ? 18 : 8);
        int dataLen = pkt.getData().remaining();
        if (writeBuf.remaining() < 15 + dataLen) {
            return false;
        } else {
            writeBuf.putInt(writeBuf.position() - this.startOfLastPacket);
            this.startOfLastPacket = writeBuf.position();
            writeBuf.put((byte) pktType);
            writeBuf.putShort((short) (dataLen >> 8));
            writeBuf.put((byte) (dataLen & 0xFF));
            writeBuf.putShort((short) (pkt.getPts() >> 8 & 65535));
            writeBuf.put((byte) (pkt.getPts() & 0xFF));
            writeBuf.put((byte) (pkt.getPts() >> 24 & 0xFF));
            writeBuf.putShort((short) 0);
            writeBuf.put((byte) 0);
            NIOUtils.write(writeBuf, pkt.getData().duplicate());
            return true;
        }
    }

    private static void writeHeader(ByteBuffer writeBuf) {
        writeBuf.put((byte) 70);
        writeBuf.put((byte) 76);
        writeBuf.put((byte) 86);
        writeBuf.put((byte) 1);
        writeBuf.put((byte) 5);
        writeBuf.putInt(9);
    }
}