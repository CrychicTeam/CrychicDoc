package com.github.alexthe666.citadel.repack.jcodec.common;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rational;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import java.nio.ByteBuffer;

public class VideoCodecMeta extends CodecMeta {

    private Size size;

    private Rational pasp;

    private boolean interlaced;

    private boolean topFieldFirst;

    private ColorSpace color;

    public static VideoCodecMeta createVideoCodecMeta(String fourcc, ByteBuffer codecPrivate, Size size, Rational pasp) {
        VideoCodecMeta self = new VideoCodecMeta(fourcc, codecPrivate);
        self.size = size;
        self.pasp = pasp;
        return self;
    }

    public static VideoCodecMeta createVideoCodecMeta2(String fourcc, ByteBuffer codecPrivate, Size size, Rational pasp, boolean interlaced, boolean topFieldFirst) {
        VideoCodecMeta self = new VideoCodecMeta(fourcc, codecPrivate);
        self.size = size;
        self.pasp = pasp;
        self.interlaced = interlaced;
        self.topFieldFirst = topFieldFirst;
        return self;
    }

    public VideoCodecMeta(String fourcc, ByteBuffer codecPrivate) {
        super(fourcc, codecPrivate);
    }

    public Size getSize() {
        return this.size;
    }

    public Rational getPasp() {
        return this.pasp;
    }

    public Rational getPixelAspectRatio() {
        return this.pasp;
    }

    public boolean isInterlaced() {
        return this.interlaced;
    }

    public boolean isTopFieldFirst() {
        return this.topFieldFirst;
    }

    public ColorSpace getColor() {
        return this.color;
    }

    public static VideoCodecMeta createSimpleVideoCodecMeta(Size size, ColorSpace color) {
        VideoCodecMeta self = new VideoCodecMeta(null, null);
        self.size = size;
        self.color = color;
        return self;
    }

    public void setPixelAspectRatio(Rational pasp) {
        this.pasp = pasp;
    }
}