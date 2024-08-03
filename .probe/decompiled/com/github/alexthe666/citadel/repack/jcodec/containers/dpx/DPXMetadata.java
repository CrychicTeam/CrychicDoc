package com.github.alexthe666.citadel.repack.jcodec.containers.dpx;

import com.github.alexthe666.citadel.repack.jcodec.common.StringUtils;

public class DPXMetadata {

    public static final String V2 = "V2.0";

    public static final String V1 = "V1.0";

    public FileHeader file;

    public ImageHeader image;

    public ImageSourceHeader imageSource;

    public FilmHeader film;

    public TelevisionHeader television;

    public String userId;

    private static String smpteTC(int tcsmpte, boolean prevent_dropframe) {
        int ff = bcd2uint(tcsmpte & 63);
        int ss = bcd2uint(tcsmpte >> 8 & 127);
        int mm = bcd2uint(tcsmpte >> 16 & 127);
        int hh = bcd2uint(tcsmpte >> 24 & 63);
        boolean drop = (long) (tcsmpte & 1073741824) > 0L && !prevent_dropframe;
        return StringUtils.zeroPad2(hh) + ":" + StringUtils.zeroPad2(mm) + ":" + StringUtils.zeroPad2(ss) + (drop ? ";" : ":") + StringUtils.zeroPad2(ff);
    }

    private static int bcd2uint(int bcd) {
        int low = bcd & 15;
        int high = bcd >> 4;
        return low <= 9 && high <= 9 ? low + 10 * high : 0;
    }

    public String getTimecodeString() {
        return smpteTC(this.television.timecode, false);
    }
}