package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class SchemeTypeBox extends FullBox {

    public static final long ITUNES_SCHEME = 1769239918L;

    private long schemeType;

    private long schemeVersion;

    private String schemeURI;

    public SchemeTypeBox() {
        super("Scheme Type Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.schemeType = in.readBytes(4);
        this.schemeVersion = in.readBytes(4);
        this.schemeURI = (this.flags & 1) == 1 ? in.readUTFString((int) this.getLeft(in), "UTF-8") : null;
    }

    public long getSchemeType() {
        return this.schemeType;
    }

    public long getSchemeVersion() {
        return this.schemeVersion;
    }

    public String getSchemeURI() {
        return this.schemeURI;
    }
}