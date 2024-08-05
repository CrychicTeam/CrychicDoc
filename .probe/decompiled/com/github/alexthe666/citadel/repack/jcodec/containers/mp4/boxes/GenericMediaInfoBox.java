package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class GenericMediaInfoBox extends FullBox {

    private short graphicsMode;

    private short rOpColor;

    private short gOpColor;

    private short bOpColor;

    private short balance;

    public static String fourcc() {
        return "gmin";
    }

    public static GenericMediaInfoBox createGenericMediaInfoBox() {
        return new GenericMediaInfoBox(new Header(fourcc()));
    }

    public GenericMediaInfoBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.graphicsMode = input.getShort();
        this.rOpColor = input.getShort();
        this.gOpColor = input.getShort();
        this.bOpColor = input.getShort();
        this.balance = input.getShort();
        input.getShort();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(this.graphicsMode);
        out.putShort(this.rOpColor);
        out.putShort(this.gOpColor);
        out.putShort(this.bOpColor);
        out.putShort(this.balance);
        out.putShort((short) 0);
    }

    @Override
    public int estimateSize() {
        return 24;
    }
}