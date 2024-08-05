package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class MediaBox extends NodeBox {

    public static String fourcc() {
        return "mdia";
    }

    public static MediaBox createMediaBox() {
        return new MediaBox(new Header(fourcc()));
    }

    public MediaBox(Header atom) {
        super(atom);
    }

    public MediaInfoBox getMinf() {
        return NodeBox.findFirst(this, MediaInfoBox.class, "minf");
    }
}