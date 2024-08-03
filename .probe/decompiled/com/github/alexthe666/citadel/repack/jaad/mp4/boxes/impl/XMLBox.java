package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class XMLBox extends FullBox {

    private String content;

    public XMLBox() {
        super("XML Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.content = in.readUTFString((int) this.getLeft(in));
    }

    public String getContent() {
        return this.content;
    }
}