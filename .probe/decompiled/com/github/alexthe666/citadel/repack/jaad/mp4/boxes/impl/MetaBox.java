package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class MetaBox extends FullBox {

    public MetaBox() {
        super("Meta Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        long possibleType = in.peekBytes(8) & 4294967295L;
        if (possibleType != 1751411826L) {
            super.decode(in);
        }
        this.readChildren(in);
    }
}