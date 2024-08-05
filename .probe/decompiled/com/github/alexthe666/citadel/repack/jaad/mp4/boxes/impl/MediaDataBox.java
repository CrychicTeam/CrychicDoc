package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class MediaDataBox extends BoxImpl {

    public MediaDataBox() {
        super("Media Data Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
    }
}