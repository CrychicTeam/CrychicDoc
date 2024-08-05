package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class RgbToYuv420p implements Transform {

    @Override
    public void transform(Picture img, Picture dst) {
        byte[] y = img.getData()[0];
        byte[][] dstData = dst.getData();
        byte[][] out = new byte[4][3];
        int offChr = 0;
        int offLuma = 0;
        int offSrc = 0;
        int strideSrc = img.getWidth() * 3;
        int strideDst = dst.getWidth();
        for (int i = 0; i < img.getHeight() >> 1; i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                dstData[1][offChr] = 0;
                dstData[2][offChr] = 0;
                rgb2yuv(y[offSrc], y[offSrc + 1], y[offSrc + 2], out[0]);
                dstData[0][offLuma] = out[0][0];
                rgb2yuv(y[offSrc + strideSrc], y[offSrc + strideSrc + 1], y[offSrc + strideSrc + 2], out[1]);
                dstData[0][offLuma + strideDst] = out[1][0];
                offLuma++;
                rgb2yuv(y[offSrc + 3], y[offSrc + 4], y[offSrc + 5], out[2]);
                dstData[0][offLuma] = out[2][0];
                rgb2yuv(y[offSrc + strideSrc + 3], y[offSrc + strideSrc + 4], y[offSrc + strideSrc + 5], out[3]);
                dstData[0][offLuma + strideDst] = out[3][0];
                offLuma++;
                dstData[1][offChr] = (byte) (out[0][1] + out[1][1] + out[2][1] + out[3][1] + 2 >> 2);
                dstData[2][offChr] = (byte) (out[0][2] + out[1][2] + out[2][2] + out[3][2] + 2 >> 2);
                offChr++;
                offSrc += 6;
            }
            offLuma += strideDst;
            offSrc += strideSrc;
        }
    }

    public static final void rgb2yuv(byte r, byte g, byte b, byte[] out) {
        int rS = r + 128;
        int gS = g + 128;
        int bS = b + 128;
        int y = 66 * rS + 129 * gS + 25 * bS;
        int u = -38 * rS - 74 * gS + 112 * bS;
        int v = 112 * rS - 94 * gS - 18 * bS;
        y = y + 128 >> 8;
        u = u + 128 >> 8;
        v = v + 128 >> 8;
        out[0] = (byte) MathUtil.clip(y - 112, -128, 127);
        out[1] = (byte) MathUtil.clip(u, -128, 127);
        out[2] = (byte) MathUtil.clip(v, -128, 127);
    }
}