package com.github.alexthe666.citadel.repack.jcodec.common.model;

import java.util.List;

public class Frame {

    private Picture pic;

    private RationalLarge pts;

    private RationalLarge duration;

    private Rational pixelAspect;

    private TapeTimecode tapeTimecode;

    private int frameNo;

    private List<String> messages;

    public Frame(Picture pic, RationalLarge pts, RationalLarge duration, Rational pixelAspect, int frameNo, TapeTimecode tapeTimecode, List<String> messages) {
        this.pic = pic;
        this.pts = pts;
        this.duration = duration;
        this.pixelAspect = pixelAspect;
        this.tapeTimecode = tapeTimecode;
        this.frameNo = frameNo;
        this.messages = messages;
    }

    public Picture getPic() {
        return this.pic;
    }

    public RationalLarge getPts() {
        return this.pts;
    }

    public RationalLarge getDuration() {
        return this.duration;
    }

    public Rational getPixelAspect() {
        return this.pixelAspect;
    }

    public TapeTimecode getTapeTimecode() {
        return this.tapeTimecode;
    }

    public int getFrameNo() {
        return this.frameNo;
    }

    public List<String> getMessages() {
        return this.messages;
    }

    public boolean isAvailable() {
        return true;
    }
}