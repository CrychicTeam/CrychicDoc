package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ID3Tag {

    private static final int ID3_TAG = 4801587;

    private static final int SUPPORTED_VERSION = 4;

    private final List<ID3Frame> frames = new ArrayList();

    private final int tag;

    private final int flags;

    private final int len;

    ID3Tag(DataInputStream in) throws IOException {
        this.tag = in.read() << 16 | in.read() << 8 | in.read();
        int majorVersion = in.read();
        in.read();
        this.flags = in.read();
        this.len = readSynch(in);
        if (this.tag == 4801587 && majorVersion <= 4) {
            if ((this.flags & 64) == 64) {
                int extSize = readSynch(in);
                in.skipBytes(extSize - 6);
            }
            int left = this.len;
            while (left > 0) {
                ID3Frame frame = new ID3Frame(in);
                this.frames.add(frame);
                left = (int) ((long) left - frame.getSize());
            }
        }
    }

    public List<ID3Frame> getFrames() {
        return Collections.unmodifiableList(this.frames);
    }

    static int readSynch(DataInputStream in) throws IOException {
        int x = 0;
        for (int i = 0; i < 4; i++) {
            x |= in.read() & 127;
        }
        return x;
    }
}