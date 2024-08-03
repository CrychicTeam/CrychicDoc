package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class ChunkOffsetsBox extends FullBox {

    private long[] chunkOffsets;

    public ChunkOffsetsBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "stco";
    }

    public static ChunkOffsetsBox createChunkOffsetsBox(long[] chunkOffsets) {
        ChunkOffsetsBox stco = new ChunkOffsetsBox(new Header(fourcc()));
        stco.chunkOffsets = chunkOffsets;
        return stco;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        int length = input.getInt();
        this.chunkOffsets = new long[length];
        for (int i = 0; i < length; i++) {
            this.chunkOffsets[i] = Platform.unsignedInt(input.getInt());
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.chunkOffsets.length);
        for (int i = 0; i < this.chunkOffsets.length; i++) {
            long offset = this.chunkOffsets[i];
            out.putInt((int) offset);
        }
    }

    @Override
    public int estimateSize() {
        return 16 + this.chunkOffsets.length * 4;
    }

    public long[] getChunkOffsets() {
        return this.chunkOffsets;
    }

    public void setChunkOffsets(long[] chunkOffsets) {
        this.chunkOffsets = chunkOffsets;
    }
}