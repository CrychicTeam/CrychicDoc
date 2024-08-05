package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.awt.Color;
import java.io.IOException;

public class VideoMediaHeaderBox extends FullBox {

    private long graphicsMode;

    private Color color;

    public VideoMediaHeaderBox() {
        super("Video Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.graphicsMode = in.readBytes(2);
        int[] c = new int[3];
        for (int i = 0; i < 3; i++) {
            c[i] = in.read() & 0xFF | in.read() << 8 & 0xFF;
        }
        this.color = new Color(c[0], c[1], c[2]);
    }

    public long getGraphicsMode() {
        return this.graphicsMode;
    }

    public Color getColor() {
        return this.color;
    }
}