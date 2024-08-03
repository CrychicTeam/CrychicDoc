package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class RgbToYuv422p implements Transform {

    @Override
    public void transform(Picture img, Picture dst) {
        byte[] y = img.getData()[0];
        byte[] out1 = new byte[3];
        byte[] out2 = new byte[3];
        byte[][] dstData = dst.getData();
        int off = 0;
        int offSrc = 0;
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                int offY = off << 1;
                RgbToYuv420p.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], out1);
                dstData[0][offY] = out1[0];
                RgbToYuv420p.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], out2);
                dstData[0][offY + 1] = out2[0];
                dstData[1][off] = (byte) (out1[1] + out2[1] + 1 >> 1);
                dstData[2][off] = (byte) (out1[2] + out2[2] + 1 >> 1);
                off++;
            }
        }
    }
}