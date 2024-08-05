package com.github.alexthe666.citadel.repack.jcodec.containers.mxf;

import com.github.alexthe666.citadel.repack.jcodec.common.Codec;
import com.github.alexthe666.citadel.repack.jcodec.containers.mxf.model.UL;

public class MXFCodec {

    private final UL ul;

    private final Codec codec;

    public static final MXFCodec MPEG2_XDCAM = mxfCodec("06.0E.2B.34.04.01.01.03.04.01.02.02.01.04.03", Codec.MPEG2);

    public static final MXFCodec MPEG2_ML = mxfCodec("06.0E.2B.34.04.01.01.03.04.01.02.02.01.01.11", Codec.MPEG2);

    public static final MXFCodec MPEG2_D10_PAL = mxfCodec("06.0E.2B.34.04.01.01.01.04.01.02.02.01.02.01.01", Codec.MPEG2);

    public static final MXFCodec MPEG2_HL = mxfCodec("06.0E.2B.34.04.01.01.03.04.01.02.02.01.03.03", Codec.MPEG2);

    public static final MXFCodec MPEG2_HL_422_I = mxfCodec("06.0E.2B.34.04.01.01.03.04.01.02.02.01.04.02", Codec.MPEG2);

    public static final MXFCodec MPEG4_XDCAM_PROXY = mxfCodec("06.0E.2B.34.04.01.01.03.04.01.02.02.01.20.02.03", Codec.MPEG4);

    public static final MXFCodec DV_25_PAL = mxfCodec("06.0E.2B.34.04.01.01.01.04.01.02.02.02.01.02", Codec.DV);

    public static final MXFCodec JPEG2000 = mxfCodec("06.0E.2B.34.04.01.01.07.04.01.02.02.03.01.01", Codec.J2K);

    public static final MXFCodec VC1 = mxfCodec("06.0e.2b.34.04.01.01.0A.04.01.02.02.04", Codec.VC1);

    public static final MXFCodec RAW = mxfCodec("06.0E.2B.34.04.01.01.01.04.01.02.01.7F", null);

    public static final MXFCodec RAW_422 = mxfCodec("06.0E.2B.34.04.01.01.0A.04.01.02.01.01.02.01", null);

    public static final MXFCodec VC3_DNXHD = mxfCodec("06.0E.2B.34.04.01.01.01.04.01.02.02.03.02", Codec.VC3);

    public static final MXFCodec VC3_DNXHD_2 = mxfCodec("06.0E.2B.34.04.01.01.01.04.01.02.02.71", Codec.VC3);

    public static final MXFCodec VC3_DNXHD_AVID = mxfCodec("06.0E.2B.34.04.01.01.01.0E.04.02.01.02.04.01", Codec.VC3);

    public static final MXFCodec AVC_INTRA = mxfCodec("06.0E.2B.34.04.01.01.0A.04.01.02.02.01.32", Codec.H264);

    public static final MXFCodec AVC_SPSPPS = mxfCodec("06.0E.2B.34.04.01.01.0A.04.01.02.02.01.31.11.01", Codec.H264);

    public static final MXFCodec V210 = mxfCodec("06.0E.2B.34.04.01.01.0A.04.01.02.01.01.02.02", Codec.V210);

    public static final MXFCodec PRORES_AVID = mxfCodec("06.0E.2B.34.04.01.01.01.0E.04.02.01.02.11", Codec.PRORES);

    public static final MXFCodec PRORES = mxfCodec("06.0E.2B.34.04.01.01.0D.04.01.02.02.03.06", Codec.PRORES);

    public static final MXFCodec PCM_S16LE_1 = mxfCodec("06.0E.2B.34.04.01.01.01.04.02.02.01", null);

    public static final MXFCodec PCM_S16LE_3 = mxfCodec("06.0E.2B.34.04.01.01.01.04.02.02.01.01", null);

    public static final MXFCodec PCM_S16LE_2 = mxfCodec("06.0E.2B.34.04.01.01.01.04.02.02.01.7F", null);

    public static final MXFCodec PCM_S16BE = mxfCodec("06.0E.2B.34.04.01.01.07.04.02.02.01.7E", null);

    public static final MXFCodec PCM_ALAW = mxfCodec("06.0E.2B.34.04.01.01.04.04.02.02.02.03.01.01", Codec.ALAW);

    public static final MXFCodec AC3 = mxfCodec("06.0E.2B.34.04.01.01.01.04.02.02.02.03.02.01", Codec.AC3);

    public static final MXFCodec MP2 = mxfCodec("06.0E.2B.34.04.01.01.01.04.02.02.02.03.02.05", Codec.MP3);

    public static final MXFCodec UNKNOWN = new MXFCodec(new UL(new byte[0]), null);

    static MXFCodec mxfCodec(String ul, Codec codec) {
        return new MXFCodec(UL.newUL(ul), codec);
    }

    MXFCodec(UL ul, Codec codec) {
        this.ul = ul;
        this.codec = codec;
    }

    public UL getUl() {
        return this.ul;
    }

    public Codec getCodec() {
        return this.codec;
    }

    public static MXFCodec[] values() {
        return new MXFCodec[] { MPEG2_XDCAM, MPEG2_ML, MPEG2_D10_PAL, MPEG2_HL, MPEG2_HL_422_I, MPEG4_XDCAM_PROXY, DV_25_PAL, JPEG2000, VC1, RAW, RAW_422, VC3_DNXHD, VC3_DNXHD_2, VC3_DNXHD_AVID, AVC_INTRA, AVC_SPSPPS, V210, PRORES_AVID, PRORES, PCM_S16LE_1, PCM_S16LE_3, PCM_S16LE_2, PCM_S16BE, PCM_ALAW, AC3, MP2 };
    }
}