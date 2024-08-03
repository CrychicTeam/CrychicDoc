package net.minecraft.nbt;

public class TagTypes {

    private static final TagType<?>[] TYPES = new TagType[] { EndTag.TYPE, ByteTag.TYPE, ShortTag.TYPE, IntTag.TYPE, LongTag.TYPE, FloatTag.TYPE, DoubleTag.TYPE, ByteArrayTag.TYPE, StringTag.TYPE, ListTag.TYPE, CompoundTag.TYPE, IntArrayTag.TYPE, LongArrayTag.TYPE };

    public static TagType<?> getType(int int0) {
        return int0 >= 0 && int0 < TYPES.length ? TYPES[int0] : TagType.createInvalid(int0);
    }
}