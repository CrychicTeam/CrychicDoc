package dev.ftb.mods.ftblibrary.snbt;

import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public class SNBTNet {

    public static final ByteArrayTag EMPTY_BYTE_ARRAY = new ByteArrayTag(new byte[0]);

    public static final IntArrayTag EMPTY_INT_ARRAY = new IntArrayTag(new int[0]);

    public static final LongArrayTag EMPTY_LONG_ARRAY = new LongArrayTag(new long[0]);

    public static void write(FriendlyByteBuf buf, @Nullable Tag tag) {
        if (tag instanceof ByteTag) {
            buf.writeByte(((ByteTag) tag).getAsByte());
        } else if (tag instanceof ShortTag) {
            buf.writeShort(((ShortTag) tag).getAsShort());
        } else if (tag instanceof IntTag) {
            buf.writeInt(((IntTag) tag).getAsInt());
        } else if (tag instanceof LongTag) {
            buf.writeLong(((LongTag) tag).getAsLong());
        } else if (tag instanceof FloatTag) {
            buf.writeFloat(((FloatTag) tag).getAsFloat());
        } else if (tag instanceof DoubleTag) {
            buf.writeDouble(((DoubleTag) tag).getAsDouble());
        } else if (tag instanceof ByteArrayTag) {
            writeByteArray(buf, (ByteArrayTag) tag);
        } else if (tag instanceof StringTag) {
            buf.writeUtf(tag.getAsString(), 32767);
        } else if (tag instanceof ListTag) {
            writeList(buf, (ListTag) tag);
        } else if (tag instanceof CompoundTag) {
            writeCompound(buf, SNBTCompoundTag.of(tag));
        } else if (tag instanceof IntArrayTag) {
            writeIntArray(buf, (IntArrayTag) tag);
        } else if (tag instanceof LongArrayTag) {
            writeLongArray(buf, (LongArrayTag) tag);
        }
    }

    public static void writeCompound(FriendlyByteBuf buf, @Nullable SNBTCompoundTag tag) {
        if (tag == null) {
            buf.writeVarInt(-1);
        } else {
            buf.writeVarInt(tag.m_128431_().size());
            for (String s : tag.m_128431_()) {
                buf.writeUtf(s, 32767);
                buf.writeByte(tag.m_128423_(s).getId());
                write(buf, tag.m_128423_(s));
            }
        }
    }

    public static void writeList(FriendlyByteBuf buf, @Nullable ListTag tag) {
        if (tag == null) {
            buf.writeVarInt(-1);
        } else {
            buf.writeVarInt(tag.size());
            if (!tag.isEmpty()) {
                buf.writeByte(tag.getElementType());
                for (Tag value : tag) {
                    write(buf, value);
                }
            }
        }
    }

    public static void writeByteArray(FriendlyByteBuf buf, @Nullable ByteArrayTag tag) {
        if (tag == null) {
            buf.writeVarInt(-1);
        } else {
            buf.writeVarInt(tag.size());
            for (byte v : tag.getAsByteArray()) {
                buf.writeByte(v);
            }
        }
    }

    public static void writeIntArray(FriendlyByteBuf buf, @Nullable IntArrayTag tag) {
        if (tag == null) {
            buf.writeVarInt(-1);
        } else {
            buf.writeVarInt(tag.size());
            for (int v : tag.getAsIntArray()) {
                buf.writeInt(v);
            }
        }
    }

    public static void writeLongArray(FriendlyByteBuf buf, @Nullable LongArrayTag tag) {
        if (tag == null) {
            buf.writeVarInt(-1);
        } else {
            buf.writeVarInt(tag.size());
            for (long v : tag.getAsLongArray()) {
                buf.writeLong(v);
            }
        }
    }

    @Nullable
    public static Tag read(byte type, FriendlyByteBuf buf) {
        return (Tag) (switch(type) {
            case 0 ->
                EndTag.INSTANCE;
            case 1 ->
                ByteTag.valueOf(buf.readByte());
            case 2 ->
                ShortTag.valueOf(buf.readShort());
            case 3 ->
                IntTag.valueOf(buf.readInt());
            case 4 ->
                LongTag.valueOf(buf.readLong());
            case 5 ->
                FloatTag.valueOf(buf.readFloat());
            case 6 ->
                DoubleTag.valueOf(buf.readDouble());
            case 7 ->
                readByteArray(buf);
            case 8 ->
                StringTag.valueOf(buf.readUtf(32767));
            case 9 ->
                readList(buf);
            case 10 ->
                readCompound(buf);
            case 11 ->
                readIntArray(buf);
            case 12 ->
                readLongArray(buf);
            default ->
                null;
        });
    }

    @Nullable
    public static SNBTCompoundTag readCompound(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len == -1) {
            return null;
        } else {
            SNBTCompoundTag tag = new SNBTCompoundTag();
            for (int i = 0; i < len; i++) {
                String key = buf.readUtf(32767);
                byte type = buf.readByte();
                tag.m_128365_(key, read(type, buf));
            }
            return tag;
        }
    }

    @Nullable
    public static ListTag readList(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len == -1) {
            return null;
        } else if (len == 0) {
            return new ListTag();
        } else {
            byte type = buf.readByte();
            ListTag tag = new ListTag();
            for (int i = 0; i < len; i++) {
                tag.add(read(type, buf));
            }
            return tag;
        }
    }

    public static ByteArrayTag readByteArray(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len == -1) {
            return null;
        } else if (len == 0) {
            return EMPTY_BYTE_ARRAY;
        } else {
            byte[] values = new byte[len];
            for (int i = 0; i < len; i++) {
                values[i] = buf.readByte();
            }
            return new ByteArrayTag(values);
        }
    }

    public static IntArrayTag readIntArray(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len == -1) {
            return null;
        } else if (len == 0) {
            return EMPTY_INT_ARRAY;
        } else {
            int[] values = new int[len];
            for (int i = 0; i < len; i++) {
                values[i] = buf.readInt();
            }
            return new IntArrayTag(values);
        }
    }

    public static LongArrayTag readLongArray(FriendlyByteBuf buf) {
        int len = buf.readVarInt();
        if (len == -1) {
            return null;
        } else if (len == 0) {
            return EMPTY_LONG_ARRAY;
        } else {
            long[] values = new long[len];
            for (int i = 0; i < len; i++) {
                values[i] = buf.readLong();
            }
            return new LongArrayTag(values);
        }
    }
}