package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class MovieExtendsBox extends NodeBox {

    public MovieExtendsBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "mvex";
    }

    public static MovieExtendsBox createMovieExtendsBox() {
        return new MovieExtendsBox(new Header(fourcc()));
    }
}