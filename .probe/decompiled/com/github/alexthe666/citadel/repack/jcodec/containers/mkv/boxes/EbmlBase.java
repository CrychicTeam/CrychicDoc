package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.MKVType;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class EbmlBase {

    protected EbmlMaster parent;

    public MKVType type;

    public byte[] id;

    public int dataLen = 0;

    public long offset;

    public long dataOffset;

    public int typeSizeLength;

    @UsedViaReflection
    public EbmlBase(byte[] id) {
        this.id = id;
    }

    public boolean equalId(byte[] typeId) {
        return Platform.arrayEqualsByte(this.id, typeId);
    }

    public abstract ByteBuffer getData();

    public long size() {
        return (long) (this.dataLen + EbmlUtil.ebmlLength((long) this.dataLen) + this.id.length);
    }

    public long mux(SeekableByteChannel os) throws IOException {
        ByteBuffer bb = this.getData();
        return (long) os.write(bb);
    }
}