package com.github.alexthe666.citadel.repack.jcodec.codecs.mpa;

import java.util.Arrays;

public class Mp3Mdct {

    private static final float factor36pt0 = 0.34729636F;

    private static final float factor36pt1 = 1.5320889F;

    private static final float factor36pt2 = 1.8793852F;

    private static final float factor36pt3 = 1.7320508F;

    private static final float factor36pt4 = 1.9696155F;

    private static final float factor36pt5 = 1.2855753F;

    private static final float factor36pt6 = 0.6840403F;

    private static final float[] factor36 = new float[] { 0.5019099F, 0.5176381F, 0.55168897F, 0.61038727F, 0.8717234F, 1.1831008F, 1.9318516F, 5.7368565F };

    private static final float cos075 = 0.9914449F;

    private static final float cos225 = 0.9238795F;

    private static final float cos300 = 0.8660254F;

    private static final float cos375 = 0.7933533F;

    private static final float cos450 = 0.70710677F;

    private static final float cos525 = 0.6087614F;

    private static final float cos600 = 0.5F;

    private static final float cos675 = 0.38268343F;

    private static final float cos825 = 0.13052619F;

    private static final float factor12pt0 = 1.9318516F;

    private static final float factor12pt1 = 0.5176381F;

    private static final float[] factor12 = new float[] { 0.5043145F, 0.5411961F, 0.6302362F, 0.8213398F, 1.306563F, 3.830649F };

    private static float[] tmp = new float[16];

    static void oneLong(float[] src, float[] dst) {
        for (int i = 17; i > 0; i--) {
            src[i] += src[i - 1];
        }
        for (int i = 17; i > 2; i -= 2) {
            src[i] += src[i - 2];
        }
        int i = 0;
        for (int k = 0; i < 2; k += 8) {
            float tmp0 = src[i] + src[i];
            float tmp1 = tmp0 + src[12 + i];
            float tmp2 = src[6 + i] * 1.7320508F;
            tmp[k + 0] = tmp1 + src[4 + i] * 1.8793852F + src[8 + i] * 1.5320889F + src[16 + i] * 0.34729636F;
            tmp[k + 1] = tmp0 + src[4 + i] - src[8 + i] - src[12 + i] - src[12 + i] - src[16 + i];
            tmp[k + 2] = tmp1 - src[4 + i] * 0.34729636F - src[8 + i] * 1.8793852F + src[16 + i] * 1.5320889F;
            tmp[k + 3] = tmp1 - src[4 + i] * 1.5320889F + src[8 + i] * 0.34729636F - src[16 + i] * 1.8793852F;
            tmp[k + 4] = src[2 + i] * 1.9696155F + tmp2 + src[10 + i] * 1.2855753F + src[14 + i] * 0.6840403F;
            tmp[k + 5] = (src[2 + i] - src[10 + i] - src[14 + i]) * 1.7320508F;
            tmp[k + 6] = src[2 + i] * 1.2855753F - tmp2 - src[10 + i] * 0.6840403F + src[14 + i] * 1.9696155F;
            tmp[k + 7] = src[2 + i] * 0.6840403F - tmp2 + src[10 + i] * 1.9696155F - src[14 + i] * 1.2855753F;
            i++;
        }
        i = 0;
        int j = 4;
        int k = 8;
        for (int l = 12; i < 4; l++) {
            float q1 = tmp[i];
            float q2 = tmp[k];
            tmp[i] = tmp[i] + tmp[j];
            tmp[j] = q1 - tmp[j];
            tmp[k] = (tmp[k] + tmp[l]) * factor36[i];
            tmp[l] = (q2 - tmp[l]) * factor36[7 - i];
            i++;
            j++;
            k++;
        }
        for (int ix = 0; ix < 4; ix++) {
            dst[26 - ix] = tmp[ix] + tmp[8 + ix];
            dst[8 - ix] = tmp[8 + ix] - tmp[ix];
            dst[27 + ix] = dst[26 - ix];
            dst[9 + ix] = -dst[8 - ix];
        }
        for (int ix = 0; ix < 4; ix++) {
            dst[21 - ix] = tmp[7 - ix] + tmp[15 - ix];
            dst[3 - ix] = tmp[15 - ix] - tmp[7 - ix];
            dst[32 + ix] = dst[21 - ix];
            dst[14 + ix] = -dst[3 - ix];
        }
        float tmp0 = src[0] - src[4] + src[8] - src[12] + src[16];
        float tmp1 = (src[1] - src[5] + src[9] - src[13] + src[17]) * 0.70710677F;
        dst[4] = tmp1 - tmp0;
        dst[13] = -dst[4];
        dst[31] = dst[22] = tmp0 + tmp1;
    }

    static void threeShort(float[] src, float[] dst) {
        Arrays.fill(dst, 0.0F);
        int i = 0;
        for (int outOff = 0; i < 3; outOff += 6) {
            imdct12(src, dst, outOff, i);
            i++;
        }
    }

    private static void imdct12(float[] src, float[] dst, int outOff, int wndIdx) {
        int j = 15 + wndIdx;
        for (int k = 12 + wndIdx; j >= 3 + wndIdx; k -= 3) {
            src[j] += src[k];
            j -= 3;
        }
        src[15 + wndIdx] = src[15 + wndIdx] + src[9 + wndIdx];
        src[9 + wndIdx] = src[9 + wndIdx] + src[3 + wndIdx];
        float pp2 = src[12 + wndIdx] * 0.5F;
        float pp1 = src[6 + wndIdx] * 0.8660254F;
        float sum = src[0 + wndIdx] + pp2;
        tmp[1] = src[wndIdx] - src[12 + wndIdx];
        tmp[0] = sum + pp1;
        tmp[2] = sum - pp1;
        float var11 = src[15 + wndIdx] * 0.5F;
        pp1 = src[9 + wndIdx] * 0.8660254F;
        sum = src[3 + wndIdx] + var11;
        tmp[4] = src[3 + wndIdx] - src[15 + wndIdx];
        tmp[5] = sum + pp1;
        tmp[3] = sum - pp1;
        tmp[3] = tmp[3] * 1.9318516F;
        tmp[4] = tmp[4] * 0.70710677F;
        tmp[5] = tmp[5] * 0.5176381F;
        float t = tmp[0];
        tmp[0] = tmp[0] + tmp[5];
        tmp[5] = t - tmp[5];
        t = tmp[1];
        tmp[1] = tmp[1] + tmp[4];
        tmp[4] = t - tmp[4];
        t = tmp[2];
        tmp[2] = tmp[2] + tmp[3];
        tmp[3] = t - tmp[3];
        for (int jx = 0; jx < 6; jx++) {
            tmp[jx] = tmp[jx] * factor12[jx];
        }
        tmp[8] = -tmp[0] * 0.7933533F;
        tmp[9] = -tmp[0] * 0.6087614F;
        tmp[7] = -tmp[1] * 0.9238795F;
        tmp[10] = -tmp[1] * 0.38268343F;
        tmp[6] = -tmp[2] * 0.9914449F;
        tmp[11] = -tmp[2] * 0.13052619F;
        tmp[0] = tmp[3];
        tmp[1] = tmp[4] * 0.38268343F;
        tmp[2] = tmp[5] * 0.6087614F;
        tmp[3] = -tmp[5] * 0.7933533F;
        tmp[4] = -tmp[4] * 0.9238795F;
        tmp[5] = -tmp[0] * 0.9914449F;
        tmp[0] = tmp[0] * 0.13052619F;
        int i = 0;
        for (int jx = outOff + 6; i < 12; jx++) {
            dst[jx] += tmp[i];
            i++;
        }
    }
}