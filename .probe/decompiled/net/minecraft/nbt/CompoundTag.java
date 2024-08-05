package net.minecraft.nbt;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;

public class CompoundTag implements Tag {

    public static final Codec<CompoundTag> CODEC = Codec.PASSTHROUGH.comapFlatMap(p_274781_ -> {
        Tag $$1 = (Tag) p_274781_.convert(NbtOps.INSTANCE).getValue();
        return $$1 instanceof CompoundTag ? DataResult.success((CompoundTag) $$1) : DataResult.error(() -> "Not a compound tag: " + $$1);
    }, p_128412_ -> new Dynamic(NbtOps.INSTANCE, p_128412_));

    private static final int SELF_SIZE_IN_BYTES = 48;

    private static final int MAP_ENTRY_SIZE_IN_BYTES = 32;

    public static final TagType<CompoundTag> TYPE = new TagType.VariableSize<CompoundTag>() {

        public CompoundTag load(DataInput p_128485_, int p_128486_, NbtAccounter p_128487_) throws IOException {
            p_128487_.accountBytes(48L);
            if (p_128486_ > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            } else {
                Map<String, Tag> $$3 = Maps.newHashMap();
                byte $$4;
                while (($$4 = CompoundTag.readNamedTagType(p_128485_, p_128487_)) != 0) {
                    String $$5 = CompoundTag.readNamedTagName(p_128485_, p_128487_);
                    p_128487_.accountBytes((long) (28 + 2 * $$5.length()));
                    Tag $$6 = CompoundTag.readNamedTagData(TagTypes.getType($$4), $$5, p_128485_, p_128486_ + 1, p_128487_);
                    if ($$3.put($$5, $$6) == null) {
                        p_128487_.accountBytes(36L);
                    }
                }
                return new CompoundTag($$3);
            }
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput p_197446_, StreamTagVisitor p_197447_) throws IOException {
            byte $$2;
            label33: while (($$2 = p_197446_.readByte()) != 0) {
                TagType<?> $$3 = TagTypes.getType($$2);
                switch(p_197447_.visitEntry($$3)) {
                    case HALT:
                        return StreamTagVisitor.ValueResult.HALT;
                    case BREAK:
                        StringTag.skipString(p_197446_);
                        $$3.skip(p_197446_);
                        break label33;
                    case SKIP:
                        StringTag.skipString(p_197446_);
                        $$3.skip(p_197446_);
                        break;
                    default:
                        String $$4 = p_197446_.readUTF();
                        switch(p_197447_.visitEntry($$3, $$4)) {
                            case HALT:
                                return StreamTagVisitor.ValueResult.HALT;
                            case BREAK:
                                $$3.skip(p_197446_);
                                break label33;
                            case SKIP:
                                $$3.skip(p_197446_);
                                break;
                            default:
                                switch($$3.parse(p_197446_, p_197447_)) {
                                    case HALT:
                                        return StreamTagVisitor.ValueResult.HALT;
                                    case BREAK:
                                }
                        }
                }
            }
            if ($$2 != 0) {
                while (($$2 = p_197446_.readByte()) != 0) {
                    StringTag.skipString(p_197446_);
                    TagTypes.getType($$2).skip(p_197446_);
                }
            }
            return p_197447_.visitContainerEnd();
        }

        @Override
        public void skip(DataInput p_197444_) throws IOException {
            byte $$1;
            while (($$1 = p_197444_.readByte()) != 0) {
                StringTag.skipString(p_197444_);
                TagTypes.getType($$1).skip(p_197444_);
            }
        }

        @Override
        public String getName() {
            return "COMPOUND";
        }

        @Override
        public String getPrettyName() {
            return "TAG_Compound";
        }
    };

    private final Map<String, Tag> tags;

    protected CompoundTag(Map<String, Tag> mapStringTag0) {
        this.tags = mapStringTag0;
    }

    public CompoundTag() {
        this(Maps.newHashMap());
    }

    @Override
    public void write(DataOutput dataOutput0) throws IOException {
        for (String $$1 : this.tags.keySet()) {
            Tag $$2 = (Tag) this.tags.get($$1);
            writeNamedTag($$1, $$2, dataOutput0);
        }
        dataOutput0.writeByte(0);
    }

    @Override
    public int sizeInBytes() {
        int $$0 = 48;
        for (Entry<String, Tag> $$1 : this.tags.entrySet()) {
            $$0 += 28 + 2 * ((String) $$1.getKey()).length();
            $$0 += 36;
            $$0 += ((Tag) $$1.getValue()).sizeInBytes();
        }
        return $$0;
    }

    public Set<String> getAllKeys() {
        return this.tags.keySet();
    }

    @Override
    public byte getId() {
        return 10;
    }

    @Override
    public TagType<CompoundTag> getType() {
        return TYPE;
    }

    public int size() {
        return this.tags.size();
    }

    @Nullable
    public Tag put(String string0, Tag tag1) {
        return (Tag) this.tags.put(string0, tag1);
    }

    public void putByte(String string0, byte byte1) {
        this.tags.put(string0, ByteTag.valueOf(byte1));
    }

    public void putShort(String string0, short short1) {
        this.tags.put(string0, ShortTag.valueOf(short1));
    }

    public void putInt(String string0, int int1) {
        this.tags.put(string0, IntTag.valueOf(int1));
    }

    public void putLong(String string0, long long1) {
        this.tags.put(string0, LongTag.valueOf(long1));
    }

    public void putUUID(String string0, UUID uUID1) {
        this.tags.put(string0, NbtUtils.createUUID(uUID1));
    }

    public UUID getUUID(String string0) {
        return NbtUtils.loadUUID(this.get(string0));
    }

    public boolean hasUUID(String string0) {
        Tag $$1 = this.get(string0);
        return $$1 != null && $$1.getType() == IntArrayTag.TYPE && ((IntArrayTag) $$1).getAsIntArray().length == 4;
    }

    public void putFloat(String string0, float float1) {
        this.tags.put(string0, FloatTag.valueOf(float1));
    }

    public void putDouble(String string0, double double1) {
        this.tags.put(string0, DoubleTag.valueOf(double1));
    }

    public void putString(String string0, String string1) {
        this.tags.put(string0, StringTag.valueOf(string1));
    }

    public void putByteArray(String string0, byte[] byte1) {
        this.tags.put(string0, new ByteArrayTag(byte1));
    }

    public void putByteArray(String string0, List<Byte> listByte1) {
        this.tags.put(string0, new ByteArrayTag(listByte1));
    }

    public void putIntArray(String string0, int[] int1) {
        this.tags.put(string0, new IntArrayTag(int1));
    }

    public void putIntArray(String string0, List<Integer> listInteger1) {
        this.tags.put(string0, new IntArrayTag(listInteger1));
    }

    public void putLongArray(String string0, long[] long1) {
        this.tags.put(string0, new LongArrayTag(long1));
    }

    public void putLongArray(String string0, List<Long> listLong1) {
        this.tags.put(string0, new LongArrayTag(listLong1));
    }

    public void putBoolean(String string0, boolean boolean1) {
        this.tags.put(string0, ByteTag.valueOf(boolean1));
    }

    @Nullable
    public Tag get(String string0) {
        return (Tag) this.tags.get(string0);
    }

    public byte getTagType(String string0) {
        Tag $$1 = (Tag) this.tags.get(string0);
        return $$1 == null ? 0 : $$1.getId();
    }

    public boolean contains(String string0) {
        return this.tags.containsKey(string0);
    }

    public boolean contains(String string0, int int1) {
        int $$2 = this.getTagType(string0);
        if ($$2 == int1) {
            return true;
        } else {
            return int1 != 99 ? false : $$2 == 1 || $$2 == 2 || $$2 == 3 || $$2 == 4 || $$2 == 5 || $$2 == 6;
        }
    }

    public byte getByte(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsByte();
            }
        } catch (ClassCastException var3) {
        }
        return 0;
    }

    public short getShort(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsShort();
            }
        } catch (ClassCastException var3) {
        }
        return 0;
    }

    public int getInt(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsInt();
            }
        } catch (ClassCastException var3) {
        }
        return 0;
    }

    public long getLong(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsLong();
            }
        } catch (ClassCastException var3) {
        }
        return 0L;
    }

    public float getFloat(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsFloat();
            }
        } catch (ClassCastException var3) {
        }
        return 0.0F;
    }

    public double getDouble(String string0) {
        try {
            if (this.contains(string0, 99)) {
                return ((NumericTag) this.tags.get(string0)).getAsDouble();
            }
        } catch (ClassCastException var3) {
        }
        return 0.0;
    }

    public String getString(String string0) {
        try {
            if (this.contains(string0, 8)) {
                return ((Tag) this.tags.get(string0)).getAsString();
            }
        } catch (ClassCastException var3) {
        }
        return "";
    }

    public byte[] getByteArray(String string0) {
        try {
            if (this.contains(string0, 7)) {
                return ((ByteArrayTag) this.tags.get(string0)).getAsByteArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(string0, ByteArrayTag.TYPE, var3));
        }
        return new byte[0];
    }

    public int[] getIntArray(String string0) {
        try {
            if (this.contains(string0, 11)) {
                return ((IntArrayTag) this.tags.get(string0)).getAsIntArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(string0, IntArrayTag.TYPE, var3));
        }
        return new int[0];
    }

    public long[] getLongArray(String string0) {
        try {
            if (this.contains(string0, 12)) {
                return ((LongArrayTag) this.tags.get(string0)).getAsLongArray();
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(string0, LongArrayTag.TYPE, var3));
        }
        return new long[0];
    }

    public CompoundTag getCompound(String string0) {
        try {
            if (this.contains(string0, 10)) {
                return (CompoundTag) this.tags.get(string0);
            }
        } catch (ClassCastException var3) {
            throw new ReportedException(this.createReport(string0, TYPE, var3));
        }
        return new CompoundTag();
    }

    public ListTag getList(String string0, int int1) {
        try {
            if (this.getTagType(string0) == 9) {
                ListTag $$2 = (ListTag) this.tags.get(string0);
                if (!$$2.isEmpty() && $$2.getElementType() != int1) {
                    return new ListTag();
                }
                return $$2;
            }
        } catch (ClassCastException var4) {
            throw new ReportedException(this.createReport(string0, ListTag.TYPE, var4));
        }
        return new ListTag();
    }

    public boolean getBoolean(String string0) {
        return this.getByte(string0) != 0;
    }

    public void remove(String string0) {
        this.tags.remove(string0);
    }

    @Override
    public String toString() {
        return this.m_7916_();
    }

    public boolean isEmpty() {
        return this.tags.isEmpty();
    }

    private CrashReport createReport(String string0, TagType<?> tagType1, ClassCastException classCastException2) {
        CrashReport $$3 = CrashReport.forThrowable(classCastException2, "Reading NBT data");
        CrashReportCategory $$4 = $$3.addCategory("Corrupt NBT tag", 1);
        $$4.setDetail("Tag type found", (CrashReportDetail<String>) (() -> ((Tag) this.tags.get(string0)).getType().getName()));
        $$4.setDetail("Tag type expected", tagType1::m_5987_);
        $$4.setDetail("Tag name", string0);
        return $$3;
    }

    public CompoundTag copy() {
        Map<String, Tag> $$0 = Maps.newHashMap(Maps.transformValues(this.tags, Tag::m_6426_));
        return new CompoundTag($$0);
    }

    public boolean equals(Object object0) {
        return this == object0 ? true : object0 instanceof CompoundTag && Objects.equals(this.tags, ((CompoundTag) object0).tags);
    }

    public int hashCode() {
        return this.tags.hashCode();
    }

    private static void writeNamedTag(String string0, Tag tag1, DataOutput dataOutput2) throws IOException {
        dataOutput2.writeByte(tag1.getId());
        if (tag1.getId() != 0) {
            dataOutput2.writeUTF(string0);
            tag1.write(dataOutput2);
        }
    }

    static byte readNamedTagType(DataInput dataInput0, NbtAccounter nbtAccounter1) throws IOException {
        return dataInput0.readByte();
    }

    static String readNamedTagName(DataInput dataInput0, NbtAccounter nbtAccounter1) throws IOException {
        return dataInput0.readUTF();
    }

    static Tag readNamedTagData(TagType<?> tagType0, String string1, DataInput dataInput2, int int3, NbtAccounter nbtAccounter4) {
        try {
            return tagType0.load(dataInput2, int3, nbtAccounter4);
        } catch (IOException var8) {
            CrashReport $$6 = CrashReport.forThrowable(var8, "Loading NBT data");
            CrashReportCategory $$7 = $$6.addCategory("NBT Tag");
            $$7.setDetail("Tag name", string1);
            $$7.setDetail("Tag type", tagType0.getName());
            throw new ReportedException($$6);
        }
    }

    public CompoundTag merge(CompoundTag compoundTag0) {
        for (String $$1 : compoundTag0.tags.keySet()) {
            Tag $$2 = (Tag) compoundTag0.tags.get($$1);
            if ($$2.getId() == 10) {
                if (this.contains($$1, 10)) {
                    CompoundTag $$3 = this.getCompound($$1);
                    $$3.merge((CompoundTag) $$2);
                } else {
                    this.put($$1, $$2.copy());
                }
            } else {
                this.put($$1, $$2.copy());
            }
        }
        return this;
    }

    @Override
    public void accept(TagVisitor tagVisitor0) {
        tagVisitor0.visitCompound(this);
    }

    protected Map<String, Tag> entries() {
        return Collections.unmodifiableMap(this.tags);
    }

    @Override
    public StreamTagVisitor.ValueResult accept(StreamTagVisitor streamTagVisitor0) {
        for (Entry<String, Tag> $$1 : this.tags.entrySet()) {
            Tag $$2 = (Tag) $$1.getValue();
            TagType<?> $$3 = $$2.getType();
            StreamTagVisitor.EntryResult $$4 = streamTagVisitor0.visitEntry($$3);
            switch($$4) {
                case HALT:
                    return StreamTagVisitor.ValueResult.HALT;
                case BREAK:
                    return streamTagVisitor0.visitContainerEnd();
                case SKIP:
                    break;
                default:
                    $$4 = streamTagVisitor0.visitEntry($$3, (String) $$1.getKey());
                    switch($$4) {
                        case HALT:
                            return StreamTagVisitor.ValueResult.HALT;
                        case BREAK:
                            return streamTagVisitor0.visitContainerEnd();
                        case SKIP:
                            break;
                        default:
                            StreamTagVisitor.ValueResult $$5 = $$2.accept(streamTagVisitor0);
                            switch($$5) {
                                case HALT:
                                    return StreamTagVisitor.ValueResult.HALT;
                                case BREAK:
                                    return streamTagVisitor0.visitContainerEnd();
                            }
                    }
            }
        }
        return streamTagVisitor0.visitContainerEnd();
    }
}