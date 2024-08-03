package com.github.alexthe666.citadel.repack.jcodec.common;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Codec {

    public static final Codec H264 = new Codec("H264", TrackType.VIDEO);

    public static final Codec MPEG2 = new Codec("MPEG2", TrackType.VIDEO);

    public static final Codec MPEG4 = new Codec("MPEG4", TrackType.VIDEO);

    public static final Codec PRORES = new Codec("PRORES", TrackType.VIDEO);

    public static final Codec DV = new Codec("DV", TrackType.VIDEO);

    public static final Codec VC1 = new Codec("VC1", TrackType.VIDEO);

    public static final Codec VC3 = new Codec("VC3", TrackType.VIDEO);

    public static final Codec V210 = new Codec("V210", TrackType.VIDEO);

    public static final Codec SORENSON = new Codec("SORENSON", TrackType.VIDEO);

    public static final Codec FLASH_SCREEN_VIDEO = new Codec("FLASH_SCREEN_VIDEO", TrackType.VIDEO);

    public static final Codec FLASH_SCREEN_V2 = new Codec("FLASH_SCREEN_V2", TrackType.VIDEO);

    public static final Codec PNG = new Codec("PNG", TrackType.VIDEO);

    public static final Codec JPEG = new Codec("JPEG", TrackType.VIDEO);

    public static final Codec J2K = new Codec("J2K", TrackType.VIDEO);

    public static final Codec VP6 = new Codec("VP6", TrackType.VIDEO);

    public static final Codec VP8 = new Codec("VP8", TrackType.VIDEO);

    public static final Codec VP9 = new Codec("VP9", TrackType.VIDEO);

    public static final Codec VORBIS = new Codec("VORBIS", TrackType.VIDEO);

    public static final Codec AAC = new Codec("AAC", TrackType.AUDIO);

    public static final Codec MP3 = new Codec("MP3", TrackType.AUDIO);

    public static final Codec MP2 = new Codec("MP2", TrackType.AUDIO);

    public static final Codec MP1 = new Codec("MP1", TrackType.AUDIO);

    public static final Codec AC3 = new Codec("AC3", TrackType.AUDIO);

    public static final Codec DTS = new Codec("DTS", TrackType.AUDIO);

    public static final Codec TRUEHD = new Codec("TRUEHD", TrackType.AUDIO);

    public static final Codec PCM_DVD = new Codec("PCM_DVD", TrackType.AUDIO);

    public static final Codec PCM = new Codec("PCM", TrackType.AUDIO);

    public static final Codec ADPCM = new Codec("ADPCM", TrackType.AUDIO);

    public static final Codec ALAW = new Codec("ALAW", TrackType.AUDIO);

    public static final Codec NELLYMOSER = new Codec("NELLYMOSER", TrackType.AUDIO);

    public static final Codec G711 = new Codec("G711", TrackType.AUDIO);

    public static final Codec SPEEX = new Codec("SPEEX", TrackType.AUDIO);

    public static final Codec RAW = new Codec("RAW", null);

    public static final Codec TIMECODE = new Codec("TIMECODE", TrackType.OTHER);

    private static final Map<String, Codec> _values = new LinkedHashMap();

    private final String _name;

    private final TrackType type;

    public Codec(String name, TrackType type) {
        this._name = name;
        this.type = type;
    }

    public TrackType getType() {
        return this.type;
    }

    public static Codec codecByFourcc(String fourcc) {
        if (fourcc.equals("avc1")) {
            return H264;
        } else if (fourcc.equals("m1v1") || fourcc.equals("m2v1")) {
            return MPEG2;
        } else if (fourcc.equals("apco") || fourcc.equals("apcs") || fourcc.equals("apcn") || fourcc.equals("apch") || fourcc.equals("ap4h")) {
            return PRORES;
        } else if (fourcc.equals("mp4a")) {
            return AAC;
        } else {
            return fourcc.equals("jpeg") ? JPEG : null;
        }
    }

    public static Codec valueOf(String s) {
        return (Codec) _values.get(s);
    }

    public String toString() {
        return this._name;
    }

    public String name() {
        return this._name;
    }

    static {
        _values.put("H264", H264);
        _values.put("MPEG2", MPEG2);
        _values.put("MPEG4", MPEG4);
        _values.put("PRORES", PRORES);
        _values.put("DV", DV);
        _values.put("VC1", VC1);
        _values.put("VC3", VC3);
        _values.put("V210", V210);
        _values.put("SORENSON", SORENSON);
        _values.put("FLASH_SCREEN_VIDEO", FLASH_SCREEN_VIDEO);
        _values.put("FLASH_SCREEN_V2", FLASH_SCREEN_V2);
        _values.put("PNG", PNG);
        _values.put("JPEG", JPEG);
        _values.put("J2K", J2K);
        _values.put("VP6", VP6);
        _values.put("VP8", VP8);
        _values.put("VP9", VP9);
        _values.put("VORBIS", VORBIS);
        _values.put("AAC", AAC);
        _values.put("MP3", MP3);
        _values.put("MP2", MP2);
        _values.put("MP1", MP1);
        _values.put("AC3", AC3);
        _values.put("DTS", DTS);
        _values.put("TRUEHD", TRUEHD);
        _values.put("PCM_DVD", PCM_DVD);
        _values.put("PCM", PCM);
        _values.put("ADPCM", ADPCM);
        _values.put("ALAW", ALAW);
        _values.put("NELLYMOSER", NELLYMOSER);
        _values.put("G711", G711);
        _values.put("SPEEX", SPEEX);
        _values.put("RAW", RAW);
        _values.put("TIMECODE", TIMECODE);
    }
}