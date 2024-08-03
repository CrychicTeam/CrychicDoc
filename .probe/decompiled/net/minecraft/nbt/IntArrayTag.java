package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class IntArrayTag extends CollectionTag<IntTag> {

    private static final int SELF_SIZE_IN_BYTES = 24;

    public static final TagType<IntArrayTag> TYPE = new TagType.VariableSize<IntArrayTag>() {

        public IntArrayTag load(DataInput p_128662_, int p_128663_, NbtAccounter p_128664_) throws IOException {
            p_128664_.accountBytes(24L);
            int $$3 = p_128662_.readInt();
            p_128664_.accountBytes(4L * (long) $$3);
            int[] $$4 = new int[$$3];
            for (int $$5 = 0; $$5 < $$3; $$5++) {
                $$4[$$5] = p_128662_.readInt();
            }
            return new IntArrayTag($$4);
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197478_, StreamTagVisitor p_197479_) throws IOException {
            int $$2 = p_197478_.readInt();
            int[] $$3 = new int[$$2];
            for (int $$4 = 0; $$4 < $$2; $$4++) {
                $$3[$$4] = p_197478_.readInt();
            }
            return p_197479_.visit($$3);
        }

        @Override
        public void skip(DataInput p_197476_) throws IOException {
            p_197476_.skipBytes(p_197476_.readInt() * 4);
        }

        @Override
        public String getName() {
            return "INT[]";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Int_Array";
        }
    };

    private int[] data;

    public IntArrayTag(int[] int0) {
        this.data = int0;
    }

    public IntArrayTag(List<Integer> listInteger0) {
        this(toArray(listInteger0));
    }

    private static int[] toArray(List<Integer> listInteger0) {
        int[] $$1 = new int[listInteger0.size()];
        for (int $$2 = 0; $$2 < listInteger0.size(); $$2++) {
            Integer $$3 = (Integer) listInteger0.get($$2);
            $$1[$$2] = $$3 == null ? 0 : $$3;
        }
        return $$1;
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeInt(this.data.length);
        for (int $$1 : this.data) {
            dataOutput0.writeInt($$1);
        }
    }

    @Override
    public int sizeInBytes() {
        return 24 + 4 * this.data.length;
    }

    @Override
    public byte getId() {
        return 11;
    }

    @Override
    public TagType<IntArrayTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    public IntArrayTag copy() {
        int[] $$0 = new int[this.data.length];
        System.arraycopy(this.data, 0, $$0, 0, this.data.length);
        return new IntArrayTag($$0);
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof IntArrayTag && Arrays.equals(this.data, ((IntArrayTag) object0).data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    public int[] getAsIntArray() {
        return this.data;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitIntArray(this);
    }

    public int size() {
        return this.data.length;
    }

    public IntTag get(int int0) {
        return IntTag.valueOf(this.data[int0]);
    }

    public IntTag set(int int0, IntTag intTag1) {
        int $$2 = this.data[int0];
        this.data[int0] = intTag1.getAsInt();
        return IntTag.valueOf($$2);
    }

    public void add(int int0, IntTag intTag1) {
        this.data = ArrayUtils.add(this.data, int0, intTag1.getAsInt());
    }

    @Override
    public boolean setTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data[int0] = ((NumericTag) tag1).getAsInt();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data = ArrayUtils.add(this.data, int0, ((NumericTag) tag1).getAsInt());
            return true;
        } else {
            return false;
        }
    }

    public IntTag remove(int int0) {
        int $$1 = this.data[int0];
        this.data = ArrayUtils.remove(this.data, int0);
        return IntTag.valueOf($$1);
    }

    @Override
    public byte getElementType() {
        return 3;
    }

    public void clear() {
        this.data = new int[0];
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visit(this.data);
    }
}