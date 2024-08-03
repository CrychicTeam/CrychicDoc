package com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.SeekableByteChannel;
import java.io.IOException;
import java.nio.ByteBuffer;

public class EbmlVoid extends EbmlBase {

    public EbmlVoid(byte[] id) {
        super(id);
    }

    @Override
    public ByteBuffer getData() {
        return null;
    }

    public void skip(SeekableByteChannel is) throws IOException {
        is.setPosition(this.dataOffset + (long) this.dataLen);
    }
}