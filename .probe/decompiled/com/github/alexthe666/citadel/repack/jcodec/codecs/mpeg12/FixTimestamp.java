package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg12;

import com.github.alexthe666.citadel.repack.jcodec.common.io.FileChannelWrapper;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mps.MTSUtils;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public abstract class FixTimestamp {

    public void fix(File file) throws IOException {
        RandomAccessFile ra = null;
        try {
            ra = new RandomAccessFile(file, "rw");
            SeekableByteChannel ch = new FileChannelWrapper(ra.getChannel());
            final FixTimestamp self = this;
            (new MTSUtils.TSReader(true) {

                @Override
                public boolean onPkt(int guid, boolean payloadStart, ByteBuffer bb, long filePos, boolean sectionSyntax, ByteBuffer fullPkt) {
                    return self.processPacket(payloadStart, bb, sectionSyntax, fullPkt);
                }
            }).readTsFile(ch);
        } finally {
            if (ra != null) {
                ra.close();
            }
        }
    }

    private boolean processPacket(boolean payloadStart, ByteBuffer bb, boolean sectionSyntax, ByteBuffer fullPkt) {
        if (payloadStart && !sectionSyntax) {
            int streamId = bb.getInt();
            if (streamId == 445 || streamId >= 448 && streamId < 495) {
                int len = bb.getShort();
                int b0 = bb.get() & 255;
                bb.position(bb.position() - 1);
                if ((b0 & 192) == 128) {
                    this.fixMpeg2(streamId & 0xFF, bb);
                } else {
                    this.fixMpeg1(streamId & 0xFF, bb);
                }
            }
            return true;
        } else {
            return true;
        }
    }

    public void fixMpeg1(int streamId, ByteBuffer is) {
        int c = is.getInt() & 0xFF;
        while (c == 255) {
            c = is.get() & 255;
        }
        if ((c & 192) == 64) {
            is.get();
            c = is.get() & 255;
        }
        if ((c & 240) == 32) {
            is.position(is.position() - 1);
            this.fixTs(streamId, is, true);
        } else if ((c & 240) == 48) {
            is.position(is.position() - 1);
            this.fixTs(streamId, is, true);
            this.fixTs(streamId, is, false);
        } else if (c != 15) {
            throw new RuntimeException("Invalid data");
        }
    }

    public long fixTs(int streamId, ByteBuffer is, boolean isPts) {
        byte b0 = is.get();
        byte b1 = is.get();
        byte b2 = is.get();
        byte b3 = is.get();
        byte b4 = is.get();
        long pts = ((long) b0 & 14L) << 29 | (long) ((b1 & 255) << 22) | (long) ((b2 & 255) >> 1 << 15) | (long) ((b3 & 255) << 7) | (long) ((b4 & 255) >> 1);
        pts = this.doWithTimestamp(streamId, pts, isPts);
        is.position(is.position() - 5);
        is.put((byte) ((int) ((long) (b0 & 240) | pts >>> 29 | 1L)));
        is.put((byte) ((int) (pts >>> 22)));
        is.put((byte) ((int) (pts >>> 14 | 1L)));
        is.put((byte) ((int) (pts >>> 7)));
        is.put((byte) ((int) (pts << 1 | 1L)));
        return pts;
    }

    public void fixMpeg2(int streamId, ByteBuffer is) {
        int flags1 = is.get() & 255;
        int flags2 = is.get() & 255;
        int header_len = is.get() & 255;
        if ((flags2 & 192) == 128) {
            this.fixTs(streamId, is, true);
        } else if ((flags2 & 192) == 192) {
            this.fixTs(streamId, is, true);
            this.fixTs(streamId, is, false);
        }
    }

    public boolean isVideo(int streamId) {
        return streamId >= 224 && streamId <= 239;
    }

    public boolean isAudio(int streamId) {
        return streamId >= 191 && streamId <= 223;
    }

    protected abstract long doWithTimestamp(int var1, long var2, boolean var4);
}