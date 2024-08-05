package com.github.alexthe666.citadel.repack.jcodec.api.transcode.filters;

import com.github.alexthe666.citadel.repack.jcodec.api.transcode.Filter;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.PixelStore;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class CropFilter implements Filter {

    @Override
    public PixelStore.LoanerPicture filter(Picture picture, PixelStore store) {
        return null;
    }

    @Override
    public ColorSpace getInputColor() {
        return null;
    }

    @Override
    public ColorSpace getOutputColor() {
        return null;
    }
}