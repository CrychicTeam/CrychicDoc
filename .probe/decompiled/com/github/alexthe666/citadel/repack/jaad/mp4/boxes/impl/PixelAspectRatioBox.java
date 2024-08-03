package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class PixelAspectRatioBox extends BoxImpl {

    private long hSpacing;

    private long vSpacing;

    public PixelAspectRatioBox() {
        super("Pixel Aspect Ratio Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.hSpacing = in.readBytes(4);
        this.vSpacing = in.readBytes(4);
    }

    public long getHorizontalSpacing() {
        return this.hSpacing;
    }

    public long getVerticalSpacing() {
        return this.vSpacing;
    }
}