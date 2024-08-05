package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChapterBox extends FullBox {

    private final Map<Long, String> chapters = new HashMap();

    public ChapterBox() {
        super("Chapter Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        in.skipBytes(4L);
        int count = in.read();
        for (int i = 0; i < count; i++) {
            long timestamp = in.readBytes(8);
            int len = in.read();
            String name = in.readString(len);
            this.chapters.put(timestamp, name);
        }
    }

    public Map<Long, String> getChapters() {
        return this.chapters;
    }
}