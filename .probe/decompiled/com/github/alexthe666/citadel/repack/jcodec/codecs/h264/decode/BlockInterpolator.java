package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class BlockInterpolator {

    private int[] tmp1 = new int[1024];

    private int[] tmp2 = new int[1024];

    private byte[] tmp3 = new byte[1024];

    private BlockInterpolator.LumaInterpolator[] safe = this.initSafe();

    private BlockInterpolator.LumaInterpolator[] unsafe = this.initUnsafe();

    public void getBlockLuma(Picture pic, Picture out, int off, int x, int y, int w, int h) {
        int xInd = x & 3;
        int yInd = y & 3;
        int xFp = x >> 2;
        int yFp = y >> 2;
        if (xFp >= 2 && yFp >= 2 && xFp <= pic.getWidth() - w - 5 && yFp <= pic.getHeight() - h - 5) {
            this.safe[(yInd << 2) + xInd].getLuma(pic.getData()[0], pic.getWidth(), pic.getHeight(), out.getPlaneData(0), off, out.getPlaneWidth(0), xFp, yFp, w, h);
        } else {
            this.unsafe[(yInd << 2) + xInd].getLuma(pic.getData()[0], pic.getWidth(), pic.getHeight(), out.getPlaneData(0), off, out.getPlaneWidth(0), xFp, yFp, w, h);
        }
    }

    public static void getBlockChroma(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int xInd = x & 7;
        int yInd = y & 7;
        int xFull = x >> 3;
        int yFull = y >> 3;
        if (xFull >= 0 && xFull <= picW - blkW - 1 && yFull >= 0 && yFull <= picH - blkH - 1) {
            if (xInd == 0 && yInd == 0) {
                getChroma00(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, blkW, blkH);
            } else if (yInd == 0) {
                getChromaX0(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, xInd, blkW, blkH);
            } else if (xInd == 0) {
                getChroma0X(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, yInd, blkW, blkH);
            } else {
                getChromaXX(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, xInd, yInd, blkW, blkH);
            }
        } else if (xInd == 0 && yInd == 0) {
            getChroma00Unsafe(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, blkW, blkH);
        } else if (yInd == 0) {
            getChromaX0Unsafe(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, xInd, blkW, blkH);
        } else if (xInd == 0) {
            getChroma0XUnsafe(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, yInd, blkW, blkH);
        } else {
            getChromaXXUnsafe(pels, picW, picH, blk, blkOff, blkStride, xFull, yFull, xInd, yInd, blkW, blkH);
        }
    }

    static void getLuma00(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            System.arraycopy(pic, off, blk, blkOff, blkW);
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma00Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(j + y, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = pic[lineStart + MathUtil.clip(x + i, 0, maxW)];
            }
            blkOff += blkStride;
        }
    }

    static void getLuma20NoRoundInt(int[] pic, int picW, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            int off1 = -2;
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + off1] + pic[off + off1 + 5];
                int b = pic[off + off1 + 1] + pic[off + off1 + 4];
                int c = pic[off + off1 + 2] + pic[off + off1 + 3];
                blk[blkOff + i] = a + 5 * ((c << 2) - b);
                off1++;
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma20NoRound(byte[] pic, int picW, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            int off1 = -2;
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + off1] + pic[off + off1 + 5];
                int b = pic[off + off1 + 1] + pic[off + off1 + 4];
                int c = pic[off + off1 + 2] + pic[off + off1 + 3];
                blk[blkOff + i] = a + 5 * ((c << 2) - b);
                off1++;
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma20(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            int off1 = -2;
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + off1] + pic[off + off1 + 5];
                int b = pic[off + off1 + 1] + pic[off + off1 + 4];
                int c = pic[off + off1 + 2] + pic[off + off1 + 3];
                blk[blkOff + i] = (byte) MathUtil.clip(a + 5 * ((c << 2) - b) + 16 >> 5, -128, 127);
                off1++;
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma20UnsafeNoRound(byte[] pic, int picW, int picH, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxW = picW - 1;
        int maxH = picH - 1;
        for (int i = 0; i < blkW; i++) {
            int ipos_m2 = MathUtil.clip(x + i - 2, 0, maxW);
            int ipos_m1 = MathUtil.clip(x + i - 1, 0, maxW);
            int ipos = MathUtil.clip(x + i, 0, maxW);
            int ipos_p1 = MathUtil.clip(x + i + 1, 0, maxW);
            int ipos_p2 = MathUtil.clip(x + i + 2, 0, maxW);
            int ipos_p3 = MathUtil.clip(x + i + 3, 0, maxW);
            int boff = blkOff;
            for (int j = 0; j < blkH; j++) {
                int lineStart = MathUtil.clip(j + y, 0, maxH) * picW;
                int a = pic[lineStart + ipos_m2] + pic[lineStart + ipos_p3];
                int b = pic[lineStart + ipos_m1] + pic[lineStart + ipos_p2];
                int c = pic[lineStart + ipos] + pic[lineStart + ipos_p1];
                blk[boff + i] = a + 5 * ((c << 2) - b);
                boff += blkStride;
            }
        }
    }

    void getLuma20Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20UnsafeNoRound(pic, picW, picH, this.tmp1, blkOff, blkStride, x, y, blkW, blkH);
        for (int i = 0; i < blkW; i++) {
            int boff = blkOff;
            for (int j = 0; j < blkH; j++) {
                blk[boff + i] = (byte) MathUtil.clip(this.tmp1[boff + i] + 16 >> 5, -128, 127);
                boff += blkStride;
            }
        }
    }

    static void getLuma02NoRoundInt(int[] pic, int picW, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = (y - 2) * picW + x;
        int picWx2 = picW + picW;
        int picWx3 = picWx2 + picW;
        int picWx4 = picWx3 + picW;
        int picWx5 = picWx4 + picW;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + i] + pic[off + i + picWx5];
                int b = pic[off + i + picW] + pic[off + i + picWx4];
                int c = pic[off + i + picWx2] + pic[off + i + picWx3];
                blk[blkOff + i] = a + 5 * ((c << 2) - b);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma02NoRound(byte[] pic, int picW, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = (y - 2) * picW + x;
        int picWx2 = picW + picW;
        int picWx3 = picWx2 + picW;
        int picWx4 = picWx3 + picW;
        int picWx5 = picWx4 + picW;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + i] + pic[off + i + picWx5];
                int b = pic[off + i + picW] + pic[off + i + picWx4];
                int c = pic[off + i + picWx2] + pic[off + i + picWx3];
                blk[blkOff + i] = a + 5 * ((c << 2) - b);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma02(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = (y - 2) * picW + x;
        int picWx2 = picW + picW;
        int picWx3 = picWx2 + picW;
        int picWx4 = picWx3 + picW;
        int picWx5 = picWx4 + picW;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int a = pic[off + i] + pic[off + i + picWx5];
                int b = pic[off + i + picW] + pic[off + i + picWx4];
                int c = pic[off + i + picWx2] + pic[off + i + picWx3];
                blk[blkOff + i] = (byte) MathUtil.clip(a + 5 * ((c << 2) - b) + 16 >> 5, -128, 127);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    static void getLuma02UnsafeNoRound(byte[] pic, int picW, int picH, int[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        for (int j = 0; j < blkH; j++) {
            int offP0 = MathUtil.clip(y + j - 2, 0, maxH) * picW;
            int offP1 = MathUtil.clip(y + j - 1, 0, maxH) * picW;
            int offP2 = MathUtil.clip(y + j, 0, maxH) * picW;
            int offP3 = MathUtil.clip(y + j + 1, 0, maxH) * picW;
            int offP4 = MathUtil.clip(y + j + 2, 0, maxH) * picW;
            int offP5 = MathUtil.clip(y + j + 3, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                int pres_x = MathUtil.clip(x + i, 0, maxW);
                int a = pic[pres_x + offP0] + pic[pres_x + offP5];
                int b = pic[pres_x + offP1] + pic[pres_x + offP4];
                int c = pic[pres_x + offP2] + pic[pres_x + offP3];
                blk[blkOff + i] = a + 5 * ((c << 2) - b);
            }
            blkOff += blkStride;
        }
    }

    void getLuma02Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma02UnsafeNoRound(pic, picW, picH, this.tmp1, blkOff, blkStride, x, y, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) MathUtil.clip(this.tmp1[blkOff + i] + 16 >> 5, -128, 127);
            }
            blkOff += blkStride;
        }
    }

    static void getLuma10(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pic, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[off + i] + 1 >> 1);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    void getLuma10Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        this.getLuma20Unsafe(pic, picW, picH, blk, blkOff, blkStride, x, y, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(j + y, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[lineStart + MathUtil.clip(x + i, 0, maxW)] + 1 >> 1);
            }
            blkOff += blkStride;
        }
    }

    static void getLuma30(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pic, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (pic[off + i + 1] + blk[blkOff + i] + 1 >> 1);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    void getLuma30Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        this.getLuma20Unsafe(pic, picW, picH, blk, blkOff, blkStride, x, y, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(j + y, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[lineStart + MathUtil.clip(x + i + 1, 0, maxW)] + 1 >> 1);
            }
            blkOff += blkStride;
        }
    }

    static void getLuma01(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma02(pic, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[off + i] + 1 >> 1);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    void getLuma01Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        this.getLuma02Unsafe(pic, picW, picH, blk, blkOff, blkStride, x, y, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(y + j, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[lineStart + MathUtil.clip(x + i, 0, maxW)] + 1 >> 1);
            }
            blkOff += blkStride;
        }
    }

    static void getLuma03(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma02(pic, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[off + i + picW] + 1 >> 1);
            }
            off += picW;
            blkOff += blkStride;
        }
    }

    void getLuma03Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        this.getLuma02Unsafe(pic, picW, picH, blk, blkOff, blkStride, x, y, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(y + j + 1, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (blk[blkOff + i] + pic[lineStart + MathUtil.clip(x + i, 0, maxW)] + 1 >> 1);
            }
            blkOff += blkStride;
        }
    }

    void getLuma21(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20NoRound(pic, picW, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        int off = blkW << 1;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += blkW;
        }
    }

    void getLuma21Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20UnsafeNoRound(pic, picW, imgH, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        int off = blkW << 1;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += blkW;
        }
    }

    void getLuma22(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20NoRound(pic, picW, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
            }
            blkOff += blkStride;
        }
    }

    void getLuma22Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20UnsafeNoRound(pic, picW, imgH, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
            }
            blkOff += blkStride;
        }
    }

    void getLuma23(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20NoRound(pic, picW, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        int off = blkW << 1;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i + blkW] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += blkW;
        }
    }

    void getLuma23Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20UnsafeNoRound(pic, picW, imgH, this.tmp1, 0, blkW, x, y - 2, blkW, blkH + 7);
        getLuma02NoRoundInt(this.tmp1, blkW, this.tmp2, blkOff, blkStride, 0, 2, blkW, blkH);
        int off = blkW << 1;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i + blkW] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += blkW;
        }
    }

    void getLuma12(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int tmpW = blkW + 7;
        getLuma02NoRound(pic, picW, this.tmp1, 0, tmpW, x - 2, y, tmpW, blkH);
        getLuma20NoRoundInt(this.tmp1, tmpW, this.tmp2, blkOff, blkStride, 2, 0, blkW, blkH);
        int off = 2;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += tmpW;
        }
    }

    void getLuma12Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int tmpW = blkW + 7;
        getLuma02UnsafeNoRound(pic, picW, imgH, this.tmp1, 0, tmpW, x - 2, y, tmpW, blkH);
        getLuma20NoRoundInt(this.tmp1, tmpW, this.tmp2, blkOff, blkStride, 2, 0, blkW, blkH);
        int off = 2;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += tmpW;
        }
    }

    void getLuma32(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int tmpW = blkW + 7;
        getLuma02NoRound(pic, picW, this.tmp1, 0, tmpW, x - 2, y, tmpW, blkH);
        getLuma20NoRoundInt(this.tmp1, tmpW, this.tmp2, blkOff, blkStride, 2, 0, blkW, blkH);
        int off = 2;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i + 1] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += tmpW;
        }
    }

    void getLuma32Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int tmpW = blkW + 7;
        getLuma02UnsafeNoRound(pic, picW, imgH, this.tmp1, 0, tmpW, x - 2, y, tmpW, blkH);
        getLuma20NoRoundInt(this.tmp1, tmpW, this.tmp2, blkOff, blkStride, 2, 0, blkW, blkH);
        int off = 2;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int rounded = MathUtil.clip(this.tmp2[blkOff + i] + 512 >> 10, -128, 127);
                int rounded2 = MathUtil.clip(this.tmp1[off + i + 1] + 16 >> 5, -128, 127);
                blk[blkOff + i] = (byte) (rounded + rounded2 + 1 >> 1);
            }
            blkOff += blkStride;
            off += tmpW;
        }
    }

    void getLuma33(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pic, picW, blk, blkOff, blkStride, x, y + 1, blkW, blkH);
        getLuma02(pic, picW, this.tmp3, 0, blkW, x + 1, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma33Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        this.getLuma20Unsafe(pic, picW, imgH, blk, blkOff, blkStride, x, y + 1, blkW, blkH);
        this.getLuma02Unsafe(pic, picW, imgH, this.tmp3, 0, blkW, x + 1, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma11(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pic, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        getLuma02(pic, picW, this.tmp3, 0, blkW, x, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma11Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        this.getLuma20Unsafe(pic, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
        this.getLuma02Unsafe(pic, picW, imgH, this.tmp3, 0, blkW, x, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma13(byte[] pic, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pic, picW, blk, blkOff, blkStride, x, y + 1, blkW, blkH);
        getLuma02(pic, picW, this.tmp3, 0, blkW, x, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma13Unsafe(byte[] pic, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        this.getLuma20Unsafe(pic, picW, imgH, blk, blkOff, blkStride, x, y + 1, blkW, blkH);
        this.getLuma02Unsafe(pic, picW, imgH, this.tmp3, 0, blkW, x, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma31(byte[] pels, int picW, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        getLuma20(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
        getLuma02(pels, picW, this.tmp3, 0, blkW, x + 1, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    void getLuma31Unsafe(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        this.getLuma20Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
        this.getLuma02Unsafe(pels, picW, imgH, this.tmp3, 0, blkW, x + 1, y, blkW, blkH);
        merge(blk, this.tmp3, blkOff, blkStride, blkW, blkH);
    }

    private static void merge(byte[] first, byte[] second, int blkOff, int blkStride, int blkW, int blkH) {
        int tOff = 0;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                first[blkOff + i] = (byte) (first[blkOff + i] + second[tOff + i] + 1 >> 1);
            }
            blkOff += blkStride;
            tOff += blkW;
        }
    }

    private static void getChroma00(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int off = y * picW + x;
        for (int j = 0; j < blkH; j++) {
            System.arraycopy(pic, off, blk, blkOff, blkW);
            off += picW;
            blkOff += blkStride;
        }
    }

    private static void getChroma00Unsafe(byte[] pic, int picW, int picH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        for (int j = 0; j < blkH; j++) {
            int lineStart = MathUtil.clip(j + y, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = pic[lineStart + MathUtil.clip(x + i, 0, maxW)];
            }
            blkOff += blkStride;
        }
    }

    private static void getChroma0X(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracY, int blkW, int blkH) {
        int w00 = fullY * picW + fullX;
        int w01 = w00 + (fullY < picH - 1 ? picW : 0);
        int eMy = 8 - fracY;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (eMy * pels[w00 + i] + fracY * pels[w01 + i] + 4 >> 3);
            }
            w00 += picW;
            w01 += picW;
            blkOff += blkStride;
        }
    }

    private static void getChroma0XUnsafe(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracY, int blkW, int blkH) {
        int maxW = picW - 1;
        int maxH = picH - 1;
        int eMy = 8 - fracY;
        for (int j = 0; j < blkH; j++) {
            int off00 = MathUtil.clip(fullY + j, 0, maxH) * picW;
            int off01 = MathUtil.clip(fullY + j + 1, 0, maxH) * picW;
            for (int i = 0; i < blkW; i++) {
                int w00 = MathUtil.clip(fullX + i, 0, maxW) + off00;
                int w01 = MathUtil.clip(fullX + i, 0, maxW) + off01;
                blk[blkOff + i] = (byte) (eMy * pels[w00] + fracY * pels[w01] + 4 >> 3);
            }
            blkOff += blkStride;
        }
    }

    private static void getChromaX0(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracX, int blkW, int blkH) {
        int w00 = fullY * picW + fullX;
        int w10 = w00 + (fullX < picW - 1 ? 1 : 0);
        int eMx = 8 - fracX;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (eMx * pels[w00 + i] + fracX * pels[w10 + i] + 4 >> 3);
            }
            w00 += picW;
            w10 += picW;
            blkOff += blkStride;
        }
    }

    private static void getChromaX0Unsafe(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracX, int blkW, int blkH) {
        int eMx = 8 - fracX;
        int maxW = picW - 1;
        int maxH = picH - 1;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int w00 = MathUtil.clip(fullY + j, 0, maxH) * picW + MathUtil.clip(fullX + i, 0, maxW);
                int w10 = MathUtil.clip(fullY + j, 0, maxH) * picW + MathUtil.clip(fullX + i + 1, 0, maxW);
                blk[blkOff + i] = (byte) (eMx * pels[w00] + fracX * pels[w10] + 4 >> 3);
            }
            blkOff += blkStride;
        }
    }

    private static void getChromaXX(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracX, int fracY, int blkW, int blkH) {
        int w00 = fullY * picW + fullX;
        int w01 = w00 + (fullY < picH - 1 ? picW : 0);
        int w10 = w00 + (fullX < picW - 1 ? 1 : 0);
        int w11 = w10 + w01 - w00;
        int eMx = 8 - fracX;
        int eMy = 8 - fracY;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                blk[blkOff + i] = (byte) (eMx * eMy * pels[w00 + i] + fracX * eMy * pels[w10 + i] + eMx * fracY * pels[w01 + i] + fracX * fracY * pels[w11 + i] + 32 >> 6);
            }
            blkOff += blkStride;
            w00 += picW;
            w01 += picW;
            w10 += picW;
            w11 += picW;
        }
    }

    private static void getChromaXXUnsafe(byte[] pels, int picW, int picH, byte[] blk, int blkOff, int blkStride, int fullX, int fullY, int fracX, int fracY, int blkW, int blkH) {
        int maxH = picH - 1;
        int maxW = picW - 1;
        int eMx = 8 - fracX;
        int eMy = 8 - fracY;
        for (int j = 0; j < blkH; j++) {
            for (int i = 0; i < blkW; i++) {
                int w00 = MathUtil.clip(fullY + j, 0, maxH) * picW + MathUtil.clip(fullX + i, 0, maxW);
                int w01 = MathUtil.clip(fullY + j + 1, 0, maxH) * picW + MathUtil.clip(fullX + i, 0, maxW);
                int w10 = MathUtil.clip(fullY + j, 0, maxH) * picW + MathUtil.clip(fullX + i + 1, 0, maxW);
                int w11 = MathUtil.clip(fullY + j + 1, 0, maxH) * picW + MathUtil.clip(fullX + i + 1, 0, maxW);
                blk[blkOff + i] = (byte) (eMx * eMy * pels[w00] + fracX * eMy * pels[w10] + eMx * fracY * pels[w01] + fracX * fracY * pels[w11] + 32 >> 6);
            }
            blkOff += blkStride;
        }
    }

    private BlockInterpolator.LumaInterpolator[] initSafe() {
        return new BlockInterpolator.LumaInterpolator[] { new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma00(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma10(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma20(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma30(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma01(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma11(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma21(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma31(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma02(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma12(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma22(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma32(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma03(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma13(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma23(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma33(pels, picW, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        } };
    }

    private BlockInterpolator.LumaInterpolator[] initUnsafe() {
        return new BlockInterpolator.LumaInterpolator[] { new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.getLuma00Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma10Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma20Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma30Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma01Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma11Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma21Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma31Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma02Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma12Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma22Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma32Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma03Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma13Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma23Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        }, new BlockInterpolator.LumaInterpolator() {

            @Override
            public void getLuma(byte[] pels, int picW, int imgH, byte[] blk, int blkOff, int blkStride, int x, int y, int blkW, int blkH) {
                BlockInterpolator.this.getLuma33Unsafe(pels, picW, imgH, blk, blkOff, blkStride, x, y, blkW, blkH);
            }
        } };
    }

    private interface LumaInterpolator {

        void getLuma(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6, int var7, int var8, int var9, int var10);
    }
}