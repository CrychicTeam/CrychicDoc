package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.BoxImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrackReferenceBox extends BoxImpl {

    private String referenceType;

    private List<Long> trackIDs = new ArrayList();

    public TrackReferenceBox() {
        super("Track Reference Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.referenceType = in.readString(4);
        while (this.getLeft(in) > 3L) {
            this.trackIDs.add(in.readBytes(4));
        }
    }

    public String getReferenceType() {
        return this.referenceType;
    }

    public List<Long> getTrackIDs() {
        return Collections.unmodifiableList(this.trackIDs);
    }
}