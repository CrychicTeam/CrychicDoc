package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.oma;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class OMAContentIDBox extends FullBox {

    private String contentID;

    public OMAContentIDBox() {
        super("OMA DRM Content ID Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int len = (int) in.readBytes(2);
        this.contentID = in.readString(len);
    }

    public String getContentID() {
        return this.contentID;
    }
}