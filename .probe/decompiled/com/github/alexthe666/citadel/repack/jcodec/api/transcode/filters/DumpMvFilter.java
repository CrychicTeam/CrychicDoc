package com.github.alexthe666.citadel.repack.jcodec.api.transcode.filters;

import com.github.alexthe666.citadel.repack.jcodec.api.transcode.Filter;
import com.github.alexthe666.citadel.repack.jcodec.api.transcode.PixelStore;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.Frame;
import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model.SliceType;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class DumpMvFilter implements Filter {

    private boolean js;

    public DumpMvFilter(boolean js) {
        this.js = js;
    }

    @Override
    public PixelStore.LoanerPicture filter(Picture picture, PixelStore pixelStore) {
        Frame dec = (Frame) picture;
        if (!this.js) {
            this.dumpMvTxt(dec);
        } else {
            this.dumpMvJs(dec);
        }
        return null;
    }

    private void dumpMvTxt(Frame dec) {
        System.err.println("FRAME ================================================================");
        if (dec.getFrameType() != SliceType.I) {
            H264Utils.MvList2D mvs = dec.getMvs();
            for (int i = 0; i < 2; i++) {
                System.err.println((i == 0 ? "BCK" : "FWD") + " ===========================================================================");
                for (int blkY = 0; blkY < mvs.getHeight(); blkY++) {
                    StringBuilder line0 = new StringBuilder();
                    StringBuilder line1 = new StringBuilder();
                    StringBuilder line2 = new StringBuilder();
                    StringBuilder line3 = new StringBuilder();
                    line0.append("+");
                    line1.append("|");
                    line2.append("|");
                    line3.append("|");
                    for (int blkX = 0; blkX < mvs.getWidth(); blkX++) {
                        line0.append("------+");
                        line1.append(String.format("%6d|", H264Utils.Mv.mvX(mvs.getMv(blkX, blkY, i))));
                        line2.append(String.format("%6d|", H264Utils.Mv.mvY(mvs.getMv(blkX, blkY, i))));
                        line3.append(String.format("    %2d|", H264Utils.Mv.mvRef(mvs.getMv(blkX, blkY, i))));
                    }
                    System.err.println(line0.toString());
                    System.err.println(line1.toString());
                    System.err.println(line2.toString());
                    System.err.println(line3.toString());
                }
                if (dec.getFrameType() != SliceType.B) {
                    break;
                }
            }
        }
    }

    private void dumpMvJs(Frame dec) {
        System.err.println("{");
        if (dec.getFrameType() != SliceType.I) {
            H264Utils.MvList2D mvs = dec.getMvs();
            for (int i = 0; i < 2; i++) {
                System.err.println((i == 0 ? "backRef" : "forwardRef") + ": [");
                for (int blkY = 0; blkY < mvs.getHeight(); blkY++) {
                    for (int blkX = 0; blkX < mvs.getWidth(); blkX++) {
                        System.err.println("{x: " + blkX + ", y: " + blkY + ", mx: " + H264Utils.Mv.mvX(mvs.getMv(blkX, blkY, i)) + ", my: " + H264Utils.Mv.mvY(mvs.getMv(blkX, blkY, i)) + ", ridx:" + H264Utils.Mv.mvRef(mvs.getMv(blkX, blkY, i)) + "},");
                    }
                }
                System.err.println("],");
                if (dec.getFrameType() != SliceType.B) {
                    break;
                }
            }
            System.err.println("}");
        }
    }

    @Override
    public ColorSpace getInputColor() {
        return null;
    }

    @Override
    public ColorSpace getOutputColor() {
        return null;
    }
}