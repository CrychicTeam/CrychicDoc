package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.TimeUtil;
import java.nio.ByteBuffer;

public class MovieHeaderBox extends FullBox {

    private int timescale;

    private long duration;

    private float rate;

    private float volume;

    private long created;

    private long modified;

    private int[] matrix;

    private int nextTrackId;

    public static String fourcc() {
        return "mvhd";
    }

    public static MovieHeaderBox createMovieHeaderBox(int timescale, long duration, float rate, float volume, long created, long modified, int[] matrix, int nextTrackId) {
        MovieHeaderBox mvhd = new MovieHeaderBox(new Header(fourcc()));
        mvhd.timescale = timescale;
        mvhd.duration = duration;
        mvhd.rate = rate;
        mvhd.volume = volume;
        mvhd.created = created;
        mvhd.modified = modified;
        mvhd.matrix = matrix;
        mvhd.nextTrackId = nextTrackId;
        return mvhd;
    }

    public MovieHeaderBox(Header header) {
        super(header);
    }

    public int getTimescale() {
        return this.timescale;
    }

    public long getDuration() {
        return this.duration;
    }

    public int getNextTrackId() {
        return this.nextTrackId;
    }

    public float getRate() {
        return this.rate;
    }

    public float getVolume() {
        return this.volume;
    }

    public long getCreated() {
        return this.created;
    }

    public long getModified() {
        return this.modified;
    }

    public int[] getMatrix() {
        return this.matrix;
    }

    public void setTimescale(int newTs) {
        this.timescale = newTs;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setNextTrackId(int nextTrackId) {
        this.nextTrackId = nextTrackId;
    }

    private int[] readMatrix(ByteBuffer input) {
        int[] matrix = new int[9];
        for (int i = 0; i < 9; i++) {
            matrix[i] = input.getInt();
        }
        return matrix;
    }

    private float readVolume(ByteBuffer input) {
        return (float) input.getShort() / 256.0F;
    }

    private float readRate(ByteBuffer input) {
        return (float) input.getInt() / 65536.0F;
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if (this.version == 0) {
            this.created = TimeUtil.fromMovTime(input.getInt());
            this.modified = TimeUtil.fromMovTime(input.getInt());
            this.timescale = input.getInt();
            this.duration = (long) input.getInt();
        } else {
            if (this.version != 1) {
                throw new RuntimeException("Unsupported version");
            }
            this.created = TimeUtil.fromMovTime((int) input.getLong());
            this.modified = TimeUtil.fromMovTime((int) input.getLong());
            this.timescale = input.getInt();
            this.duration = input.getLong();
        }
        this.rate = this.readRate(input);
        this.volume = this.readVolume(input);
        NIOUtils.skip(input, 10);
        this.matrix = this.readMatrix(input);
        NIOUtils.skip(input, 24);
        this.nextTrackId = input.getInt();
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(TimeUtil.toMovTime(this.created));
        out.putInt(TimeUtil.toMovTime(this.modified));
        out.putInt(this.timescale);
        out.putInt((int) this.duration);
        this.writeFixed1616(out, this.rate);
        this.writeFixed88(out, this.volume);
        out.put(new byte[10]);
        this.writeMatrix(out);
        out.put(new byte[24]);
        out.putInt(this.nextTrackId);
    }

    @Override
    public int estimateSize() {
        return 144;
    }

    private void writeMatrix(ByteBuffer out) {
        for (int i = 0; i < Math.min(9, this.matrix.length); i++) {
            out.putInt(this.matrix[i]);
        }
        for (int i = Math.min(9, this.matrix.length); i < 9; i++) {
            out.putInt(0);
        }
    }

    private void writeFixed88(ByteBuffer out, float volume) {
        out.putShort((short) ((int) ((double) volume * 256.0)));
    }

    private void writeFixed1616(ByteBuffer out, float rate) {
        out.putInt((int) ((double) rate * 65536.0));
    }
}