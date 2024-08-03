package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

public class TrackFragmentBox extends NodeBox {

    public TrackFragmentBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "traf";
    }

    public int getTrackId() {
        TrackFragmentHeaderBox tfhd = NodeBox.findFirst(this, TrackFragmentHeaderBox.class, TrackFragmentHeaderBox.fourcc());
        if (tfhd == null) {
            throw new RuntimeException("Corrupt track fragment, no header atom found");
        } else {
            return tfhd.getTrackId();
        }
    }

    public static TrackFragmentBox createTrackFragmentBox() {
        return new TrackFragmentBox(new Header(fourcc()));
    }
}