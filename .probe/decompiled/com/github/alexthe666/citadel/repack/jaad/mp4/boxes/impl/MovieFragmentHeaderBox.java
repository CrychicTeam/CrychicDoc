package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class MovieFragmentHeaderBox extends FullBox {

    private long sequenceNumber;

    public MovieFragmentHeaderBox() {
        super("Movie Fragment Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.sequenceNumber = in.readBytes(4);
    }

    public long getSequenceNumber() {
        return this.sequenceNumber;
    }
}