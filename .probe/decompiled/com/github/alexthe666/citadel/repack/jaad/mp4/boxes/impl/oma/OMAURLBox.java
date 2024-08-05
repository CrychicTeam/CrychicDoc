package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.oma;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class OMAURLBox extends FullBox {

    private String content;

    public OMAURLBox(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        byte[] b = new byte[(int) this.getLeft(in)];
        in.readBytes(b);
        this.content = new String(b, "UTF-8");
    }

    public String getContent() {
        return this.content;
    }
}