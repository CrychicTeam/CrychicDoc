package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class MovieFragmentHeaderBox extends FullBox {

    private int sequenceNumber;

    public MovieFragmentHeaderBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "mfhd";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.sequenceNumber = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.sequenceNumber);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public int getSequenceNumber() {
        return this.sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public static MovieFragmentHeaderBox createMovieFragmentHeaderBox() {
        return new MovieFragmentHeaderBox(new Header(fourcc()));
    }
}