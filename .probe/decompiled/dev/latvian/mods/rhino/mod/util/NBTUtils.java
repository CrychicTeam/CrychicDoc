package dev.latvian.mods.rhino.mod.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.rhino.Undefined;
import dev.latvian.mods.rhino.util.ValueUnwrapper;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.EncoderException;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.EndTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StreamTagVisitor;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagParser;
import net.minecraft.nbt.TagType;
import net.minecraft.nbt.TagTypes;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.Nullable;

public interface NBTUtils {

    ValueUnwrapper VALUE_UNWRAPPER = (contextData, scope, value) -> value instanceof Tag tag ? fromTag(tag) : value;

    TagType<OrderedCompoundTag> COMPOUND_TYPE = new TagType.VariableSize<OrderedCompoundTag>() {

        public OrderedCompoundTag load(DataInput dataInput, int i, NbtAccounter nbtAccounter) throws IOException {
            nbtAccounter.accountBytes(48L);
            if (i > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            } else {
                Map<String, Tag> map = new LinkedHashMap();
                byte typeId;
                while ((typeId = dataInput.readByte()) != 0) {
                    String key = dataInput.readUTF();
                    nbtAccounter.accountBytes(28L + 2L * (long) key.length());
                    TagType<?> valueType = NBTUtils.convertType(TagTypes.getType(typeId));
                    Tag value = valueType.load(dataInput, i + 1, nbtAccounter);
                    if (map.put(key, value) != null) {
                        nbtAccounter.accountBytes(36L);
                    }
                }
                return new OrderedCompoundTag(map);
            }
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput dataInput, StreamTagVisitor visitor) throws IOException {
            byte typeId;
            label33: while ((typeId = dataInput.readByte()) != 0) {
                TagType<?> valueType = NBTUtils.convertType(TagTypes.getType(typeId));
                switch(visitor.visitEntry(valueType)) {
                    case HALT:
                        return StreamTagVisitor.ValueResult.HALT;
                    case BREAK:
                        StringTag.skipString(dataInput);
                        valueType.skip(dataInput);
                        break label33;
                    case SKIP:
                        StringTag.skipString(dataInput);
                        valueType.skip(dataInput);
                        break;
                    default:
                        String key = dataInput.readUTF();
                        switch(visitor.visitEntry(valueType, key)) {
                            case HALT:
                                return StreamTagVisitor.ValueResult.HALT;
                            case BREAK:
                                valueType.skip(dataInput);
                                break label33;
                            case SKIP:
                                valueType.skip(dataInput);
                                break;
                            default:
                                switch(valueType.parse(dataInput, visitor)) {
                                    case HALT:
                                        return StreamTagVisitor.ValueResult.HALT;
                                    case BREAK:
                                }
                        }
                }
            }
            if (typeId != 0) {
                while ((typeId = dataInput.readByte()) != 0) {
                    StringTag.skipString(dataInput);
                    NBTUtils.convertType(TagTypes.getType(typeId)).skip(dataInput);
                }
            }
            return visitor.visitContainerEnd();
        }

        @Override
        public void skip(DataInput dataInput) throws IOException {
            byte typeId;
            while ((typeId = dataInput.readByte()) != 0) {
                StringTag.skipString(dataInput);
                NBTUtils.convertType(TagTypes.getType(typeId)).skip(dataInput);
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

    TagType<ListTag> LIST_TYPE = new TagType.VariableSize<ListTag>() {

        public ListTag load(DataInput dataInput, int i, NbtAccounter nbtAccounter) throws IOException {
            nbtAccounter.accountBytes(37L);
            if (i > 512) {
                throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
            } else {
                byte typeId = dataInput.readByte();
                int size = dataInput.readInt();
                if (typeId == 0 && size > 0) {
                    throw new RuntimeException("Missing type on ListTag");
                } else {
                    nbtAccounter.accountBytes(4L * (long) size);
                    TagType<?> valueType = NBTUtils.convertType(TagTypes.getType(typeId));
                    ListTag list = new ListTag();
                    for (int k = 0; k < size; k++) {
                        list.add(valueType.load(dataInput, i + 1, nbtAccounter));
                    }
                    return list;
                }
            }
        }

        @Override
        public StreamTagVisitor.ValueResult parse(DataInput dataInput, StreamTagVisitor visitor) throws IOException {
            TagType<?> tagType = NBTUtils.convertType(TagTypes.getType(dataInput.readByte()));
            int size = dataInput.readInt();
            switch(visitor.visitList(tagType, size)) {
                case HALT:
                    return StreamTagVisitor.ValueResult.HALT;
                case BREAK:
                    tagType.skip(dataInput, size);
                    return visitor.visitContainerEnd();
                default:
                    int i = 0;
                    while (true) {
                        label41: {
                            if (i < size) {
                                switch(visitor.visitElement(tagType, i)) {
                                    case HALT:
                                        return StreamTagVisitor.ValueResult.HALT;
                                    case BREAK:
                                        tagType.skip(dataInput);
                                        break;
                                    case SKIP:
                                        tagType.skip(dataInput);
                                        break label41;
                                    default:
                                        switch(tagType.parse(dataInput, visitor)) {
                                            case HALT:
                                                return StreamTagVisitor.ValueResult.HALT;
                                            case BREAK:
                                                break;
                                            default:
                                                break label41;
                                        }
                                }
                            }
                            int toSkip = size - 1 - i;
                            if (toSkip > 0) {
                                tagType.skip(dataInput, toSkip);
                            }
                            return visitor.visitContainerEnd();
                        }
                        i++;
                    }
            }
        }

        @Override
        public void skip(DataInput visitor) throws IOException {
            TagType<?> tagType = NBTUtils.convertType(TagTypes.getType(visitor.readByte()));
            int size = visitor.readInt();
            tagType.skip(visitor, size);
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

    @Nullable
    static Object fromTag(@Nullable Tag t) {
        if (t == null || t instanceof EndTag) {
            return null;
        } else if (t instanceof StringTag) {
            return t.getAsString();
        } else {
            return t instanceof NumericTag num ? num.getAsNumber() : t;
        }
    }

    @Nullable
    static Tag toTag(@Nullable Object v) {
        if (v == null || v instanceof EndTag) {
            return null;
        } else if (v instanceof Tag) {
            return (Tag) v;
        } else if (v instanceof NBTSerializable s) {
            return s.toNBTJS();
        } else if (v instanceof CharSequence || v instanceof Character) {
            return StringTag.valueOf(v.toString());
        } else if (v instanceof Boolean b) {
            return ByteTag.valueOf(b);
        } else if (v instanceof Number number) {
            if (number instanceof Byte) {
                return ByteTag.valueOf(number.byteValue());
            } else if (number instanceof Short) {
                return ShortTag.valueOf(number.shortValue());
            } else if (number instanceof Integer) {
                return IntTag.valueOf(number.intValue());
            } else if (number instanceof Long) {
                return LongTag.valueOf(number.longValue());
            } else {
                return (Tag) (number instanceof Float ? FloatTag.valueOf(number.floatValue()) : DoubleTag.valueOf(number.doubleValue()));
            }
        } else if (v instanceof JsonPrimitive json) {
            if (json.isNumber()) {
                return toTag(json.getAsNumber());
            } else {
                return (Tag) (json.isBoolean() ? ByteTag.valueOf(json.getAsBoolean()) : StringTag.valueOf(json.getAsString()));
            }
        } else if (v instanceof Map<?, ?> map) {
            CompoundTag tag = new OrderedCompoundTag();
            for (Entry<?, ?> entry : map.entrySet()) {
                Tag nbt1 = toTag(entry.getValue());
                if (nbt1 != null) {
                    tag.put(String.valueOf(entry.getKey()), nbt1);
                }
            }
            return tag;
        } else if (v instanceof JsonObject jsonx) {
            CompoundTag tag = new OrderedCompoundTag();
            for (Entry<String, JsonElement> entryx : jsonx.entrySet()) {
                Tag nbt1 = toTag(entryx.getValue());
                if (nbt1 != null) {
                    tag.put((String) entryx.getKey(), nbt1);
                }
            }
            return tag;
        } else if (v instanceof Collection<?> c) {
            return toTagCollection(c);
        } else if (!(v instanceof JsonArray array)) {
            return null;
        } else {
            List<Tag> list = new ArrayList(array.size());
            for (JsonElement element : array) {
                list.add(toTag(element));
            }
            return toTagCollection((Collection<?>) list);
        }
    }

    static boolean isTagCompound(Object o) {
        return o == null || Undefined.isUndefined(o) || o instanceof CompoundTag || o instanceof CharSequence || o instanceof Map || o instanceof JsonElement;
    }

    @Nullable
    static CompoundTag toTagCompound(@Nullable Object v) {
        if (v instanceof CompoundTag) {
            return (CompoundTag) v;
        } else if (v instanceof CharSequence) {
            try {
                return TagParser.parseTag(v.toString());
            } catch (Exception var5) {
                return null;
            }
        } else if (v instanceof JsonPrimitive json) {
            try {
                return TagParser.parseTag(json.getAsString());
            } catch (Exception var6) {
                return null;
            }
        } else if (v instanceof JsonObject json) {
            try {
                return TagParser.parseTag(json.toString());
            } catch (Exception var7) {
                return null;
            }
        } else {
            return toTag(v) instanceof CompoundTag nbt ? nbt : null;
        }
    }

    static boolean isTagCollection(Object o) {
        return o == null || Undefined.isUndefined(o) || o instanceof CharSequence || o instanceof Collection || o instanceof JsonArray;
    }

    @Nullable
    static CollectionTag<?> toTagCollection(@Nullable Object v) {
        if (v instanceof CollectionTag) {
            return (CollectionTag<?>) v;
        } else if (v instanceof CharSequence) {
            try {
                return (CollectionTag<?>) TagParser.parseTag("{a:" + v + "}").get("a");
            } catch (Exception var6) {
                return null;
            }
        } else if (!(v instanceof JsonArray array)) {
            return v == null ? null : toTagCollection((Collection<?>) v);
        } else {
            List<Tag> list = new ArrayList(array.size());
            for (JsonElement element : array) {
                list.add(toTag(element));
            }
            return toTagCollection((Collection<?>) list);
        }
    }

    @Nullable
    static ListTag toTagList(@Nullable Object list) {
        return (ListTag) toTagCollection(list);
    }

    static CollectionTag<?> toTagCollection(Collection<?> c) {
        if (c.isEmpty()) {
            return new ListTag();
        } else {
            Tag[] values = new Tag[c.size()];
            int s = 0;
            byte commmonId = -1;
            for (Object o : c) {
                values[s] = toTag(o);
                if (values[s] != null) {
                    if (commmonId == -1) {
                        commmonId = values[s].getId();
                    } else if (commmonId != values[s].getId()) {
                        commmonId = 0;
                    }
                    s++;
                }
            }
            if (commmonId == 3) {
                int[] array = new int[s];
                for (int i = 0; i < s; i++) {
                    array[i] = ((NumericTag) values[i]).getAsInt();
                }
                return new IntArrayTag(array);
            } else if (commmonId == 1) {
                byte[] array = new byte[s];
                for (int i = 0; i < s; i++) {
                    array[i] = ((NumericTag) values[i]).getAsByte();
                }
                return new ByteArrayTag(array);
            } else if (commmonId != 4) {
                if (commmonId != 0 && commmonId != -1) {
                    ListTag nbt = new ListTag();
                    for (Tag nbt1 : values) {
                        if (nbt1 == null) {
                            return nbt;
                        }
                        nbt.add(nbt1);
                    }
                    return nbt;
                } else {
                    return new ListTag();
                }
            } else {
                long[] array = new long[s];
                for (int i = 0; i < s; i++) {
                    array[i] = ((NumericTag) values[i]).getAsLong();
                }
                return new LongArrayTag(array);
            }
        }
    }

    static Tag compoundTag() {
        return new OrderedCompoundTag();
    }

    static Tag compoundTag(Map<?, ?> map) {
        OrderedCompoundTag tag = new OrderedCompoundTag();
        for (Entry<?, ?> entry : map.entrySet()) {
            Tag tag1 = toTag(entry.getValue());
            if (tag1 != null) {
                tag.m_128365_(String.valueOf(entry.getKey()), tag1);
            }
        }
        return tag;
    }

    static Tag listTag() {
        return new ListTag();
    }

    static Tag listTag(List<?> list) {
        ListTag tag = new ListTag();
        for (Object v : list) {
            tag.add(toTag(v));
        }
        return tag;
    }

    static Tag byteTag(byte v) {
        return ByteTag.valueOf(v);
    }

    static Tag b(byte v) {
        return ByteTag.valueOf(v);
    }

    static Tag shortTag(short v) {
        return ShortTag.valueOf(v);
    }

    static Tag s(short v) {
        return ShortTag.valueOf(v);
    }

    static Tag intTag(int v) {
        return IntTag.valueOf(v);
    }

    static Tag i(int v) {
        return IntTag.valueOf(v);
    }

    static Tag longTag(long v) {
        return LongTag.valueOf(v);
    }

    static Tag l(long v) {
        return LongTag.valueOf(v);
    }

    static Tag floatTag(float v) {
        return FloatTag.valueOf(v);
    }

    static Tag f(float v) {
        return FloatTag.valueOf(v);
    }

    static Tag doubleTag(double v) {
        return DoubleTag.valueOf(v);
    }

    static Tag d(double v) {
        return DoubleTag.valueOf(v);
    }

    static Tag stringTag(String v) {
        return StringTag.valueOf(v);
    }

    static Tag intArrayTag(int[] v) {
        return new IntArrayTag(v);
    }

    static Tag ia(int[] v) {
        return new IntArrayTag(v);
    }

    static Tag longArrayTag(long[] v) {
        return new LongArrayTag(v);
    }

    static Tag la(long[] v) {
        return new LongArrayTag(v);
    }

    static Tag byteArrayTag(byte[] v) {
        return new ByteArrayTag(v);
    }

    static Tag ba(byte[] v) {
        return new ByteArrayTag(v);
    }

    static void quoteAndEscapeForJS(StringBuilder stringBuilder, String string) {
        int start = stringBuilder.length();
        stringBuilder.append(' ');
        char c = 0;
        for (int i = 0; i < string.length(); i++) {
            char d = string.charAt(i);
            if (d == '\\') {
                stringBuilder.append('\\');
            } else if (d == '"' || d == '\'') {
                if (c == 0) {
                    c = (char) (d == '\'' ? 34 : 39);
                }
                if (c == d) {
                    stringBuilder.append('\\');
                }
            }
            stringBuilder.append(d);
        }
        if (c == 0) {
            c = '\'';
        }
        stringBuilder.setCharAt(start, c);
        stringBuilder.append(c);
    }

    static TagType<?> convertType(TagType<?> tagType) {
        return tagType == CompoundTag.TYPE ? COMPOUND_TYPE : (tagType == ListTag.TYPE ? LIST_TYPE : tagType);
    }

    static JsonElement toJson(@Nullable Tag t) {
        if (t == null || t instanceof EndTag) {
            return JsonNull.INSTANCE;
        } else if (t instanceof StringTag) {
            return new JsonPrimitive(t.getAsString());
        } else if (t instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) t).getAsNumber());
        } else if (t instanceof CollectionTag<?> l) {
            JsonArray array = new JsonArray();
            for (Tag tag : l) {
                array.add(toJson(tag));
            }
            return array;
        } else if (!(t instanceof CompoundTag c)) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject object = new JsonObject();
            for (String key : c.getAllKeys()) {
                object.add(key, toJson(c.get(key)));
            }
            return object;
        }
    }

    @Nullable
    static OrderedCompoundTag read(FriendlyByteBuf buf) {
        int i = buf.readerIndex();
        byte b = buf.readByte();
        if (b == 0) {
            return null;
        } else {
            buf.readerIndex(i);
            try {
                DataInputStream stream = new DataInputStream(new ByteBufInputStream(buf));
                byte b1 = stream.readByte();
                if (b1 == 0) {
                    return null;
                } else {
                    stream.readUTF();
                    TagType<?> tagType = convertType(TagTypes.getType(b1));
                    return tagType != COMPOUND_TYPE ? null : COMPOUND_TYPE.load(stream, 0, NbtAccounter.UNLIMITED);
                }
            } catch (IOException var6) {
                throw new EncoderException(var6);
            }
        }
    }

    static Map<String, Tag> accessTagMap(CompoundTag tag) {
        return tag.tags;
    }
}