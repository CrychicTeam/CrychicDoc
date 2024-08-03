package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.common.io.StringReader;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class Header {

    public static final byte[] FOURCC_FREE = new byte[] { 102, 114, 101, 101 };

    private static final long MAX_UNSIGNED_INT = 4294967296L;

    private String fourcc;

    private long size;

    private boolean lng;

    public Header(String fourcc) {
        this.fourcc = fourcc;
    }

    public static Header createHeader(String fourcc, long size) {
        Header header = new Header(fourcc);
        header.size = size;
        return header;
    }

    public static Header newHeader(String fourcc, long size, boolean lng) {
        Header header = new Header(fourcc);
        header.size = size;
        header.lng = lng;
        return header;
    }

    public static Header read(ByteBuffer input) {
        long size = 0L;
        while (input.remaining() >= 4 && (size = Platform.unsignedInt(input.getInt())) == 0L) {
        }
        if (input.remaining() >= 4 && (size >= 8L || size == 1L)) {
            String fourcc = NIOUtils.readString(input, 4);
            boolean lng = false;
            if (size == 1L) {
                if (input.remaining() < 8) {
                    Logger.error("Broken atom of size " + size);
                    return null;
                }
                lng = true;
                size = input.getLong();
            }
            return newHeader(fourcc, size, lng);
        } else {
            Logger.error("Broken atom of size " + size);
            return null;
        }
    }

    public void skip(InputStream di) throws IOException {
        StringReader.sureSkip(di, this.size - this.headerSize());
    }

    public long headerSize() {
        return !this.lng && this.size <= 4294967296L ? 8L : 16L;
    }

    public static int estimateHeaderSize(int size) {
        return (long) (size + 8) > 4294967296L ? 16 : 8;
    }

    public byte[] readContents(InputStream di) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; (long) i < this.size - this.headerSize(); i++) {
            baos.write(di.read());
        }
        return baos.toByteArray();
    }

    public String getFourcc() {
        return this.fourcc;
    }

    public long getBodySize() {
        return this.size - this.headerSize();
    }

    public void setBodySize(int length) {
        this.size = (long) length + this.headerSize();
    }

    public void write(ByteBuffer out) {
        if (this.size > 4294967296L) {
            out.putInt(1);
        } else {
            out.putInt((int) this.size);
        }
        byte[] bt = JCodecUtil2.asciiString(this.fourcc);
        if (bt != null && bt.length == 4) {
            out.put(bt);
        } else {
            out.put(FOURCC_FREE);
        }
        if (this.size > 4294967296L) {
            out.putLong(this.size);
        }
    }

    public void writeChannel(SeekableByteChannel output) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(16);
        this.write(bb);
        bb.flip();
        output.write(bb);
    }

    public long getSize() {
        return this.size;
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        return 31 * result + (this.fourcc == null ? 0 : this.fourcc.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            Header other = (Header) obj;
            if (this.fourcc == null) {
                if (other.fourcc != null) {
                    return false;
                }
            } else if (!this.fourcc.equals(other.fourcc)) {
                return false;
            }
            return true;
        }
    }
}