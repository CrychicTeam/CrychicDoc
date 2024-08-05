package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class TextMetadataSampleEntry extends MetadataSampleEntry {

    private String mimeType;

    public TextMetadataSampleEntry() {
        super("Text Metadata Sample Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.mimeType = in.readUTFString((int) this.getLeft(in), "UTF-8");
    }

    public String getMimeType() {
        return this.mimeType;
    }
}