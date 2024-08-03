package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class DataRefBox extends NodeBox {

    public static String fourcc() {
        return "dref";
    }

    public static DataRefBox createDataRefBox() {
        return new DataRefBox(new Header(fourcc()));
    }

    public DataRefBox(Header atom) {
        super(atom);
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
        out.putInt(this.boxes.size());
        super.doWrite(out);
    }

    @Override
    public int estimateSize() {
        return 8 + super.estimateSize();
    }
}