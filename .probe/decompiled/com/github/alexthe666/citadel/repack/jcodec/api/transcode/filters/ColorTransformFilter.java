package com.github.alexthe666.citadel.repack.jcodec.api.transcode.filters;

import com.github.alexthe666.citadel.repack.jcodec.api.transcode.Filter;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.PixelStore;
import com.github.alexthe666.citadel.repack.jcodec.common.logging.Logger;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.scale.ColorUtil;
import com.github.alexthe666.citadel.repack.jcodec.scale.Transform;

public class ColorTransformFilter implements Filter {

    private Transform transform;

    private ColorSpace outputColor;

    public ColorTransformFilter(ColorSpace outputColor) {
        this.outputColor = outputColor;
    }

    @Override
    public PixelStore.LoanerPicture filter(Picture picture, PixelStore store) {
        if (this.transform == null) {
            this.transform = ColorUtil.getTransform(picture.getColor(), this.outputColor);
            Logger.debug("Creating transform: " + this.transform);
        }
        PixelStore.LoanerPicture outFrame = store.getPicture(picture.getWidth(), picture.getHeight(), this.outputColor);
        outFrame.getPicture().setCrop(picture.getCrop());
        this.transform.transform(picture, outFrame.getPicture());
        return outFrame;
    }

    @Override
    public ColorSpace getInputColor() {
        return ColorSpace.ANY_PLANAR;
    }

    @Override
    public ColorSpace getOutputColor() {
        return this.outputColor;
    }
}