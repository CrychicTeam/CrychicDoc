package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import java.nio.ByteBuffer;

public class TrackFragmentBaseMediaDecodeTimeBox extends FullBox {

    private long baseMediaDecodeTime;

    public TrackFragmentBaseMediaDecodeTimeBox(Header atom) {
        super(atom);
    }

    public static TrackFragmentBaseMediaDecodeTimeBox createTrackFragmentBaseMediaDecodeTimeBox(long baseMediaDecodeTime) {
        TrackFragmentBaseMediaDecodeTimeBox box = new TrackFragmentBaseMediaDecodeTimeBox(new Header(fourcc()));
        box.baseMediaDecodeTime = baseMediaDecodeTime;
        if (box.baseMediaDecodeTime > 2147483647L) {
            box.version = 1;
        }
        return box;
    }

    public static String fourcc() {
        return "tfdt";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        if (this.version == 0) {
            this.baseMediaDecodeTime = (long) input.getInt();
        } else {
            if (this.version != 1) {
                throw new RuntimeException("Unsupported tfdt version");
            }
            this.baseMediaDecodeTime = input.getLong();
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        if (this.version == 0) {
            out.putInt((int) this.baseMediaDecodeTime);
        } else {
            if (this.version != 1) {
                throw new RuntimeException("Unsupported tfdt version");
            }
            out.putLong(this.baseMediaDecodeTime);
        }
    }

    @Override
    public int estimateSize() {
        return 20;
    }

    public long getBaseMediaDecodeTime() {
        return this.baseMediaDecodeTime;
    }

    public void setBaseMediaDecodeTime(long baseMediaDecodeTime) {
        this.baseMediaDecodeTime = baseMediaDecodeTime;
    }

    public static TrackFragmentBaseMediaDecodeTimeBox.Factory copy(TrackFragmentBaseMediaDecodeTimeBox other) {
        return new TrackFragmentBaseMediaDecodeTimeBox.Factory(other);
    }

    public static class Factory {

        private TrackFragmentBaseMediaDecodeTimeBox box;

        protected Factory(TrackFragmentBaseMediaDecodeTimeBox other) {
            this.box = TrackFragmentBaseMediaDecodeTimeBox.createTrackFragmentBaseMediaDecodeTimeBox(other.baseMediaDecodeTime);
            this.box.version = other.version;
            this.box.flags = other.flags;
        }

        public TrackFragmentBaseMediaDecodeTimeBox.Factory baseMediaDecodeTime(long val) {
            this.box.baseMediaDecodeTime = val;
            return this;
        }

        public TrackFragmentBaseMediaDecodeTimeBox create() {
            TrackFragmentBaseMediaDecodeTimeBox var1;
            try {
                var1 = this.box;
            } finally {
                this.box = null;
            }
            return var1;
        }
    }
}