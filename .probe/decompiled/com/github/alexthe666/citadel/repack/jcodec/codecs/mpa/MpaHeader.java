package com.github.alexthe666.citadel.repack.jcodec.codecs.mpa;

import java.nio.ByteBuffer;

class MpaHeader {

    int layer;

    int protectionBit;

    int bitrateIndex;

    int paddingBit;

    int modeExtension;

    int version;

    int mode;

    int sampleFreq;

    int numSubbands;

    int intensityStereoBound;

    boolean copyright;

    boolean original;

    int framesize;

    int frameBytes;

    static MpaHeader read_header(ByteBuffer bb) {
        MpaHeader ret = new MpaHeader();
        int headerstring = bb.getInt();
        ret.version = headerstring >>> 19 & 1;
        if ((headerstring >>> 20 & 1) == 0) {
            if (ret.version != 0) {
                throw new RuntimeException("UNKNOWN_ERROR");
            }
            ret.version = 2;
        }
        if ((ret.sampleFreq = headerstring >>> 10 & 3) == 3) {
            throw new RuntimeException("UNKNOWN_ERROR");
        } else {
            ret.layer = 4 - (headerstring >>> 17) & 3;
            ret.protectionBit = headerstring >>> 16 & 1;
            ret.bitrateIndex = headerstring >>> 12 & 15;
            ret.paddingBit = headerstring >>> 9 & 1;
            ret.mode = headerstring >>> 6 & 3;
            ret.modeExtension = headerstring >>> 4 & 3;
            if (ret.mode == 1) {
                ret.intensityStereoBound = (ret.modeExtension << 2) + 4;
            } else {
                ret.intensityStereoBound = 0;
            }
            if ((headerstring >>> 3 & 1) == 1) {
                ret.copyright = true;
            }
            if ((headerstring >>> 2 & 1) == 1) {
                ret.original = true;
            }
            if (ret.layer == 1) {
                ret.numSubbands = 32;
            } else {
                int channel_bitrate = ret.bitrateIndex;
                if (ret.mode != 3) {
                    if (channel_bitrate == 4) {
                        channel_bitrate = 1;
                    } else {
                        channel_bitrate -= 4;
                    }
                }
                if (channel_bitrate != 1 && channel_bitrate != 2) {
                    if (ret.sampleFreq != 1 && (channel_bitrate < 3 || channel_bitrate > 5)) {
                        ret.numSubbands = 30;
                    } else {
                        ret.numSubbands = 27;
                    }
                } else if (ret.sampleFreq == 2) {
                    ret.numSubbands = 12;
                } else {
                    ret.numSubbands = 8;
                }
            }
            if (ret.intensityStereoBound > ret.numSubbands) {
                ret.intensityStereoBound = ret.numSubbands;
            }
            calculateFramesize(ret);
            return ret;
        }
    }

    public static void calculateFramesize(MpaHeader ret) {
        if (ret.layer == 1) {
            ret.framesize = 12 * MpaConst.bitrates[ret.version][0][ret.bitrateIndex] / MpaConst.frequencies[ret.version][ret.sampleFreq];
            if (ret.paddingBit != 0) {
                ret.framesize++;
            }
            ret.framesize <<= 2;
            ret.frameBytes = 0;
        } else {
            ret.framesize = 144 * MpaConst.bitrates[ret.version][ret.layer - 1][ret.bitrateIndex] / MpaConst.frequencies[ret.version][ret.sampleFreq];
            if (ret.version == 0 || ret.version == 2) {
                ret.framesize >>= 1;
            }
            if (ret.paddingBit != 0) {
                ret.framesize++;
            }
            if (ret.layer == 3) {
                if (ret.version == 1) {
                    ret.frameBytes = ret.framesize - (ret.mode == 3 ? 17 : 32) - (ret.protectionBit != 0 ? 0 : 2) - 4;
                } else {
                    ret.frameBytes = ret.framesize - (ret.mode == 3 ? 9 : 17) - (ret.protectionBit != 0 ? 0 : 2) - 4;
                }
            } else {
                ret.frameBytes = 0;
            }
        }
        ret.framesize -= 4;
    }
}