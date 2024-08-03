package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class BinaryXMLBox extends FullBox {

    private byte[] data;

    public BinaryXMLBox() {
        super("Binary XML Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.data = new byte[(int) this.getLeft(in)];
        in.readBytes(this.data);
    }

    public byte[] getData() {
        return this.data;
    }
}