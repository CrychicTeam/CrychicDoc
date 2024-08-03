package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.oma;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class OMADiscreteMediaHeadersBox extends FullBox {

    private String contentType;

    public OMADiscreteMediaHeadersBox() {
        super("OMA DRM Discrete Media Headers Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int len = in.read();
        this.contentType = in.readString(len);
        this.readChildren(in);
    }

    public String getContentType() {
        return this.contentType;
    }
}