package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 16;

    public static final TagType<LongTag> TYPE = new TagType.StaticSize<LongTag>() {

        public LongTag load(DataInput p_128906_, int p_128907_, NbtAccounter p_128908_) throws IOException {
            p_128908_.accountBytes(16L);
            return LongTag.valueOf(p_128906_.readLong());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197506_, StreamTagVisitor p_197507_) throws IOException {
            return p_197507_.visit(p_197506_.readLong());
        }

        @Override
        public int size() {
            return 8;
        }

        @Override
        public String getName() {
            return "LONG";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Long";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    private final long data;

    LongTag(long long0) {
        this.data = long0;
    }

    public static LongTag valueOf(long long0) {
        return long0 >= -128L && long0 <= 1024L ? LongTag.Cache.cache[(int) long0 - -128] : new LongTag(long0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeLong(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 16;
    }

    @Override
    public byte getId() {
        return 4;
    }

    @Override
    public TagType<LongTag> getType() {
        return TYPE;
    }

    public LongTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof LongTag && this.data == ((LongTag) object0).data;
    }

    public int hashCode() {
        return (int) (this.data ^ this.data >>> 32);
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitLong(this);
    }

    @Override
    public long getAsLong() {
        return this.data;
    }

    @Override
    public int getAsInt() {
        return (int) (this.data & -1L);
    }

    @Override
    public short getAsShort() {
        return (short) ((int) (this.data & 65535L));
    }

    @Override
    public byte getAsByte() {
        return (byte) ((int) (this.data & 255L));
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

        static final LongTag[] cache = new LongTag[1153];

        private Cache() {
        }

        static {
            for (int $$0 = 0; $$0 < cache.length; $$0++) {
                cache[$$0] = new LongTag((long) (-128 + $$0));
            }
        }
    }
}