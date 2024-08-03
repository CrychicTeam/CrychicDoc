package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class SoundMediaHeaderBox extends FullBox {

    private short balance;

    public static String fourcc() {
        return "smhd";
    }

    public static SoundMediaHeaderBox createSoundMediaHeaderBox() {
        return new SoundMediaHeaderBox(new Header(fourcc()));
    }

    public SoundMediaHeaderBox(Header atom) {
        super(atom);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.balance = input.getShort();
        input.getShort();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putShort(this.balance);
        out.putShort((short) 0);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public short getBalance() {
        return this.balance;
    }
}