package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

import com.github.alexthe666.citadel.repack.jcodec.api.NotImplementedException;
import com.github.alexthe666.citadel.repack.jcodec.api.NotSupportedException;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import java.util.Arrays;

public class VPXMacroblock {

    public int filterLevel;

    public int chromaMode;

    public int skipCoeff;

    public final VPXMacroblock.Subblock[][] ySubblocks;

    public final VPXMacroblock.Subblock y2;

    public final VPXMacroblock.Subblock[][] uSubblocks;

    public final VPXMacroblock.Subblock[][] vSubblocks;

    public final int Rrow;

    public final int column;

    public int lumaMode;

    boolean skipFilter;

    public int segment = 0;

    public boolean debug = true;

    public VP8Util.QuantizationParams quants;

    public VPXMacroblock(int y, int x) {
        this.ySubblocks = new VPXMacroblock.Subblock[4][4];
        this.y2 = new VPXMacroblock.Subblock(this, 0, 0, VP8Util.PLANE.Y2);
        this.uSubblocks = new VPXMacroblock.Subblock[2][2];
        this.vSubblocks = new VPXMacroblock.Subblock[2][2];
        this.Rrow = y;
        this.column = x;
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                this.ySubblocks[row][col] = new VPXMacroblock.Subblock(this, row, col, VP8Util.PLANE.Y1);
            }
        }
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                this.uSubblocks[row][col] = new VPXMacroblock.Subblock(this, row, col, VP8Util.PLANE.U);
                this.vSubblocks[row][col] = new VPXMacroblock.Subblock(this, row, col, VP8Util.PLANE.V);
            }
        }
    }

    public void dequantMacroBlock(VPXMacroblock[][] mbs) {
        VP8Util.QuantizationParams p = this.quants;
        if (this.lumaMode != 4) {
            int acQValue = p.y2AC;
            int dcQValue = p.y2DC;
            int[] input = new int[16];
            input[0] = this.y2.tokens[0] * dcQValue;
            for (int x = 1; x < 16; x++) {
                input[x] = this.y2.tokens[x] * acQValue;
            }
            this.y2.residue = VP8DCT.decodeWHT(input);
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    this.ySubblocks[row][col].dequantSubblock(p.yDC, p.yAC, this.y2.residue[row * 4 + col]);
                }
            }
            this.predictY(mbs);
            this.predictUV(mbs);
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    this.uSubblocks[row][col].dequantSubblock(p.chromaDC, p.chromaAC, null);
                    this.vSubblocks[row][col].dequantSubblock(p.chromaDC, p.chromaAC, null);
                }
            }
            this.reconstruct();
        } else {
            for (int row = 0; row < 4; row++) {
                for (int col = 0; col < 4; col++) {
                    VPXMacroblock.Subblock sb = this.ySubblocks[row][col];
                    sb.dequantSubblock(p.yDC, p.yAC, null);
                    sb.predict(mbs);
                    sb.reconstruct();
                }
            }
            this.predictUV(mbs);
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    VPXMacroblock.Subblock sb = this.uSubblocks[row][col];
                    sb.dequantSubblock(p.chromaDC, p.chromaAC, null);
                    sb.reconstruct();
                }
            }
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    VPXMacroblock.Subblock sb = this.vSubblocks[row][col];
                    sb.dequantSubblock(p.chromaDC, p.chromaAC, null);
                    sb.reconstruct();
                }
            }
        }
    }

    public void reconstruct() {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                this.ySubblocks[row][col].reconstruct();
            }
        }
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                this.uSubblocks[row][col].reconstruct();
            }
        }
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                this.vSubblocks[row][col].reconstruct();
            }
        }
    }

    public void predictUV(VPXMacroblock[][] mbs) {
        VPXMacroblock aboveMb = mbs[this.Rrow - 1][this.column];
        VPXMacroblock leftMb = mbs[this.Rrow][this.column - 1];
        switch(this.chromaMode) {
            case 0:
                boolean up_available = false;
                boolean left_available = false;
                int uAvg = 0;
                int vAvg = 0;
                int expected_udc = 128;
                int expected_vdc = 128;
                if (this.column > 1) {
                    left_available = true;
                }
                if (this.Rrow > 1) {
                    up_available = true;
                }
                if (up_available || left_available) {
                    if (up_available) {
                        for (int j = 0; j < 2; j++) {
                            VPXMacroblock.Subblock usb = aboveMb.uSubblocks[1][j];
                            VPXMacroblock.Subblock vsb = aboveMb.vSubblocks[1][j];
                            for (int i = 0; i < 4; i++) {
                                uAvg += usb.val[12 + i];
                                vAvg += vsb.val[12 + i];
                            }
                        }
                    }
                    if (left_available) {
                        for (int j = 0; j < 2; j++) {
                            VPXMacroblock.Subblock usb = leftMb.uSubblocks[j][1];
                            VPXMacroblock.Subblock vsb = leftMb.vSubblocks[j][1];
                            for (int i = 0; i < 4; i++) {
                                uAvg += usb.val[i * 4 + 3];
                                vAvg += vsb.val[i * 4 + 3];
                            }
                        }
                    }
                    int shift = 2;
                    if (up_available) {
                        shift++;
                    }
                    if (left_available) {
                        shift++;
                    }
                    expected_udc = uAvg + (1 << shift - 1) >> shift;
                    expected_vdc = vAvg + (1 << shift - 1) >> shift;
                }
                int[] ufill = new int[16];
                for (int aRow = 0; aRow < 4; aRow++) {
                    for (int aCol = 0; aCol < 4; aCol++) {
                        ufill[aRow * 4 + aCol] = expected_udc;
                    }
                }
                int[] vfill = new int[16];
                for (int aRow = 0; aRow < 4; aRow++) {
                    for (int aCol = 0; aCol < 4; aCol++) {
                        vfill[aRow * 4 + aCol] = expected_vdc;
                    }
                }
                for (int aRow = 0; aRow < 2; aRow++) {
                    for (int aCol = 0; aCol < 2; aCol++) {
                        VPXMacroblock.Subblock usb = this.uSubblocks[aRow][aCol];
                        VPXMacroblock.Subblock vsb = this.vSubblocks[aRow][aCol];
                        usb._predict = ufill;
                        vsb._predict = vfill;
                    }
                }
                break;
            case 1:
                VPXMacroblock.Subblock[] aboveUSb = new VPXMacroblock.Subblock[2];
                VPXMacroblock.Subblock[] aboveVSb = new VPXMacroblock.Subblock[2];
                for (int aCol = 0; aCol < 2; aCol++) {
                    aboveUSb[aCol] = aboveMb.uSubblocks[1][aCol];
                    aboveVSb[aCol] = aboveMb.vSubblocks[1][aCol];
                }
                for (int aRow = 0; aRow < 2; aRow++) {
                    for (int aCol = 0; aCol < 2; aCol++) {
                        VPXMacroblock.Subblock usb = this.uSubblocks[aRow][aCol];
                        VPXMacroblock.Subblock vsb = this.vSubblocks[aRow][aCol];
                        int[] ublock = new int[16];
                        int[] vblock = new int[16];
                        for (int pRow = 0; pRow < 4; pRow++) {
                            for (int pCol = 0; pCol < 4; pCol++) {
                                ublock[pRow * 4 + pCol] = aboveUSb[aCol].val != null ? aboveUSb[aCol].val[12 + pCol] : 127;
                                vblock[pRow * 4 + pCol] = aboveVSb[aCol].val != null ? aboveVSb[aCol].val[12 + pCol] : 127;
                            }
                        }
                        usb._predict = ublock;
                        vsb._predict = vblock;
                    }
                }
                break;
            case 2:
                VPXMacroblock.Subblock[] leftUSb = new VPXMacroblock.Subblock[2];
                VPXMacroblock.Subblock[] leftVSb = new VPXMacroblock.Subblock[2];
                for (int aCol = 0; aCol < 2; aCol++) {
                    leftUSb[aCol] = leftMb.uSubblocks[aCol][1];
                    leftVSb[aCol] = leftMb.vSubblocks[aCol][1];
                }
                for (int aRow = 0; aRow < 2; aRow++) {
                    for (int aCol = 0; aCol < 2; aCol++) {
                        VPXMacroblock.Subblock usb = this.uSubblocks[aRow][aCol];
                        VPXMacroblock.Subblock vsb = this.vSubblocks[aRow][aCol];
                        int[] ublock = new int[16];
                        int[] vblock = new int[16];
                        for (int pRow = 0; pRow < 4; pRow++) {
                            for (int pCol = 0; pCol < 4; pCol++) {
                                ublock[pRow * 4 + pCol] = leftUSb[aRow].val != null ? leftUSb[aRow].val[pRow * 4 + 3] : 129;
                                vblock[pRow * 4 + pCol] = leftVSb[aRow].val != null ? leftVSb[aRow].val[pRow * 4 + 3] : 129;
                            }
                        }
                        usb._predict = ublock;
                        vsb._predict = vblock;
                    }
                }
                break;
            case 3:
                VPXMacroblock ALMb = mbs[this.Rrow - 1][this.column - 1];
                VPXMacroblock.Subblock ALUSb = ALMb.uSubblocks[1][1];
                int alu = ALUSb.val[15];
                VPXMacroblock.Subblock ALVSb = ALMb.vSubblocks[1][1];
                int alv = ALVSb.val[15];
                VPXMacroblock.Subblock[] aboveUSb = new VPXMacroblock.Subblock[2];
                VPXMacroblock.Subblock[] leftUSb = new VPXMacroblock.Subblock[2];
                VPXMacroblock.Subblock[] aboveVSb = new VPXMacroblock.Subblock[2];
                VPXMacroblock.Subblock[] leftVSb = new VPXMacroblock.Subblock[2];
                for (int x = 0; x < 2; x++) {
                    aboveUSb[x] = aboveMb.uSubblocks[1][x];
                    leftUSb[x] = leftMb.uSubblocks[x][1];
                    aboveVSb[x] = aboveMb.vSubblocks[1][x];
                    leftVSb[x] = leftMb.vSubblocks[x][1];
                }
                for (int sbRow = 0; sbRow < 2; sbRow++) {
                    for (int pRow = 0; pRow < 4; pRow++) {
                        for (int sbCol = 0; sbCol < 2; sbCol++) {
                            if (this.uSubblocks[sbRow][sbCol].val == null) {
                                this.uSubblocks[sbRow][sbCol].val = new int[16];
                            }
                            if (this.vSubblocks[sbRow][sbCol].val == null) {
                                this.vSubblocks[sbRow][sbCol].val = new int[16];
                            }
                            for (int pCol = 0; pCol < 4; pCol++) {
                                int upred = leftUSb[sbRow].val[pRow * 4 + 3] + aboveUSb[sbCol].val[12 + pCol] - alu;
                                upred = VP8Util.QuantizationParams.clip255(upred);
                                this.uSubblocks[sbRow][sbCol].val[pRow * 4 + pCol] = upred;
                                int vpred = leftVSb[sbRow].val[pRow * 4 + 3] + aboveVSb[sbCol].val[12 + pCol] - alv;
                                vpred = VP8Util.QuantizationParams.clip255(vpred);
                                this.vSubblocks[sbRow][sbCol].val[pRow * 4 + pCol] = vpred;
                            }
                        }
                    }
                }
                break;
            default:
                System.err.println("TODO predict_mb_uv: " + this.lumaMode);
                System.exit(0);
        }
    }

    private void predictY(VPXMacroblock[][] mbs) {
        VPXMacroblock aboveMb = mbs[this.Rrow - 1][this.column];
        VPXMacroblock leftMb = mbs[this.Rrow][this.column - 1];
        switch(this.lumaMode) {
            case 0:
                this.predictLumaDC(aboveMb, leftMb);
                break;
            case 1:
                this.predictLumaV(aboveMb);
                break;
            case 2:
                this.predictLumaH(leftMb);
                break;
            case 3:
                VPXMacroblock upperLeft = mbs[this.Rrow - 1][this.column - 1];
                VPXMacroblock.Subblock ALSb = upperLeft.ySubblocks[3][3];
                int aboveLeft = ALSb.val[15];
                this.predictLumaTM(aboveMb, leftMb, aboveLeft);
                break;
            default:
                System.err.println("TODO predict_mb_y: " + this.lumaMode);
                System.exit(0);
        }
    }

    private void predictLumaDC(VPXMacroblock above, VPXMacroblock left) {
        boolean hasAbove = this.Rrow > 1;
        boolean hasLeft = this.column > 1;
        int expected_dc = 128;
        if (hasAbove || hasLeft) {
            int average = 0;
            if (hasAbove) {
                for (int j = 0; j < 4; j++) {
                    VPXMacroblock.Subblock sb = above.ySubblocks[3][j];
                    for (int i = 0; i < 4; i++) {
                        average += sb.val[12 + i];
                    }
                }
            }
            if (hasLeft) {
                for (int j = 0; j < 4; j++) {
                    VPXMacroblock.Subblock sb = left.ySubblocks[j][3];
                    for (int i = 0; i < 4; i++) {
                        average += sb.val[i * 4 + 3];
                    }
                }
            }
            int shift = 3;
            if (hasAbove) {
                shift++;
            }
            if (hasLeft) {
                shift++;
            }
            expected_dc = average + (1 << shift - 1) >> shift;
        }
        int[] fill = new int[16];
        for (int i = 0; i < 16; i++) {
            fill[i] = expected_dc;
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                this.ySubblocks[y][x]._predict = fill;
            }
        }
    }

    private void predictLumaH(VPXMacroblock leftMb) {
        VPXMacroblock.Subblock[] leftYSb = new VPXMacroblock.Subblock[4];
        for (int row = 0; row < 4; row++) {
            leftYSb[row] = leftMb.ySubblocks[row][3];
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                VPXMacroblock.Subblock sb = this.ySubblocks[row][col];
                int[] block = new int[16];
                for (int bRow = 0; bRow < 4; bRow++) {
                    for (int bCol = 0; bCol < 4; bCol++) {
                        block[bRow * 4 + bCol] = leftYSb[row].val != null ? leftYSb[row].val[bRow * 4 + 3] : 129;
                    }
                }
                sb._predict = block;
            }
        }
    }

    private void predictLumaTM(VPXMacroblock above, VPXMacroblock left, int aboveLeft) {
        VPXMacroblock.Subblock[] aboveYSb = new VPXMacroblock.Subblock[4];
        VPXMacroblock.Subblock[] leftYSb = new VPXMacroblock.Subblock[4];
        for (int col = 0; col < 4; col++) {
            aboveYSb[col] = above.ySubblocks[3][col];
        }
        for (int row = 0; row < 4; row++) {
            leftYSb[row] = left.ySubblocks[row][3];
        }
        for (int row = 0; row < 4; row++) {
            for (int pRow = 0; pRow < 4; pRow++) {
                for (int col = 0; col < 4; col++) {
                    if (this.ySubblocks[row][col].val == null) {
                        this.ySubblocks[row][col].val = new int[16];
                    }
                    for (int pCol = 0; pCol < 4; pCol++) {
                        int pred = leftYSb[row].val[pRow * 4 + 3] + aboveYSb[col].val[12 + pCol] - aboveLeft;
                        this.ySubblocks[row][col].val[pRow * 4 + pCol] = VP8Util.QuantizationParams.clip255(pred);
                    }
                }
            }
        }
    }

    private void predictLumaV(VPXMacroblock above) {
        VPXMacroblock.Subblock[] aboveYSb = new VPXMacroblock.Subblock[4];
        for (int col = 0; col < 4; col++) {
            aboveYSb[col] = above.ySubblocks[3][col];
        }
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                VPXMacroblock.Subblock sb = this.ySubblocks[row][col];
                int[] block = new int[16];
                for (int j = 0; j < 4; j++) {
                    for (int i = 0; i < 4; i++) {
                        block[j * 4 + i] = aboveYSb[col].val != null ? aboveYSb[col].val[12 + i] : 127;
                    }
                }
                sb._predict = block;
            }
        }
    }

    public VPXMacroblock.Subblock getBottomSubblock(int x, VP8Util.PLANE plane) {
        if (plane == VP8Util.PLANE.Y1) {
            return this.ySubblocks[3][x];
        } else if (plane == VP8Util.PLANE.U) {
            return this.uSubblocks[1][x];
        } else if (plane == VP8Util.PLANE.V) {
            return this.vSubblocks[1][x];
        } else {
            return plane == VP8Util.PLANE.Y2 ? this.y2 : null;
        }
    }

    public VPXMacroblock.Subblock getRightSubBlock(int y, VP8Util.PLANE plane) {
        if (plane == VP8Util.PLANE.Y1) {
            return this.ySubblocks[y][3];
        } else if (plane == VP8Util.PLANE.U) {
            return this.uSubblocks[y][1];
        } else if (plane == VP8Util.PLANE.V) {
            return this.vSubblocks[y][1];
        } else {
            return plane == VP8Util.PLANE.Y2 ? this.y2 : null;
        }
    }

    public void decodeMacroBlock(VPXMacroblock[][] mbs, VPXBooleanDecoder tockenDecoder, int[][][][] coefProbs) {
        if (this.skipCoeff > 0) {
            this.skipFilter = this.lumaMode != 4;
        } else if (this.lumaMode != 4) {
            this.decodeMacroBlockTokens(true, mbs, tockenDecoder, coefProbs);
        } else {
            this.decodeMacroBlockTokens(false, mbs, tockenDecoder, coefProbs);
        }
    }

    private void decodeMacroBlockTokens(boolean withY2, VPXMacroblock[][] mbs, VPXBooleanDecoder decoder, int[][][][] coefProbs) {
        this.skipFilter = false;
        if (withY2) {
            this.skipFilter = this.skipFilter | this.decodePlaneTokens(1, VP8Util.PLANE.Y2, false, mbs, decoder, coefProbs);
        }
        this.skipFilter = this.skipFilter | this.decodePlaneTokens(4, VP8Util.PLANE.Y1, withY2, mbs, decoder, coefProbs);
        this.skipFilter = this.skipFilter | this.decodePlaneTokens(2, VP8Util.PLANE.U, false, mbs, decoder, coefProbs);
        this.skipFilter = this.skipFilter | this.decodePlaneTokens(2, VP8Util.PLANE.V, false, mbs, decoder, coefProbs);
        this.skipFilter = !this.skipFilter;
    }

    private boolean decodePlaneTokens(int dimentions, VP8Util.PLANE plane, boolean withY2, VPXMacroblock[][] mbs, VPXBooleanDecoder decoder, int[][][][] coefProbs) {
        boolean r = false;
        for (int row = 0; row < dimentions; row++) {
            for (int col = 0; col < dimentions; col++) {
                int lc = 0;
                VPXMacroblock.Subblock sb = null;
                if (VP8Util.PLANE.Y1.equals(plane)) {
                    sb = this.ySubblocks[row][col];
                } else if (VP8Util.PLANE.U.equals(plane)) {
                    sb = this.uSubblocks[row][col];
                } else if (VP8Util.PLANE.V.equals(plane)) {
                    sb = this.vSubblocks[row][col];
                } else {
                    if (!VP8Util.PLANE.Y2.equals(plane)) {
                        throw new IllegalStateException("unknown plane type " + plane);
                    }
                    sb = this.y2;
                }
                VPXMacroblock.Subblock l = sb.getLeft(plane, mbs);
                VPXMacroblock.Subblock a = sb.getAbove(plane, mbs);
                lc = (l.someValuePresent ? 1 : 0) + (a.someValuePresent ? 1 : 0);
                sb.decodeSubBlock(decoder, coefProbs, lc, VP8Util.planeToType(plane, withY2), withY2);
                r |= sb.someValuePresent;
            }
        }
        return r;
    }

    public void put(int mbRow, int mbCol, Picture p) {
        byte[] luma = p.getPlaneData(0);
        byte[] cb = p.getPlaneData(1);
        byte[] cr = p.getPlaneData(2);
        int strideLuma = p.getPlaneWidth(0);
        int strideChroma = p.getPlaneWidth(1);
        for (int lumaRow = 0; lumaRow < 4; lumaRow++) {
            for (int lumaCol = 0; lumaCol < 4; lumaCol++) {
                for (int lumaPRow = 0; lumaPRow < 4; lumaPRow++) {
                    for (int lumaPCol = 0; lumaPCol < 4; lumaPCol++) {
                        int y = (mbRow << 4) + (lumaRow << 2) + lumaPRow;
                        int x = (mbCol << 4) + (lumaCol << 2) + lumaPCol;
                        if (x < strideLuma && y < luma.length / strideLuma) {
                            int yy = this.ySubblocks[lumaRow][lumaCol].val[lumaPRow * 4 + lumaPCol];
                            luma[strideLuma * y + x] = (byte) (yy - 128);
                        }
                    }
                }
            }
        }
        for (int chromaRow = 0; chromaRow < 2; chromaRow++) {
            for (int chromaCol = 0; chromaCol < 2; chromaCol++) {
                for (int chromaPRow = 0; chromaPRow < 4; chromaPRow++) {
                    for (int chromaPCol = 0; chromaPCol < 4; chromaPCol++) {
                        int y = (mbRow << 3) + (chromaRow << 2) + chromaPRow;
                        int x = (mbCol << 3) + (chromaCol << 2) + chromaPCol;
                        if (x < strideChroma && y < cb.length / strideChroma) {
                            int u = this.uSubblocks[chromaRow][chromaCol].val[chromaPRow * 4 + chromaPCol];
                            int v = this.vSubblocks[chromaRow][chromaCol].val[chromaPRow * 4 + chromaPCol];
                            cb[strideChroma * y + x] = (byte) (u - 128);
                            cr[strideChroma * y + x] = (byte) (v - 128);
                        }
                    }
                }
            }
        }
    }

    public static class Subblock {

        public int[] val;

        public int[] _predict;

        public int[] residue;

        private int col;

        private int row;

        private VP8Util.PLANE plane;

        public int mode;

        public boolean someValuePresent;

        private int[] tokens;

        private VPXMacroblock self;

        public Subblock(VPXMacroblock self, int row, int col, VP8Util.PLANE plane) {
            this.self = self;
            this.row = row;
            this.col = col;
            this.plane = plane;
            this.tokens = new int[16];
        }

        public void predict(VPXMacroblock[][] mbs) {
            VPXMacroblock.Subblock aboveSb = this.getAbove(this.plane, mbs);
            VPXMacroblock.Subblock leftSb = this.getLeft(this.plane, mbs);
            int[] above = new int[4];
            int[] left = new int[4];
            int[] aboveValues = aboveSb.val != null ? aboveSb.val : VP8Util.PRED_BLOCK_127;
            above[0] = aboveValues[12];
            above[1] = aboveValues[13];
            above[2] = aboveValues[14];
            above[3] = aboveValues[15];
            int[] leftValues = leftSb.val != null ? leftSb.val : VP8Util.pickDefaultPrediction(this.mode);
            left[0] = leftValues[3];
            left[1] = leftValues[7];
            left[2] = leftValues[11];
            left[3] = leftValues[15];
            VPXMacroblock.Subblock aboveLeftSb = aboveSb.getLeft(this.plane, mbs);
            int aboveLeft;
            if (leftSb.val == null && aboveSb.val == null) {
                aboveLeft = 127;
            } else if (aboveSb.val == null) {
                aboveLeft = 127;
            } else {
                aboveLeft = aboveLeftSb.val != null ? aboveLeftSb.val[15] : VP8Util.pickDefaultPrediction(this.mode)[15];
            }
            int[] ar = this.getAboveRightLowestRow(mbs);
            switch(this.mode) {
                case 0:
                    this._predict = VP8Util.predictDC(above, left);
                    break;
                case 1:
                    this._predict = VP8Util.predictTM(above, left, aboveLeft);
                    break;
                case 2:
                    this._predict = VP8Util.predictVE(above, aboveLeft, ar);
                    break;
                case 3:
                    this._predict = VP8Util.predictHE(left, aboveLeft);
                    break;
                case 4:
                    this._predict = VP8Util.predictLD(above, ar);
                    break;
                case 5:
                    this._predict = VP8Util.predictRD(above, left, aboveLeft);
                    break;
                case 6:
                    this._predict = VP8Util.predictVR(above, left, aboveLeft);
                    break;
                case 7:
                    this._predict = VP8Util.predictVL(above, ar);
                    break;
                case 8:
                    this._predict = VP8Util.predictHD(above, left, aboveLeft);
                    break;
                case 9:
                    this._predict = VP8Util.predictHU(left);
                    break;
                default:
                    throw new NotSupportedException("TODO: unknowwn mode: " + this.mode);
            }
        }

        public void reconstruct() {
            int[] p = this.val != null ? this.val : this._predict;
            int[] dest = new int[16];
            for (int aRow = 0; aRow < 4; aRow++) {
                for (int aCol = 0; aCol < 4; aCol++) {
                    int a = VP8Util.QuantizationParams.clip255(this.residue[aRow * 4 + aCol] + p[aRow * 4 + aCol]);
                    dest[aRow * 4 + aCol] = a;
                }
            }
            this.val = dest;
        }

        public VPXMacroblock.Subblock getAbove(VP8Util.PLANE plane, VPXMacroblock[][] mbs) {
            if (this.row > 0) {
                if (VP8Util.PLANE.Y1.equals(this.plane)) {
                    return this.self.ySubblocks[this.row - 1][this.col];
                }
                if (VP8Util.PLANE.U.equals(this.plane)) {
                    return this.self.uSubblocks[this.row - 1][this.col];
                }
                if (VP8Util.PLANE.V.equals(this.plane)) {
                    return this.self.vSubblocks[this.row - 1][this.col];
                }
            }
            int x = this.col;
            VPXMacroblock mb2 = mbs[this.self.Rrow - 1][this.self.column];
            if (plane == VP8Util.PLANE.Y2) {
                while (mb2.lumaMode == 4) {
                    mb2 = mbs[mb2.Rrow - 1][mb2.column];
                }
            }
            return mb2.getBottomSubblock(x, plane);
        }

        public VPXMacroblock.Subblock getLeft(VP8Util.PLANE p, VPXMacroblock[][] mbs) {
            if (this.col > 0) {
                if (VP8Util.PLANE.Y1.equals(this.plane)) {
                    return this.self.ySubblocks[this.row][this.col - 1];
                }
                if (VP8Util.PLANE.U.equals(this.plane)) {
                    return this.self.uSubblocks[this.row][this.col - 1];
                }
                if (VP8Util.PLANE.V.equals(this.plane)) {
                    return this.self.vSubblocks[this.row][this.col - 1];
                }
            }
            int y = this.row;
            VPXMacroblock mb2 = mbs[this.self.Rrow][this.self.column - 1];
            if (p == VP8Util.PLANE.Y2) {
                while (mb2.lumaMode == 4) {
                    mb2 = mbs[mb2.Rrow][mb2.column - 1];
                }
            }
            return mb2.getRightSubBlock(y, p);
        }

        private int[] getAboveRightLowestRow(VPXMacroblock[][] mbs) {
            if (!VP8Util.PLANE.Y1.equals(this.plane)) {
                throw new NotImplementedException("Decoder.getAboveRight: not implemented for Y2 and chroma planes");
            } else {
                int[] aboveRightDistValues;
                if (this.row == 0 && this.col < 3) {
                    VPXMacroblock mb2 = mbs[this.self.Rrow - 1][this.self.column];
                    VPXMacroblock.Subblock aboveRight = mb2.ySubblocks[3][this.col + 1];
                    aboveRightDistValues = aboveRight.val;
                } else if (this.row > 0 && this.col < 3) {
                    VPXMacroblock.Subblock aboveRight = this.self.ySubblocks[this.row - 1][this.col + 1];
                    aboveRightDistValues = aboveRight.val;
                } else {
                    if (this.row != 0 || this.col != 3) {
                        VPXMacroblock.Subblock sb2 = this.self.ySubblocks[0][3];
                        return sb2.getAboveRightLowestRow(mbs);
                    }
                    VPXMacroblock aboveRightMb = mbs[this.self.Rrow - 1][this.self.column + 1];
                    if (aboveRightMb.column < mbs[0].length - 1) {
                        VPXMacroblock.Subblock aboveRightSb = aboveRightMb.ySubblocks[3][0];
                        aboveRightDistValues = aboveRightSb.val;
                    } else {
                        aboveRightDistValues = new int[16];
                        int fillVal = aboveRightMb.Rrow == 0 ? 127 : mbs[this.self.Rrow - 1][this.self.column].ySubblocks[3][3].val[15];
                        Arrays.fill(aboveRightDistValues, fillVal);
                    }
                }
                if (aboveRightDistValues == null) {
                    aboveRightDistValues = VP8Util.PRED_BLOCK_127;
                }
                return new int[] { aboveRightDistValues[12], aboveRightDistValues[13], aboveRightDistValues[14], aboveRightDistValues[15] };
            }
        }

        public void decodeSubBlock(VPXBooleanDecoder decoder, int[][][][] allProbs, int ilc, int type, boolean withY2) {
            int startAt = 0;
            if (withY2) {
                startAt = 1;
            }
            int lc = ilc;
            int count = 0;
            int v = 1;
            boolean skip = false;
            for (this.someValuePresent = false; v != 11 && count + startAt < 16; count++) {
                int[] probs = allProbs[type][VP8Util.SubblockConstants.vp8CoefBands[count + startAt]][lc];
                if (!skip) {
                    v = decoder.readTree(VP8Util.SubblockConstants.vp8CoefTree, probs);
                } else {
                    v = decoder.readTreeSkip(VP8Util.SubblockConstants.vp8CoefTree, probs, 1);
                }
                int dv = this.decodeToken(decoder, v);
                lc = 0;
                skip = false;
                if (dv == 1 || dv == -1) {
                    lc = 1;
                } else if (dv > 1 || dv < -1) {
                    lc = 2;
                } else if (dv == 0) {
                    skip = true;
                }
                if (v != 11) {
                    this.tokens[VP8Util.SubblockConstants.vp8defaultZigZag1d[count + startAt]] = dv;
                }
            }
            for (int x = 0; x < 16; x++) {
                if (this.tokens[x] != 0) {
                    this.someValuePresent = true;
                }
            }
        }

        private int decodeToken(VPXBooleanDecoder decoder, int initialValue) {
            int token = initialValue;
            if (initialValue == 5) {
                token = 5 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat1);
            }
            if (initialValue == 6) {
                token = 7 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat2);
            }
            if (initialValue == 7) {
                token = 11 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat3);
            }
            if (initialValue == 8) {
                token = 19 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat4);
            }
            if (initialValue == 9) {
                token = 35 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat5);
            }
            if (initialValue == 10) {
                token = 67 + this.DCTextra(decoder, VP8Util.SubblockConstants.Pcat6);
            }
            if (initialValue != 0 && initialValue != 11 && decoder.readBitEq() > 0) {
                token = -token;
            }
            return token;
        }

        private int DCTextra(VPXBooleanDecoder decoder, int[] p) {
            int v = 0;
            int offset = 0;
            do {
                v += v + decoder.readBit(p[offset]);
            } while (p[++offset] > 0);
            return v;
        }

        public void dequantSubblock(int dc, int ac, Integer Dc) {
            int[] adjustedValues = new int[16];
            adjustedValues[0] = this.tokens[0] * dc;
            for (int i = 1; i < 16; i++) {
                adjustedValues[i] = this.tokens[i] * ac;
            }
            if (Dc != null) {
                adjustedValues[0] = Dc;
            }
            this.residue = VP8DCT.decodeDCT(adjustedValues);
        }
    }
}