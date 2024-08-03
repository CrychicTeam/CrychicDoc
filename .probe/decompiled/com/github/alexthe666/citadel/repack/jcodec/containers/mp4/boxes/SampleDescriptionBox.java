package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class SampleDescriptionBox extends NodeBox {

    public static String fourcc() {
        return "stsd";
    }

    public static SampleDescriptionBox createSampleDescriptionBox(SampleEntry[] entries) {
        SampleDescriptionBox box = new SampleDescriptionBox(new Header(fourcc()));
        for (int i = 0; i < entries.length; i++) {
            SampleEntry e = entries[i];
            box.boxes.add(e);
        }
        return box;
    }

    public SampleDescriptionBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        input.getInt();
        input.getInt();
        super.parse(input);
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.putInt(0);
        out.putInt(Math.max(1, this.boxes.size()));
        super.doWrite(out);
    }

    @Override
    public int estimateSize() {
        return 8 + super.estimateSize();
    }
}