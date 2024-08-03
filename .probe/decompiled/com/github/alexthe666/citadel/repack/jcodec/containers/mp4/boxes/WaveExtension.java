package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class WaveExtension extends NodeBox {

    public static String fourcc() {
        return "wave";
    }

    public WaveExtension(Header atom) {
        super(atom);
    }
}