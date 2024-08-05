package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class SampleSizesBox extends FullBox {

    private int defaultSize;

    private int count;

    private int[] sizes;

    public SampleSizesBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "stsz";
    }

    public static SampleSizesBox createSampleSizesBox(int defaultSize, int count) {
        SampleSizesBox stsz = new SampleSizesBox(new Header(fourcc()));
        stsz.defaultSize = defaultSize;
        stsz.count = count;
        return stsz;
    }

    public static SampleSizesBox createSampleSizesBox2(int[] sizes) {
        SampleSizesBox stsz = new SampleSizesBox(new Header(fourcc()));
        stsz.sizes = sizes;
        stsz.count = sizes.length;
        return stsz;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.defaultSize = input.getInt();
        this.count = input.getInt();
        if (this.defaultSize == 0) {
            this.sizes = new int[this.count];
            for (int i = 0; i < this.count; i++) {
                this.sizes[i] = input.getInt();
            }
        }
    }

    public int getDefaultSize() {
        return this.defaultSize;
    }

    public int[] getSizes() {
        return this.sizes;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.defaultSize);
        if (this.defaultSize == 0) {
            out.putInt(this.count);
            for (int i = 0; i < this.sizes.length; i++) {
                long size = (long) this.sizes[i];
                out.putInt((int) size);
            }
        } else {
            out.putInt(this.count);
        }
    }

    @Override
    public int estimateSize() {
        return (this.defaultSize == 0 ? this.sizes.length * 4 : 0) + 20;
    }

    public void setSizes(int[] sizes) {
        this.sizes = sizes;
        this.count = sizes.length;
    }
}