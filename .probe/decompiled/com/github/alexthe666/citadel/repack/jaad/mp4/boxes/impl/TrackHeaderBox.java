package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Utils;
import java.io.IOException;

public class TrackHeaderBox extends FullBox {

    private boolean enabled;

    private boolean inMovie;

    private boolean inPreview;

    private long creationTime;

    private long modificationTime;

    private long duration;

    private int trackID;

    private int layer;

    private int alternateGroup;

    private double volume;

    private double width;

    private double height;

    private double[] matrix = new double[9];

    public TrackHeaderBox() {
        super("Track Header Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.enabled = (this.flags & 1) == 1;
        this.inMovie = (this.flags & 2) == 2;
        this.inPreview = (this.flags & 4) == 4;
        int len = this.version == 1 ? 8 : 4;
        this.creationTime = in.readBytes(len);
        this.modificationTime = in.readBytes(len);
        this.trackID = (int) in.readBytes(4);
        in.skipBytes(4L);
        this.duration = Utils.detectUndetermined(in.readBytes(len));
        in.skipBytes(8L);
        this.layer = (int) in.readBytes(2);
        this.alternateGroup = (int) in.readBytes(2);
        this.volume = in.readFixedPoint(8, 8);
        in.skipBytes(2L);
        for (int i = 0; i < 9; i++) {
            if (i < 6) {
                this.matrix[i] = in.readFixedPoint(16, 16);
            } else {
                this.matrix[i] = in.readFixedPoint(2, 30);
            }
        }
        this.width = in.readFixedPoint(16, 16);
        this.height = in.readFixedPoint(16, 16);
    }

    public boolean isTrackEnabled() {
        return this.enabled;
    }

    public boolean isTrackInMovie() {
        return this.inMovie;
    }

    public boolean isTrackInPreview() {
        return this.inPreview;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getModificationTime() {
        return this.modificationTime;
    }

    public int getTrackID() {
        return this.trackID;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getLayer() {
        return this.layer;
    }

    public int getAlternateGroup() {
        return this.alternateGroup;
    }

    public double getVolume() {
        return this.volume;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double[] getMatrix() {
        return this.matrix;
    }
}