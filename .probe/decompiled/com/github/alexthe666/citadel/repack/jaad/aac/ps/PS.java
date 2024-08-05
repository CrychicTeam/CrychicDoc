package com.github.alexthe666.citadel.repack.jaad.aac.ps;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;

public class PS implements PSConstants, PSTables, HuffmanTables {

    boolean enable_iid;

    boolean enable_icc;

    boolean enable_ext;

    int iid_mode;

    int icc_mode;

    int nr_iid_par;

    int nr_ipdopd_par;

    int nr_icc_par;

    int frame_class;

    int num_env;

    int[] border_position = new int[6];

    boolean[] iid_dt = new boolean[5];

    boolean[] icc_dt = new boolean[5];

    boolean enable_ipdopd;

    int ipd_mode;

    boolean[] ipd_dt = new boolean[5];

    boolean[] opd_dt = new boolean[5];

    int[] iid_index_prev = new int[34];

    int[] icc_index_prev = new int[34];

    int[] ipd_index_prev = new int[17];

    int[] opd_index_prev = new int[17];

    int[][] iid_index = new int[5][34];

    int[][] icc_index = new int[5][34];

    int[][] ipd_index = new int[5][17];

    int[][] opd_index = new int[5][17];

    int[] ipd_index_1 = new int[17];

    int[] opd_index_1 = new int[17];

    int[] ipd_index_2 = new int[17];

    int[] opd_index_2 = new int[17];

    int ps_data_available;

    public boolean header_read;

    Filterbank hyb;

    boolean use34hybrid_bands;

    int numTimeSlotsRate;

    int num_groups;

    int num_hybrid_groups;

    int nr_par_bands;

    int nr_allpass_bands;

    int decay_cutoff;

    int[] group_border;

    int[] map_group2bk;

    int saved_delay;

    int[] delay_buf_index_ser = new int[3];

    int[] num_sample_delay_ser = new int[3];

    int[] delay_D = new int[64];

    int[] delay_buf_index_delay = new int[64];

    float[][][] delay_Qmf = new float[14][64][2];

    float[][][] delay_SubQmf = new float[2][32][2];

    float[][][][] delay_Qmf_ser = new float[3][5][64][2];

    float[][][][] delay_SubQmf_ser = new float[3][5][32][2];

    float alpha_decay;

    float alpha_smooth;

    float[] P_PeakDecayNrg = new float[34];

    float[] P_prev = new float[34];

    float[] P_SmoothPeakDecayDiffNrg_prev = new float[34];

    float[][] h11_prev = new float[50][2];

    float[][] h12_prev = new float[50][2];

    float[][] h21_prev = new float[50][2];

    float[][] h22_prev = new float[50][2];

    int phase_hist;

    float[][][] ipd_prev = new float[20][2][2];

    float[][][] opd_prev = new float[20][2][2];

    public PS(SampleFrequency sr, int numTimeSlotsRate) {
        this.hyb = new Filterbank(numTimeSlotsRate);
        this.numTimeSlotsRate = numTimeSlotsRate;
        this.ps_data_available = 0;
        this.saved_delay = 0;
        for (int i = 0; i < 64; i++) {
            this.delay_buf_index_delay[i] = 0;
        }
        for (int var5 = 0; var5 < 3; var5++) {
            this.delay_buf_index_ser[var5] = 0;
            this.num_sample_delay_ser[var5] = delay_length_d[var5];
        }
        int short_delay_band = 35;
        this.nr_allpass_bands = 22;
        this.alpha_decay = 0.7659283F;
        this.alpha_smooth = 0.25F;
        for (int var6 = 0; var6 < short_delay_band; var6++) {
            this.delay_D[var6] = 14;
        }
        for (int var7 = short_delay_band; var7 < 64; var7++) {
            this.delay_D[var7] = 1;
        }
        for (int var8 = 0; var8 < 50; var8++) {
            this.h11_prev[var8][0] = 1.0F;
            this.h12_prev[var8][1] = 1.0F;
            this.h11_prev[var8][0] = 1.0F;
            this.h12_prev[var8][1] = 1.0F;
        }
        this.phase_hist = 0;
        for (int var9 = 0; var9 < 20; var9++) {
            this.ipd_prev[var9][0][0] = 0.0F;
            this.ipd_prev[var9][0][1] = 0.0F;
            this.ipd_prev[var9][1][0] = 0.0F;
            this.ipd_prev[var9][1][1] = 0.0F;
            this.opd_prev[var9][0][0] = 0.0F;
            this.opd_prev[var9][0][1] = 0.0F;
            this.opd_prev[var9][1][0] = 0.0F;
            this.opd_prev[var9][1][1] = 0.0F;
        }
    }

    public int decode(BitStream ld) throws AACException {
        long bits = (long) ld.getPosition();
        if (ld.readBool()) {
            this.header_read = true;
            this.use34hybrid_bands = false;
            this.enable_iid = ld.readBool();
            if (this.enable_iid) {
                this.iid_mode = ld.readBits(3);
                this.nr_iid_par = nr_iid_par_tab[this.iid_mode];
                this.nr_ipdopd_par = nr_ipdopd_par_tab[this.iid_mode];
                if (this.iid_mode == 2 || this.iid_mode == 5) {
                    this.use34hybrid_bands = true;
                }
                this.ipd_mode = this.iid_mode;
            }
            this.enable_icc = ld.readBool();
            if (this.enable_icc) {
                this.icc_mode = ld.readBits(3);
                this.nr_icc_par = nr_icc_par_tab[this.icc_mode];
                if (this.icc_mode == 2 || this.icc_mode == 5) {
                    this.use34hybrid_bands = true;
                }
            }
            this.enable_ext = ld.readBool();
        }
        if (!this.header_read) {
            this.ps_data_available = 0;
            return 1;
        } else {
            this.frame_class = ld.readBit();
            int tmp = ld.readBits(2);
            this.num_env = num_env_tab[this.frame_class][tmp];
            if (this.frame_class != 0) {
                for (int n = 1; n < this.num_env + 1; n++) {
                    this.border_position[n] = ld.readBits(5) + 1;
                }
            }
            if (this.enable_iid) {
                for (int n = 0; n < this.num_env; n++) {
                    this.iid_dt[n] = ld.readBool();
                    if (this.iid_mode < 3) {
                        this.huff_data(ld, this.iid_dt[n], this.nr_iid_par, t_huff_iid_def, f_huff_iid_def, this.iid_index[n]);
                    } else {
                        this.huff_data(ld, this.iid_dt[n], this.nr_iid_par, t_huff_iid_fine, f_huff_iid_fine, this.iid_index[n]);
                    }
                }
            }
            if (this.enable_icc) {
                for (int nx = 0; nx < this.num_env; nx++) {
                    this.icc_dt[nx] = ld.readBool();
                    this.huff_data(ld, this.icc_dt[nx], this.nr_icc_par, t_huff_icc, f_huff_icc, this.icc_index[nx]);
                }
            }
            if (this.enable_ext) {
                int cnt = ld.readBits(4);
                if (cnt == 15) {
                    cnt += ld.readBits(8);
                }
                int num_bits_left = 8 * cnt;
                while (num_bits_left > 7) {
                    int ps_extension_id = ld.readBits(2);
                    num_bits_left -= 2;
                    num_bits_left -= this.ps_extension(ld, ps_extension_id, num_bits_left);
                }
                ld.skipBits(num_bits_left);
            }
            int bits2 = (int) ((long) ld.getPosition() - bits);
            this.ps_data_available = 1;
            return bits2;
        }
    }

    private int ps_extension(BitStream ld, int ps_extension_id, int num_bits_left) throws AACException {
        long bits = (long) ld.getPosition();
        if (ps_extension_id == 0) {
            this.enable_ipdopd = ld.readBool();
            if (this.enable_ipdopd) {
                for (int n = 0; n < this.num_env; n++) {
                    this.ipd_dt[n] = ld.readBool();
                    this.huff_data(ld, this.ipd_dt[n], this.nr_ipdopd_par, t_huff_ipd, f_huff_ipd, this.ipd_index[n]);
                    this.opd_dt[n] = ld.readBool();
                    this.huff_data(ld, this.opd_dt[n], this.nr_ipdopd_par, t_huff_opd, f_huff_opd, this.opd_index[n]);
                }
            }
            ld.readBit();
        }
        return (int) ((long) ld.getPosition() - bits);
    }

    private void huff_data(BitStream ld, boolean dt, int nr_par, int[][] t_huff, int[][] f_huff, int[] par) throws AACException {
        if (dt) {
            for (int n = 0; n < nr_par; n++) {
                par[n] = this.ps_huff_dec(ld, t_huff);
            }
        } else {
            par[0] = this.ps_huff_dec(ld, f_huff);
            for (int n = 1; n < nr_par; n++) {
                par[n] = this.ps_huff_dec(ld, f_huff);
            }
        }
    }

    private int ps_huff_dec(BitStream ld, int[][] t_huff) throws AACException {
        int index = 0;
        while (index >= 0) {
            int bit = ld.readBit();
            index = t_huff[index][bit];
        }
        return index + 31;
    }

    private int delta_clip(int i, int min, int max) {
        if (i < min) {
            return min;
        } else {
            return i > max ? max : i;
        }
    }

    private void delta_decode(boolean enable, int[] index, int[] index_prev, boolean dt_flag, int nr_par, int stride, int min_index, int max_index) {
        if (enable) {
            if (!dt_flag) {
                index[0] += 0;
                index[0] = this.delta_clip(index[0], min_index, max_index);
                for (int i = 1; i < nr_par; i++) {
                    index[i] += index[i - 1];
                    index[i] = this.delta_clip(index[i], min_index, max_index);
                }
            } else {
                for (int i = 0; i < nr_par; i++) {
                    index[i] += index_prev[i * stride];
                    index[i] = this.delta_clip(index[i], min_index, max_index);
                }
            }
        } else {
            for (int i = 0; i < nr_par; i++) {
                index[i] = 0;
            }
        }
        if (stride == 2) {
            for (int var12 = (nr_par << 1) - 1; var12 > 0; var12--) {
                index[var12] = index[var12 >> 1];
            }
        }
    }

    private void delta_modulo_decode(boolean enable, int[] index, int[] index_prev, boolean dt_flag, int nr_par, int stride, int and_modulo) {
        if (enable) {
            if (!dt_flag) {
                index[0] += 0;
                index[0] &= and_modulo;
                for (int i = 1; i < nr_par; i++) {
                    index[i] += index[i - 1];
                    index[i] &= and_modulo;
                }
            } else {
                for (int i = 0; i < nr_par; i++) {
                    index[i] += index_prev[i * stride];
                    index[i] &= and_modulo;
                }
            }
        } else {
            for (int i = 0; i < nr_par; i++) {
                index[i] = 0;
            }
        }
        if (stride == 2) {
            index[0] = 0;
            for (int var11 = (nr_par << 1) - 1; var11 > 0; var11--) {
                index[var11] = index[var11 >> 1];
            }
        }
    }

    private void map20indexto34(int[] index, int bins) {
        index[1] = (index[0] + index[1]) / 2;
        index[2] = index[1];
        index[3] = index[2];
        index[4] = (index[2] + index[3]) / 2;
        index[5] = index[3];
        index[6] = index[4];
        index[7] = index[4];
        index[8] = index[5];
        index[9] = index[5];
        index[10] = index[6];
        index[11] = index[7];
        index[12] = index[8];
        index[13] = index[8];
        index[14] = index[9];
        index[15] = index[9];
        index[16] = index[10];
        if (bins == 34) {
            index[17] = index[11];
            index[18] = index[12];
            index[19] = index[13];
            index[20] = index[14];
            index[21] = index[14];
            index[22] = index[15];
            index[23] = index[15];
            index[24] = index[16];
            index[25] = index[16];
            index[26] = index[17];
            index[27] = index[17];
            index[28] = index[18];
            index[29] = index[18];
            index[30] = index[18];
            index[31] = index[18];
            index[32] = index[19];
            index[33] = index[19];
        }
    }

    private void ps_data_decode() {
        if (this.ps_data_available == 0) {
            this.num_env = 0;
        }
        for (int env = 0; env < this.num_env; env++) {
            int num_iid_steps = this.iid_mode < 3 ? 7 : 15;
            int[] iid_index_prev;
            int[] icc_index_prev;
            int[] ipd_index_prev;
            int[] opd_index_prev;
            if (env == 0) {
                iid_index_prev = this.iid_index_prev;
                icc_index_prev = this.icc_index_prev;
                ipd_index_prev = this.ipd_index_prev;
                opd_index_prev = this.opd_index_prev;
            } else {
                iid_index_prev = this.iid_index[env - 1];
                icc_index_prev = this.icc_index[env - 1];
                ipd_index_prev = this.ipd_index[env - 1];
                opd_index_prev = this.opd_index[env - 1];
            }
            this.delta_decode(this.enable_iid, this.iid_index[env], iid_index_prev, this.iid_dt[env], this.nr_iid_par, this.iid_mode != 0 && this.iid_mode != 3 ? 1 : 2, -num_iid_steps, num_iid_steps);
            this.delta_decode(this.enable_icc, this.icc_index[env], icc_index_prev, this.icc_dt[env], this.nr_icc_par, this.icc_mode != 0 && this.icc_mode != 3 ? 1 : 2, 0, 7);
            this.delta_modulo_decode(this.enable_ipdopd, this.ipd_index[env], ipd_index_prev, this.ipd_dt[env], this.nr_ipdopd_par, 1, 7);
            this.delta_modulo_decode(this.enable_ipdopd, this.opd_index[env], opd_index_prev, this.opd_dt[env], this.nr_ipdopd_par, 1, 7);
        }
        if (this.num_env == 0) {
            this.num_env = 1;
            if (this.enable_iid) {
                for (int bin = 0; bin < 34; bin++) {
                    this.iid_index[0][bin] = this.iid_index_prev[bin];
                }
            } else {
                for (int bin = 0; bin < 34; bin++) {
                    this.iid_index[0][bin] = 0;
                }
            }
            if (this.enable_icc) {
                for (int var13 = 0; var13 < 34; var13++) {
                    this.icc_index[0][var13] = this.icc_index_prev[var13];
                }
            } else {
                for (int var12 = 0; var12 < 34; var12++) {
                    this.icc_index[0][var12] = 0;
                }
            }
            if (this.enable_ipdopd) {
                for (int var14 = 0; var14 < 17; var14++) {
                    this.ipd_index[0][var14] = this.ipd_index_prev[var14];
                    this.opd_index[0][var14] = this.opd_index_prev[var14];
                }
            } else {
                for (int var15 = 0; var15 < 17; var15++) {
                    this.ipd_index[0][var15] = 0;
                    this.opd_index[0][var15] = 0;
                }
            }
        }
        for (int bin = 0; bin < 34; bin++) {
            this.iid_index_prev[bin] = this.iid_index[this.num_env - 1][bin];
        }
        for (int var17 = 0; var17 < 34; var17++) {
            this.icc_index_prev[var17] = this.icc_index[this.num_env - 1][var17];
        }
        for (int var18 = 0; var18 < 17; var18++) {
            this.ipd_index_prev[var18] = this.ipd_index[this.num_env - 1][var18];
            this.opd_index_prev[var18] = this.opd_index[this.num_env - 1][var18];
        }
        this.ps_data_available = 0;
        if (this.frame_class == 0) {
            this.border_position[0] = 0;
            for (int var8 = 1; var8 < this.num_env; var8++) {
                this.border_position[var8] = var8 * this.numTimeSlotsRate / this.num_env;
            }
            this.border_position[this.num_env] = this.numTimeSlotsRate;
        } else {
            this.border_position[0] = 0;
            if (this.border_position[this.num_env] < this.numTimeSlotsRate) {
                for (int var19 = 0; var19 < 34; var19++) {
                    this.iid_index[this.num_env][var19] = this.iid_index[this.num_env - 1][var19];
                    this.icc_index[this.num_env][var19] = this.icc_index[this.num_env - 1][var19];
                }
                for (int var20 = 0; var20 < 17; var20++) {
                    this.ipd_index[this.num_env][var20] = this.ipd_index[this.num_env - 1][var20];
                    this.opd_index[this.num_env][var20] = this.opd_index[this.num_env - 1][var20];
                }
                this.num_env++;
                this.border_position[this.num_env] = this.numTimeSlotsRate;
            }
            for (int var9 = 1; var9 < this.num_env; var9++) {
                int thr = this.numTimeSlotsRate - (this.num_env - var9);
                if (this.border_position[var9] > thr) {
                    this.border_position[var9] = thr;
                } else {
                    thr = this.border_position[var9 - 1] + 1;
                    if (this.border_position[var9] < thr) {
                        this.border_position[var9] = thr;
                    }
                }
            }
        }
        if (this.use34hybrid_bands) {
            for (int var10 = 0; var10 < this.num_env; var10++) {
                if (this.iid_mode != 2 && this.iid_mode != 5) {
                    this.map20indexto34(this.iid_index[var10], 34);
                }
                if (this.icc_mode != 2 && this.icc_mode != 5) {
                    this.map20indexto34(this.icc_index[var10], 34);
                }
                if (this.ipd_mode != 2 && this.ipd_mode != 5) {
                    this.map20indexto34(this.ipd_index[var10], 17);
                    this.map20indexto34(this.opd_index[var10], 17);
                }
            }
        }
    }

    private void ps_decorrelate(float[][][] X_left, float[][][] X_right, float[][][] X_hybrid_left, float[][][] X_hybrid_right) {
        int temp_delay = 0;
        int[] temp_delay_ser = new int[3];
        float[][] P = new float[32][34];
        float[][] G_TransientRatio = new float[32][34];
        float[] inputLeft = new float[2];
        float[][] Phi_Fract_SubQmf;
        if (this.use34hybrid_bands) {
            Phi_Fract_SubQmf = Phi_Fract_SubQmf34;
        } else {
            Phi_Fract_SubQmf = Phi_Fract_SubQmf20;
        }
        for (int n = 0; n < 32; n++) {
            for (int bk = 0; bk < 34; bk++) {
                P[n][bk] = 0.0F;
            }
        }
        for (int gr = 0; gr < this.num_groups; gr++) {
            int bk = -4097 & this.map_group2bk[gr];
            int maxsb = gr < this.num_hybrid_groups ? this.group_border[gr] + 1 : this.group_border[gr + 1];
            for (int sb = this.group_border[gr]; sb < maxsb; sb++) {
                for (int var28 = this.border_position[0]; var28 < this.border_position[this.num_env]; var28++) {
                    if (gr < this.num_hybrid_groups) {
                        inputLeft[0] = X_hybrid_left[var28][sb][0];
                        inputLeft[1] = X_hybrid_left[var28][sb][1];
                    } else {
                        inputLeft[0] = X_left[var28][sb][0];
                        inputLeft[1] = X_left[var28][sb][1];
                    }
                    P[var28][bk] = P[var28][bk] + inputLeft[0] * inputLeft[0] + inputLeft[1] * inputLeft[1];
                }
            }
        }
        for (int bk = 0; bk < this.nr_par_bands; bk++) {
            for (int var29 = this.border_position[0]; var29 < this.border_position[this.num_env]; var29++) {
                float gamma = 1.5F;
                this.P_PeakDecayNrg[bk] = this.P_PeakDecayNrg[bk] * this.alpha_decay;
                if (this.P_PeakDecayNrg[bk] < P[var29][bk]) {
                    this.P_PeakDecayNrg[bk] = P[var29][bk];
                }
                float P_SmoothPeakDecayDiffNrg = this.P_SmoothPeakDecayDiffNrg_prev[bk];
                P_SmoothPeakDecayDiffNrg += (this.P_PeakDecayNrg[bk] - P[var29][bk] - this.P_SmoothPeakDecayDiffNrg_prev[bk]) * this.alpha_smooth;
                this.P_SmoothPeakDecayDiffNrg_prev[bk] = P_SmoothPeakDecayDiffNrg;
                float nrg = this.P_prev[bk];
                nrg += (P[var29][bk] - this.P_prev[bk]) * this.alpha_smooth;
                this.P_prev[bk] = nrg;
                if (P_SmoothPeakDecayDiffNrg * gamma <= nrg) {
                    G_TransientRatio[var29][bk] = 1.0F;
                } else {
                    G_TransientRatio[var29][bk] = nrg / (P_SmoothPeakDecayDiffNrg * gamma);
                }
            }
        }
        for (int var27 = 0; var27 < this.num_groups; var27++) {
            int maxsb;
            if (var27 < this.num_hybrid_groups) {
                maxsb = this.group_border[var27] + 1;
            } else {
                maxsb = this.group_border[var27 + 1];
            }
            for (int sb = this.group_border[var27]; sb < maxsb; sb++) {
                float[] g_DecaySlope_filt = new float[3];
                float g_DecaySlope;
                if (var27 >= this.num_hybrid_groups && sb > this.decay_cutoff) {
                    int decay = this.decay_cutoff - sb;
                    if (decay <= -20) {
                        g_DecaySlope = 0.0F;
                    } else {
                        g_DecaySlope = 1.0F + 0.05F * (float) decay;
                    }
                } else {
                    g_DecaySlope = 1.0F;
                }
                for (int m = 0; m < 3; m++) {
                    g_DecaySlope_filt[m] = g_DecaySlope * filter_a[m];
                }
                temp_delay = this.saved_delay;
                for (int var30 = 0; var30 < 3; var30++) {
                    temp_delay_ser[var30] = this.delay_buf_index_ser[var30];
                }
                for (int var31 = this.border_position[0]; var31 < this.border_position[this.num_env]; var31++) {
                    float[] tmp = new float[2];
                    float[] tmp0 = new float[2];
                    float[] R0 = new float[2];
                    if (var27 < this.num_hybrid_groups) {
                        inputLeft[0] = X_hybrid_left[var31][sb][0];
                        inputLeft[1] = X_hybrid_left[var31][sb][1];
                    } else {
                        inputLeft[0] = X_left[var31][sb][0];
                        inputLeft[1] = X_left[var31][sb][1];
                    }
                    if (sb > this.nr_allpass_bands && var27 >= this.num_hybrid_groups) {
                        tmp[0] = this.delay_Qmf[this.delay_buf_index_delay[sb]][sb][0];
                        tmp[1] = this.delay_Qmf[this.delay_buf_index_delay[sb]][sb][1];
                        R0[0] = tmp[0];
                        R0[1] = tmp[1];
                        this.delay_Qmf[this.delay_buf_index_delay[sb]][sb][0] = inputLeft[0];
                        this.delay_Qmf[this.delay_buf_index_delay[sb]][sb][1] = inputLeft[1];
                    } else {
                        float[] Phi_Fract = new float[2];
                        if (var27 < this.num_hybrid_groups) {
                            tmp0[0] = this.delay_SubQmf[temp_delay][sb][0];
                            tmp0[1] = this.delay_SubQmf[temp_delay][sb][1];
                            this.delay_SubQmf[temp_delay][sb][0] = inputLeft[0];
                            this.delay_SubQmf[temp_delay][sb][1] = inputLeft[1];
                            Phi_Fract[0] = Phi_Fract_SubQmf[sb][0];
                            Phi_Fract[1] = Phi_Fract_SubQmf[sb][1];
                        } else {
                            tmp0[0] = this.delay_Qmf[temp_delay][sb][0];
                            tmp0[1] = this.delay_Qmf[temp_delay][sb][1];
                            this.delay_Qmf[temp_delay][sb][0] = inputLeft[0];
                            this.delay_Qmf[temp_delay][sb][1] = inputLeft[1];
                            Phi_Fract[0] = Phi_Fract_Qmf[sb][0];
                            Phi_Fract[1] = Phi_Fract_Qmf[sb][1];
                        }
                        tmp[0] = tmp[0] * Phi_Fract[0] + tmp0[1] * Phi_Fract[1];
                        tmp[1] = tmp0[1] * Phi_Fract[0] - tmp0[0] * Phi_Fract[1];
                        R0[0] = tmp[0];
                        R0[1] = tmp[1];
                        for (int var32 = 0; var32 < 3; var32++) {
                            float[] Q_Fract_allpass = new float[2];
                            float[] tmp2 = new float[2];
                            if (var27 < this.num_hybrid_groups) {
                                tmp0[0] = this.delay_SubQmf_ser[var32][temp_delay_ser[var32]][sb][0];
                                tmp0[1] = this.delay_SubQmf_ser[var32][temp_delay_ser[var32]][sb][1];
                                if (this.use34hybrid_bands) {
                                    Q_Fract_allpass[0] = Q_Fract_allpass_SubQmf34[sb][var32][0];
                                    Q_Fract_allpass[1] = Q_Fract_allpass_SubQmf34[sb][var32][1];
                                } else {
                                    Q_Fract_allpass[0] = Q_Fract_allpass_SubQmf20[sb][var32][0];
                                    Q_Fract_allpass[1] = Q_Fract_allpass_SubQmf20[sb][var32][1];
                                }
                            } else {
                                tmp0[0] = this.delay_Qmf_ser[var32][temp_delay_ser[var32]][sb][0];
                                tmp0[1] = this.delay_Qmf_ser[var32][temp_delay_ser[var32]][sb][1];
                                Q_Fract_allpass[0] = Q_Fract_allpass_Qmf[sb][var32][0];
                                Q_Fract_allpass[1] = Q_Fract_allpass_Qmf[sb][var32][1];
                            }
                            tmp[0] = tmp0[0] * Q_Fract_allpass[0] + tmp0[1] * Q_Fract_allpass[1];
                            tmp[1] = tmp0[1] * Q_Fract_allpass[0] - tmp0[0] * Q_Fract_allpass[1];
                            tmp[0] += -(g_DecaySlope_filt[var32] * R0[0]);
                            tmp[1] += -(g_DecaySlope_filt[var32] * R0[1]);
                            tmp2[0] = R0[0] + g_DecaySlope_filt[var32] * tmp[0];
                            tmp2[1] = R0[1] + g_DecaySlope_filt[var32] * tmp[1];
                            if (var27 < this.num_hybrid_groups) {
                                this.delay_SubQmf_ser[var32][temp_delay_ser[var32]][sb][0] = tmp2[0];
                                this.delay_SubQmf_ser[var32][temp_delay_ser[var32]][sb][1] = tmp2[1];
                            } else {
                                this.delay_Qmf_ser[var32][temp_delay_ser[var32]][sb][0] = tmp2[0];
                                this.delay_Qmf_ser[var32][temp_delay_ser[var32]][sb][1] = tmp2[1];
                            }
                            R0[0] = tmp[0];
                            R0[1] = tmp[1];
                        }
                    }
                    int var37 = -4097 & this.map_group2bk[var27];
                    R0[0] = G_TransientRatio[var31][var37] * R0[0];
                    R0[1] = G_TransientRatio[var31][var37] * R0[1];
                    if (var27 < this.num_hybrid_groups) {
                        X_hybrid_right[var31][sb][0] = R0[0];
                        X_hybrid_right[var31][sb][1] = R0[1];
                    } else {
                        X_right[var31][sb][0] = R0[0];
                        X_right[var31][sb][1] = R0[1];
                    }
                    if (++temp_delay >= 2) {
                        temp_delay = 0;
                    }
                    if (sb > this.nr_allpass_bands && var27 >= this.num_hybrid_groups && ++this.delay_buf_index_delay[sb] >= this.delay_D[sb]) {
                        this.delay_buf_index_delay[sb] = 0;
                    }
                    for (int var33 = 0; var33 < 3; var33++) {
                        if (++temp_delay_ser[var33] >= this.num_sample_delay_ser[var33]) {
                            temp_delay_ser[var33] = 0;
                        }
                    }
                }
            }
        }
        this.saved_delay = temp_delay;
        for (int m = 0; m < 3; m++) {
            this.delay_buf_index_ser[m] = temp_delay_ser[m];
        }
    }

    private float magnitude_c(float[] c) {
        return (float) Math.sqrt((double) (c[0] * c[0] + c[1] * c[1]));
    }

    private void ps_mix_phase(float[][][] X_left, float[][][] X_right, float[][][] X_hybrid_left, float[][][] X_hybrid_right) {
        int bk = 0;
        float[] h11 = new float[2];
        float[] h12 = new float[2];
        float[] h21 = new float[2];
        float[] h22 = new float[2];
        float[] H11 = new float[2];
        float[] H12 = new float[2];
        float[] H21 = new float[2];
        float[] H22 = new float[2];
        float[] deltaH11 = new float[2];
        float[] deltaH12 = new float[2];
        float[] deltaH21 = new float[2];
        float[] deltaH22 = new float[2];
        float[] tempLeft = new float[2];
        float[] tempRight = new float[2];
        float[] phaseLeft = new float[2];
        float[] phaseRight = new float[2];
        float[] sf_iid;
        int no_iid_steps;
        if (this.iid_mode >= 3) {
            no_iid_steps = 15;
            sf_iid = sf_iid_fine;
        } else {
            no_iid_steps = 7;
            sf_iid = sf_iid_normal;
        }
        int nr_ipdopd_par;
        if (this.ipd_mode != 0 && this.ipd_mode != 3) {
            nr_ipdopd_par = this.nr_ipdopd_par;
        } else {
            nr_ipdopd_par = 11;
        }
        for (int gr = 0; gr < this.num_groups; gr++) {
            bk = -4097 & this.map_group2bk[gr];
            int maxsb = gr < this.num_hybrid_groups ? this.group_border[gr] + 1 : this.group_border[gr + 1];
            for (int env = 0; env < this.num_env; env++) {
                if (this.icc_mode < 3) {
                    float c_1 = sf_iid[no_iid_steps + this.iid_index[env][bk]];
                    float c_2 = sf_iid[no_iid_steps - this.iid_index[env][bk]];
                    float cosa = cos_alphas[this.icc_index[env][bk]];
                    float sina = sin_alphas[this.icc_index[env][bk]];
                    float cosb;
                    float sinb;
                    if (this.iid_mode >= 3) {
                        if (this.iid_index[env][bk] < 0) {
                            cosb = cos_betas_fine[-this.iid_index[env][bk]][this.icc_index[env][bk]];
                            sinb = -sin_betas_fine[-this.iid_index[env][bk]][this.icc_index[env][bk]];
                        } else {
                            cosb = cos_betas_fine[this.iid_index[env][bk]][this.icc_index[env][bk]];
                            sinb = sin_betas_fine[this.iid_index[env][bk]][this.icc_index[env][bk]];
                        }
                    } else if (this.iid_index[env][bk] < 0) {
                        cosb = cos_betas_normal[-this.iid_index[env][bk]][this.icc_index[env][bk]];
                        sinb = -sin_betas_normal[-this.iid_index[env][bk]][this.icc_index[env][bk]];
                    } else {
                        cosb = cos_betas_normal[this.iid_index[env][bk]][this.icc_index[env][bk]];
                        sinb = sin_betas_normal[this.iid_index[env][bk]][this.icc_index[env][bk]];
                    }
                    float ab1 = cosb * cosa;
                    float ab2 = sinb * sina;
                    float ab3 = sinb * cosa;
                    float ab4 = cosb * sina;
                    h11[0] = c_2 * (ab1 - ab2);
                    h12[0] = c_1 * (ab1 + ab2);
                    h21[0] = c_2 * (ab3 + ab4);
                    h22[0] = c_1 * (ab3 - ab4);
                } else {
                    float sina;
                    float cosa;
                    float cosg;
                    float sing;
                    if (this.iid_mode >= 3) {
                        int abs_iid = Math.abs(this.iid_index[env][bk]);
                        cosa = sincos_alphas_B_fine[no_iid_steps + this.iid_index[env][bk]][this.icc_index[env][bk]];
                        sina = sincos_alphas_B_fine[30 - (no_iid_steps + this.iid_index[env][bk])][this.icc_index[env][bk]];
                        cosg = cos_gammas_fine[abs_iid][this.icc_index[env][bk]];
                        sing = sin_gammas_fine[abs_iid][this.icc_index[env][bk]];
                    } else {
                        int abs_iid = Math.abs(this.iid_index[env][bk]);
                        cosa = sincos_alphas_B_normal[no_iid_steps + this.iid_index[env][bk]][this.icc_index[env][bk]];
                        sina = sincos_alphas_B_normal[14 - (no_iid_steps + this.iid_index[env][bk])][this.icc_index[env][bk]];
                        cosg = cos_gammas_normal[abs_iid][this.icc_index[env][bk]];
                        sing = sin_gammas_normal[abs_iid][this.icc_index[env][bk]];
                    }
                    h11[0] = 1.4142135F * cosa * cosg;
                    h12[0] = 1.4142135F * sina * cosg;
                    h21[0] = 1.4142135F * -cosa * sing;
                    h22[0] = 1.4142135F * sina * sing;
                }
                if (this.enable_ipdopd && bk < nr_ipdopd_par) {
                    int i = this.phase_hist;
                    tempLeft[0] = this.ipd_prev[bk][i][0] * 0.25F;
                    tempLeft[1] = this.ipd_prev[bk][i][1] * 0.25F;
                    tempRight[0] = this.opd_prev[bk][i][0] * 0.25F;
                    tempRight[1] = this.opd_prev[bk][i][1] * 0.25F;
                    this.ipd_prev[bk][i][0] = ipdopd_cos_tab[Math.abs(this.ipd_index[env][bk])];
                    this.ipd_prev[bk][i][1] = ipdopd_sin_tab[Math.abs(this.ipd_index[env][bk])];
                    this.opd_prev[bk][i][0] = ipdopd_cos_tab[Math.abs(this.opd_index[env][bk])];
                    this.opd_prev[bk][i][1] = ipdopd_sin_tab[Math.abs(this.opd_index[env][bk])];
                    tempLeft[0] += this.ipd_prev[bk][i][0];
                    tempLeft[1] += this.ipd_prev[bk][i][1];
                    tempRight[0] += this.opd_prev[bk][i][0];
                    tempRight[1] += this.opd_prev[bk][i][1];
                    if (i == 0) {
                        i = 2;
                    }
                    tempLeft[0] += this.ipd_prev[bk][--i][0] * 0.5F;
                    tempLeft[1] += this.ipd_prev[bk][i][1] * 0.5F;
                    tempRight[0] += this.opd_prev[bk][i][0] * 0.5F;
                    tempRight[1] += this.opd_prev[bk][i][1] * 0.5F;
                    float xy = this.magnitude_c(tempRight);
                    float pq = this.magnitude_c(tempLeft);
                    if (xy != 0.0F) {
                        phaseLeft[0] = tempRight[0] / xy;
                        phaseLeft[1] = tempRight[1] / xy;
                    } else {
                        phaseLeft[0] = 0.0F;
                        phaseLeft[1] = 0.0F;
                    }
                    float xypq = xy * pq;
                    if (xypq != 0.0F) {
                        float tmp1 = tempRight[0] * tempLeft[0] + tempRight[1] * tempLeft[1];
                        float tmp2 = tempRight[1] * tempLeft[0] - tempRight[0] * tempLeft[1];
                        phaseRight[0] = tmp1 / xypq;
                        phaseRight[1] = tmp2 / xypq;
                    } else {
                        phaseRight[0] = 0.0F;
                        phaseRight[1] = 0.0F;
                    }
                    h11[1] = h11[0] * phaseLeft[1];
                    h12[1] = h12[0] * phaseRight[1];
                    h21[1] = h21[0] * phaseLeft[1];
                    h22[1] = h22[0] * phaseRight[1];
                    h11[0] *= phaseLeft[0];
                    h12[0] *= phaseRight[0];
                    h21[0] *= phaseLeft[0];
                    h22[0] *= phaseRight[0];
                }
                float L = (float) (this.border_position[env + 1] - this.border_position[env]);
                deltaH11[0] = (h11[0] - this.h11_prev[gr][0]) / L;
                deltaH12[0] = (h12[0] - this.h12_prev[gr][0]) / L;
                deltaH21[0] = (h21[0] - this.h21_prev[gr][0]) / L;
                deltaH22[0] = (h22[0] - this.h22_prev[gr][0]) / L;
                H11[0] = this.h11_prev[gr][0];
                H12[0] = this.h12_prev[gr][0];
                H21[0] = this.h21_prev[gr][0];
                H22[0] = this.h22_prev[gr][0];
                this.h11_prev[gr][0] = h11[0];
                this.h12_prev[gr][0] = h12[0];
                this.h21_prev[gr][0] = h21[0];
                this.h22_prev[gr][0] = h22[0];
                if (this.enable_ipdopd && bk < nr_ipdopd_par) {
                    deltaH11[1] = (h11[1] - this.h11_prev[gr][1]) / L;
                    deltaH12[1] = (h12[1] - this.h12_prev[gr][1]) / L;
                    deltaH21[1] = (h21[1] - this.h21_prev[gr][1]) / L;
                    deltaH22[1] = (h22[1] - this.h22_prev[gr][1]) / L;
                    H11[1] = this.h11_prev[gr][1];
                    H12[1] = this.h12_prev[gr][1];
                    H21[1] = this.h21_prev[gr][1];
                    H22[1] = this.h22_prev[gr][1];
                    if ((4096 & this.map_group2bk[gr]) != 0) {
                        deltaH11[1] = -deltaH11[1];
                        deltaH12[1] = -deltaH12[1];
                        deltaH21[1] = -deltaH21[1];
                        deltaH22[1] = -deltaH22[1];
                        H11[1] = -H11[1];
                        H12[1] = -H12[1];
                        H21[1] = -H21[1];
                        H22[1] = -H22[1];
                    }
                    this.h11_prev[gr][1] = h11[1];
                    this.h12_prev[gr][1] = h12[1];
                    this.h21_prev[gr][1] = h21[1];
                    this.h22_prev[gr][1] = h22[1];
                }
                for (int n = this.border_position[env]; n < this.border_position[env + 1]; n++) {
                    H11[0] += deltaH11[0];
                    H12[0] += deltaH12[0];
                    H21[0] += deltaH21[0];
                    H22[0] += deltaH22[0];
                    if (this.enable_ipdopd && bk < nr_ipdopd_par) {
                        H11[1] += deltaH11[1];
                        H12[1] += deltaH12[1];
                        H21[1] += deltaH21[1];
                        H22[1] += deltaH22[1];
                    }
                    for (int sb = this.group_border[gr]; sb < maxsb; sb++) {
                        float[] inLeft = new float[2];
                        float[] inRight = new float[2];
                        if (gr < this.num_hybrid_groups) {
                            inLeft[0] = X_hybrid_left[n][sb][0];
                            inLeft[1] = X_hybrid_left[n][sb][1];
                            inRight[0] = X_hybrid_right[n][sb][0];
                            inRight[1] = X_hybrid_right[n][sb][1];
                        } else {
                            inLeft[0] = X_left[n][sb][0];
                            inLeft[1] = X_left[n][sb][1];
                            inRight[0] = X_right[n][sb][0];
                            inRight[1] = X_right[n][sb][1];
                        }
                        tempLeft[0] = H11[0] * inLeft[0] + H21[0] * inRight[0];
                        tempLeft[1] = H11[0] * inLeft[1] + H21[0] * inRight[1];
                        tempRight[0] = H12[0] * inLeft[0] + H22[0] * inRight[0];
                        tempRight[1] = H12[0] * inLeft[1] + H22[0] * inRight[1];
                        if (this.enable_ipdopd && bk < nr_ipdopd_par) {
                            tempLeft[0] -= H11[1] * inLeft[1] + H21[1] * inRight[1];
                            tempLeft[1] += H11[1] * inLeft[0] + H21[1] * inRight[0];
                            tempRight[0] -= H12[1] * inLeft[1] + H22[1] * inRight[1];
                            tempRight[1] += H12[1] * inLeft[0] + H22[1] * inRight[0];
                        }
                        if (gr < this.num_hybrid_groups) {
                            X_hybrid_left[n][sb][0] = tempLeft[0];
                            X_hybrid_left[n][sb][1] = tempLeft[1];
                            X_hybrid_right[n][sb][0] = tempRight[0];
                            X_hybrid_right[n][sb][1] = tempRight[1];
                        } else {
                            X_left[n][sb][0] = tempLeft[0];
                            X_left[n][sb][1] = tempLeft[1];
                            X_right[n][sb][0] = tempRight[0];
                            X_right[n][sb][1] = tempRight[1];
                        }
                    }
                }
                this.phase_hist++;
                if (this.phase_hist == 2) {
                    this.phase_hist = 0;
                }
            }
        }
    }

    public int process(float[][][] X_left, float[][][] X_right) {
        float[][][] X_hybrid_left = new float[32][32][2];
        float[][][] X_hybrid_right = new float[32][32][2];
        this.ps_data_decode();
        if (this.use34hybrid_bands) {
            this.group_border = group_border34;
            this.map_group2bk = map_group2bk34;
            this.num_groups = 50;
            this.num_hybrid_groups = 32;
            this.nr_par_bands = 34;
            this.decay_cutoff = 5;
        } else {
            this.group_border = group_border20;
            this.map_group2bk = map_group2bk20;
            this.num_groups = 22;
            this.num_hybrid_groups = 10;
            this.nr_par_bands = 20;
            this.decay_cutoff = 3;
        }
        this.hyb.hybrid_analysis(X_left, X_hybrid_left, this.use34hybrid_bands, this.numTimeSlotsRate);
        this.ps_decorrelate(X_left, X_right, X_hybrid_left, X_hybrid_right);
        this.ps_mix_phase(X_left, X_right, X_hybrid_left, X_hybrid_right);
        this.hyb.hybrid_synthesis(X_left, X_hybrid_left, this.use34hybrid_bands, this.numTimeSlotsRate);
        this.hyb.hybrid_synthesis(X_right, X_hybrid_right, this.use34hybrid_bands, this.numTimeSlotsRate);
        return 0;
    }
}