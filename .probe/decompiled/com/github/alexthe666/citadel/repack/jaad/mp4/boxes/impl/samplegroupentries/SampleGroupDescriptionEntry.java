package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.samplegroupentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;

public abstract class SampleGroupDescriptionEntry extends BoxImpl {

    protected SampleGroupDescriptionEntry(String name) {
        super(name);
    }

    @Override
    public abstract void decode(MP4InputStream var1) throws IOException;
}