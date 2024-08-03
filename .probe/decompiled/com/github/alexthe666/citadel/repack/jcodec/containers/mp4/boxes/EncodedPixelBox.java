package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class EncodedPixelBox extends ClearApertureBox {

    public static final String ENOF = "enof";

    public static EncodedPixelBox createEncodedPixelBox(int width, int height) {
        EncodedPixelBox enof = new EncodedPixelBox(new Header("enof"));
        enof.width = (float) width;
        enof.height = (float) height;
        return enof;
    }

    public EncodedPixelBox(Header atom) {
        super(atom);
    }
}