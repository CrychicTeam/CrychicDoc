package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProgressiveDownloadInformationBox extends FullBox {

    private Map<Long, Long> pairs = new HashMap();

    public ProgressiveDownloadInformationBox() {
        super("Progressive Download Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        while (this.getLeft(in) > 0L) {
            long rate = in.readBytes(4);
            long initialDelay = in.readBytes(4);
            this.pairs.put(rate, initialDelay);
        }
    }

    public Map<Long, Long> getInformationPairs() {
        return this.pairs;
    }
}