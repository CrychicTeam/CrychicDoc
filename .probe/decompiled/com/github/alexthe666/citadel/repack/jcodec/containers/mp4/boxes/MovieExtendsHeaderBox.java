package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class MovieExtendsHeaderBox extends FullBox {

    private int fragmentDuration;

    public MovieExtendsHeaderBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "mehd";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.fragmentDuration = input.getInt();
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(this.fragmentDuration);
    }

    @Override
    public int estimateSize() {
        return 16;
    }

    public int getFragmentDuration() {
        return this.fragmentDuration;
    }

    public void setFragmentDuration(int fragmentDuration) {
        this.fragmentDuration = fragmentDuration;
    }

    public static MovieExtendsHeaderBox createMovieExtendsHeaderBox() {
        return new MovieExtendsHeaderBox(new Header(fourcc()));
    }
}