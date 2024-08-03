package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.ArrayList;
import java.util.List;

public class PixelStoreImpl implements PixelStore {

    private List<Picture> buffers = new ArrayList();

    @Override
    public PixelStore.LoanerPicture getPicture(int width, int height, ColorSpace color) {
        for (Picture picture : this.buffers) {
            if (picture.getWidth() == width && picture.getHeight() == height && picture.getColor() == color) {
                this.buffers.remove(picture);
                return new PixelStore.LoanerPicture(picture, 1);
            }
        }
        return new PixelStore.LoanerPicture(Picture.create(width, height, color), 1);
    }

    @Override
    public void putBack(PixelStore.LoanerPicture frame) {
        frame.decRefCnt();
        if (frame.unused()) {
            Picture pixels = frame.getPicture();
            pixels.setCrop(null);
            this.buffers.add(pixels);
        }
    }

    @Override
    public void retake(PixelStore.LoanerPicture frame) {
        frame.incRefCnt();
    }
}