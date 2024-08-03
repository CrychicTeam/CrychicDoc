package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

class HFGeneration {

    private static final int[] goalSbTab = new int[] { 21, 23, 32, 43, 46, 64, 85, 93, 128, 0, 0, 0 };

    public static void hf_generation(SBR sbr, float[][][] Xlow, float[][][] Xhigh, int ch) {
        float[][] alpha_0 = new float[64][2];
        float[][] alpha_1 = new float[64][2];
        int offset = sbr.tHFAdj;
        int first = sbr.t_E[ch][0];
        int last = sbr.t_E[ch][sbr.L_E[ch]];
        calc_chirp_factors(sbr, ch);
        if (ch == 0 && sbr.Reset) {
            patch_construction(sbr);
        }
        for (int i = 0; i < sbr.noPatches; i++) {
            for (int x = 0; x < sbr.patchNoSubbands[i]; x++) {
                int k = sbr.kx + x;
                for (int q = 0; q < i; q++) {
                    k += sbr.patchNoSubbands[q];
                }
                int p = sbr.patchStartSubband[i] + x;
                int g = sbr.table_map_k_to_g[k];
                float bw = sbr.bwArray[ch][g];
                float bw2 = bw * bw;
                if (bw2 > 0.0F) {
                    calc_prediction_coef(sbr, Xlow, alpha_0, alpha_1, p);
                    float a0_r = alpha_0[p][0] * bw;
                    float a1_r = alpha_1[p][0] * bw2;
                    float a0_i = alpha_0[p][1] * bw;
                    float a1_i = alpha_1[p][1] * bw2;
                    float temp2_r = Xlow[first - 2 + offset][p][0];
                    float temp3_r = Xlow[first - 1 + offset][p][0];
                    float temp2_i = Xlow[first - 2 + offset][p][1];
                    float temp3_i = Xlow[first - 1 + offset][p][1];
                    for (int l = first; l < last; l++) {
                        float temp1_r = temp2_r;
                        temp2_r = temp3_r;
                        temp3_r = Xlow[l + offset][p][0];
                        float temp1_i = temp2_i;
                        temp2_i = temp3_i;
                        temp3_i = Xlow[l + offset][p][1];
                        Xhigh[l + offset][k][0] = temp3_r + (a0_r * temp2_r - a0_i * temp2_i + a1_r * temp1_r - a1_i * temp1_i);
                        Xhigh[l + offset][k][1] = temp3_i + a0_i * temp2_r + a0_r * temp2_i + a1_i * temp1_r + a1_r * temp1_i;
                    }
                } else {
                    for (int l = first; l < last; l++) {
                        Xhigh[l + offset][k][0] = Xlow[l + offset][p][0];
                        Xhigh[l + offset][k][1] = Xlow[l + offset][p][1];
                    }
                }
            }
        }
        if (sbr.Reset) {
            FBT.limiter_frequency_table(sbr);
        }
    }

    private static void auto_correlation(SBR sbr, HFGeneration.acorr_coef ac, float[][][] buffer, int bd, int len) {
        float r01r = 0.0F;
        float r01i = 0.0F;
        float r02r = 0.0F;
        float r02i = 0.0F;
        float r11r = 0.0F;
        float rel = 0.99999905F;
        int offset = sbr.tHFAdj;
        float temp2_r = buffer[offset - 2][bd][0];
        float temp2_i = buffer[offset - 2][bd][1];
        float temp3_r = buffer[offset - 1][bd][0];
        float temp3_i = buffer[offset - 1][bd][1];
        float temp4_r = temp2_r;
        float temp4_i = temp2_i;
        float temp5_r = temp3_r;
        float temp5_i = temp3_i;
        for (int j = offset; j < len + offset; j++) {
            float temp1_r = temp2_r;
            float temp1_i = temp2_i;
            temp2_r = temp3_r;
            temp2_i = temp3_i;
            temp3_r = buffer[j][bd][0];
            temp3_i = buffer[j][bd][1];
            r01r += temp3_r * temp2_r + temp3_i * temp2_i;
            r01i += temp3_i * temp2_r - temp3_r * temp2_i;
            r02r += temp3_r * temp1_r + temp3_i * temp1_i;
            r02i += temp3_i * temp1_r - temp3_r * temp1_i;
            r11r += temp2_r * temp2_r + temp2_i * temp2_i;
        }
        ac.r12[0] = r01r - (temp3_r * temp2_r + temp3_i * temp2_i) + temp5_r * temp4_r + temp5_i * temp4_i;
        ac.r12[1] = r01i - (temp3_i * temp2_r - temp3_r * temp2_i) + (temp5_i * temp4_r - temp5_r * temp4_i);
        ac.r22[0] = r11r - (temp2_r * temp2_r + temp2_i * temp2_i) + temp4_r * temp4_r + temp4_i * temp4_i;
        ac.r01[0] = r01r;
        ac.r01[1] = r01i;
        ac.r02[0] = r02r;
        ac.r02[1] = r02i;
        ac.r11[0] = r11r;
        ac.det = ac.r11[0] * ac.r22[0] - rel * (ac.r12[0] * ac.r12[0] + ac.r12[1] * ac.r12[1]);
    }

    private static void calc_prediction_coef(SBR sbr, float[][][] Xlow, float[][] alpha_0, float[][] alpha_1, int k) {
        HFGeneration.acorr_coef ac = new HFGeneration.acorr_coef();
        auto_correlation(sbr, ac, Xlow, k, sbr.numTimeSlotsRate + 6);
        if (ac.det == 0.0F) {
            alpha_1[k][0] = 0.0F;
            alpha_1[k][1] = 0.0F;
        } else {
            float tmp = 1.0F / ac.det;
            alpha_1[k][0] = (ac.r01[0] * ac.r12[0] - ac.r01[1] * ac.r12[1] - ac.r02[0] * ac.r11[0]) * tmp;
            alpha_1[k][1] = (ac.r01[1] * ac.r12[0] + ac.r01[0] * ac.r12[1] - ac.r02[1] * ac.r11[0]) * tmp;
        }
        if (ac.r11[0] == 0.0F) {
            alpha_0[k][0] = 0.0F;
            alpha_0[k][1] = 0.0F;
        } else {
            float tmp = 1.0F / ac.r11[0];
            alpha_0[k][0] = -(ac.r01[0] + alpha_1[k][0] * ac.r12[0] + alpha_1[k][1] * ac.r12[1]) * tmp;
            alpha_0[k][1] = -(ac.r01[1] + alpha_1[k][1] * ac.r12[0] - alpha_1[k][0] * ac.r12[1]) * tmp;
        }
        if (alpha_0[k][0] * alpha_0[k][0] + alpha_0[k][1] * alpha_0[k][1] >= 16.0F || alpha_1[k][0] * alpha_1[k][0] + alpha_1[k][1] * alpha_1[k][1] >= 16.0F) {
            alpha_0[k][0] = 0.0F;
            alpha_0[k][1] = 0.0F;
            alpha_1[k][0] = 0.0F;
            alpha_1[k][1] = 0.0F;
        }
    }

    private static float mapNewBw(int invf_mode, int invf_mode_prev) {
        switch(invf_mode) {
            case 1:
                if (invf_mode_prev == 0) {
                    return 0.6F;
                }
                return 0.75F;
            case 2:
                return 0.9F;
            case 3:
                return 0.98F;
            default:
                return invf_mode_prev == 1 ? 0.6F : 0.0F;
        }
    }

    private static void calc_chirp_factors(SBR sbr, int ch) {
        for (int i = 0; i < sbr.N_Q; i++) {
            sbr.bwArray[ch][i] = mapNewBw(sbr.bs_invf_mode[ch][i], sbr.bs_invf_mode_prev[ch][i]);
            if (sbr.bwArray[ch][i] < sbr.bwArray_prev[ch][i]) {
                sbr.bwArray[ch][i] = sbr.bwArray[ch][i] * 0.75F + sbr.bwArray_prev[ch][i] * 0.25F;
            } else {
                sbr.bwArray[ch][i] = sbr.bwArray[ch][i] * 0.90625F + sbr.bwArray_prev[ch][i] * 0.09375F;
            }
            if (sbr.bwArray[ch][i] < 0.015625F) {
                sbr.bwArray[ch][i] = 0.0F;
            }
            if (sbr.bwArray[ch][i] >= 0.99609375F) {
                sbr.bwArray[ch][i] = 0.99609375F;
            }
            sbr.bwArray_prev[ch][i] = sbr.bwArray[ch][i];
            sbr.bs_invf_mode_prev[ch][i] = sbr.bs_invf_mode[ch][i];
        }
    }

    private static void patch_construction(SBR sbr) {
        int msb = sbr.k0;
        int usb = sbr.kx;
        int goalSb = goalSbTab[sbr.sample_rate.getIndex()];
        sbr.noPatches = 0;
        int k;
        if (goalSb < sbr.kx + sbr.M) {
            int i = 0;
            for (k = 0; sbr.f_master[i] < goalSb; i++) {
                k = i + 1;
            }
        } else {
            k = sbr.N_master;
        }
        if (sbr.N_master == 0) {
            sbr.noPatches = 0;
            sbr.patchNoSubbands[0] = 0;
            sbr.patchStartSubband[0] = 0;
        } else {
            int sb;
            do {
                int j = k + 1;
                int odd;
                do {
                    sb = sbr.f_master[--j];
                    odd = (sb - 2 + sbr.k0) % 2;
                } while (sb > sbr.k0 - 1 + msb - odd);
                sbr.patchNoSubbands[sbr.noPatches] = Math.max(sb - usb, 0);
                sbr.patchStartSubband[sbr.noPatches] = sbr.k0 - odd - sbr.patchNoSubbands[sbr.noPatches];
                if (sbr.patchNoSubbands[sbr.noPatches] > 0) {
                    usb = sb;
                    msb = sb;
                    sbr.noPatches++;
                } else {
                    msb = sbr.kx;
                }
                if (sbr.f_master[k] - sb < 3) {
                    k = sbr.N_master;
                }
            } while (sb != sbr.kx + sbr.M);
            if (sbr.patchNoSubbands[sbr.noPatches - 1] < 3 && sbr.noPatches > 1) {
                sbr.noPatches--;
            }
            sbr.noPatches = Math.min(sbr.noPatches, 5);
        }
    }

    private static class acorr_coef {

        float[] r01 = new float[2];

        float[] r02 = new float[2];

        float[] r11 = new float[2];

        float[] r12 = new float[2];

        float[] r22 = new float[2];

        float det;
    }
}