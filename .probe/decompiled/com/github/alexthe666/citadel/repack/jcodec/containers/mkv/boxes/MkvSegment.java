package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class MkvSegment extends EbmlMaster {

    int headerSize = 0;

    public static final byte[] SEGMENT_ID = new byte[] { 24, 83, -128, 103 };

    public MkvSegment(byte[] id) {
        super(id);
    }

    public static MkvSegment createMkvSegment() {
        return new MkvSegment(SEGMENT_ID);
    }

    public ByteBuffer getHeader() {
        long headerSize = this.getHeaderSize();
        if (headerSize > 2147483647L) {
            System.out.println("MkvSegment.getHeader: id.length " + this.id.length + "  Element.getEbmlSize(" + this.dataLen + "): " + EbmlUtil.ebmlLength((long) this.dataLen) + " size: " + this.dataLen);
        }
        ByteBuffer bb = ByteBuffer.allocate((int) headerSize);
        bb.put(this.id);
        bb.put(EbmlUtil.ebmlEncode(this.getDataLen()));
        if (this.children != null && !this.children.isEmpty()) {
            for (EbmlBase e : this.children) {
                if (!Platform.arrayEqualsByte(CLUSTER_ID, e.type.id)) {
                    bb.put(e.getData());
                }
            }
        }
        bb.flip();
        return bb;
    }

    public long getHeaderSize() {
        long returnValue = (long) this.id.length;
        returnValue += (long) EbmlUtil.ebmlLength(this.getDataLen());
        if (this.children != null && !this.children.isEmpty()) {
            for (EbmlBase e : this.children) {
                if (!Platform.arrayEqualsByte(CLUSTER_ID, e.type.id)) {
                    returnValue += e.size();
                }
            }
        }
        return returnValue;
    }
}