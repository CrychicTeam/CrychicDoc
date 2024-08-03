package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.util.Mth;

public class DoubleTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 16;

    public static final DoubleTag ZERO = new DoubleTag(0.0);

    public static final TagType<DoubleTag> TYPE = new TagType.StaticSize<DoubleTag>() {

        public DoubleTag load(DataInput p_128524_, int p_128525_, NbtAccounter p_128526_) throws IOException {
            p_128526_.accountBytes(16L);
            return DoubleTag.valueOf(p_128524_.readDouble());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197454_, StreamTagVisitor p_197455_) throws IOException {
            return p_197455_.visit(p_197454_.readDouble());
        }

        @Override
        public int size() {
            return 8;
        }

        @Override
        public String getName() {
            return "DOUBLE";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Double";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private final double data;

    private DoubleTag(double double0) {
        this.data = double0;
    }

    public static DoubleTag valueOf(double double0) {
        return double0 == 0.0 ? ZERO : new DoubleTag(double0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeDouble(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 16;
    }

    @Override
    public byte getId() {
        return 6;
    }

    @Override
    public TagType<DoubleTag> getType() {
        return TYPE;
    }

    public DoubleTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof DoubleTag && this.data == ((DoubleTag) object0).data;
    }

    public int hashCode() {
        long $$0 = Double.doubleToLongBits(this.data);
        return (int) ($$0 ^ $$0 >>> 32);
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitDouble(this);
    }

    @Override
    public long getAsLong() {
        return (long) Math.floor(this.data);
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
        return this.data;
    }

    @Override
    public float getAsFloat() {
        return (float) this.data;
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