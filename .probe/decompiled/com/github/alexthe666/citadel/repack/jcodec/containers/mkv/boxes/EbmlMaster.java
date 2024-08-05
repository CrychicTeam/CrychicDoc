package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class EbmlMaster extends EbmlBase {

    protected long usedSize;

    public final ArrayList<EbmlBase> children = new ArrayList();

    public static final byte[] CLUSTER_ID = new byte[] { 31, 67, -74, 117 };

    public EbmlMaster(byte[] id) {
        super(id);
        this.id = id;
    }

    public void add(EbmlBase elem) {
        if (elem != null) {
            elem.parent = this;
            this.children.add(elem);
        }
    }

    @Override
    public ByteBuffer getData() {
        long size = this.getDataLen();
        if (size > 2147483647L) {
            System.out.println("EbmlMaster.getData: id.length " + this.id.length + "  EbmlUtil.ebmlLength(" + size + "): " + EbmlUtil.ebmlLength(size) + " size: " + size);
        }
        ByteBuffer bb = ByteBuffer.allocate((int) ((long) (this.id.length + EbmlUtil.ebmlLength(size)) + size));
        bb.put(this.id);
        bb.put(EbmlUtil.ebmlEncode(size));
        for (int i = 0; i < this.children.size(); i++) {
            bb.put(((EbmlBase) this.children.get(i)).getData());
        }
        bb.flip();
        return bb;
    }

    protected long getDataLen() {
        if (this.children != null && !this.children.isEmpty()) {
            long dataLength = 0L;
            for (EbmlBase e : this.children) {
                dataLength += e.size();
            }
            return dataLength;
        } else {
            return (long) this.dataLen;
        }
    }

    @Override
    public long size() {
        long size = this.getDataLen();
        size += (long) EbmlUtil.ebmlLength(size);
        return size + (long) this.id.length;
    }
}