package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Format {

    public static final Format MOV = new Format("MOV", true, true);

    public static final Format MPEG_PS = new Format("MPEG_PS", true, true);

    public static final Format MPEG_TS = new Format("MPEG_TS", true, true);

    public static final Format MKV = new Format("MKV", true, true);

    public static final Format H264 = new Format("H264", true, false);

    public static final Format RAW = new Format("RAW", true, true);

    public static final Format FLV = new Format("FLV", true, true);

    public static final Format AVI = new Format("AVI", true, true);

    public static final Format IMG = new Format("IMG", true, false);

    public static final Format IVF = new Format("IVF", true, false);

    public static final Format MJPEG = new Format("MJPEG", true, false);

    public static final Format Y4M = new Format("Y4M", true, false);

    public static final Format WAV = new Format("WAV", false, true);

    public static final Format WEBP = new Format("WEBP", true, false);

    public static final Format MPEG_AUDIO = new Format("MPEG_AUDIO", false, true);

    private static final Map<String, Format> _values = new LinkedHashMap();

    private final boolean video;

    private final boolean audio;

    private final String name;

    private Format(String name, boolean video, boolean audio) {
        this.name = name;
        this.video = video;
        this.audio = audio;
    }

    public boolean isAudio() {
        return this.audio;
    }

    public boolean isVideo() {
        return this.video;
    }

    public static Format valueOf(String s) {
        return (Format) _values.get(s);
    }

    static {
        _values.put("MOV", MOV);
        _values.put("MPEG_PS", MPEG_PS);
        _values.put("MPEG_TS", MPEG_TS);
        _values.put("MKV", MKV);
        _values.put("H264", H264);
        _values.put("RAW", RAW);
        _values.put("FLV", FLV);
        _values.put("AVI", AVI);
        _values.put("IMG", IMG);
        _values.put("IVF", IVF);
        _values.put("MJPEG", MJPEG);
        _values.put("Y4M", Y4M);
        _values.put("WAV", WAV);
        _values.put("WEBP", WEBP);
        _values.put("MPEG_AUDIO", MPEG_AUDIO);
    }
}