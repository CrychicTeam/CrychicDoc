package com.github.alexthe666.citadel.repack.jcodec.codecs.aac.blocks;

import com.github.alexthe666.citadel.repack.jcodec.codecs.aac.Profile;
import com.github.alexthe666.citadel.repack.jcodec.codecs.prores.ProresDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLCBuilder;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class BlockICS extends Block {

    private boolean commonWindow;

    private boolean scaleFlag;

    private Profile profile;

    private int samplingIndex;

    private static VLC[] spectral = new VLC[] { VLCBuilder.createVLCBuilder(AACTab.codes1, AACTab.bits1, AACTab.codebook_vector02_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes2, AACTab.bits2, AACTab.codebook_vector02_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes3, AACTab.bits3, AACTab.codebook_vector02_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes4, AACTab.bits4, AACTab.codebook_vector02_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes5, AACTab.bits5, AACTab.codebook_vector4_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes6, AACTab.bits6, AACTab.codebook_vector4_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes7, AACTab.bits7, AACTab.codebook_vector6_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes8, AACTab.bits8, AACTab.codebook_vector6_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes9, AACTab.bits9, AACTab.codebook_vector8_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes10, AACTab.bits10, AACTab.codebook_vector8_idx).getVLC(), VLCBuilder.createVLCBuilder(AACTab.codes11, AACTab.bits11, AACTab.codebook_vector10_idx).getVLC() };

    private static VLC vlc = new VLC(AACTab.ff_aac_scalefactor_code, AACTab.ff_aac_scalefactor_bits);

    float[][] ff_aac_codebook_vector_vals = new float[][] { AACTab.codebook_vector0_vals, AACTab.codebook_vector0_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector4_vals, AACTab.codebook_vector4_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector10_vals, AACTab.codebook_vector10_vals };

    private static final int MAX_LTP_LONG_SFB = 40;

    private int windowSequence;

    int num_window_groups;

    private int[] group_len = new int[8];

    int maxSfb;

    private int[] band_type = new int[120];

    private int[] band_type_run_end = new int[120];

    private int globalGain;

    static float[] ff_aac_pow2sf_tab = new float[428];

    private static final int POW_SF2_ZERO = 200;

    private double[] sfs;

    private int numSwb;

    private int[] swbOffset;

    private int numWindows;

    protected int parseICSInfo(BitReader _in) {
        _in.read1Bit();
        this.windowSequence = _in.readNBit(2);
        int useKbWindow = _in.read1Bit();
        this.num_window_groups = 1;
        this.group_len[0] = 1;
        if (this.windowSequence == BlockICS.WindowSequence.EIGHT_SHORT_SEQUENCE.ordinal()) {
            int max_sfb = _in.readNBit(4);
            for (int i = 0; i < 7; i++) {
                if (_in.read1Bit() != 0) {
                    this.group_len[this.num_window_groups - 1]++;
                } else {
                    this.num_window_groups++;
                    this.group_len[this.num_window_groups - 1] = 1;
                }
            }
            this.numSwb = AACTab.ff_aac_num_swb_128[this.samplingIndex];
            this.swbOffset = AACTab.ff_swb_offset_128[this.samplingIndex];
            this.numWindows = 8;
        } else {
            this.maxSfb = _in.readNBit(6);
            this.numSwb = AACTab.ff_aac_num_swb_1024[this.samplingIndex];
            this.swbOffset = AACTab.ff_swb_offset_1024[this.samplingIndex];
            this.numWindows = 1;
            int predictor_present = _in.read1Bit();
            if (predictor_present != 0) {
                if (this.profile == Profile.MAIN) {
                    this.decodePrediction(_in, this.maxSfb);
                } else {
                    if (this.profile == Profile.LC) {
                        throw new RuntimeException("Prediction is not allowed _in AAC-LC.\n");
                    }
                    int ltpPresent = _in.read1Bit();
                    if (ltpPresent != 0) {
                        this.decodeLtp(_in, this.maxSfb);
                    }
                }
            }
        }
        return 0;
    }

    private void decodePrediction(BitReader _in, int maxSfb) {
        if (_in.read1Bit() != 0) {
            int sfb = _in.readNBit(5);
        }
        for (int sfb = 0; sfb < Math.min(maxSfb, AACTab.maxSfbTab[this.samplingIndex]); sfb++) {
            _in.read1Bit();
        }
    }

    private void decodeLtp(BitReader _in, int maxSfb) {
        int lag = _in.readNBit(11);
        float coef = AACTab.ltpCoefTab[_in.readNBit(3)];
        for (int sfb = 0; sfb < Math.min(maxSfb, 40); sfb++) {
            _in.read1Bit();
        }
    }

    private void decodeBandTypes(BitReader _in) {
        int idx = 0;
        int bits = this.windowSequence == BlockICS.WindowSequence.EIGHT_SHORT_SEQUENCE.ordinal() ? 3 : 5;
        for (int g = 0; g < this.num_window_groups; g++) {
            int k = 0;
            while (k < this.maxSfb) {
                int sect_end = k;
                int sect_band_type = _in.readNBit(4);
                if (sect_band_type == 12) {
                    throw new RuntimeException("invalid band type");
                }
                int sect_len_incr;
                while ((sect_len_incr = _in.readNBit(bits)) == (1 << bits) - 1) {
                    sect_end += sect_len_incr;
                }
                sect_end += sect_len_incr;
                if (!_in.moreData() || sect_len_incr == (1 << bits) - 1) {
                    throw new RuntimeException("Overread");
                }
                if (sect_end > this.maxSfb) {
                    throw new RuntimeException(String.format("Number of bands (%d) exceeds limit (%d).\n", sect_end, this.maxSfb));
                }
                while (k < sect_end) {
                    this.band_type[idx] = sect_band_type;
                    this.band_type_run_end[idx++] = sect_end;
                    k++;
                }
            }
        }
    }

    private void decodeScalefactors(BitReader _in) {
        int[] offset = new int[] { this.globalGain, this.globalGain - 90, 0 };
        int noise_flag = 1;
        String[] sf_str = new String[] { "Global gain", "Noise gain", "Intensity stereo position" };
        int idx = 0;
        for (int g = 0; g < this.num_window_groups; g++) {
            int i = 0;
            while (i < this.maxSfb) {
                int run_end = this.band_type_run_end[idx];
                if (this.band_type[idx] == BlockICS.BandType.ZERO_BT.ordinal()) {
                    while (i < run_end) {
                        this.sfs[idx] = 0.0;
                        i++;
                        idx++;
                    }
                } else if (this.band_type[idx] != BlockICS.BandType.INTENSITY_BT.ordinal() && this.band_type[idx] != BlockICS.BandType.INTENSITY_BT2.ordinal()) {
                    if (this.band_type[idx] == BlockICS.BandType.NOISE_BT.ordinal()) {
                        while (i < run_end) {
                            if (noise_flag-- > 0) {
                                offset[1] += _in.readNBit(9) - 256;
                            } else {
                                offset[1] += vlc.readVLC(_in) - 60;
                            }
                            int clipped_offset = MathUtil.clip(offset[1], -100, 155);
                            if (offset[1] != clipped_offset) {
                                System.out.println(String.format("Noise gain clipped (%d -> %d).\nIf you heard an audible artifact, there may be a bug _in the decoder. ", offset[1], clipped_offset));
                            }
                            this.sfs[idx] = (double) (-ff_aac_pow2sf_tab[clipped_offset + 200]);
                            i++;
                            idx++;
                        }
                    } else {
                        while (i < run_end) {
                            offset[0] += vlc.readVLC(_in) - 60;
                            if (offset[0] > 255) {
                                throw new RuntimeException(String.format("%s (%d) out of range.\n", sf_str[0], offset[0]));
                            }
                            this.sfs[idx] = (double) (-ff_aac_pow2sf_tab[offset[0] - 100 + 200]);
                            i++;
                            idx++;
                        }
                    }
                } else {
                    while (i < run_end) {
                        offset[2] += vlc.readVLC(_in) - 60;
                        int clipped_offset = MathUtil.clip(offset[2], -155, 100);
                        if (offset[2] != clipped_offset) {
                            System.out.println(String.format("Intensity stereo position clipped (%d -> %d).\nIf you heard an audible artifact, there may be a bug _in the decoder. ", offset[2], clipped_offset));
                        }
                        this.sfs[idx] = (double) ff_aac_pow2sf_tab[-clipped_offset + 200];
                        i++;
                        idx++;
                    }
                }
            }
        }
    }

    private BlockICS.Pulse decodePulses(BitReader _in) {
        int[] pos = new int[4];
        int[] amp = new int[4];
        int numPulse = _in.readNBit(2) + 1;
        int pulseSwb = _in.readNBit(6);
        if (pulseSwb >= this.numSwb) {
            throw new RuntimeException("pulseSwb >= numSwb");
        } else {
            pos[0] = this.swbOffset[pulseSwb];
            pos[0] += _in.readNBit(5);
            if (pos[0] > 1023) {
                throw new RuntimeException("pos[0] > 1023");
            } else {
                amp[0] = _in.readNBit(4);
                for (int i = 1; i < numPulse; i++) {
                    pos[i] = _in.readNBit(5) + pos[i - 1];
                    if (pos[i] > 1023) {
                        throw new RuntimeException("pos[" + i + "] > 1023");
                    }
                    amp[i] = _in.readNBit(5);
                }
                return new BlockICS.Pulse(numPulse, pos, amp);
            }
        }
    }

    private BlockICS.Tns decodeTns(BitReader _in) {
        int is8 = this.windowSequence == BlockICS.WindowSequence.EIGHT_SHORT_SEQUENCE.ordinal() ? 1 : 0;
        int tns_max_order = is8 != 0 ? 7 : (this.profile == Profile.MAIN ? 20 : 12);
        int[] nFilt = new int[this.numWindows];
        int[][] length = new int[this.numWindows][2];
        int[][] order = new int[this.numWindows][2];
        int[][] direction = new int[this.numWindows][2];
        float[][][] coeff = new float[this.numWindows][2][1 << 5 - 2 * is8];
        for (int w = 0; w < this.numWindows; w++) {
            if ((nFilt[w] = _in.readNBit(2 - is8)) != 0) {
                int coefRes = _in.read1Bit();
                for (int filt = 0; filt < nFilt[w]; filt++) {
                    length[w][filt] = _in.readNBit(6 - 2 * is8);
                    if ((order[w][filt] = _in.readNBit(5 - 2 * is8)) > tns_max_order) {
                        throw new RuntimeException(String.format("TNS filter order %d is greater than maximum %d.\n", order[w][filt], tns_max_order));
                    }
                    if (order[w][filt] != 0) {
                        direction[w][filt] = _in.read1Bit();
                        int coefCompress = _in.read1Bit();
                        int coefLen = coefRes + 3 - coefCompress;
                        int tmp2_idx = 2 * coefCompress + coefRes;
                        for (int i = 0; i < order[w][filt]; i++) {
                            coeff[w][filt][i] = AACTab.tns_tmp2_map[tmp2_idx][_in.readNBit(coefLen)];
                        }
                    }
                }
            }
        }
        return new BlockICS.Tns(nFilt, length, order, direction, coeff);
    }

    void VMUL4(float[] result, int idx, float[] v, int code, float scale) {
        result[idx] = v[code & 3] * scale;
        result[idx + 1] = v[code >> 2 & 3] * scale;
        result[idx + 2] = v[code >> 4 & 3] * scale;
        result[idx + 3] = v[code >> 6 & 3] * scale;
    }

    void VMUL4S(float[] result, int idx, float[] v, int code, int sign, float scale) {
        int nz = code >> 12;
        result[idx + 0] = v[idx & 3] * scale;
        sign <<= nz & 1;
        nz >>= 1;
        result[idx + 1] = v[idx >> 2 & 3] * scale;
        sign <<= nz & 1;
        nz >>= 1;
        result[idx + 2] = v[idx >> 4 & 3] * scale;
        sign <<= nz & 1;
        nz >>= 1;
        result[idx + 3] = v[idx >> 6 & 3] * scale;
    }

    void VMUL2(float[] result, int idx, float[] v, int code, float scale) {
        result[idx] = v[code & 15] * scale;
        result[idx + 1] = v[code >> 4 & 15] * scale;
    }

    void VMUL2S(float[] result, int idx, float[] v, int code, int sign, float scale) {
        result[idx] = v[code & 15] * scale;
        result[idx + 1] = v[code >> 4 & 15] * scale;
    }

    private void decodeSpectrum(BitReader _in) {
        float[] coef = new float[1024];
        int idx = 0;
        for (int g = 0; g < this.num_window_groups; g++) {
            for (int i = 0; i < this.maxSfb; idx++) {
                int cbt_m1 = this.band_type[idx] - 1;
                if (cbt_m1 < BlockICS.BandType.INTENSITY_BT2.ordinal() - 1 && cbt_m1 != BlockICS.BandType.NOISE_BT.ordinal() - 1) {
                    float[] vq = this.ff_aac_codebook_vector_vals[cbt_m1];
                    VLC vlc = spectral[cbt_m1];
                    switch(cbt_m1 >> 1) {
                        case 0:
                            this.readBandType1And2(_in, coef, idx, g, i, vq, vlc);
                            break;
                        case 1:
                            this.readBandType3And4(_in, coef, idx, g, i, vq, vlc);
                            break;
                        case 2:
                            this.readBandType5And6(_in, coef, idx, g, i, vq, vlc);
                            break;
                        case 3:
                        case 4:
                            this.readBandType7Through10(_in, coef, idx, g, i, vq, vlc);
                            break;
                        default:
                            this.readOther(_in, coef, idx, g, i, vq, vlc);
                    }
                }
                i++;
            }
        }
    }

    private void readBandType3And4(BitReader _in, float[] coef, int idx, int g, int sfb, float[] vq, VLC vlc) {
        int g_len = this.group_len[g];
        int cfo = this.swbOffset[sfb];
        int off_len = this.swbOffset[sfb + 1] - this.swbOffset[sfb];
        for (int group = 0; group < g_len; cfo += 128) {
            int cf = cfo;
            int len = off_len;
            do {
                int cb_idx = vlc.readVLC(_in);
                int nnz = cb_idx >> 8 & 15;
                int bits = nnz == 0 ? 0 : _in.readNBit(nnz);
                this.VMUL4S(coef, cf, vq, cb_idx, bits, (float) this.sfs[idx]);
                cf += 4;
                len -= 4;
            } while (len > 0);
            group++;
        }
    }

    private void readBandType7Through10(BitReader _in, float[] coef, int idx, int g, int sfb, float[] vq, VLC vlc) {
        int g_len = this.group_len[g];
        int cfo = this.swbOffset[sfb];
        int off_len = this.swbOffset[sfb + 1] - this.swbOffset[sfb];
        for (int group = 0; group < g_len; cfo += 128) {
            int cf = cfo;
            int len = off_len;
            do {
                int cb_idx = vlc.readVLC(_in);
                int nnz = cb_idx >> 8 & 15;
                int bits = nnz == 0 ? 0 : _in.readNBit(nnz) << (cb_idx >> 12);
                this.VMUL2S(coef, cf, vq, cb_idx, bits, (float) this.sfs[idx]);
                cf += 2;
                len -= 2;
            } while (len > 0);
            group++;
        }
    }

    private void readOther(BitReader _in, float[] coef, int idx, int g, int sfb, float[] vq, VLC vlc) {
        int g_len = this.group_len[g];
        int cfo = this.swbOffset[sfb];
        int off_len = this.swbOffset[sfb + 1] - this.swbOffset[sfb];
        for (int group = 0; group < g_len; cfo += 128) {
            int cf = cfo;
            int len = off_len;
            do {
                int cb_idx = vlc.readVLC(_in);
                if (cb_idx != 0) {
                    int nnz = cb_idx >> 12;
                    int nzt = cb_idx >> 8;
                    int bits = _in.readNBit(nnz) << 32 - nnz;
                    for (int j = 0; j < 2; j++) {
                        if ((nzt & 1 << j) != 0) {
                            int b = ProresDecoder.nZeros(~_in.checkNBit(14));
                            if (b > 8) {
                                throw new RuntimeException("error _in spectral data, ESC overflow\n");
                            }
                            _in.skip(b + 1);
                            b += 4;
                            int n = (1 << b) + _in.readNBit(b);
                            coef[cf++] = (float) (MathUtil.cubeRoot(n) | bits & -2147483648);
                            bits <<= 1;
                        } else {
                            int v = (int) vq[cb_idx & 15];
                            coef[cf++] = (float) (bits & -2147483648 | v);
                        }
                        cb_idx >>= 4;
                    }
                    cf += 2;
                    len += 2;
                }
            } while (len > 0);
            group++;
        }
    }

    private void readBandType1And2(BitReader _in, float[] coef, int idx, int g, int sfb, float[] vq, VLC vlc) {
        int g_len = this.group_len[g];
        int cfo = this.swbOffset[sfb];
        int off_len = this.swbOffset[sfb + 1] - this.swbOffset[sfb];
        for (int group = 0; group < g_len; cfo += 128) {
            int cf = cfo;
            int len = off_len;
            do {
                int cb_idx = vlc.readVLC(_in);
                this.VMUL4(coef, cf, vq, cb_idx, (float) this.sfs[idx]);
                cf += 4;
                len -= 4;
            } while (len > 0);
            group++;
        }
    }

    private void readBandType5And6(BitReader _in, float[] coef, int idx, int g, int sfb, float[] vq, VLC vlc) {
        int g_len = this.group_len[g];
        int cfo = this.swbOffset[sfb];
        int off_len = this.swbOffset[sfb + 1] - this.swbOffset[sfb];
        for (int group = 0; group < g_len; cfo += 128) {
            int cf = cfo;
            int len = off_len;
            do {
                int cb_idx = vlc.readVLC(_in);
                this.VMUL2(coef, cf, vq, cb_idx, (float) this.sfs[idx]);
                cf += 2;
                len -= 2;
            } while (len > 0);
            group++;
        }
    }

    @Override
    public void parse(BitReader _in) {
        this.globalGain = _in.readNBit(8);
        if (!this.commonWindow && !this.scaleFlag) {
            this.parseICSInfo(_in);
        }
        this.decodeBandTypes(_in);
        this.decodeScalefactors(_in);
        int pulse_present = 0;
        if (!this.scaleFlag) {
            if (_in.read1Bit() != 0) {
                if (this.windowSequence == BlockICS.WindowSequence.EIGHT_SHORT_SEQUENCE.ordinal()) {
                    throw new RuntimeException("Pulse tool not allowed _in eight short sequence.");
                }
                this.decodePulses(_in);
            }
            if (_in.read1Bit() != 0) {
                this.decodeTns(_in);
            }
            if (_in.read1Bit() != 0) {
                throw new RuntimeException("SSR is not supported");
            }
        }
        this.decodeSpectrum(_in);
    }

    static {
        for (int i = 0; i < 428; i++) {
            ff_aac_pow2sf_tab[i] = (float) Math.pow(2.0, (double) (i - 200) / 4.0);
        }
    }

    static enum BandType {

        ZERO_BT,
        BT_1,
        BT_2,
        BT_3,
        BT_4,
        FIRST_PAIR_BT,
        BT_6,
        BT_7,
        BT_8,
        BT_9,
        BT_10,
        ESC_BT,
        BT_12,
        NOISE_BT,
        INTENSITY_BT2,
        INTENSITY_BT
    }

    public static class Pulse {

        private int numPulse;

        private int[] pos;

        private int[] amp;

        public Pulse(int numPulse, int[] pos, int[] amp) {
            this.numPulse = numPulse;
            this.pos = pos;
            this.amp = amp;
        }

        public int getNumPulse() {
            return this.numPulse;
        }

        public int[] getPos() {
            return this.pos;
        }

        public int[] getAmp() {
            return this.amp;
        }
    }

    public static class Tns {

        private int[] nFilt;

        private int[][] length;

        private int[][] order;

        private int[][] direction;

        private float[][][] coeff;

        public Tns(int[] nFilt, int[][] length, int[][] order, int[][] direction, float[][][] coeff) {
            this.nFilt = nFilt;
            this.length = length;
            this.order = order;
            this.direction = direction;
            this.coeff = coeff;
        }
    }

    private static enum WindowSequence {

        ONLY_LONG_SEQUENCE, LONG_START_SEQUENCE, EIGHT_SHORT_SEQUENCE, LONG_STOP_SEQUENCE
    }
}