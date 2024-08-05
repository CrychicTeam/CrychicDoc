package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class NameBox extends Box {

    private String name;

    public static String fourcc() {
        return "name";
    }

    public static NameBox createNameBox(String name) {
        NameBox box = new NameBox(new Header(fourcc()));
        box.name = name;
        return box;
    }

    public NameBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        this.name = NIOUtils.readNullTermString(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.put(JCodecUtil2.asciiString(this.name));
        out.putInt(0);
    }

    @Override
    public int estimateSize() {
        return 12 + JCodecUtil2.asciiString(this.name).length;
    }

    public String getName() {
        return this.name;
    }
}