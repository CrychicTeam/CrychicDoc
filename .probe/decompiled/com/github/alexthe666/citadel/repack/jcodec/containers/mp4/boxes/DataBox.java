package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class DataBox extends Box {

    private static final String FOURCC = "data";

    private int type;

    private int locale;

    private byte[] data;

    public DataBox(Header header) {
        super(header);
    }

    public static DataBox createDataBox(int type, int locale, byte[] data) {
        DataBox box = new DataBox(Header.createHeader("data", 0L));
        box.type = type;
        box.locale = locale;
        box.data = data;
        return box;
    }

    @Override
    public void parse(ByteBuffer buf) {
        this.type = buf.getInt();
        this.locale = buf.getInt();
        this.data = NIOUtils.toArray(NIOUtils.readBuf(buf));
    }

    public int getType() {
        return this.type;
    }

    public int getLocale() {
        return this.locale;
    }

    public byte[] getData() {
        return this.data;
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putInt(this.type);
        out.putInt(this.locale);
        out.put(this.data);
    }

    @Override
    public int estimateSize() {
        return 16 + this.data.length;
    }

    public static String fourcc() {
        return "data";
    }
}