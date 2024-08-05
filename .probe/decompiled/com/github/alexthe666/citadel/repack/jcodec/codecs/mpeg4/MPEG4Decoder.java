package com.github.alexthe666.citadel.repack.jcodec.codecs.mpeg4;

import com.github.alexthe666.citadel.repack.jcodec.common.UsedViaReflection;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoCodecMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.VideoDecoder;
import com.github.alexthe666.citadel.repack.jcodec.common.io.BitReader;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.nio.ByteBuffer;

public class MPEG4Decoder extends VideoDecoder {

    private Picture[] refs = new Picture[2];

    private Macroblock[] prevMBs;

    private Macroblock[] mbs;

    private MPEG4DecodingContext ctx;

    @Override
    public Picture decodeFrame(ByteBuffer data, byte[][] buffer) {
        if (this.ctx == null) {
            this.ctx = new MPEG4DecodingContext();
        }
        if (!this.ctx.readHeaders(data)) {
            return null;
        } else {
            this.ctx.fcodeForward = this.ctx.fcodeBackward = this.ctx.intraDCThreshold = 0;
            BitReader br = BitReader.createBitReader(data);
            if (!this.ctx.readVOPHeader(br)) {
                return null;
            } else {
                this.mbs = new Macroblock[this.ctx.mbWidth * this.ctx.mbHeight];
                for (int i = 0; i < this.mbs.length; i++) {
                    this.mbs[i] = new Macroblock();
                }
                Picture decoded = null;
                if (this.ctx.codingType != 2) {
                    switch(this.ctx.codingType) {
                        case 0:
                            decoded = this.decodeIFrame(br, this.ctx, buffer);
                            break;
                        case 1:
                            decoded = this.decodePFrame(br, this.ctx, buffer, this.ctx.fcodeForward);
                        case 2:
                        default:
                            break;
                        case 3:
                            throw new RuntimeException("GMC not supported.");
                        case 4:
                            return null;
                    }
                    this.refs[1] = this.refs[0];
                    this.refs[0] = decoded;
                    this.prevMBs = this.mbs;
                } else {
                    decoded = this.decodeBFrame(br, this.ctx, buffer, this.ctx.quant, this.ctx.fcodeForward, this.ctx.fcodeBackward);
                }
                br.terminate();
                return decoded;
            }
        }
    }

    private Picture decodeIFrame(BitReader br, MPEG4DecodingContext ctx, byte[][] buffer) {
        Picture p = new Picture(ctx.mbWidth << 4, ctx.mbHeight << 4, buffer, (byte[][]) null, ColorSpace.YUV420, 0, new Rect(0, 0, ctx.width, ctx.height));
        int bound = 0;
        for (int y = 0; y < ctx.mbHeight; y++) {
            for (int x = 0; x < ctx.mbWidth; x++) {
                Macroblock mb = this.mbs[y * ctx.mbWidth + x];
                mb.reset(x, y, bound);
                MPEG4Bitstream.readIntraMode(br, ctx, mb);
                int index = x + y * ctx.mbWidth;
                Macroblock aboveMb = null;
                Macroblock aboveLeftMb = null;
                Macroblock leftMb = null;
                int apos = index - ctx.mbWidth;
                int lpos = index - 1;
                int alpos = index - 1 - ctx.mbWidth;
                if (apos >= bound) {
                    aboveMb = this.mbs[apos];
                }
                if (alpos >= bound) {
                    aboveLeftMb = this.mbs[alpos];
                }
                if (x > 0 && lpos >= bound) {
                    leftMb = this.mbs[lpos];
                }
                MPEG4Bitstream.readCoeffIntra(br, ctx, mb, aboveMb, leftMb, aboveLeftMb);
                x = mb.x;
                y = mb.y;
                bound = mb.bound;
                MPEG4Renderer.renderIntra(mb, ctx);
                putPix(p, mb, x, y);
            }
        }
        return p;
    }

    Picture decodePFrame(BitReader br, MPEG4DecodingContext ctx, byte[][] buffers, int fcode) {
        int bound = 0;
        int mbWidth = ctx.mbWidth;
        int mbHeight = ctx.mbHeight;
        Picture p = new Picture(ctx.mbWidth << 4, ctx.mbHeight << 4, buffers, (byte[][]) null, ColorSpace.YUV420, 0, new Rect(0, 0, ctx.width, ctx.height));
        for (int y = 0; y < mbHeight; y++) {
            for (int x = 0; x < mbWidth; x++) {
                while (br.checkNBit(10) == 1) {
                    br.skip(10);
                }
                if (MPEG4Bitstream.checkResyncMarker(br, fcode - 1)) {
                    bound = MPEG4Bitstream.readVideoPacketHeader(br, ctx, fcode - 1, true, false, true);
                    x = bound % mbWidth;
                    y = bound / mbWidth;
                }
                int index = x + y * ctx.mbWidth;
                Macroblock aboveMb = null;
                Macroblock aboveLeftMb = null;
                Macroblock leftMb = null;
                Macroblock aboveRightMb = null;
                int apos = index - ctx.mbWidth;
                int lpos = index - 1;
                int alpos = index - 1 - ctx.mbWidth;
                int arpos = index + 1 - ctx.mbWidth;
                if (apos >= bound) {
                    aboveMb = this.mbs[apos];
                }
                if (alpos >= bound) {
                    aboveLeftMb = this.mbs[alpos];
                }
                if (x > 0 && lpos >= bound) {
                    leftMb = this.mbs[lpos];
                }
                if (arpos >= bound && x < ctx.mbWidth - 1) {
                    aboveRightMb = this.mbs[arpos];
                }
                Macroblock mb = this.mbs[y * ctx.mbWidth + x];
                mb.reset(x, y, bound);
                MPEG4Bitstream.readInterModeCoeffs(br, ctx, fcode, mb, aboveMb, leftMb, aboveLeftMb, aboveRightMb);
                MPEG4Renderer.renderInter(ctx, this.refs, mb, fcode, 0, false);
                putPix(p, mb, x, y);
            }
        }
        return p;
    }

    private Picture decodeBFrame(BitReader br, MPEG4DecodingContext ctx, byte[][] buffers, int quant, int fcodeForward, int fcodeBackward) {
        Picture p = new Picture(ctx.mbWidth << 4, ctx.mbHeight << 4, buffers, (byte[][]) null, ColorSpace.YUV420, 0, new Rect(0, 0, ctx.width, ctx.height));
        Macroblock.Vector pFMV = Macroblock.vec();
        Macroblock.Vector pBMV = Macroblock.vec();
        int fcodeMax = fcodeForward > fcodeBackward ? fcodeForward : fcodeBackward;
        for (int y = 0; y < ctx.mbHeight; y++) {
            pBMV.x = pBMV.y = pFMV.x = pFMV.y = 0;
            for (int x = 0; x < ctx.mbWidth; x++) {
                Macroblock mb = this.mbs[y * ctx.mbWidth + x];
                Macroblock lastMB = this.prevMBs[y * ctx.mbWidth + x];
                if (MPEG4Bitstream.checkResyncMarker(br, fcodeMax - 1)) {
                    int bound = MPEG4Bitstream.readVideoPacketHeader(br, ctx, fcodeMax - 1, fcodeForward != 0, fcodeBackward != 0, ctx.intraDCThreshold != 0);
                    x = bound % ctx.mbWidth;
                    y = bound / ctx.mbWidth;
                    pBMV.x = pBMV.y = pFMV.x = pFMV.y = 0;
                }
                mb.x = x;
                mb.y = y;
                mb.quant = quant;
                if (lastMB.mode == 16) {
                    mb.cbp = 0;
                    mb.mode = 3;
                    MPEG4Bitstream.readInterCoeffs(br, ctx, mb);
                    MPEG4Renderer.renderInter(ctx, this.refs, lastMB, fcodeForward, 1, true);
                    putPix(p, lastMB, x, y);
                } else {
                    MPEG4Bitstream.readBi(br, ctx, fcodeForward, fcodeBackward, mb, lastMB, pFMV, pBMV);
                    MPEG4BiRenderer.renderBi(ctx, this.refs, fcodeForward, fcodeBackward, mb);
                    putPix(p, mb, x, y);
                }
            }
        }
        return p;
    }

    public static void putPix(Picture p, Macroblock mb, int x, int y) {
        byte[] plane0 = p.getPlaneData(0);
        int dsto0 = (y << 4) * p.getWidth() + (x << 4);
        int row = 0;
        for (int srco = 0; row < 16; dsto0 += p.getWidth()) {
            for (int col = 0; col < 16; srco++) {
                plane0[dsto0 + col] = mb.pred[0][srco];
                col++;
            }
            row++;
        }
        for (int pl = 1; pl < 3; pl++) {
            byte[] plane = p.getPlaneData(pl);
            int dsto = (y << 3) * p.getPlaneWidth(pl) + (x << 3);
            int rowx = 0;
            for (int srco = 0; rowx < 8; dsto += p.getPlaneWidth(pl)) {
                for (int col = 0; col < 8; srco++) {
                    plane[dsto + col] = mb.pred[pl][srco];
                    col++;
                }
                rowx++;
            }
        }
    }

    @Override
    public VideoCodecMeta getCodecMeta(ByteBuffer data) {
        MPEG4DecodingContext ctx = MPEG4DecodingContext.readFromHeaders(data.duplicate());
        return ctx == null ? null : VideoCodecMeta.createSimpleVideoCodecMeta(new Size(ctx.width, ctx.height), ColorSpace.YUV420J);
    }

    @UsedViaReflection
    public static int probe(ByteBuffer data) {
        MPEG4DecodingContext ctx = MPEG4DecodingContext.readFromHeaders(data.duplicate());
        return ctx == null ? 0 : Math.min(ctx.width > 320 ? (ctx.width < 1280 ? 100 : 50) : 50, ctx.height > 240 ? (ctx.height < 720 ? 100 : 50) : 50);
    }
}