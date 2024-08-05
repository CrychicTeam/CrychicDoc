package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Utils;
import java.io.IOException;

public class MediaHeaderBox extends FullBox {

    private long creationTime;

    private long modificationTime;

    private long timeScale;

    private long duration;

    private String language;

    public MediaHeaderBox() {
        super("Media Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int len = this.version == 1 ? 8 : 4;
        this.creationTime = in.readBytes(len);
        this.modificationTime = in.readBytes(len);
        this.timeScale = in.readBytes(4);
        this.duration = Utils.detectUndetermined(in.readBytes(len));
        this.language = Utils.getLanguageCode(in.readBytes(2));
        in.skipBytes(2L);
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getModificationTime() {
        return this.modificationTime;
    }

    public long getTimeScale() {
        return this.timeScale;
    }

    public long getDuration() {
        return this.duration;
    }

    public String getLanguage() {
        return this.language;
    }
}