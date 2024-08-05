package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NeroMetadataTagsBox extends BoxImpl {

    private final Map<String, String> pairs = new HashMap();

    public NeroMetadataTagsBox() {
        super("Nero Metadata Tags Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        in.skipBytes(12L);
        while (this.getLeft(in) > 0L && in.read() == 128) {
            in.skipBytes(2L);
            String key = in.readUTFString((int) this.getLeft(in), "UTF-8");
            in.skipBytes(4L);
            int len = in.read();
            String val = in.readString(len);
            this.pairs.put(key, val);
        }
    }

    public Map<String, String> getPairs() {
        return this.pairs;
    }
}