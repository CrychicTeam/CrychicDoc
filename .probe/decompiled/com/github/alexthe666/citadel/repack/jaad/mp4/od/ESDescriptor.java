package com.github.alexthe666.citadel.repack.jaad.mp4.od;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class ESDescriptor extends Descriptor {

    private int esID;

    private int streamPriority;

    private int dependingOnES_ID;

    private boolean streamDependency;

    private boolean urlPresent;

    private boolean ocrPresent;

    private String url;

    @Override
    void decode(MP4InputStream in) throws IOException {
        this.esID = (int) in.readBytes(2);
        int flags = in.read();
        this.streamDependency = (flags >> 7 & 1) == 1;
        this.urlPresent = (flags >> 6 & 1) == 1;
        this.streamPriority = flags & 31;
        if (this.streamDependency) {
            this.dependingOnES_ID = (int) in.readBytes(2);
        } else {
            this.dependingOnES_ID = -1;
        }
        if (this.urlPresent) {
            int len = in.read();
            this.url = in.readString(len);
        }
        this.readChildren(in);
    }

    public int getES_ID() {
        return this.esID;
    }

    public boolean hasStreamDependency() {
        return this.streamDependency;
    }

    public int getDependingOnES_ID() {
        return this.dependingOnES_ID;
    }

    public boolean isURLPresent() {
        return this.urlPresent;
    }

    public String getURL() {
        return this.url;
    }

    public int getStreamPriority() {
        return this.streamPriority;
    }
}