package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.Mth;

public class FloatTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 12;

    public static final FloatTag ZERO = new FloatTag(0.0F);

    public static final TagType<FloatTag> TYPE = new TagType.StaticSize<FloatTag>() {

        public FloatTag load(DataInput p_128590_, int p_128591_, NbtAccounter p_128592_) throws IOException {
            p_128592_.accountBytes(12L);
            return FloatTag.valueOf(p_128590_.readFloat());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197470_, StreamTagVisitor p_197471_) throws IOException {
            return p_197471_.visit(p_197470_.readFloat());
        }

        @Override
        public int size() {
            return 4;
        }

        @Override
        public String getName() {
            return "FLOAT";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Float";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private final float data;

    private FloatTag(float float0) {
        this.data = float0;
    }

    public static FloatTag valueOf(float float0) {
        return float0 == 0.0F ? ZERO : new FloatTag(float0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeFloat(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 12;
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public TagType<FloatTag> getType() {
        return TYPE;
    }

    public FloatTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof FloatTag && this.data == ((FloatTag) object0).data;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.data);
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitFloat(this);
    }

    @Override
    public long getAsLong() {
        return (long) this.data;
    }

    @Override
    public int getAsInt() {
        return Mth.floor(this.data);
    }

    @Override
    public short getAsShort() {
        return (short) (Mth.floor(this.data) & 65535);
    }

    @Override
    public byte getAsByte() {
        return (byte) (Mth.floor(this.data) & 0xFF);
    }

    @Override
    public double getAsDouble() {
        return (double) this.data;
    }

    @Override
    public float getAsFloat() {
        return this.data;
    }

    @Override
    public Number getAsNumber() {
        return this.data;
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visit(this.data);
    }
}