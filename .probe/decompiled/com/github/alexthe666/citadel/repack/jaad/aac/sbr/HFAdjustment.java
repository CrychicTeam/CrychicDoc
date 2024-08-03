package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

class HFAdjustment implements Constants, NoiseTable {

    private static final float[] h_smooth = new float[] { 0.0318305F, 0.11516383F, 0.2181695F, 0.30150282F, 0.33333334F };

    private static final int[] phi_re = new int[] { 1, 0, -1, 0 };

    private static final int[] phi_im = new int[] { 0, 1, 0, -1 };

    private static final float[] limGain = new float[] { 0.5F, 1.0F, 2.0F, 1.0E10F };

    private static final float EPS = 1.0E-12F;

    private float[][] G_lim_boost = new float[5][49];

    private float[][] Q_M_lim_boost = new float[5][49];

    private float[][] S_M_boost = new float[5][49];

    public static int hf_adjustment(SBR sbr, float[][][] Xsbr, int ch) {
        HFAdjustment adj = new HFAdjustment();
        int ret = 0;
        if (sbr.bs_frame_class[ch] == 0) {
            sbr.l_A[ch] = -1;
        } else if (sbr.bs_frame_class[ch] == 2) {
            if (sbr.bs_pointer[ch] > 1) {
                sbr.l_A[ch] = sbr.bs_pointer[ch] - 1;
            } else {
                sbr.l_A[ch] = -1;
            }
        } else if (sbr.bs_pointer[ch] == 0) {
            sbr.l_A[ch] = -1;
        } else {
            sbr.l_A[ch] = sbr.L_E[ch] + 1 - sbr.bs_pointer[ch];
        }
        ret = estimate_current_envelope(sbr, adj, Xsbr, ch);
        if (ret > 0) {
            return 1;
        } else {
            calculate_gain(sbr, adj, ch);
            hf_assembly(sbr, adj, Xsbr, ch);
            return 0;
        }
    }

    private static int get_S_mapped(SBR sbr, int ch, int l, int current_band) {
        if (sbr.f[ch][l] == 1) {
            if (l >= sbr.l_A[ch] || sbr.bs_add_harmonic_prev[ch][current_band] != 0 && sbr.bs_add_harmonic_flag_prev[ch]) {
                return sbr.bs_add_harmonic[ch][current_band];
            }
        } else {
            int lb = 2 * current_band - ((sbr.N_high & 1) != 0 ? 1 : 0);
            int ub = 2 * (current_band + 1) - ((sbr.N_high & 1) != 0 ? 1 : 0);
            for (int b = lb; b < ub; b++) {
                if ((l >= sbr.l_A[ch] || sbr.bs_add_harmonic_prev[ch][b] != 0 && sbr.bs_add_harmonic_flag_prev[ch]) && sbr.bs_add_harmonic[ch][b] == 1) {
                    return 1;
                }
            }
        }
        return 0;
    }

    private static int estimate_current_envelope(SBR sbr, HFAdjustment adj, float[][][] Xsbr, int ch) {
        if (sbr.bs_interpol_freq) {
            for (int l = 0; l < sbr.L_E[ch]; l++) {
                int l_i = sbr.t_E[ch][l];
                int u_i = sbr.t_E[ch][l + 1];
                float div = (float) (u_i - l_i);
                if (div == 0.0F) {
                    div = 1.0F;
                }
                for (int m = 0; m < sbr.M; m++) {
                    float nrg = 0.0F;
                    for (int i = l_i + sbr.tHFAdj; i < u_i + sbr.tHFAdj; i++) {
                        nrg += Xsbr[i][m + sbr.kx][0] * Xsbr[i][m + sbr.kx][0] + Xsbr[i][m + sbr.kx][1] * Xsbr[i][m + sbr.kx][1];
                    }
                    sbr.E_curr[ch][m][l] = nrg / div;
                }
            }
        } else {
            for (int l = 0; l < sbr.L_E[ch]; l++) {
                for (int p = 0; p < sbr.n[sbr.f[ch][l]]; p++) {
                    int k_l = sbr.f_table_res[sbr.f[ch][l]][p];
                    int k_h = sbr.f_table_res[sbr.f[ch][l]][p + 1];
                    for (int k = k_l; k < k_h; k++) {
                        float nrg = 0.0F;
                        int l_i = sbr.t_E[ch][l];
                        int u_i = sbr.t_E[ch][l + 1];
                        float div = (float) ((u_i - l_i) * (k_h - k_l));
                        if (div == 0.0F) {
                            div = 1.0F;
                        }
                        for (int i = l_i + sbr.tHFAdj; i < u_i + sbr.tHFAdj; i++) {
                            for (int j = k_l; j < k_h; j++) {
                                nrg += Xsbr[i][j][0] * Xsbr[i][j][0] + Xsbr[i][j][1] * Xsbr[i][j][1];
                            }
                        }
                        sbr.E_curr[ch][k - sbr.kx][l] = nrg / div;
                    }
                }
            }
        }
        return 0;
    }

    private static void hf_assembly(SBR sbr, HFAdjustment adj, float[][][] Xsbr, int ch) {
        int fIndexNoise = 0;
        int fIndexSine = 0;
        boolean assembly_reset = false;
        if (sbr.Reset) {
            assembly_reset = true;
            fIndexNoise = 0;
        } else {
            fIndexNoise = sbr.index_noise_prev[ch];
        }
        fIndexSine = sbr.psi_is_prev[ch];
        for (int l = 0; l < sbr.L_E[ch]; l++) {
            boolean no_noise = l == sbr.l_A[ch] || l == sbr.prevEnvIsShort[ch];
            int h_SL = sbr.bs_smoothing_mode ? 0 : 4;
            h_SL = no_noise ? 0 : h_SL;
            if (assembly_reset) {
                for (int n = 0; n < 4; n++) {
                    System.arraycopy(adj.G_lim_boost[l], 0, sbr.G_temp_prev[ch][n], 0, sbr.M);
                    System.arraycopy(adj.Q_M_lim_boost[l], 0, sbr.Q_temp_prev[ch][n], 0, sbr.M);
                }
                sbr.GQ_ringbuf_index[ch] = 4;
                assembly_reset = false;
            }
            for (int i = sbr.t_E[ch][l]; i < sbr.t_E[ch][l + 1]; i++) {
                System.arraycopy(adj.G_lim_boost[l], 0, sbr.G_temp_prev[ch][sbr.GQ_ringbuf_index[ch]], 0, sbr.M);
                System.arraycopy(adj.Q_M_lim_boost[l], 0, sbr.Q_temp_prev[ch][sbr.GQ_ringbuf_index[ch]], 0, sbr.M);
                for (int m = 0; m < sbr.M; m++) {
                    float[] psi = new float[2];
                    float G_filt = 0.0F;
                    float Q_filt = 0.0F;
                    if (h_SL != 0) {
                        int ri = sbr.GQ_ringbuf_index[ch];
                        for (int n = 0; n <= 4; n++) {
                            float curr_h_smooth = h_smooth[n];
                            if (++ri >= 5) {
                                ri -= 5;
                            }
                            G_filt += sbr.G_temp_prev[ch][ri][m] * curr_h_smooth;
                            Q_filt += sbr.Q_temp_prev[ch][ri][m] * curr_h_smooth;
                        }
                    } else {
                        G_filt = sbr.G_temp_prev[ch][sbr.GQ_ringbuf_index[ch]][m];
                        Q_filt = sbr.Q_temp_prev[ch][sbr.GQ_ringbuf_index[ch]][m];
                    }
                    Q_filt = adj.S_M_boost[l][m] == 0.0F && !no_noise ? Q_filt : 0.0F;
                    fIndexNoise = fIndexNoise + 1 & 511;
                    Xsbr[i + sbr.tHFAdj][m + sbr.kx][0] = G_filt * Xsbr[i + sbr.tHFAdj][m + sbr.kx][0] + Q_filt * NOISE_TABLE[fIndexNoise][0];
                    if (sbr.bs_extension_id == 3 && sbr.bs_extension_data == 42) {
                        Xsbr[i + sbr.tHFAdj][m + sbr.kx][0] = 1.642832E7F;
                    }
                    Xsbr[i + sbr.tHFAdj][m + sbr.kx][1] = G_filt * Xsbr[i + sbr.tHFAdj][m + sbr.kx][1] + Q_filt * NOISE_TABLE[fIndexNoise][1];
                    int rev = (m + sbr.kx & 1) != 0 ? -1 : 1;
                    psi[0] = adj.S_M_boost[l][m] * (float) phi_re[fIndexSine];
                    Xsbr[i + sbr.tHFAdj][m + sbr.kx][0] = Xsbr[i + sbr.tHFAdj][m + sbr.kx][0] + psi[0];
                    psi[1] = (float) rev * adj.S_M_boost[l][m] * (float) phi_im[fIndexSine];
                    Xsbr[i + sbr.tHFAdj][m + sbr.kx][1] = Xsbr[i + sbr.tHFAdj][m + sbr.kx][1] + psi[1];
                }
                fIndexSine = fIndexSine + 1 & 3;
                sbr.GQ_ringbuf_index[ch]++;
                if (sbr.GQ_ringbuf_index[ch] >= 5) {
                    sbr.GQ_ringbuf_index[ch] = 0;
                }
            }
        }
        sbr.index_noise_prev[ch] = fIndexNoise;
        sbr.psi_is_prev[ch] = fIndexSine;
    }

    private static void calculate_gain(SBR sbr, HFAdjustment adj, int ch) {
        int current_t_noise_band = 0;
        float[] Q_M_lim = new float[49];
        float[] G_lim = new float[49];
        float[] S_M = new float[49];
        for (int l = 0; l < sbr.L_E[ch]; l++) {
            int current_f_noise_band = 0;
            int current_res_band = 0;
            int current_res_band2 = 0;
            int current_hi_res_band = 0;
            float delta = l != sbr.l_A[ch] && l != sbr.prevEnvIsShort[ch] ? 1.0F : 0.0F;
            int S_mapped = get_S_mapped(sbr, ch, l, current_res_band2);
            if (sbr.t_E[ch][l + 1] > sbr.t_Q[ch][current_t_noise_band + 1]) {
                current_t_noise_band++;
            }
            for (int k = 0; k < sbr.N_L[sbr.bs_limiter_bands]; k++) {
                float den = 0.0F;
                float acc1 = 0.0F;
                float acc2 = 0.0F;
                int current_res_band_size = 0;
                int ml1 = sbr.f_table_lim[sbr.bs_limiter_bands][k];
                int ml2 = sbr.f_table_lim[sbr.bs_limiter_bands][k + 1];
                for (int m = ml1; m < ml2; m++) {
                    if (m + sbr.kx == sbr.f_table_res[sbr.f[ch][l]][current_res_band + 1]) {
                        current_res_band++;
                    }
                    acc1 += sbr.E_orig[ch][current_res_band][l];
                    acc2 += sbr.E_curr[ch][m][l];
                }
                float G_max = (1.0E-12F + acc1) / (1.0E-12F + acc2) * limGain[sbr.bs_limiter_gains];
                G_max = Math.min(G_max, 1.0E10F);
                for (int var29 = ml1; var29 < ml2; var29++) {
                    if (var29 + sbr.kx == sbr.f_table_noise[current_f_noise_band + 1]) {
                        current_f_noise_band++;
                    }
                    if (var29 + sbr.kx == sbr.f_table_res[sbr.f[ch][l]][current_res_band2 + 1]) {
                        S_mapped = get_S_mapped(sbr, ch, l, ++current_res_band2);
                    }
                    if (var29 + sbr.kx == sbr.f_table_res[1][current_hi_res_band + 1]) {
                        current_hi_res_band++;
                    }
                    int S_index_mapped = 0;
                    if ((l >= sbr.l_A[ch] || sbr.bs_add_harmonic_prev[ch][current_hi_res_band] != 0 && sbr.bs_add_harmonic_flag_prev[ch]) && var29 + sbr.kx == sbr.f_table_res[1][current_hi_res_band + 1] + sbr.f_table_res[1][current_hi_res_band] >> 1) {
                        S_index_mapped = sbr.bs_add_harmonic[ch][current_hi_res_band];
                    }
                    float Q_div = sbr.Q_div[ch][current_f_noise_band][current_t_noise_band];
                    float Q_div2 = sbr.Q_div2[ch][current_f_noise_band][current_t_noise_band];
                    float Q_M = sbr.E_orig[ch][current_res_band2][l] * Q_div2;
                    if (S_index_mapped == 0) {
                        S_M[var29] = 0.0F;
                    } else {
                        S_M[var29] = sbr.E_orig[ch][current_res_band2][l] * Q_div;
                        den += S_M[var29];
                    }
                    float G = sbr.E_orig[ch][current_res_band2][l] / (1.0F + sbr.E_curr[ch][var29][l]);
                    if (S_mapped == 0 && delta == 1.0F) {
                        G *= Q_div;
                    } else if (S_mapped == 1) {
                        G *= Q_div2;
                    }
                    if (G_max > G) {
                        Q_M_lim[var29] = Q_M;
                        G_lim[var29] = G;
                    } else {
                        Q_M_lim[var29] = Q_M * G_max / G;
                        G_lim[var29] = G_max;
                    }
                    den += sbr.E_curr[ch][var29][l] * G_lim[var29];
                    if (S_index_mapped == 0 && l != sbr.l_A[ch]) {
                        den += Q_M_lim[var29];
                    }
                }
                float G_boost = (acc1 + 1.0E-12F) / (den + 1.0E-12F);
                G_boost = Math.min(G_boost, 2.5118864F);
                for (int var30 = ml1; var30 < ml2; var30++) {
                    adj.G_lim_boost[l][var30] = (float) Math.sqrt((double) (G_lim[var30] * G_boost));
                    adj.Q_M_lim_boost[l][var30] = (float) Math.sqrt((double) (Q_M_lim[var30] * G_boost));
                    if (S_M[var30] != 0.0F) {
                        adj.S_M_boost[l][var30] = (float) Math.sqrt((double) (S_M[var30] * G_boost));
                    } else {
                        adj.S_M_boost[l][var30] = 0.0F;
                    }
                }
            }
        }
    }
}