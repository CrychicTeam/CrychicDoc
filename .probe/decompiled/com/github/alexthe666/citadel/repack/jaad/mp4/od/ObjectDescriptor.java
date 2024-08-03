package com.github.alexthe666.citadel.repack.jaad.mp4.od;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class ObjectDescriptor extends Descriptor {

    private int objectDescriptorID;

    private boolean urlPresent;

    private String url;

    @Override
    void decode(MP4InputStream in) throws IOException {
        int x = (int) in.readBytes(2);
        this.objectDescriptorID = x >> 6 & 1023;
        this.urlPresent = (x >> 5 & 1) == 1;
        if (this.urlPresent) {
            this.url = in.readString(this.size - 2);
        }
        this.readChildren(in);
    }

    public int getObjectDescriptorID() {
        return this.objectDescriptorID;
    }

    public boolean isURLPresent() {
        return this.urlPresent;
    }

    public String getURL() {
        return this.url;
    }
}