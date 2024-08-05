package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class FreeSpaceBox extends BoxImpl {

    public FreeSpaceBox() {
        super("Free Space Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
    }
}