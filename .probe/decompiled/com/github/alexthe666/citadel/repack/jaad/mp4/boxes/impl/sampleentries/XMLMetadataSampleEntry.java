package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class XMLMetadataSampleEntry extends MetadataSampleEntry {

    private String namespace;

    private String schemaLocation;

    public XMLMetadataSampleEntry() {
        super("XML Metadata Sample Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.namespace = in.readUTFString((int) this.getLeft(in), "UTF-8");
        this.schemaLocation = in.readUTFString((int) this.getLeft(in), "UTF-8");
    }

    public String getNamespace() {
        return this.namespace;
    }

    public String getSchemaLocation() {
        return this.schemaLocation;
    }
}