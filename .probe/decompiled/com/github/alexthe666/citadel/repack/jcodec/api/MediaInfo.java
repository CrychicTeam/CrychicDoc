package com.github.alexthe666.citadel.repack.jcodec.api;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public class MediaInfo {

    private Size dim;

    public MediaInfo(Size dim) {
        this.dim = dim;
    }

    public Size getDim() {
        return this.dim;
    }

    public void setDim(Size dim) {
        this.dim = dim;
    }
}