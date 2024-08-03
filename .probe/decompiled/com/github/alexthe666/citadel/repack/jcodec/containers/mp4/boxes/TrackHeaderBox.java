package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.TimeUtil;
import java.nio.ByteBuffer;

public class TrackHeaderBox extends FullBox {

    private int trackId;

    private long duration;

    private float width;

    private float height;

    private long created;

    private long modified;

    private float volume;

    private short layer;

    private long altGroup;

    private int[] matrix;

    public static String fourcc() {
        return "tkhd";
    }

    public static TrackHeaderBox createTrackHeaderBox(int trackId, long duration, float width, float height, long created, long modified, float volume, short layer, long altGroup, int[] matrix) {
        TrackHeaderBox box = new TrackHeaderBox(new Header(fourcc()));
        box.trackId = trackId;
        box.duration = duration;
        box.width = width;
        box.height = height;
        box.created = created;
        box.modified = modified;
        box.volume = volume;
        box.layer = layer;
        box.altGroup = altGroup;
        box.matrix = matrix;
        return box;
    }

    public TrackHeaderBox(Header header) {
        super(header);
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if (this.version == 0) {
            this.created = TimeUtil.fromMovTime(input.getInt());
            this.modified = TimeUtil.fromMovTime(input.getInt());
        } else {
            this.created = TimeUtil.fromMovTime((int) input.getLong());
            this.modified = TimeUtil.fromMovTime((int) input.getLong());
        }
        this.trackId = input.getInt();
        input.getInt();
        if (this.version == 0) {
            this.duration = (long) input.getInt();
        } else {
            this.duration = input.getLong();
        }
        input.getInt();
        input.getInt();
        this.layer = input.getShort();
        this.altGroup = (long) input.getShort();
        this.volume = this.readVolume(input);
        input.getShort();
        this.readMatrix(input);
        this.width = (float) input.getInt() / 65536.0F;
        this.height = (float) input.getInt() / 65536.0F;
    }

    private void readMatrix(ByteBuffer input) {
        this.matrix = new int[9];
        for (int i = 0; i < 9; i++) {
            this.matrix[i] = input.getInt();
        }
    }

    private float readVolume(ByteBuffer input) {
        return (float) ((double) input.getShort() / 256.0);
    }

    public int getNo() {
        return this.trackId;
    }

    public long getDuration() {
        return this.duration;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(TimeUtil.toMovTime(this.created));
        out.putInt(TimeUtil.toMovTime(this.modified));
        out.putInt(this.trackId);
        out.putInt(0);
        out.putInt((int) this.duration);
        out.putInt(0);
        out.putInt(0);
        out.putShort(this.layer);
        out.putShort((short) ((int) this.altGroup));
        this.writeVolume(out);
        out.putShort((short) 0);
        this.writeMatrix(out);
        out.putInt((int) (this.width * 65536.0F));
        out.putInt((int) (this.height * 65536.0F));
    }

    @Override
    public int estimateSize() {
        return 92;
    }

    private void writeMatrix(ByteBuffer out) {
        for (int i = 0; i < Math.min(9, this.matrix.length); i++) {
            out.putInt(this.matrix[i]);
        }
        for (int i = Math.min(9, this.matrix.length); i < 9; i++) {
            out.putInt(0);
        }
    }

    private void writeVolume(ByteBuffer out) {
        out.putShort((short) ((int) ((double) this.volume * 256.0)));
    }

    public int getTrackId() {
        return this.trackId;
    }

    public long getCreated() {
        return this.created;
    }

    public long getModified() {
        return this.modified;
    }

    public float getVolume() {
        return this.volume;
    }

    public short getLayer() {
        return this.layer;
    }

    public long getAltGroup() {
        return this.altGroup;
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setNo(int no) {
        this.trackId = no;
    }

    public boolean isOrientation0() {
        return this.matrix != null && this.matrix[0] == 65536 && this.matrix[4] == 65536;
    }

    public boolean isOrientation90() {
        return this.matrix != null && this.matrix[1] == 65536 && this.matrix[3] == -65536;
    }

    public boolean isOrientation180() {
        return this.matrix != null && this.matrix[0] == -65536 && this.matrix[4] == -65536;
    }

    public boolean isOrientation270() {
        return this.matrix != null && this.matrix[1] == -65536 && this.matrix[3] == 65536;
    }
}