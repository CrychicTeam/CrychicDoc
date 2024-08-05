package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;

public class RefPicMarkingIDR {

    boolean discardDecodedPics;

    boolean useForlongTerm;

    public RefPicMarkingIDR(boolean discardDecodedPics, boolean useForlongTerm) {
        this.discardDecodedPics = discardDecodedPics;
        this.useForlongTerm = useForlongTerm;
    }

    public boolean isDiscardDecodedPics() {
        return this.discardDecodedPics;
    }

    public boolean isUseForlongTerm() {
        return this.useForlongTerm;
    }

    public String toString() {
        return Platform.toJSON(this);
    }
}