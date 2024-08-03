package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EbmlBin extends EbmlBase {

    public ByteBuffer data;

    protected boolean dataRead = false;

    public EbmlBin(byte[] id) {
        super(id);
    }

    public void readChannel(SeekableByteChannel is) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(this.dataLen);
        is.read(bb);
        bb.flip();
        this.read(bb);
    }

    public void read(ByteBuffer source) {
        this.data = source.slice();
        this.data.limit(this.dataLen);
        this.dataRead = true;
    }

    public void skip(ByteBuffer source) {
        if (!this.dataRead) {
            source.position((int) (this.dataOffset + (long) this.dataLen));
            this.dataRead = true;
        }
    }

    @Override
    public long size() {
        if (this.data != null && this.data.limit() != 0) {
            long totalSize = (long) this.data.limit();
            totalSize += (long) EbmlUtil.ebmlLength((long) this.data.limit());
            return totalSize + (long) this.id.length;
        } else {
            return super.size();
        }
    }

    public void setBuf(ByteBuffer data) {
        this.data = data.slice();
        this.dataLen = this.data.limit();
    }

    @Override
    public ByteBuffer getData() {
        int sizeSize = EbmlUtil.ebmlLength((long) this.data.limit());
        byte[] size = EbmlUtil.ebmlEncodeLen((long) this.data.limit(), sizeSize);
        ByteBuffer bb = ByteBuffer.allocate(this.id.length + sizeSize + this.data.limit());
        bb.put(this.id);
        bb.put(size);
        bb.put(this.data);
        bb.flip();
        this.data.flip();
        return bb;
    }
}