package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 12;

    public static final TagType<IntTag> TYPE = new TagType.StaticSize<IntTag>() {

        public IntTag load(DataInput p_128703_, int p_128704_, NbtAccounter p_128705_) throws IOException {
            p_128705_.accountBytes(12L);
            return IntTag.valueOf(p_128703_.readInt());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197483_, StreamTagVisitor p_197484_) throws IOException {
            return p_197484_.visit(p_197483_.readInt());
        }

        @Override
        public int size() {
            return 4;
        }

        @Override
        public String getName() {
            return "INT";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Int";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private final int data;

    IntTag(int int0) {
        this.data = int0;
    }

    public static IntTag valueOf(int int0) {
        return int0 >= -128 && int0 <= 1024 ? IntTag.Cache.cache[int0 - -128] : new IntTag(int0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeInt(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 12;
    }

    @Override
    public byte getId() {
        return 3;
    }

    @Override
    public TagType<IntTag> getType() {
        return TYPE;
    }

    public IntTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof IntTag && this.data == ((IntTag) object0).data;
    }

    public int hashCode() {
        return this.data;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitInt(this);
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
        return (short) (this.data & 65535);
    }

    @Override
    public byte getAsByte() {
        return (byte) (this.data & 0xFF);
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

        static final IntTag[] cache = new IntTag[1153];

        private Cache() {
        }

        static {
            for (int $$0 = 0; $$0 < cache.length; $$0++) {
                cache[$$0] = new IntTag(-128 + $$0);
            }
        }
    }
}