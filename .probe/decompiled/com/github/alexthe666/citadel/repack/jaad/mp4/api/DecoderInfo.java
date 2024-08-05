package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.AC3DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.AMRDecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.AVCDecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.EAC3DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.EVRCDecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.H263DecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.QCELPDecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.codec.SMVDecoderInfo;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.sampleentries.codec.CodecSpecificBox;

public abstract class DecoderInfo {

    static DecoderInfo parse(CodecSpecificBox css) {
        long l = css.getType();
        DecoderInfo info;
        if (l == 1681012275L) {
            info = new H263DecoderInfo(css);
        } else if (l == 1684106610L) {
            info = new AMRDecoderInfo(css);
        } else if (l == 1684371043L) {
            info = new EVRCDecoderInfo(css);
        } else if (l == 1685152624L) {
            info = new QCELPDecoderInfo(css);
        } else if (l == 1685286262L) {
            info = new SMVDecoderInfo(css);
        } else if (l == 1635148611L) {
            info = new AVCDecoderInfo(css);
        } else if (l == 1684103987L) {
            info = new AC3DecoderInfo(css);
        } else if (l == 1684366131L) {
            info = new EAC3DecoderInfo(css);
        } else {
            info = new DecoderInfo.UnknownDecoderInfo();
        }
        return info;
    }

    private static class UnknownDecoderInfo extends DecoderInfo {
    }
}