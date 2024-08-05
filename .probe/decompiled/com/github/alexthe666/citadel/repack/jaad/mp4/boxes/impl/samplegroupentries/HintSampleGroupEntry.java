package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.samplegroupentries;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class HintSampleGroupEntry extends SampleGroupDescriptionEntry {

    public HintSampleGroupEntry() {
        super("Hint Sample Group Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
    }
}