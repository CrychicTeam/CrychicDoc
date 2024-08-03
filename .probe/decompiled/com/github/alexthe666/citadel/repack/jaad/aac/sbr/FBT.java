package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import java.util.Arrays;

class FBT implements Constants {

    private static final int[] stopMinTable = new int[] { 13, 15, 20, 21, 23, 32, 32, 35, 48, 64, 70, 96 };

    private static final int[][] STOP_OFFSET_TABLE = new int[][] { { 0, 2, 4, 6, 8, 11, 14, 18, 22, 26, 31, 37, 44, 51 }, { 0, 2, 4, 6, 8, 11, 14, 18, 22, 26, 31, 36, 42, 49 }, { 0, 2, 4, 6, 8, 11, 14, 17, 21, 25, 29, 34, 39, 44 }, { 0, 2, 4, 6, 8, 11, 14, 17, 20, 24, 28, 33, 38, 43 }, { 0, 2, 4, 6, 8, 11, 14, 17, 20, 24, 28, 32, 36, 41 }, { 0, 2, 4, 6, 8, 10, 12, 14, 17, 20, 23, 26, 29, 32 }, { 0, 2, 4, 6, 8, 10, 12, 14, 17, 20, 23, 26, 29, 32 }, { 0, 1, 3, 5, 7, 9, 11, 13, 15, 17, 20, 23, 26, 29 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 14, 16 }, { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, -1, -2, -3, -4, -5, -6, -6, -6, -6, -6, -6, -6, -6 }, { 0, -3, -6, -9, -12, -15, -18, -20, -22, -24, -26, -28, -30, -32 } };

    private static final float[] limiterBandsCompare = new float[] { 1.327152F, 1.185093F, 1.119872F };

    public static int qmf_start_channel(int bs_start_freq, int bs_samplerate_mode, SampleFrequency sample_rate) {
        int startMin = startMinTable[sample_rate.getIndex()];
        int offsetIndex = offsetIndexTable[sample_rate.getIndex()];
        return bs_samplerate_mode != 0 ? startMin + OFFSET[offsetIndex][bs_start_freq] : startMin + OFFSET[6][bs_start_freq];
    }

    public static int qmf_stop_channel(int bs_stop_freq, SampleFrequency sample_rate, int k0) {
        if (bs_stop_freq == 15) {
            return Math.min(64, k0 * 3);
        } else if (bs_stop_freq == 14) {
            return Math.min(64, k0 * 2);
        } else {
            int stopMin = stopMinTable[sample_rate.getIndex()];
            return Math.min(64, stopMin + STOP_OFFSET_TABLE[sample_rate.getIndex()][Math.min(bs_stop_freq, 13)]);
        }
    }

    public static int master_frequency_table_fs0(SBR sbr, int k0, int k2, boolean bs_alter_scale) {
        int[] vDk = new int[64];
        if (k2 <= k0) {
            sbr.N_master = 0;
            return 1;
        } else {
            int dk = bs_alter_scale ? 2 : 1;
            int nrBands;
            if (bs_alter_scale) {
                nrBands = k2 - k0 + 2 >> 2 << 1;
            } else {
                nrBands = k2 - k0 >> 1 << 1;
            }
            nrBands = Math.min(nrBands, 63);
            if (nrBands <= 0) {
                return 1;
            } else {
                int k2Achieved = k0 + nrBands * dk;
                int k2Diff = k2 - k2Achieved;
                for (int k = 0; k < nrBands; k++) {
                    vDk[k] = dk;
                }
                if (k2Diff != 0) {
                    int incr = k2Diff > 0 ? -1 : 1;
                    for (int var11 = k2Diff > 0 ? nrBands - 1 : 0; k2Diff != 0; k2Diff += incr) {
                        vDk[var11] -= incr;
                        var11 += incr;
                    }
                }
                sbr.f_master[0] = k0;
                for (int var12 = 1; var12 <= nrBands; var12++) {
                    sbr.f_master[var12] = sbr.f_master[var12 - 1] + vDk[var12 - 1];
                }
                sbr.N_master = nrBands;
                sbr.N_master = Math.min(sbr.N_master, 64);
                return 0;
            }
        }
    }

    public static int find_bands(int warp, int bands, int a0, int a1) {
        float div = (float) Math.log(2.0);
        if (warp != 0) {
            div *= 1.3F;
        }
        return (int) ((double) bands * Math.log((double) ((float) a1 / (float) a0)) / (double) div + 0.5);
    }

    public static float find_initial_power(int bands, int a0, int a1) {
        return (float) Math.pow((double) ((float) a1 / (float) a0), (double) (1.0F / (float) bands));
    }

    public static int master_frequency_table(SBR sbr, int k0, int k2, int bs_freq_scale, boolean bs_alter_scale) {
        int[] vDk0 = new int[64];
        int[] vDk1 = new int[64];
        int[] vk0 = new int[64];
        int[] vk1 = new int[64];
        int[] temp1 = new int[] { 6, 5, 4 };
        if (k2 <= k0) {
            sbr.N_master = 0;
            return 1;
        } else {
            int bands = temp1[bs_freq_scale - 1];
            boolean twoRegions;
            int k1;
            if ((double) ((float) k2 / (float) k0) > 2.2449) {
                twoRegions = true;
                k1 = k0 << 1;
            } else {
                twoRegions = false;
                k1 = k2;
            }
            int nrBand0 = 2 * find_bands(0, bands, k0, k1);
            nrBand0 = Math.min(nrBand0, 63);
            if (nrBand0 <= 0) {
                return 1;
            } else {
                float q = find_initial_power(nrBand0, k0, k1);
                float qk = (float) k0;
                int A_1 = (int) (qk + 0.5F);
                for (int k = 0; k <= nrBand0; k++) {
                    int A_0 = A_1;
                    qk *= q;
                    A_1 = (int) (qk + 0.5F);
                    vDk0[k] = A_1 - A_0;
                }
                Arrays.sort(vDk0, 0, nrBand0);
                vk0[0] = k0;
                for (int var20 = 1; var20 <= nrBand0; var20++) {
                    vk0[var20] = vk0[var20 - 1] + vDk0[var20 - 1];
                    if (vDk0[var20 - 1] == 0) {
                        return 1;
                    }
                }
                if (!twoRegions) {
                    for (int var25 = 0; var25 <= nrBand0; var25++) {
                        sbr.f_master[var25] = vk0[var25];
                    }
                    sbr.N_master = nrBand0;
                    sbr.N_master = Math.min(sbr.N_master, 64);
                    return 0;
                } else {
                    int nrBand1 = 2 * find_bands(1, bands, k1, k2);
                    nrBand1 = Math.min(nrBand1, 63);
                    q = find_initial_power(nrBand1, k1, k2);
                    qk = (float) k1;
                    A_1 = (int) (qk + 0.5F);
                    for (int var21 = 0; var21 <= nrBand1 - 1; var21++) {
                        int A_0 = A_1;
                        qk *= q;
                        A_1 = (int) (qk + 0.5F);
                        vDk1[var21] = A_1 - A_0;
                    }
                    if (vDk1[0] < vDk0[nrBand0 - 1]) {
                        Arrays.sort(vDk1, 0, nrBand1 + 1);
                        int change = vDk0[nrBand0 - 1] - vDk1[0];
                        vDk1[0] = vDk0[nrBand0 - 1];
                        vDk1[nrBand1 - 1] = vDk1[nrBand1 - 1] - change;
                    }
                    Arrays.sort(vDk1, 0, nrBand1);
                    vk1[0] = k1;
                    for (int var22 = 1; var22 <= nrBand1; var22++) {
                        vk1[var22] = vk1[var22 - 1] + vDk1[var22 - 1];
                        if (vDk1[var22 - 1] == 0) {
                            return 1;
                        }
                    }
                    sbr.N_master = nrBand0 + nrBand1;
                    sbr.N_master = Math.min(sbr.N_master, 64);
                    for (int var23 = 0; var23 <= nrBand0; var23++) {
                        sbr.f_master[var23] = vk0[var23];
                    }
                    for (int var24 = nrBand0 + 1; var24 <= sbr.N_master; var24++) {
                        sbr.f_master[var24] = vk1[var24 - nrBand0];
                    }
                    return 0;
                }
            }
        }
    }

    public static int derived_frequency_table(SBR sbr, int bs_xover_band, int k2) {
        int i = 0;
        if (sbr.N_master <= bs_xover_band) {
            return 1;
        } else {
            sbr.N_high = sbr.N_master - bs_xover_band;
            sbr.N_low = (sbr.N_high >> 1) + (sbr.N_high - (sbr.N_high >> 1 << 1));
            sbr.n[0] = sbr.N_low;
            sbr.n[1] = sbr.N_high;
            for (int k = 0; k <= sbr.N_high; k++) {
                sbr.f_table_res[1][k] = sbr.f_master[k + bs_xover_band];
            }
            sbr.M = sbr.f_table_res[1][sbr.N_high] - sbr.f_table_res[1][0];
            sbr.kx = sbr.f_table_res[1][0];
            if (sbr.kx > 32) {
                return 1;
            } else if (sbr.kx + sbr.M > 64) {
                return 1;
            } else {
                int minus = (sbr.N_high & 1) != 0 ? 1 : 0;
                for (int var7 = 0; var7 <= sbr.N_low; var7++) {
                    if (var7 == 0) {
                        i = 0;
                    } else {
                        i = 2 * var7 - minus;
                    }
                    sbr.f_table_res[0][var7] = sbr.f_table_res[1][i];
                }
                sbr.N_Q = 0;
                if (sbr.bs_noise_bands == 0) {
                    sbr.N_Q = 1;
                } else {
                    sbr.N_Q = Math.max(1, find_bands(0, sbr.bs_noise_bands, sbr.kx, k2));
                    sbr.N_Q = Math.min(5, sbr.N_Q);
                }
                for (int var8 = 0; var8 <= sbr.N_Q; var8++) {
                    if (var8 == 0) {
                        i = 0;
                    } else {
                        i += (sbr.N_low - i) / (sbr.N_Q + 1 - var8);
                    }
                    sbr.f_table_noise[var8] = sbr.f_table_res[0][i];
                }
                for (int var9 = 0; var9 < 64; var9++) {
                    for (int g = 0; g < sbr.N_Q; g++) {
                        if (sbr.f_table_noise[g] <= var9 && var9 < sbr.f_table_noise[g + 1]) {
                            sbr.table_map_k_to_g[var9] = g;
                            break;
                        }
                    }
                }
                return 0;
            }
        }
    }

    public static void limiter_frequency_table(SBR sbr) {
        sbr.f_table_lim[0][0] = sbr.f_table_res[0][0] - sbr.kx;
        sbr.f_table_lim[0][1] = sbr.f_table_res[0][sbr.N_low] - sbr.kx;
        sbr.N_L[0] = 1;
        for (int s = 1; s < 4; s++) {
            int[] limTable = new int[100];
            int[] patchBorders = new int[64];
            patchBorders[0] = sbr.kx;
            for (int k = 1; k <= sbr.noPatches; k++) {
                patchBorders[k] = patchBorders[k - 1] + sbr.patchNoSubbands[k - 1];
            }
            for (int var10 = 0; var10 <= sbr.N_low; var10++) {
                limTable[var10] = sbr.f_table_res[0][var10];
            }
            for (int var11 = 1; var11 < sbr.noPatches; var11++) {
                limTable[var11 + sbr.N_low] = patchBorders[var11];
            }
            Arrays.sort(limTable, 0, sbr.noPatches + sbr.N_low);
            int var12 = 1;
            int nrLim = sbr.noPatches + sbr.N_low - 1;
            if (nrLim < 0) {
                return;
            }
            while (var12 <= nrLim) {
                float nOctaves;
                if (limTable[var12 - 1] != 0) {
                    nOctaves = (float) limTable[var12] / (float) limTable[var12 - 1];
                } else {
                    nOctaves = 0.0F;
                }
                if (nOctaves < limiterBandsCompare[s - 1]) {
                    if (limTable[var12] != limTable[var12 - 1]) {
                        boolean found = false;
                        boolean found2 = false;
                        for (int i = 0; i <= sbr.noPatches; i++) {
                            if (limTable[var12] == patchBorders[i]) {
                                found = true;
                            }
                        }
                        if (found) {
                            found2 = false;
                            for (int var14 = 0; var14 <= sbr.noPatches; var14++) {
                                if (limTable[var12 - 1] == patchBorders[var14]) {
                                    found2 = true;
                                }
                            }
                            if (found2) {
                                var12++;
                            } else {
                                limTable[var12 - 1] = sbr.f_table_res[0][sbr.N_low];
                                Arrays.sort(limTable, 0, sbr.noPatches + sbr.N_low);
                                nrLim--;
                            }
                            continue;
                        }
                    }
                    limTable[var12] = sbr.f_table_res[0][sbr.N_low];
                    Arrays.sort(limTable, 0, nrLim);
                    nrLim--;
                } else {
                    var12++;
                }
            }
            sbr.N_L[s] = nrLim;
            for (int var13 = 0; var13 <= nrLim; var13++) {
                sbr.f_table_lim[s][var13] = limTable[var13] - sbr.kx;
            }
        }
    }
}