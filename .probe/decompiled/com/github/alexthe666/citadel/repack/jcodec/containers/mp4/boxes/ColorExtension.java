package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.JCodecUtil2;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class ColorExtension extends Box {

    private short primariesIndex;

    private short transferFunctionIndex;

    private short matrixIndex;

    private String type = "nclc";

    static final byte RANGE_UNSPECIFIED = 0;

    static final byte AVCOL_RANGE_MPEG = 1;

    static final byte AVCOL_RANGE_JPEG = 2;

    private Byte colorRange = null;

    public ColorExtension(Header header) {
        super(header);
    }

    public void setColorRange(Byte colorRange) {
        this.colorRange = colorRange;
    }

    @Override
    public void parse(ByteBuffer input) {
        byte[] dst = new byte[4];
        input.get(dst);
        this.type = Platform.stringFromBytes(dst);
        this.primariesIndex = input.getShort();
        this.transferFunctionIndex = input.getShort();
        this.matrixIndex = input.getShort();
        if (input.hasRemaining()) {
            this.colorRange = input.get();
        }
    }

    @Override
    public void doWrite(ByteBuffer out) {
        out.put(JCodecUtil2.asciiString(this.type));
        out.putShort(this.primariesIndex);
        out.putShort(this.transferFunctionIndex);
        out.putShort(this.matrixIndex);
        if (this.colorRange != null) {
            out.put(this.colorRange);
        }
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public static String fourcc() {
        return "colr";
    }

    public static ColorExtension createColorExtension(short primariesIndex, short transferFunctionIndex, short matrixIndex) {
        ColorExtension c = new ColorExtension(new Header(fourcc()));
        c.primariesIndex = primariesIndex;
        c.transferFunctionIndex = transferFunctionIndex;
        c.matrixIndex = matrixIndex;
        return c;
    }

    public static ColorExtension createColr() {
        return new ColorExtension(new Header(fourcc()));
    }

    public short getPrimariesIndex() {
        return this.primariesIndex;
    }

    public short getTransferFunctionIndex() {
        return this.transferFunctionIndex;
    }

    public short getMatrixIndex() {
        return this.matrixIndex;
    }
}