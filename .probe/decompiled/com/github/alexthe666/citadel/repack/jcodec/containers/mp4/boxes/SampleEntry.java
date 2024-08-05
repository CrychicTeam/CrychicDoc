package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class SampleEntry extends NodeBox {

    protected short drefInd;

    public SampleEntry(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        input.getInt();
        input.getShort();
        this.drefInd = input.getShort();
    }

    protected void parseExtensions(ByteBuffer input) {
        super.parse(input);
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        out.put(new byte[] { 0, 0, 0, 0, 0, 0 });
        out.putShort(this.drefInd);
    }

    protected void writeExtensions(ByteBuffer out) {
        super.doWrite(out);
    }

    public short getDrefInd() {
        return this.drefInd;
    }

    public void setDrefInd(short ind) {
        this.drefInd = ind;
    }

    public void setMediaType(String mediaType) {
        this.header = new Header(mediaType);
    }

    @Override
    public int estimateSize() {
        return 8 + super.estimateSize();
    }
}