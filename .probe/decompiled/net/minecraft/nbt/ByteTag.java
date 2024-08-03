package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteTag extends NumericTag {

    private static final int SELF_SIZE_IN_BYTES = 9;

    public static final TagType<ByteTag> TYPE = new TagType.StaticSize<ByteTag>() {

        public ByteTag load(DataInput p_128292_, int p_128293_, NbtAccounter p_128294_) throws IOException {
            p_128294_.accountBytes(9L);
            return ByteTag.valueOf(p_128292_.readByte());
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197438_, StreamTagVisitor p_197439_) throws IOException {
            return p_197439_.visit(p_197438_.readByte());
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public String getName() {
            return "BYTE";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Byte";
        }

        @Override
        public boolean isValue() {
            return true;
        }
    };

    public static final ByteTag ZERO = valueOf((byte) 0);

    public static final ByteTag ONE = valueOf((byte) 1);

    private final byte data;

    ByteTag(byte byte0) {
        this.data = byte0;
    }

    public static ByteTag valueOf(byte byte0) {
        return ByteTag.Cache.cache[128 + byte0];
    }

    public static ByteTag valueOf(boolean boolean0) {
        return boolean0 ? ONE : ZERO;
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeByte(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 9;
    }

    @Override
    public byte getId() {
        return 1;
    }

    @Override
    public TagType<ByteTag> getType() {
        return TYPE;
    }

    public ByteTag copy() {
        return this;
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof ByteTag && this.data == ((ByteTag) object0).data;
    }

    public int hashCode() {
        return this.data;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitByte(this);
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
        return (short) this.data;
    }

    @Override
    public byte getAsByte() {
        return this.data;
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

        static final ByteTag[] cache = new ByteTag[256];

        private Cache() {
        }

        static {
            for (int $$0 = 0; $$0 < cache.length; $$0++) {
                cache[$$0] = new ByteTag((byte) ($$0 - 128));
            }
        }
    }
}