package com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.nio.ByteBuffer;

public class SegmentIndexBox extends FullBox {

    public long reference_ID;

    public long timescale;

    public long earliest_presentation_time;

    public long first_offset;

    public int reserved;

    public int reference_count;

    public SegmentIndexBox.Reference[] references;

    public SegmentIndexBox(Header atom) {
        super(atom);
    }

    public static SegmentIndexBox createSegmentIndexBox() {
        return new SegmentIndexBox(new Header(fourcc()));
    }

    public static String fourcc() {
        return "sidx";
    }

    @Override
    public void parse(ByteBuffer input) {
        super.parse(input);
        this.reference_ID = Platform.unsignedInt(input.getInt());
        this.timescale = Platform.unsignedInt(input.getInt());
        if (this.version == 0) {
            this.earliest_presentation_time = Platform.unsignedInt(input.getInt());
            this.first_offset = Platform.unsignedInt(input.getInt());
        } else {
            this.earliest_presentation_time = input.getLong();
            this.first_offset = input.getLong();
        }
        this.reserved = input.getShort();
        this.reference_count = input.getShort() & '\uffff';
        this.references = new SegmentIndexBox.Reference[this.reference_count];
        for (int i = 0; i < this.reference_count; i++) {
            long i0 = Platform.unsignedInt(input.getInt());
            long i1 = Platform.unsignedInt(input.getInt());
            long i2 = Platform.unsignedInt(input.getInt());
            SegmentIndexBox.Reference ref = new SegmentIndexBox.Reference();
            ref.reference_type = (i0 >>> 31 & 1L) == 1L;
            ref.referenced_size = i0 & 2147483647L;
            ref.subsegment_duration = i1;
            ref.starts_with_SAP = (i2 >>> 31 & 1L) == 1L;
            ref.SAP_type = (int) (i2 >>> 28 & 7L);
            ref.SAP_delta_time = i2 & 268435455L;
            this.references[i] = ref;
        }
    }

    @Override
    protected void doWrite(ByteBuffer out) {
        super.doWrite(out);
        out.putInt((int) this.reference_ID);
        out.putInt((int) this.timescale);
        if (this.version == 0) {
            out.putInt((int) this.earliest_presentation_time);
            out.putInt((int) this.first_offset);
        } else {
            out.putLong(this.earliest_presentation_time);
            out.putLong(this.first_offset);
        }
        out.putShort((short) this.reserved);
        out.putShort((short) this.reference_count);
        for (int i = 0; i < this.reference_count; i++) {
            SegmentIndexBox.Reference ref = this.references[i];
            int i0 = (int) ((long) ((ref.reference_type ? 1 : 0) << 31) | ref.referenced_size);
            int i1 = (int) ref.subsegment_duration;
            int i2 = 0;
            if (ref.starts_with_SAP) {
                i2 |= Integer.MIN_VALUE;
            }
            i2 |= (ref.SAP_type & 7) << 28;
            i2 = (int) ((long) i2 | ref.SAP_delta_time & 268435455L);
            out.putInt(i0);
            out.putInt(i1);
            out.putInt(i2);
        }
    }

    @Override
    public int estimateSize() {
        return 40 + this.reference_count * 12;
    }

    @Override
    public String toString() {
        return "SegmentIndexBox [reference_ID=" + this.reference_ID + ", timescale=" + this.timescale + ", earliest_presentation_time=" + this.earliest_presentation_time + ", first_offset=" + this.first_offset + ", reserved=" + this.reserved + ", reference_count=" + this.reference_count + ", references=" + Platform.arrayToString(this.references) + ", version=" + this.version + ", flags=" + this.flags + ", header=" + this.header + "]";
    }

    public static class Reference {

        public boolean reference_type;

        public long referenced_size;

        public long subsegment_duration;

        public boolean starts_with_SAP;

        public int SAP_type;

        public long SAP_delta_time;

        public String toString() {
            return "Reference [reference_type=" + this.reference_type + ", referenced_size=" + this.referenced_size + ", subsegment_duration=" + this.subsegment_duration + ", starts_with_SAP=" + this.starts_with_SAP + ", SAP_type=" + this.SAP_type + ", SAP_delta_time=" + this.SAP_delta_time + "]";
        }
    }
}