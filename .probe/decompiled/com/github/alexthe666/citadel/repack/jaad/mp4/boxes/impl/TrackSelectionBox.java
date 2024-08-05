package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackSelectionBox extends FullBox {

    private long switchGroup;

    private final List<Long> attributes = new ArrayList();

    public TrackSelectionBox() {
        super("Track Selection Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.switchGroup = in.readBytes(4);
        while (this.getLeft(in) > 3L) {
            this.attributes.add(in.readBytes(4));
        }
    }

    public long getSwitchGroup() {
        return this.switchGroup;
    }

    public List<Long> getAttributes() {
        return Collections.unmodifiableList(this.attributes);
    }
}