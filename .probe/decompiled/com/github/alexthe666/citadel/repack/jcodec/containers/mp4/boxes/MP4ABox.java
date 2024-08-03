package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class MP4ABox extends Box {

    private int val;

    public MP4ABox(Header header) {
        super(header);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putInt(this.val);
    }

    @Override
    public void parse(ByteBuffer input) {
        this.val = input.getInt();
    }

    @Override
    public int estimateSize() {
        return 12;
    }
}