package com.github.alexthe666.citadel.repack.jcodec.api;

import com.github.alexthe666.citadel.repack.jcodec.common.DemuxerTrackMeta;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;

public class PictureWithMetadata {

    private Picture picture;

    private double timestamp;

    private double duration;

    private DemuxerTrackMeta.Orientation orientation;

    public static PictureWithMetadata createPictureWithMetadata(Picture picture, double timestamp, double duration) {
        return new PictureWithMetadata(picture, timestamp, duration, DemuxerTrackMeta.Orientation.D_0);
    }

    public PictureWithMetadata(Picture picture, double timestamp, double duration, DemuxerTrackMeta.Orientation orientation) {
        this.picture = picture;
        this.timestamp = timestamp;
        this.duration = duration;
        this.orientation = orientation;
    }

    public Picture getPicture() {
        return this.picture;
    }

    public double getTimestamp() {
        return this.timestamp;
    }

    public double getDuration() {
        return this.duration;
    }

    public DemuxerTrackMeta.Orientation getOrientation() {
        return this.orientation;
    }
}