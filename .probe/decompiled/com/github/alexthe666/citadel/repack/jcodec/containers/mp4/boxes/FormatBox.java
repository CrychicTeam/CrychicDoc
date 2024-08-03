package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class FormatBox extends Box {

    private String fmt;

    public FormatBox(Header header) {
        super(header);
    }

    public static String fourcc() {
        return "frma";
    }

    public static FormatBox createFormatBox(String fmt) {
        FormatBox frma = new FormatBox(new Header(fourcc()));
        frma.fmt = fmt;
        return frma;
    }

    @Override
    public void parse(ByteBuffer input) {
        this.fmt = NIOUtils.readString(input, 4);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.put(JCodecUtil2.asciiString(this.fmt));
    }

    @Override
    public int estimateSize() {
        return JCodecUtil2.asciiString(this.fmt).length + 8;
    }
}