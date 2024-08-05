package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class SyncSamplesBox extends FullBox {

    public static final String STSS = "stss";

    protected int[] syncSamples;

    public static SyncSamplesBox createSyncSamplesBox(int[] array) {
        SyncSamplesBox stss = new SyncSamplesBox(new Header("stss"));
        stss.syncSamples = array;
        return stss;
    }

    public SyncSamplesBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        int len = input.getInt();
        this.syncSamples = new int[len];
        for (int i = 0; i < len; i++) {
            this.syncSamples[i] = input.getInt();
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.syncSamples.length);
        for (int i = 0; i < this.syncSamples.length; i++) {
            out.putInt(this.syncSamples[i]);
        }
    }

    @Override
    public int estimateSize() {
        return 16 + this.syncSamples.length * 4;
    }

    public int[] getSyncSamples() {
        return this.syncSamples;
    }
}