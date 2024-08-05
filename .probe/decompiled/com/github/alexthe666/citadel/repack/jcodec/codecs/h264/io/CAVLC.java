package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode.CAVLCReader;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.MBType;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.PictureParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SeqParameterSet;
import com.github.alexthe666.citadel.repack.jcodec.common.SaveRestore;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitWriter;
import com.github.alexthe666.citadel.repack.jcodec.common.io.VLC;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class CAVLC implements SaveRestore {

    private ColorSpace color;

    private VLC chromaDCVLC;

    private int[] tokensLeft;

    private int[] tokensTop;

    private int[] tokensLeftSaved;

    private int[] tokensTopSaved;

    private int mbWidth;

    private int mbMask;

    public static int[] NO_ZIGZAG = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 };

    public CAVLC(SeqParameterSet sps, PictureParameterSet pps, int mbW, int mbH) {
        this.color = sps.chromaFormatIdc;
        this.chromaDCVLC = this.codeTableChromaDC();
        this.mbWidth = sps.picWidthInMbsMinus1 + 1;
        this.mbMask = (1 << mbH) - 1;
        this.tokensLeft = new int[4];
        this.tokensTop = new int[this.mbWidth << mbW];
        this.tokensLeftSaved = new int[4];
        this.tokensTopSaved = new int[this.mbWidth << mbW];
    }

    @Override
    public void save() {
        System.arraycopy(this.tokensLeft, 0, this.tokensLeftSaved, 0, this.tokensLeft.length);
        System.arraycopy(this.tokensTop, 0, this.tokensTopSaved, 0, this.tokensTop.length);
    }

    @Override
    public void restore() {
        int[] tmp = this.tokensLeft;
        this.tokensLeft = this.tokensLeftSaved;
        this.tokensLeftSaved = tmp;
        tmp = this.tokensTop;
        this.tokensTop = this.tokensTopSaved;
        this.tokensTopSaved = tmp;
    }

    public int writeACBlock(BitWriter out, int blkIndX, int blkIndY, MBType leftMBType, MBType topMBType, int[] coeff, VLC[] totalZerosTab, int firstCoeff, int maxCoeff, int[] scan) {
        VLC coeffTokenTab = this.getCoeffTokenVLCForLuma(blkIndX != 0, leftMBType, this.tokensLeft[blkIndY & this.mbMask], blkIndY != 0, topMBType, this.tokensTop[blkIndX]);
        int coeffToken = this.writeBlockGen(out, coeff, totalZerosTab, firstCoeff, maxCoeff, scan, coeffTokenTab);
        this.tokensLeft[blkIndY & this.mbMask] = coeffToken;
        this.tokensTop[blkIndX] = coeffToken;
        return coeffToken;
    }

    public void writeChrDCBlock(BitWriter out, int[] coeff, VLC[] totalZerosTab, int firstCoeff, int maxCoeff, int[] scan) {
        this.writeBlockGen(out, coeff, totalZerosTab, firstCoeff, maxCoeff, scan, this.getCoeffTokenVLCForChromaDC());
    }

    public void writeLumaDCBlock(BitWriter out, int blkIndX, int blkIndY, MBType leftMBType, MBType topMBType, int[] coeff, VLC[] totalZerosTab, int firstCoeff, int maxCoeff, int[] scan) {
        VLC coeffTokenTab = this.getCoeffTokenVLCForLuma(blkIndX != 0, leftMBType, this.tokensLeft[blkIndY & this.mbMask], blkIndY != 0, topMBType, this.tokensTop[blkIndX]);
        this.writeBlockGen(out, coeff, totalZerosTab, firstCoeff, maxCoeff, scan, coeffTokenTab);
    }

    private int writeBlockGen(BitWriter out, int[] coeff, VLC[] totalZerosTab, int firstCoeff, int maxCoeff, int[] scan, VLC coeffTokenTab) {
        int trailingOnes = 0;
        int totalCoeff = 0;
        int totalZeros = 0;
        int[] runBefore = new int[maxCoeff];
        int[] levels = new int[maxCoeff];
        for (int i = 0; i < maxCoeff; i++) {
            int c = coeff[scan[i + firstCoeff]];
            if (c == 0) {
                runBefore[totalCoeff]++;
                totalZeros++;
            } else {
                levels[totalCoeff++] = c;
            }
        }
        if (totalCoeff < maxCoeff) {
            totalZeros -= runBefore[totalCoeff];
        }
        trailingOnes = 0;
        while (trailingOnes < totalCoeff && trailingOnes < 3 && Math.abs(levels[totalCoeff - trailingOnes - 1]) == 1) {
            trailingOnes++;
        }
        int coeffToken = H264Const.coeffToken(totalCoeff, trailingOnes);
        coeffTokenTab.writeVLC(out, coeffToken);
        if (totalCoeff > 0) {
            this.writeTrailingOnes(out, levels, totalCoeff, trailingOnes);
            this.writeLevels(out, levels, totalCoeff, trailingOnes);
            if (totalCoeff < maxCoeff) {
                totalZerosTab[totalCoeff - 1].writeVLC(out, totalZeros);
                this.writeRuns(out, runBefore, totalCoeff, totalZeros);
            }
        }
        return coeffToken;
    }

    private void writeTrailingOnes(BitWriter out, int[] levels, int totalCoeff, int trailingOne) {
        for (int i = totalCoeff - 1; i >= totalCoeff - trailingOne; i--) {
            out.write1Bit(levels[i] >>> 31);
        }
    }

    private void writeLevels(BitWriter out, int[] levels, int totalCoeff, int trailingOnes) {
        int suffixLen = totalCoeff > 10 && trailingOnes < 3 ? 1 : 0;
        for (int i = totalCoeff - trailingOnes - 1; i >= 0; i--) {
            int absLev = this.unsigned(levels[i]);
            if (i == totalCoeff - trailingOnes - 1 && trailingOnes < 3) {
                absLev -= 2;
            }
            int prefix = absLev >> suffixLen;
            if ((suffixLen != 0 || prefix >= 14) && (suffixLen <= 0 || prefix >= 15)) {
                if (suffixLen == 0 && absLev < 30) {
                    out.writeNBit(1, 15);
                    out.writeNBit(absLev - 14, 4);
                } else {
                    if (suffixLen == 0) {
                        absLev -= 15;
                    }
                    int len = 12;
                    int code;
                    while ((code = absLev - (len + 3 << suffixLen) - (1 << len) + 4096) >= 1 << len) {
                        len++;
                    }
                    out.writeNBit(1, len + 4);
                    out.writeNBit(code, len);
                }
            } else {
                out.writeNBit(1, prefix + 1);
                out.writeNBit(absLev, suffixLen);
            }
            if (suffixLen == 0) {
                suffixLen = 1;
            }
            if (MathUtil.abs(levels[i]) > 3 << suffixLen - 1 && suffixLen < 6) {
                suffixLen++;
            }
        }
    }

    private final int unsigned(int signed) {
        int sign = signed >>> 31;
        int s = signed >> 31;
        return ((signed ^ s) - s << 1) + sign - 2;
    }

    private void writeRuns(BitWriter out, int[] run, int totalCoeff, int totalZeros) {
        for (int i = totalCoeff - 1; i > 0 && totalZeros > 0; i--) {
            H264Const.run[Math.min(6, totalZeros - 1)].writeVLC(out, run[i]);
            totalZeros -= run[i];
        }
    }

    public VLC getCoeffTokenVLCForLuma(boolean leftAvailable, MBType leftMBType, int leftToken, boolean topAvailable, MBType topMBType, int topToken) {
        int nc = this.codeTableLuma(leftAvailable, leftMBType, leftToken, topAvailable, topMBType, topToken);
        return H264Const.CoeffToken[Math.min(nc, 8)];
    }

    public VLC getCoeffTokenVLCForChromaDC() {
        return this.chromaDCVLC;
    }

    protected int codeTableLuma(boolean leftAvailable, MBType leftMBType, int leftToken, boolean topAvailable, MBType topMBType, int topToken) {
        int nA = leftMBType == null ? 0 : totalCoeff(leftToken);
        int nB = topMBType == null ? 0 : totalCoeff(topToken);
        if (leftAvailable && topAvailable) {
            return nA + nB + 1 >> 1;
        } else if (leftAvailable) {
            return nA;
        } else {
            return topAvailable ? nB : 0;
        }
    }

    protected VLC codeTableChromaDC() {
        if (this.color == ColorSpace.YUV420J) {
            return H264Const.coeffTokenChromaDCY420;
        } else if (this.color == ColorSpace.YUV422) {
            return H264Const.coeffTokenChromaDCY422;
        } else {
            return this.color == ColorSpace.YUV444 ? H264Const.CoeffToken[0] : null;
        }
    }

    public int readCoeffs(BitReader _in, VLC coeffTokenTab, VLC[] totalZerosTab, int[] coeffLevel, int firstCoeff, int nCoeff, int[] zigzag) {
        int coeffToken = coeffTokenTab.readVLC(_in);
        int totalCoeff = totalCoeff(coeffToken);
        int trailingOnes = trailingOnes(coeffToken);
        if (totalCoeff > 0) {
            int suffixLength = totalCoeff > 10 && trailingOnes < 3 ? 1 : 0;
            int[] level = new int[totalCoeff];
            int i;
            for (i = 0; i < trailingOnes; i++) {
                level[i] = 1 - 2 * _in.read1Bit();
            }
            for (; i < totalCoeff; i++) {
                int level_prefix = CAVLCReader.readZeroBitCount(_in, "");
                int levelSuffixSize = suffixLength;
                if (level_prefix == 14 && suffixLength == 0) {
                    levelSuffixSize = 4;
                }
                if (level_prefix >= 15) {
                    levelSuffixSize = level_prefix - 3;
                }
                int levelCode = Min(15, level_prefix) << suffixLength;
                if (levelSuffixSize > 0) {
                    int level_suffix = CAVLCReader.readU(_in, levelSuffixSize, "RB: level_suffix");
                    levelCode += level_suffix;
                }
                if (level_prefix >= 15 && suffixLength == 0) {
                    levelCode += 15;
                }
                if (level_prefix >= 16) {
                    levelCode += (1 << level_prefix - 3) - 4096;
                }
                if (i == trailingOnes && trailingOnes < 3) {
                    levelCode += 2;
                }
                if (levelCode % 2 == 0) {
                    level[i] = levelCode + 2 >> 1;
                } else {
                    level[i] = -levelCode - 1 >> 1;
                }
                if (suffixLength == 0) {
                    suffixLength = 1;
                }
                if (Abs(level[i]) > 3 << suffixLength - 1 && suffixLength < 6) {
                    suffixLength++;
                }
            }
            int zerosLeft;
            if (totalCoeff < nCoeff) {
                if (coeffLevel.length == 4) {
                    zerosLeft = H264Const.totalZeros4[totalCoeff - 1].readVLC(_in);
                } else if (coeffLevel.length == 8) {
                    zerosLeft = H264Const.totalZeros8[totalCoeff - 1].readVLC(_in);
                } else {
                    zerosLeft = H264Const.totalZeros16[totalCoeff - 1].readVLC(_in);
                }
            } else {
                zerosLeft = 0;
            }
            int[] runs = new int[totalCoeff];
            int r;
            for (r = 0; r < totalCoeff - 1 && zerosLeft > 0; r++) {
                int run = H264Const.run[Math.min(6, zerosLeft - 1)].readVLC(_in);
                zerosLeft -= run;
                runs[r] = run;
            }
            runs[r] = zerosLeft;
            int j = totalCoeff - 1;
            for (int cn = 0; j >= 0 && cn < nCoeff; cn++) {
                cn += runs[j];
                coeffLevel[zigzag[cn + firstCoeff]] = level[j];
                j--;
            }
        }
        return coeffToken;
    }

    private static int Min(int i, int level_prefix) {
        return i < level_prefix ? i : level_prefix;
    }

    private static int Abs(int i) {
        return i < 0 ? -i : i;
    }

    public static final int totalCoeff(int coeffToken) {
        return coeffToken >> 4;
    }

    public static final int trailingOnes(int coeffToken) {
        return coeffToken & 15;
    }

    public void readChromaDCBlock(BitReader reader, int[] coeff, boolean leftAvailable, boolean topAvailable) {
        VLC coeffTokenTab = this.getCoeffTokenVLCForChromaDC();
        this.readCoeffs(reader, coeffTokenTab, coeff.length == 16 ? H264Const.totalZeros16 : (coeff.length == 8 ? H264Const.totalZeros8 : H264Const.totalZeros4), coeff, 0, coeff.length, NO_ZIGZAG);
    }

    public void readLumaDCBlock(BitReader reader, int[] coeff, int mbX, boolean leftAvailable, MBType leftMbType, boolean topAvailable, MBType topMbType, int[] zigzag4x4) {
        VLC coeffTokenTab = this.getCoeffTokenVLCForLuma(leftAvailable, leftMbType, this.tokensLeft[0], topAvailable, topMbType, this.tokensTop[mbX << 2]);
        this.readCoeffs(reader, coeffTokenTab, H264Const.totalZeros16, coeff, 0, 16, zigzag4x4);
    }

    public int readACBlock(BitReader reader, int[] coeff, int blkIndX, int blkIndY, boolean leftAvailable, MBType leftMbType, boolean topAvailable, MBType topMbType, int firstCoeff, int nCoeff, int[] zigzag4x4) {
        VLC coeffTokenTab = this.getCoeffTokenVLCForLuma(leftAvailable, leftMbType, this.tokensLeft[blkIndY & this.mbMask], topAvailable, topMbType, this.tokensTop[blkIndX]);
        int readCoeffs = this.readCoeffs(reader, coeffTokenTab, H264Const.totalZeros16, coeff, firstCoeff, nCoeff, zigzag4x4);
        this.tokensLeft[blkIndY & this.mbMask] = this.tokensTop[blkIndX] = readCoeffs;
        return totalCoeff(readCoeffs);
    }

    public void setZeroCoeff(int blkIndX, int blkIndY) {
        this.tokensLeft[blkIndY & this.mbMask] = this.tokensTop[blkIndX] = 0;
    }
}