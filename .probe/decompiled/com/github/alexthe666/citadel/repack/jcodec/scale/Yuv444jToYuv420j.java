package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class Yuv444jToYuv420j implements Transform {

    @Override
    public void transform(Picture src, Picture dst) {
        int size = src.getWidth() * src.getHeight();
        System.arraycopy(src.getPlaneData(0), 0, dst.getPlaneData(0), 0, size);
        for (int plane = 1; plane < 3; plane++) {
            byte[] srcPl = src.getPlaneData(plane);
            byte[] dstPl = dst.getPlaneData(plane);
            int srcStride = src.getPlaneWidth(plane);
            int y = 0;
            int srcOff = 0;
            for (int dstOff = 0; y < src.getHeight(); srcOff += srcStride) {
                for (int x = 0; x < src.getWidth(); dstOff++) {
                    dstPl[dstOff] = (byte) (srcPl[srcOff] + srcPl[srcOff + 1] + srcPl[srcOff + srcStride] + srcPl[srcOff + srcStride + 1] + 2 >> 2);
                    x += 2;
                    srcOff += 2;
                }
                y += 2;
            }
        }
    }
}