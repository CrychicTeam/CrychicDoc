package net.minecraft.nbt;

import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class LongArrayTag extends CollectionTag<LongTag> {

    private static final int SELF_SIZE_IN_BYTES = 24;

    public static final TagType<LongArrayTag> TYPE = new TagType.VariableSize<LongArrayTag>() {

        public LongArrayTag load(DataInput p_128865_, int p_128866_, NbtAccounter p_128867_) throws IOException {
            p_128867_.accountBytes(24L);
            int $$3 = p_128865_.readInt();
            p_128867_.accountBytes(8L * (long) $$3);
            long[] $$4 = new long[$$3];
            for (int $$5 = 0; $$5 < $$3; $$5++) {
                $$4[$$5] = p_128865_.readLong();
            }
            return new LongArrayTag($$4);
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197501_, StreamTagVisitor p_197502_) throws IOException {
            int $$2 = p_197501_.readInt();
            long[] $$3 = new long[$$2];
            for (int $$4 = 0; $$4 < $$2; $$4++) {
                $$3[$$4] = p_197501_.readLong();
            }
            return p_197502_.visit($$3);
        }

        @Override
        public void skip(DataInput p_197499_) throws IOException {
            p_197499_.skipBytes(p_197499_.readInt() * 8);
        }

        @Override
        public String getName() {
            return "LONG[]";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Long_Array";
        }
    };

    private long[] data;

    public LongArrayTag(long[] long0) {
        this.data = long0;
    }

    public LongArrayTag(LongSet longSet0) {
        this.data = longSet0.toLongArray();
    }

    public LongArrayTag(List<Long> listLong0) {
        this(toArray(listLong0));
    }

    private static long[] toArray(List<Long> listLong0) {
        long[] $$1 = new long[listLong0.size()];
        for (int $$2 = 0; $$2 < listLong0.size(); $$2++) {
            Long $$3 = (Long) listLong0.get($$2);
            $$1[$$2] = $$3 == null ? 0L : $$3;
        }
        return $$1;
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeInt(this.data.length);
        for (long $$1 : this.data) {
            dataOutput0.writeLong($$1);
        }
    }

    @Override
    public int sizeInBytes() {
        return 24 + 8 * this.data.length;
    }

    @Override
    public byte getId() {
        return 12;
    }

    @Override
    public TagType<LongArrayTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    public LongArrayTag copy() {
        long[] $$0 = new long[this.data.length];
        System.arraycopy(this.data, 0, $$0, 0, this.data.length);
        return new LongArrayTag($$0);
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof LongArrayTag && Arrays.equals(this.data, ((LongArrayTag) object0).data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitLongArray(this);
    }

    public long[] getAsLongArray() {
        return this.data;
    }

    public int size() {
        return this.data.length;
    }

    public LongTag get(int int0) {
        return LongTag.valueOf(this.data[int0]);
    }

    public LongTag set(int int0, LongTag longTag1) {
        long $$2 = this.data[int0];
        this.data[int0] = longTag1.getAsLong();
        return LongTag.valueOf($$2);
    }

    public void add(int int0, LongTag longTag1) {
        this.data = ArrayUtils.add(this.data, int0, longTag1.getAsLong());
    }

    @Override
    public boolean setTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data[int0] = ((NumericTag) tag1).getAsLong();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data = ArrayUtils.add(this.data, int0, ((NumericTag) tag1).getAsLong());
            return true;
        } else {
            return false;
        }
    }

    public LongTag remove(int int0) {
        long $$1 = this.data[int0];
        this.data = ArrayUtils.remove(this.data, int0);
        return LongTag.valueOf($$1);
    }

    @Override
    public byte getElementType() {
        return 4;
    }

    public void clear() {
        this.data = new long[0];
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visit(this.data);
    }
}