package com.github.alexthe666.citadel.repack.jcodec.containers.mps;

import java.util.ArrayList;
import java.util.List;

public final class MTSStreamType {

    private static final List<MTSStreamType> _values = new ArrayList();

    public static final MTSStreamType RESERVED = new MTSStreamType(0, false, false);

    public static final MTSStreamType VIDEO_MPEG1 = new MTSStreamType(1, true, false);

    public static final MTSStreamType VIDEO_MPEG2 = new MTSStreamType(2, true, false);

    public static final MTSStreamType AUDIO_MPEG1 = new MTSStreamType(3, false, true);

    public static final MTSStreamType AUDIO_MPEG2 = new MTSStreamType(4, false, true);

    public static final MTSStreamType PRIVATE_SECTION = new MTSStreamType(5, false, false);

    public static final MTSStreamType PRIVATE_DATA = new MTSStreamType(6, false, false);

    public static final MTSStreamType MHEG = new MTSStreamType(7, false, false);

    public static final MTSStreamType DSM_CC = new MTSStreamType(8, false, false);

    public static final MTSStreamType ATM_SYNC = new MTSStreamType(9, false, false);

    public static final MTSStreamType DSM_CC_A = new MTSStreamType(10, false, false);

    public static final MTSStreamType DSM_CC_B = new MTSStreamType(11, false, false);

    public static final MTSStreamType DSM_CC_C = new MTSStreamType(12, false, false);

    public static final MTSStreamType DSM_CC_D = new MTSStreamType(13, false, false);

    public static final MTSStreamType MPEG_AUX = new MTSStreamType(14, false, false);

    public static final MTSStreamType AUDIO_AAC_ADTS = new MTSStreamType(15, false, true);

    public static final MTSStreamType VIDEO_MPEG4 = new MTSStreamType(16, true, false);

    public static final MTSStreamType AUDIO_AAC_LATM = new MTSStreamType(17, false, true);

    public static final MTSStreamType FLEXMUX_PES = new MTSStreamType(18, false, false);

    public static final MTSStreamType FLEXMUX_SEC = new MTSStreamType(19, false, false);

    public static final MTSStreamType DSM_CC_SDP = new MTSStreamType(20, false, false);

    public static final MTSStreamType META_PES = new MTSStreamType(21, false, false);

    public static final MTSStreamType META_SEC = new MTSStreamType(22, false, false);

    public static final MTSStreamType DSM_CC_DATA_CAROUSEL = new MTSStreamType(23, false, false);

    public static final MTSStreamType DSM_CC_OBJ_CAROUSEL = new MTSStreamType(24, false, false);

    public static final MTSStreamType DSM_CC_SDP1 = new MTSStreamType(25, false, false);

    public static final MTSStreamType IPMP = new MTSStreamType(26, false, false);

    public static final MTSStreamType VIDEO_H264 = new MTSStreamType(27, true, false);

    public static final MTSStreamType AUDIO_AAC_RAW = new MTSStreamType(28, false, true);

    public static final MTSStreamType SUBS = new MTSStreamType(29, false, false);

    public static final MTSStreamType AUX_3D = new MTSStreamType(30, false, false);

    public static final MTSStreamType VIDEO_AVC_SVC = new MTSStreamType(31, true, false);

    public static final MTSStreamType VIDEO_AVC_MVC = new MTSStreamType(32, true, false);

    public static final MTSStreamType VIDEO_J2K = new MTSStreamType(33, true, false);

    public static final MTSStreamType VIDEO_MPEG2_3D = new MTSStreamType(34, true, false);

    public static final MTSStreamType VIDEO_H264_3D = new MTSStreamType(35, true, false);

    public static final MTSStreamType VIDEO_CAVS = new MTSStreamType(66, false, true);

    public static final MTSStreamType IPMP_STREAM = new MTSStreamType(127, false, false);

    public static final MTSStreamType AUDIO_AC3 = new MTSStreamType(129, false, true);

    public static final MTSStreamType AUDIO_DTS = new MTSStreamType(138, false, true);

    private int tag;

    private boolean video;

    private boolean audio;

    private MTSStreamType(int tag, boolean video, boolean audio) {
        this.tag = tag;
        this.video = video;
        this.audio = audio;
        _values.add(this);
    }

    public static MTSStreamType[] values() {
        return (MTSStreamType[]) _values.toArray(new MTSStreamType[0]);
    }

    public static MTSStreamType fromTag(int streamTypeTag) {
        MTSStreamType[] values = values();
        for (int i = 0; i < values.length; i++) {
            MTSStreamType streamType = values[i];
            if (streamType.tag == streamTypeTag) {
                return streamType;
            }
        }
        return null;
    }

    public int getTag() {
        return this.tag;
    }

    public boolean isVideo() {
        return this.video;
    }

    public boolean isAudio() {
        return this.audio;
    }
}