package net.minecraft.nbt;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ListTag extends CollectionTag<Tag> {

    private static final int SELF_SIZE_IN_BYTES = 37;

    public static final TagType<ListTag> TYPE = new TagType.VariableSize<ListTag>() {

        public ListTag load(DataInput p_128792_, int p_128793_, NbtAccounter p_128794_) throws IOException {
            p_128794_.accountBytes(37L);
            if (p_128793_ > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            } else {
                byte $$3 = p_128792_.readByte();
                int $$4 = p_128792_.readInt();
                if ($$3 == 0 && $$4 > 0) {
                    throw new RuntimeException("Missing type on ListTag");
                } else {
                    p_128794_.accountBytes(4L * (long) $$4);
                    TagType<?> $$5 = TagTypes.getType($$3);
                    List<Tag> $$6 = Lists.newArrayListWithCapacity($$4);
                    for (int $$7 = 0; $$7 < $$4; $$7++) {
                        $$6.add($$5.load(p_128792_, p_128793_ + 1, p_128794_));
                    }
                    return new ListTag($$6, $$3);
                }
            }
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197491_, StreamTagVisitor p_197492_) throws IOException {
            TagType<?> $$2 = TagTypes.getType(p_197491_.readByte());
            int $$3 = p_197491_.readInt();
            switch(p_197492_.visitList($$2, $$3)) {
                case HALT:
                    return StreamTagVisitor.ValueResult.HALT;
                case BREAK:
                    $$2.skip(p_197491_, $$3);
                    return p_197492_.visitContainerEnd();
                default:
                    int $$4 = 0;
                    while (true) {
                        label41: {
                            if ($$4 < $$3) {
                                switch(p_197492_.visitElement($$2, $$4)) {
                                    case HALT:
                                        return StreamTagVisitor.ValueResult.HALT;
                                    case BREAK:
                                        $$2.skip(p_197491_);
                                        break;
                                    case SKIP:
                                        $$2.skip(p_197491_);
                                        break label41;
                                    default:
                                        switch($$2.parse(p_197491_, p_197492_)) {
                                            case HALT:
                                                return StreamTagVisitor.ValueResult.HALT;
                                            case BREAK:
                                                break;
                                            default:
                                                break label41;
                                        }
                                }
                            }
                            int $$5 = $$3 - 1 - $$4;
                            if ($$5 > 0) {
                                $$2.skip(p_197491_, $$5);
                            }
                            return p_197492_.visitContainerEnd();
                        }
                        $$4++;
                    }
            }
        }

        @Override
        public void skip(DataInput p_197489_) throws IOException {
            TagType<?> $$1 = TagTypes.getType(p_197489_.readByte());
            int $$2 = p_197489_.readInt();
            $$1.skip(p_197489_, $$2);
        }

        @Override
        public String getName() {
            return "LIST";
        }

        @Override
        public String getPrettyName() {
            return "TAG_List";
        }
    };

    private final List<Tag> list;

    private byte type;

    ListTag(List<Tag> listTag0, byte byte1) {
        this.list = listTag0;
        this.type = byte1;
    }

    public ListTag() {
        this(Lists.newArrayList(), (byte) 0);
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        if (this.list.isEmpty()) {
            this.type = 0;
        } else {
            this.type = ((Tag) this.list.get(0)).getId();
        }
        dataOutput0.writeByte(this.type);
        dataOutput0.writeInt(this.list.size());
        for (Tag $$1 : this.list) {
            $$1.write(dataOutput0);
        }
    }

    @Override
    public int sizeInBytes() {
        int $$0 = 37;
        $$0 += 4 * this.list.size();
        for (Tag $$1 : this.list) {
            $$0 += $$1.sizeInBytes();
        }
        return $$0;
    }

    @Override
    public byte getId() {
        return 9;
    }

    @Override
    public TagType<ListTag> getType() {
        return TYPE;
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    private void updateTypeAfterRemove() {
        if (this.list.isEmpty()) {
            this.type = 0;
        }
    }

    @Override
    public Tag remove(int int0) {
        Tag $$1 = (Tag) this.list.remove(int0);
        this.updateTypeAfterRemove();
        return $$1;
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public CompoundTag getCompound(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 10) {
                return (CompoundTag) $$1;
            }
        }
        return new CompoundTag();
    }

    public ListTag getList(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 9) {
                return (ListTag) $$1;
            }
        }
        return new ListTag();
    }

    public short getShort(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 2) {
                return ((ShortTag) $$1).getAsShort();
            }
        }
        return 0;
    }

    public int getInt(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 3) {
                return ((IntTag) $$1).getAsInt();
            }
        }
        return 0;
    }

    public int[] getIntArray(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 11) {
                return ((IntArrayTag) $$1).getAsIntArray();
            }
        }
        return new int[0];
    }

    public long[] getLongArray(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 12) {
                return ((LongArrayTag) $$1).getAsLongArray();
            }
        }
        return new long[0];
    }

    public double getDouble(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 6) {
                return ((DoubleTag) $$1).getAsDouble();
            }
        }
        return 0.0;
    }

    public float getFloat(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            if ($$1.getId() == 5) {
                return ((FloatTag) $$1).getAsFloat();
            }
        }
        return 0.0F;
    }

    public String getString(int int0) {
        if (int0 >= 0 && int0 < this.list.size()) {
            Tag $$1 = (Tag) this.list.get(int0);
            return $$1.getId() == 8 ? $$1.getAsString() : $$1.toString();
        } else {
            return "";
        }
    }

    public int size() {
        return this.list.size();
    }

    public Tag get(int int0) {
        return (Tag) this.list.get(int0);
    }

    @Override
    public Tag set(int int0, Tag tag1) {
        Tag $$2 = this.get(int0);
        if (!this.setTag(int0, tag1)) {
            throw new UnsupportedOperationException(String.format(Locale.ROOT, "Trying to add tag of type %d to list of %d", tag1.getId(), this.type));
        } else {
            return $$2;
        }
    }

    @Override
    public void add(int int0, Tag tag1) {
        if (!this.addTag(int0, tag1)) {
            throw new UnsupportedOperationException(String.format(Locale.ROOT, "Trying to add tag of type %d to list of %d", tag1.getId(), this.type));
        }
    }

    @Override
    public boolean setTag(int int0, Tag tag1) {
        if (this.updateType(tag1)) {
            this.list.set(int0, tag1);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addTag(int int0, Tag tag1) {
        if (this.updateType(tag1)) {
            this.list.add(int0, tag1);
            return true;
        } else {
            return false;
        }
    }

    private boolean updateType(Tag tag0) {
        if (tag0.getId() == 0) {
            return false;
        } else if (this.type == 0) {
            this.type = tag0.getId();
            return true;
        } else {
            return this.type == tag0.getId();
        }
    }

    public ListTag copy() {
        Iterable<Tag> $$0 = (Iterable<Tag>) (TagTypes.getType(this.type).isValue() ? this.list : Iterables.transform(this.list, Tag::m_6426_));
        List<Tag> $$1 = Lists.newArrayList($$0);
        return new ListTag($$1, this.type);
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof ListTag && Objects.equals(this.list, ((ListTag) object0).list);
    }

    public int hashCode() {
        return this.list.hashCode();
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitList(this);
    }

    @Override
    public byte getElementType() {
        return this.type;
    }

    public void clear() {
        this.list.clear();
        this.type = 0;
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        switch(streamTagVisitor0.visitList(TagTypes.getType(this.type), this.list.size())) {
            case HALT:
                return StreamTagVisitor.ValueResult.HALT;
            case BREAK:
                return streamTagVisitor0.visitContainerEnd();
            default:
                int $$1 = 0;
                while ($$1 < this.list.size()) {
                    Tag $$2 = (Tag) this.list.get($$1);
                    switch(streamTagVisitor0.visitElement($$2.getType(), $$1)) {
                        case HALT:
                            return StreamTagVisitor.ValueResult.HALT;
                        case BREAK:
                            return streamTagVisitor0.visitContainerEnd();
                        default:
                            switch($$2.accept(streamTagVisitor0)) {
                                case HALT:
                                    return StreamTagVisitor.ValueResult.HALT;
                                case BREAK:
                                    return streamTagVisitor0.visitContainerEnd();
                            }
                        case SKIP:
                            $$1++;
                    }
                }
                return streamTagVisitor0.visitContainerEnd();
        }
    }
}