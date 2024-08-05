package net.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;

public class ByteArrayTag extends CollectionTag<ByteTag> {

    private static final int SELF_SIZE_IN_BYTES = 24;

    public static final TagType<ByteArrayTag> TYPE = new TagType.VariableSize<ByteArrayTag>() {

        public ByteArrayTag load(DataInput p_128247_, int p_128248_, NbtAccounter p_128249_) throws IOException {
            p_128249_.accountBytes(24L);
            int $$3 = p_128247_.readInt();
            p_128249_.accountBytes(1L * (long) $$3);
            byte[] $$4 = new byte[$$3];
            p_128247_.readFully($$4);
            return new ByteArrayTag($$4);
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197433_, StreamTagVisitor p_197434_) throws IOException {
            int $$2 = p_197433_.readInt();
            byte[] $$3 = new byte[$$2];
            p_197433_.readFully($$3);
            return p_197434_.visit($$3);
        }

        @Override
        public void skip(DataInput p_197431_) throws IOException {
            p_197431_.skipBytes(p_197431_.readInt() * 1);
        }

        @Override
        public String getName() {
            return "BYTE[]";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Byte_Array";
        }
    };

    private byte[] data;

    public ByteArrayTag(byte[] byte0) {
        this.data = byte0;
    }

    public ByteArrayTag(List<Byte> listByte0) {
        this(toArray(listByte0));
    }

    private static byte[] toArray(List<Byte> listByte0) {
        byte[] $$1 = new byte[listByte0.size()];
        for (int $$2 = 0; $$2 < listByte0.size(); $$2++) {
            Byte $$3 = (Byte) listByte0.get($$2);
            $$1[$$2] = $$3 == null ? 0 : $$3;
        }
        return $$1;
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        dataOutput0.writeInt(this.data.length);
        dataOutput0.write(this.data);
    }

    @Override
    public int sizeInBytes() {
        return 24 + 1 * this.data.length;
    }

    @Override
    public byte getId() {
        return 7;
    }

    @Override
    public TagType<ByteArrayTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    @Override
    public Tag copy() {
        byte[] $$0 = new byte[this.data.length];
        System.arraycopy(this.data, 0, $$0, 0, this.data.length);
        return new ByteArrayTag($$0);
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof ByteArrayTag && Arrays.equals(this.data, ((ByteArrayTag) object0).data);
    }

    public int hashCode() {
        return Arrays.hashCode(this.data);
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitByteArray(this);
    }

    public byte[] getAsByteArray() {
        return this.data;
    }

    public int size() {
        return this.data.length;
    }

    public ByteTag get(int int0) {
        return ByteTag.valueOf(this.data[int0]);
    }

    public ByteTag set(int int0, ByteTag byteTag1) {
        byte $$2 = this.data[int0];
        this.data[int0] = byteTag1.getAsByte();
        return ByteTag.valueOf($$2);
    }

    public void add(int int0, ByteTag byteTag1) {
        this.data = ArrayUtils.add(this.data, int0, byteTag1.getAsByte());
    }

    @Override
    public boolean setTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data[int0] = ((NumericTag) tag1).getAsByte();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addTag(int int0, Tag tag1) {
        if (tag1 instanceof NumericTag) {
            this.data = ArrayUtils.add(this.data, int0, ((NumericTag) tag1).getAsByte());
            return true;
        } else {
            return false;
        }
    }

    public ByteTag remove(int int0) {
        byte $$1 = this.data[int0];
        this.data = ArrayUtils.remove(this.data, int0);
        return ByteTag.valueOf($$1);
    }

    @Override
    public byte getElementType() {
        return 1;
    }

    public void clear() {
        this.data = new byte[0];
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        return streamTagVisitor0.visit(this.data);
    }
}