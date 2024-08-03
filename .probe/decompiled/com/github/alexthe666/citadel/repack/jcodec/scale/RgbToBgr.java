package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class RgbToBgr implements Transform {

    @Override
    public void transform(Picture src, Picture dst) {
        if ((src.getColor() == ColorSpace.RGB || src.getColor() == ColorSpace.BGR) && (dst.getColor() == ColorSpace.RGB || dst.getColor() == ColorSpace.BGR)) {
            byte[] dataSrc = src.getPlaneData(0);
            byte[] dataDst = dst.getPlaneData(0);
            for (int i = 0; i < dataSrc.length; i += 3) {
                byte tmp = dataSrc[i + 2];
                dataDst[i + 2] = dataSrc[i];
                dataDst[i] = tmp;
                dataDst[i + 1] = dataSrc[i + 1];
            }
        } else {
            throw new IllegalArgumentException("Expected RGB or BGR inputs, was: " + src.getColor() + ", " + dst.getColor());
        }
    }
}