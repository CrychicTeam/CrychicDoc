package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4.es;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public abstract class Descriptor {

    private int _tag;

    private int size;

    public Descriptor(int tag, int size) {
        this._tag = tag;
        this.size = size;
    }

    public void write(ByteBuffer out) {
        ByteBuffer fork = out.duplicate();
        NIOUtils.skip(out, 5);
        this.doWrite(out);
        int length = out.position() - fork.position() - 5;
        fork.put((byte) this._tag);
        JCodecUtil2.writeBER32(fork, length);
    }

    protected abstract void doWrite(ByteBuffer var1);

    int getTag() {
        return this._tag;
    }
}