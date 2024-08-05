package com.github.alexthe666.citadel.repack.jaad.aac.error;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;

public class RVLC implements RVLCTables {

    private static final int ESCAPE_FLAG = 7;

    public void decode(BitStream in, ICStream ics, int[][] scaleFactors) throws AACException {
        int bits = ics.getInfo().isEightShortFrame() ? 11 : 9;
        boolean sfConcealment = in.readBool();
        int revGlobalGain = in.readBits(8);
        int rvlcSFLen = in.readBits(bits);
        ICSInfo info = ics.getInfo();
        int windowGroupCount = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[][] sfbCB = null;
        int sf = ics.getGlobalGain();
        int intensityPosition = 0;
        int noiseEnergy = sf - 90 - 256;
        boolean intensityUsed = false;
        boolean noiseUsed = false;
        for (int g = 0; g < windowGroupCount; g++) {
            for (int sfb = 0; sfb < maxSFB; sfb++) {
                switch(sfbCB[g][sfb]) {
                    case 0:
                        scaleFactors[g][sfb] = 0;
                        break;
                    case 13:
                        if (noiseUsed) {
                            noiseEnergy += this.decodeHuffman(in);
                            scaleFactors[g][sfb] = noiseEnergy;
                        } else {
                            noiseUsed = true;
                            noiseEnergy = this.decodeHuffman(in);
                        }
                        break;
                    case 14:
                    case 15:
                        if (!intensityUsed) {
                            intensityUsed = true;
                        }
                        intensityPosition += this.decodeHuffman(in);
                        scaleFactors[g][sfb] = intensityPosition;
                        break;
                    default:
                        sf += this.decodeHuffman(in);
                        scaleFactors[g][sfb] = sf;
                }
            }
        }
        int lastIntensityPosition = 0;
        if (intensityUsed) {
            lastIntensityPosition = this.decodeHuffman(in);
        }
        noiseUsed = false;
        if (in.readBool()) {
            this.decodeEscapes(in, ics, scaleFactors);
        }
    }

    private void decodeEscapes(BitStream in, ICStream ics, int[][] scaleFactors) throws AACException {
        ICSInfo info = ics.getInfo();
        int windowGroupCount = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[][] sfbCB = null;
        int escapesLen = in.readBits(8);
        boolean noiseUsed = false;
        for (int g = 0; g < windowGroupCount; g++) {
            for (int sfb = 0; sfb < maxSFB; sfb++) {
                if (sfbCB[g][sfb] == 13 && !noiseUsed) {
                    noiseUsed = true;
                } else if (Math.abs(sfbCB[g][sfb]) == 7) {
                    int val = this.decodeHuffmanEscape(in);
                    if (sfbCB[g][sfb] == -7) {
                        scaleFactors[g][sfb] = scaleFactors[g][sfb] - val;
                    } else {
                        scaleFactors[g][sfb] = scaleFactors[g][sfb] + val;
                    }
                }
            }
        }
    }

    private int decodeHuffman(BitStream in) throws AACException {
        int off = 0;
        int i = RVLC_BOOK[off][1];
        int cw = in.readBits(i);
        while (cw != RVLC_BOOK[off][2] && i < 10) {
            off++;
            int j = RVLC_BOOK[off][1] - i;
            i += j;
            cw <<= j;
            cw |= in.readBits(j);
        }
        return RVLC_BOOK[off][0];
    }

    private int decodeHuffmanEscape(BitStream in) throws AACException {
        int off = 0;
        int i = ESCAPE_BOOK[off][1];
        int cw = in.readBits(i);
        while (cw != ESCAPE_BOOK[off][2] && i < 21) {
            off++;
            int j = ESCAPE_BOOK[off][1] - i;
            i += j;
            cw <<= j;
            cw |= in.readBits(j);
        }
        return ESCAPE_BOOK[off][0];
    }
}