package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ShortTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 10;

    public static final TagType<ShortTag> TYPE = new TagType.StaticSize<ShortTag>() {

        public ShortTag load(DataInput p_129277_, int p_129278_, NbtAccounter p_129279_) throws IOException {
            p_129279_.accountBytes(10L);
            return ShortTag.valueOf(p_129277_.readShort());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197517_, StreamTagVisitor p_197518_) throws IOException {
            return p_197518_.visit(p_197517_.readShort());
        }

        @Override
        public int size() {
            return 2;
        }

        @Override
        public String getName() {
            return "SHORT";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Short";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private final short data;

    ShortTag(short short0) {
        this.data = short0;
    }

    public static ShortTag valueOf(short short0) {
        return short0 >= -128 && short0 <= 1024 ? ShortTag.Cache.cache[short0 - -128] : new ShortTag(short0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeShort(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 10;
    }

    @Override
    public byte getId() {
        return 2;
    }

    @Override
    public TagType<ShortTag> getType() {
        return TYPE;
    }

    public ShortTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof ShortTag && this.data == ((ShortTag) object0).data;
    }

    public int hashCode() {
        return this.data;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitShort(this);
    }

    @Override
    public long getAsLong() {
        return (long) this.data;
    }

    @Override
    public int getAsInt() {
        return this.data;
    }

    @Override
    public short getAsShort() {
        return this.data;
    }

    @Override
    public byte getAsByte() {
        return (byte) (this.data & 255);
    }

    @Override
    public double getAsDouble() {
        return (double) this.data;
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

    static class Cache {

        private static final int HIGH = 1024;

        private static final int LOW = -128;

        static final ShortTag[] cache = new ShortTag[1153];

        private Cache() {
        }

        static {
            for (int $$0 = 0; $$0 < cache.length; $$0++) {
                cache[$$0] = new ShortTag((short) (-128 + $$0));
            }
        }
    }
}