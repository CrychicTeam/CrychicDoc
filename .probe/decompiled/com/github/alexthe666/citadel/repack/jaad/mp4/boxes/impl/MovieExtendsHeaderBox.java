package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class MovieExtendsHeaderBox extends FullBox {

    private long fragmentDuration;

    public MovieExtendsHeaderBox() {
        super("Movie Extends Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int len = this.version == 1 ? 8 : 4;
        this.fragmentDuration = in.readBytes(len);
    }

    public long getFragmentDuration() {
        return this.fragmentDuration;
    }
}