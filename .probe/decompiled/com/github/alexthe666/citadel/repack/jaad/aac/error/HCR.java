package com.github.alexthe666.citadel.repack.jaad.aac.error;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICSInfo;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.ICStream;

public class HCR implements Constants {

    private static final int NUM_CB = 6;

    private static final int NUM_CB_ER = 22;

    private static final int MAX_CB = 32;

    private static final int VCB11_FIRST = 16;

    private static final int VCB11_LAST = 31;

    private static final int[] PRE_SORT_CB_STD = new int[] { 11, 9, 7, 5, 3, 1 };

    private static final int[] PRE_SORT_CB_ER = new int[] { 11, 31, 30, 29, 28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 9, 7, 5, 3, 1 };

    private static final int[] MAX_CW_LEN = new int[] { 0, 11, 9, 20, 16, 13, 11, 14, 12, 17, 14, 49, 0, 0, 0, 0, 14, 17, 21, 21, 25, 25, 29, 29, 29, 29, 33, 33, 33, 37, 37, 41 };

    private static final int[] S = new int[] { 1, 2, 4, 8, 16 };

    private static final int[] B = new int[] { 1431655765, 858993459, 252645135, 16711935, 65535 };

    private static int rewindReverse(int v, int len) {
        v = v >> S[0] & B[0] | v << S[0] & ~B[0];
        v = v >> S[1] & B[1] | v << S[1] & ~B[1];
        v = v >> S[2] & B[2] | v << S[2] & ~B[2];
        v = v >> S[3] & B[3] | v << S[3] & ~B[3];
        v = v >> S[4] & B[4] | v << S[4] & ~B[4];
        return v >> 32 - len;
    }

    static int[] rewindReverse64(int hi, int lo, int len) {
        int[] i = new int[2];
        if (len <= 32) {
            i[0] = 0;
            i[1] = rewindReverse(lo, len);
        } else {
            lo = lo >> S[0] & B[0] | lo << S[0] & ~B[0];
            hi = hi >> S[0] & B[0] | hi << S[0] & ~B[0];
            lo = lo >> S[1] & B[1] | lo << S[1] & ~B[1];
            hi = hi >> S[1] & B[1] | hi << S[1] & ~B[1];
            lo = lo >> S[2] & B[2] | lo << S[2] & ~B[2];
            hi = hi >> S[2] & B[2] | hi << S[2] & ~B[2];
            lo = lo >> S[3] & B[3] | lo << S[3] & ~B[3];
            hi = hi >> S[3] & B[3] | hi << S[3] & ~B[3];
            lo = lo >> S[4] & B[4] | lo << S[4] & ~B[4];
            hi = hi >> S[4] & B[4] | hi << S[4] & ~B[4];
            i[1] = hi >> 64 - len | lo << len - 32;
            i[1] = lo >> 64 - len;
        }
        return i;
    }

    private static boolean isGoodCB(int cb, int sectCB) {
        boolean b = false;
        if (sectCB > 0 && sectCB <= 11 || sectCB >= 16 && sectCB <= 31) {
            if (cb < 11) {
                b = sectCB == cb || sectCB == cb + 1;
            } else {
                b = sectCB == cb;
            }
        }
        return b;
    }

    public static void decodeReorderedSpectralData(ICStream ics, BitStream in, short[] spectralData, boolean sectionDataResilience) throws AACException {
        ICSInfo info = ics.getInfo();
        int windowGroupCount = info.getWindowGroupCount();
        int maxSFB = info.getMaxSFB();
        int[] swbOffsets = info.getSWBOffsets();
        int swbOffsetMax = info.getSWBOffsetMax();
        int[][] sectStart = null;
        int[][] sectEnd = null;
        int[] numSec = null;
        int[][] sectCB = null;
        int[][] sectSFBOffsets = null;
        int spDataLen = ics.getReorderedSpectralDataLength();
        if (spDataLen != 0) {
            int longestLen = ics.getLongestCodewordLength();
            if (longestLen != 0 && longestLen < spDataLen) {
                int[] spOffsets = new int[8];
                int shortFrameLen = spectralData.length / 8;
                spOffsets[0] = 0;
                for (int g = 1; g < windowGroupCount; g++) {
                    spOffsets[g] = spOffsets[g - 1] + shortFrameLen * info.getWindowGroupLength(g - 1);
                }
                HCR.Codeword[] codeword = new HCR.Codeword[512];
                BitsBuffer[] segment = new BitsBuffer[512];
                int lastCB;
                int[] preSortCB;
                if (sectionDataResilience) {
                    preSortCB = PRE_SORT_CB_ER;
                    lastCB = 22;
                } else {
                    preSortCB = PRE_SORT_CB_STD;
                    lastCB = 6;
                }
                int PCWs_done = 0;
                int segmentsCount = 0;
                int numberOfCodewords = 0;
                int bitsread = 0;
                for (int sortloop = 0; sortloop < lastCB; sortloop++) {
                    int thisCB = preSortCB[sortloop];
                    for (int sfb = 0; sfb < maxSFB; sfb++) {
                        for (int w_idx = 0; 4 * w_idx < Math.min(swbOffsets[sfb + 1], swbOffsetMax) - swbOffsets[sfb]; w_idx++) {
                            for (int var40 = 0; var40 < windowGroupCount; var40++) {
                                for (int i = 0; i < numSec[var40]; i++) {
                                    if (sectStart[var40][i] <= sfb && sectEnd[var40][i] > sfb) {
                                        int thisSectCB = sectCB[var40][i];
                                        if (isGoodCB(thisCB, thisSectCB)) {
                                            int sect_sfb_size = sectSFBOffsets[var40][sfb + 1] - sectSFBOffsets[var40][sfb];
                                            int inc = thisSectCB < 5 ? 4 : 2;
                                            int group_cws_count = 4 * info.getWindowGroupLength(var40) / inc;
                                            int segwidth = Math.min(MAX_CW_LEN[thisSectCB], longestLen);
                                            for (int cws = 0; cws < group_cws_count && cws + w_idx * group_cws_count < sect_sfb_size; cws++) {
                                                int sp = spOffsets[var40] + sectSFBOffsets[var40][sfb] + inc * (cws + w_idx * group_cws_count);
                                                if (PCWs_done == 0) {
                                                    if (bitsread + segwidth <= spDataLen) {
                                                        segment[segmentsCount].readSegment(segwidth, in);
                                                        bitsread += segwidth;
                                                        segment[segmentsCount].rewindReverse();
                                                        segmentsCount++;
                                                    } else {
                                                        if (bitsread < spDataLen) {
                                                            int additional_bits = spDataLen - bitsread;
                                                            segment[segmentsCount].readSegment(additional_bits, in);
                                                            segment[segmentsCount].len = segment[segmentsCount].len + segment[segmentsCount - 1].len;
                                                            segment[segmentsCount].rewindReverse();
                                                            if (segment[segmentsCount - 1].len > 32) {
                                                                segment[segmentsCount - 1].bufb = segment[segmentsCount].bufb + segment[segmentsCount - 1].showBits(segment[segmentsCount - 1].len - 32);
                                                                segment[segmentsCount - 1].bufa = segment[segmentsCount].bufa + segment[segmentsCount - 1].showBits(32);
                                                            } else {
                                                                segment[segmentsCount - 1].bufa = segment[segmentsCount].bufa + segment[segmentsCount - 1].showBits(segment[segmentsCount - 1].len);
                                                                segment[segmentsCount - 1].bufb = segment[segmentsCount].bufb;
                                                            }
                                                            segment[segmentsCount - 1].len += additional_bits;
                                                        }
                                                        bitsread = spDataLen;
                                                        PCWs_done = 1;
                                                        codeword[0].fill(sp, thisSectCB);
                                                    }
                                                } else {
                                                    codeword[numberOfCodewords - segmentsCount].fill(sp, thisSectCB);
                                                }
                                                numberOfCodewords++;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (segmentsCount == 0) {
                    throw new AACException("no segments in HCR");
                } else {
                    int numberOfSets = numberOfCodewords / segmentsCount;
                    for (int set = 1; set <= numberOfSets; set++) {
                        for (int trial = 0; trial < segmentsCount; trial++) {
                            for (int codewordBase = 0; codewordBase < segmentsCount; codewordBase++) {
                                int segmentID = (trial + codewordBase) % segmentsCount;
                                int codewordID = codewordBase + set * segmentsCount - segmentsCount;
                                if (codewordID >= numberOfCodewords - segmentsCount) {
                                    break;
                                }
                                if (codeword[codewordID].decoded == 0 && segment[segmentID].len > 0) {
                                    if (codeword[codewordID].bits.len != 0) {
                                        segment[segmentID].concatBits(codeword[codewordID].bits);
                                    }
                                    int var48 = segment[segmentID].len;
                                }
                            }
                        }
                        for (int ix = 0; ix < segmentsCount; ix++) {
                            segment[ix].rewindReverse();
                        }
                    }
                }
            } else {
                throw new AACException("length of longest HCR codeword out of range");
            }
        }
    }

    private static class Codeword {

        int cb;

        int decoded;

        int sp_offset;

        BitsBuffer bits;

        private void fill(int sp, int cb) {
            this.sp_offset = sp;
            this.cb = cb;
            this.decoded = 0;
            this.bits = new BitsBuffer();
        }
    }
}