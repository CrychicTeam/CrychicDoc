package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

abstract class MetadataSampleEntry extends SampleEntry {

    private String contentEncoding;

    MetadataSampleEntry(String name) {
        super(name);
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.contentEncoding = in.readUTFString((int) this.getLeft(in), "UTF-8");
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }
}