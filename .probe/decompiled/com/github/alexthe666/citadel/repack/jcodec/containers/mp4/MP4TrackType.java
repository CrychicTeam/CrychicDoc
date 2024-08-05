package com.github.alexthe666.citadel.repack.jcodec.containers.mp4;

public final class MP4TrackType {

    public static final MP4TrackType VIDEO = new MP4TrackType("vide");

    public static final MP4TrackType SOUND = new MP4TrackType("soun");

    public static final MP4TrackType TIMECODE = new MP4TrackType("tmcd");

    public static final MP4TrackType HINT = new MP4TrackType("hint");

    public static final MP4TrackType TEXT = new MP4TrackType("text");

    public static final MP4TrackType HYPER_TEXT = new MP4TrackType("wtxt");

    public static final MP4TrackType CC = new MP4TrackType("clcp");

    public static final MP4TrackType SUB = new MP4TrackType("sbtl");

    public static final MP4TrackType MUSIC = new MP4TrackType("musi");

    public static final MP4TrackType MPEG1 = new MP4TrackType("MPEG");

    public static final MP4TrackType SPRITE = new MP4TrackType("sprt");

    public static final MP4TrackType TWEEN = new MP4TrackType("twen");

    public static final MP4TrackType CHAPTERS = new MP4TrackType("chap");

    public static final MP4TrackType THREE_D = new MP4TrackType("qd3d");

    public static final MP4TrackType STREAMING = new MP4TrackType("strm");

    public static final MP4TrackType OBJECTS = new MP4TrackType("obje");

    public static final MP4TrackType DATA = new MP4TrackType("url ");

    private static final MP4TrackType[] _values = new MP4TrackType[] { VIDEO, SOUND, TIMECODE, HINT, TEXT, HYPER_TEXT, CC, SUB, MUSIC, MPEG1, SPRITE, TWEEN, CHAPTERS, THREE_D, STREAMING, OBJECTS, DATA };

    private String handler;

    private MP4TrackType(String handler) {
        this.handler = handler;
    }

    public String getHandler() {
        return this.handler;
    }

    public static MP4TrackType fromHandler(String handler) {
        for (int i = 0; i < _values.length; i++) {
            MP4TrackType val = _values[i];
            if (val.getHandler().equals(handler)) {
                return val;
            }
        }
        return null;
    }
}