package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Utils;
import java.io.IOException;

public class ThreeGPPMetadataBox extends FullBox {

    private String languageCode;

    private String data;

    public ThreeGPPMetadataBox(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.decodeCommon(in);
        this.data = in.readUTFString((int) this.getLeft(in));
    }

    protected void decodeCommon(MP4InputStream in) throws IOException {
        super.decode(in);
        this.languageCode = Utils.getLanguageCode(in.readBytes(2));
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public String getData() {
        return this.data;
    }
}