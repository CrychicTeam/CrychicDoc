package com.github.alexthe666.citadel.repack.jaad.aac.tools;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.SampleFrequency;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;

public class TNS implements Constants, TNSTables {

    private static final int TNS_MAX_ORDER = 20;

    private static final int[] SHORT_BITS = new int[] { 1, 4, 3 };

    private static final int[] LONG_BITS = new int[] { 2, 6, 5 };

    private int[] nFilt = new int[8];

    private int[][] length = new int[8][4];

    private int[][] order;

    private boolean[][] direction = new boolean[8][4];

    private float[][][] coef;

    public TNS() {
        this.order = new int[8][4];
        this.coef = new float[8][4][20];
    }

    public void decode(BitStream in, ICSInfo info) throws AACException {
        int windowCount = info.getWindowCount();
        int[] bits = info.isEightShortFrame() ? SHORT_BITS : LONG_BITS;
        for (int w = 0; w < windowCount; w++) {
            if ((this.nFilt[w] = in.readBits(bits[0])) != 0) {
                int coefRes = in.readBit();
                for (int filt = 0; filt < this.nFilt[w]; filt++) {
                    this.length[w][filt] = in.readBits(bits[1]);
                    if ((this.order[w][filt] = in.readBits(bits[2])) > 20) {
                        throw new AACException("TNS filter out of range: " + this.order[w][filt]);
                    }
                    if (this.order[w][filt] != 0) {
                        this.direction[w][filt] = in.readBool();
                        int coefCompress = in.readBit();
                        int coefLen = coefRes + 3 - coefCompress;
                        int tmp = 2 * coefCompress + coefRes;
                        for (int i = 0; i < this.order[w][filt]; i++) {
                            this.coef[w][filt][i] = TNS_TABLES[tmp][in.readBits(coefLen)];
                        }
                    }
                }
            }
        }
    }

    public void process(ICStream ics, float[] spec, SampleFrequency sf, boolean decode) {
    }
}