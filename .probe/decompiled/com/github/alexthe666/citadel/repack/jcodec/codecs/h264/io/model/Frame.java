package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.io.model;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Utils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;
import java.util.Comparator;

public class Frame extends Picture {

    private int frameNo;

    private SliceType frameType;

    private H264Utils.MvList2D mvs;

    private Frame[][][] refsUsed;

    private boolean shortTerm;

    private int poc;

    public static Comparator<Frame> POCAsc = new Comparator<Frame>() {

        public int compare(Frame o1, Frame o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else {
                return o1.poc > o2.poc ? 1 : (o1.poc == o2.poc ? 0 : -1);
            }
        }
    };

    public static Comparator<Frame> POCDesc = new Comparator<Frame>() {

        public int compare(Frame o1, Frame o2) {
            if (o1 == null && o2 == null) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else {
                return o1.poc < o2.poc ? 1 : (o1.poc == o2.poc ? 0 : -1);
            }
        }
    };

    public Frame(int width, int height, byte[][] data, ColorSpace color, Rect crop, int frameNo, SliceType frameType, H264Utils.MvList2D mvs, Frame[][][] refsUsed, int poc) {
        super(width, height, data, (byte[][]) null, color, 0, crop);
        this.frameNo = frameNo;
        this.mvs = mvs;
        this.refsUsed = refsUsed;
        this.poc = poc;
        this.shortTerm = true;
    }

    public static Frame createFrame(Frame pic) {
        Picture comp = pic.createCompatible();
        return new Frame(comp.getWidth(), comp.getHeight(), comp.getData(), comp.getColor(), pic.getCrop(), pic.frameNo, pic.frameType, pic.mvs, pic.refsUsed, pic.poc);
    }

    public Frame cropped() {
        Picture cropped = super.cropped();
        return new Frame(cropped.getWidth(), cropped.getHeight(), cropped.getData(), cropped.getColor(), null, this.frameNo, this.frameType, this.mvs, this.refsUsed, this.poc);
    }

    public void copyFromFrame(Frame src) {
        super.copyFrom(src);
        this.frameNo = src.frameNo;
        this.mvs = src.mvs;
        this.shortTerm = src.shortTerm;
        this.refsUsed = src.refsUsed;
        this.poc = src.poc;
    }

    public Frame cloneCropped() {
        if (this.cropNeeded()) {
            return this.cropped();
        } else {
            Frame clone = createFrame(this);
            clone.copyFrom(this);
            return clone;
        }
    }

    public int getFrameNo() {
        return this.frameNo;
    }

    public H264Utils.MvList2D getMvs() {
        return this.mvs;
    }

    public boolean isShortTerm() {
        return this.shortTerm;
    }

    public void setShortTerm(boolean shortTerm) {
        this.shortTerm = shortTerm;
    }

    public int getPOC() {
        return this.poc;
    }

    public Frame[][][] getRefsUsed() {
        return this.refsUsed;
    }

    public SliceType getFrameType() {
        return this.frameType;
    }
}