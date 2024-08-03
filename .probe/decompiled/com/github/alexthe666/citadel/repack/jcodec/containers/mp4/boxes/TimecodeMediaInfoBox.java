package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import java.nio.ByteBuffer;

public class TimecodeMediaInfoBox extends FullBox {

    private short font;

    private short face;

    private short size;

    private short[] color = new short[3];

    private short[] bgcolor = new short[3];

    private String name;

    public static String fourcc() {
        return "tcmi";
    }

    public static TimecodeMediaInfoBox createTimecodeMediaInfoBox(short font, short face, short size, short[] color, short[] bgcolor, String name) {
        TimecodeMediaInfoBox box = new TimecodeMediaInfoBox(new Header(fourcc()));
        box.font = font;
        box.face = face;
        box.size = size;
        box.color = color;
        box.bgcolor = bgcolor;
        box.name = name;
        return box;
    }

    public TimecodeMediaInfoBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.font = input.getShort();
        this.face = input.getShort();
        this.size = input.getShort();
        input.getShort();
        this.color[0] = input.getShort();
        this.color[1] = input.getShort();
        this.color[2] = input.getShort();
        this.bgcolor[0] = input.getShort();
        this.bgcolor[1] = input.getShort();
        this.bgcolor[2] = input.getShort();
        this.name = NIOUtils.readPascalString(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(this.font);
        out.putShort(this.face);
        out.putShort(this.size);
        out.putShort((short) 0);
        out.putShort(this.color[0]);
        out.putShort(this.color[1]);
        out.putShort(this.color[2]);
        out.putShort(this.bgcolor[0]);
        out.putShort(this.bgcolor[1]);
        out.putShort(this.bgcolor[2]);
        NIOUtils.writePascalString(out, this.name);
    }

    @Override
    public int estimateSize() {
        return 33 + NIOUtils.asciiString(this.name).length;
    }
}