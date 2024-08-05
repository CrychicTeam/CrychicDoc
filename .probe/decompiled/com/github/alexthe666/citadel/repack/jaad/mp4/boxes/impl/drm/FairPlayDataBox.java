package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.drm;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public class FairPlayDataBox extends BoxImpl {

    private byte[] data;

    public FairPlayDataBox() {
        super("iTunes FairPlay Data Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.data = new byte[(int) this.getLeft(in)];
        in.readBytes(this.data);
    }

    public byte[] getData() {
        return this.data;
    }
}