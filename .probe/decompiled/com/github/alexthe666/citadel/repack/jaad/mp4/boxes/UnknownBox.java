package com.github.alexthe666.citadel.repack.jaad.mp4.boxes;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

class UnknownBox extends BoxImpl {

    UnknownBox() {
        super("unknown");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
    }
}