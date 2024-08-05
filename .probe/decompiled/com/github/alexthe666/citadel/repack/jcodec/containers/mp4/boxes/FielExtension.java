package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class FielExtension extends Box {

    private int type;

    private int order;

    public FielExtension(Header header) {
        super(header);
    }

    public static String fourcc() {
        return "fiel";
    }

    public boolean isInterlaced() {
        return this.type == 2;
    }

    public boolean topFieldFirst() {
        return this.order == 1 || this.order == 6;
    }

    public String getOrderInterpretation() {
        if (this.isInterlaced()) {
            switch(this.order) {
                case 1:
                    return "top";
                case 6:
                    return "bottom";
                case 9:
                    return "bottomtop";
                case 14:
                    return "topbottom";
            }
        }
        return "";
    }

    @Override
    public void parse(ByteBuffer input) {
        this.type = input.get() & 255;
        if (this.isInterlaced()) {
            this.order = input.get() & 255;
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.put((byte) this.type);
        out.put((byte) this.order);
    }

    @Override
    public int estimateSize() {
        return 10;
    }
}