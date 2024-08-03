package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class ChunkOffsets64Box extends FullBox {

    private long[] chunkOffsets;

    public static String fourcc() {
        return "co64";
    }

    public static ChunkOffsets64Box createChunkOffsets64Box(long[] offsets) {
        ChunkOffsets64Box co64 = new ChunkOffsets64Box(Header.createHeader(fourcc(), 0L));
        co64.chunkOffsets = offsets;
        return co64;
    }

    public ChunkOffsets64Box(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        int length = input.getInt();
        this.chunkOffsets = new long[length];
        for (int i = 0; i < length; i++) {
            this.chunkOffsets[i] = input.getLong();
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.chunkOffsets.length);
        for (int i = 0; i < this.chunkOffsets.length; i++) {
            long offset = this.chunkOffsets[i];
            out.putLong(offset);
        }
    }

    @Override
    public int estimateSize() {
        return 16 + this.chunkOffsets.length * 8;
    }

    public long[] getChunkOffsets() {
        return this.chunkOffsets;
    }

    public void setChunkOffsets(long[] chunkOffsets) {
        this.chunkOffsets = chunkOffsets;
    }
}