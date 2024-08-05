package com.github.alexthe666.citadel.repack.jaad.adts;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class ADTSDemultiplexer {

    private static final int MAXIMUM_FRAME_SIZE = 6144;

    private PushbackInputStream in;

    private DataInputStream din;

    private boolean first;

    private ADTSFrame frame;

    public ADTSDemultiplexer(InputStream in) throws IOException {
        this.in = new PushbackInputStream(in);
        this.din = new DataInputStream(this.in);
        this.first = true;
        if (!this.findNextFrame()) {
            throw new IOException("no ADTS header found");
        }
    }

    public byte[] getDecoderSpecificInfo() {
        return this.frame.createDecoderSpecificInfo();
    }

    public byte[] readNextFrame() throws IOException {
        if (this.first) {
            this.first = false;
        } else {
            this.findNextFrame();
        }
        byte[] b = new byte[this.frame.getFrameLength()];
        this.din.readFully(b);
        return b;
    }

    private boolean findNextFrame() throws IOException {
        boolean found = false;
        int left = 6144;
        while (!found && left > 0) {
            int i = this.in.read();
            left--;
            if (i == 255) {
                i = this.in.read();
                if ((i & 246) == 240) {
                    found = true;
                }
                this.in.unread(i);
            }
        }
        if (found) {
            this.frame = new ADTSFrame(this.din);
        }
        return found;
    }

    public int getSampleFrequency() {
        return this.frame.getSampleFrequency();
    }

    public int getChannelCount() {
        return this.frame.getChannelCount();
    }
}