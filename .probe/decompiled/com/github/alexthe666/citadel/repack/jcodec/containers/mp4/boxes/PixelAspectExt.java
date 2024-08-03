package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import java.nio.ByteBuffer;

public class PixelAspectExt extends Box {

    private int hSpacing;

    private int vSpacing;

    public PixelAspectExt(Header header) {
        super(header);
    }

    public static PixelAspectExt createPixelAspectExt(Rational par) {
        PixelAspectExt pasp = new PixelAspectExt(new Header(fourcc()));
        pasp.hSpacing = par.getNum();
        pasp.vSpacing = par.getDen();
        return pasp;
    }

    @Override
    public void parse(ByteBuffer input) {
        this.hSpacing = input.getInt();
        this.vSpacing = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.putInt(this.hSpacing);
        out.putInt(this.vSpacing);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public int gethSpacing() {
        return this.hSpacing;
    }

    public int getvSpacing() {
        return this.vSpacing;
    }

    public Rational getRational() {
        return new Rational(this.hSpacing, this.vSpacing);
    }

    public static String fourcc() {
        return "pasp";
    }
}