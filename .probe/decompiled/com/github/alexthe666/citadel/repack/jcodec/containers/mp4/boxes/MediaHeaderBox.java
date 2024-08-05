package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.TimeUtil;
import java.nio.ByteBuffer;

public class MediaHeaderBox extends FullBox {

    private long created;

    private long modified;

    private int timescale;

    private long duration;

    private int language;

    private int quality;

    public MediaHeaderBox(Header atom) {
        super(atom);
    }

    public static String fourcc() {
        return "mdhd";
    }

    public static MediaHeaderBox createMediaHeaderBox(int timescale, long duration, int language, long created, long modified, int quality) {
        MediaHeaderBox mdhd = new MediaHeaderBox(new Header(fourcc()));
        mdhd.timescale = timescale;
        mdhd.duration = duration;
        mdhd.language = language;
        mdhd.created = created;
        mdhd.modified = modified;
        mdhd.quality = quality;
        return mdhd;
    }

    public int getTimescale() {
        return this.timescale;
    }

    public long getDuration() {
        return this.duration;
    }

    public long getCreated() {
        return this.created;
    }

    public long getModified() {
        return this.modified;
    }

    public int getLanguage() {
        return this.language;
    }

    public int getQuality() {
        return this.quality;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setTimescale(int timescale) {
        this.timescale = timescale;
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
    }

    @Override
    public void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt(TimeUtil.toMovTime(this.created));
        out.putInt(TimeUtil.toMovTime(this.modified));
        out.putInt(this.timescale);
        out.putInt((int) this.duration);
        out.putShort((short) this.language);
        out.putShort((short) this.quality);
    }

    @Override
    public int estimateSize() {
        return 32;
    }
}