package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

@Deprecated
public class RgbToBgrHiBD implements TransformHiBD {

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        if ((src.getColor() == ColorSpace.RGB || src.getColor() == ColorSpace.BGR) && (dst.getColor() == ColorSpace.RGB || dst.getColor() == ColorSpace.BGR)) {
            if (src.getCrop() == null && dst.getCrop() == null) {
                int[] dataSrc = src.getPlaneData(0);
                int[] dataDst = dst.getPlaneData(0);
                for (int i = 0; i < dataSrc.length; i += 3) {
                    int tmp = dataSrc[i + 2];
                    dataDst[i + 2] = dataSrc[i];
                    dataDst[i] = tmp;
                }
            } else {
                throw new NotSupportedException("Cropped images not supported");
            }
        } else {
            throw new IllegalArgumentException("Expected RGB or BGR inputs, was: " + src.getColor() + ", " + dst.getColor());
        }
    }
}