package noppes.npcs.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.TagType;
import net.minecraft.resources.ResourceLocation;

public class ValueUtil {

    public static final UUID EMPTY_UUID = new UUID(0L, 0L);

    public static float correctFloat(float given, float min, float max) {
        if (given < min) {
            return min;
        } else {
            return given > max ? max : given;
        }
    }

    public static int CorrectInt(int given, int min, int max) {
        if (given < min) {
            return min;
        } else {
            return given > max ? max : given;
        }
    }

    public static String nbtToJson(CompoundTag nbt) {
        return new Gson().toJson(getJsonValue(nbt));
    }

    private static JsonElement getJsonValue(Tag value) {
        if (value.getType() == CompoundTag.TYPE) {
            CompoundTag nbt = (CompoundTag) value;
            JsonObject root = new JsonObject();
            for (String key : nbt.getAllKeys()) {
                Tag n = nbt.get(key);
                JsonElement ele = getJsonValue(n);
                if (ele != null) {
                    JsonObject ob = new JsonObject();
                    ob.addProperty("type", n.getType().getName());
                    ob.addProperty("type_id", n.getId());
                    ob.addProperty("pretty_type", n.getType().getPrettyName());
                    ob.add("value", ele);
                    root.add(key, ob);
                }
            }
            return root;
        } else if (value == StringTag.TYPE) {
            return new JsonPrimitive(value.getAsString());
        } else if (value instanceof NumericTag) {
            return new JsonPrimitive(((NumericTag) value).getAsNumber());
        } else if (!(value instanceof CollectionTag)) {
            return null;
        } else {
            JsonArray jsonValue = new JsonArray();
            for (Tag n : (CollectionTag) value) {
                jsonValue.add(getJsonValue(n));
            }
            return jsonValue;
        }
    }

    public static CompoundTag jsonToNbt(String json) {
        JsonObject ob = (JsonObject) new Gson().fromJson(json, JsonObject.class);
        return toNbt(ob);
    }

    private static CompoundTag toNbt(JsonObject json) {
        CompoundTag nbt = new CompoundTag();
        for (Entry<String, JsonElement> entry : json.entrySet()) {
            String key = (String) entry.getKey();
            JsonObject ele = (JsonObject) entry.getValue();
            TagType<? extends Tag> type = stringToType(ele.get("type").getAsString());
            if (type == StringTag.TYPE) {
                nbt.putString(key, ele.get("value").getAsString());
            }
            if (type == IntTag.TYPE) {
                nbt.putInt(key, ele.get("value").getAsInt());
            }
            if (type == ByteTag.TYPE) {
                nbt.putByte(key, ele.get("value").getAsByte());
            }
            if (type == LongTag.TYPE) {
                nbt.putLong(key, ele.get("value").getAsLong());
            }
            if (type == FloatTag.TYPE) {
                nbt.putFloat(key, ele.get("value").getAsFloat());
            }
            if (type == DoubleTag.TYPE) {
                nbt.putDouble(key, ele.get("value").getAsDouble());
            }
            if (type == ShortTag.TYPE) {
                nbt.putShort(key, ele.get("value").getAsShort());
            }
            if (type == CompoundTag.TYPE) {
                nbt.put(key, toNbt((JsonObject) ele.get("value")));
            }
            if (type == IntArrayTag.TYPE) {
                JsonArray array = (JsonArray) ele.get("value");
                nbt.put(key, new IntArrayTag((List<Integer>) StreamSupport.stream(array.spliterator(), false).map(JsonElement::getAsInt).collect(Collectors.toList())));
            }
            if (type == ByteArrayTag.TYPE) {
                JsonArray array = (JsonArray) ele.get("value");
                nbt.put(key, new ByteArrayTag((List<Byte>) StreamSupport.stream(array.spliterator(), false).map(JsonElement::getAsByte).collect(Collectors.toList())));
            }
            if (type == LongArrayTag.TYPE) {
                JsonArray array = (JsonArray) ele.get("value");
                nbt.put(key, new LongArrayTag((List<Long>) StreamSupport.stream(array.spliterator(), false).map(JsonElement::getAsLong).collect(Collectors.toList())));
            }
        }
        return nbt;
    }

    private static TagType<? extends Tag> stringToType(String type) {
        if (type.equals(IntTag.TYPE.getName())) {
            return IntTag.TYPE;
        } else if (type.equals(ByteTag.TYPE.getName())) {
            return ByteTag.TYPE;
        } else if (type.equals(FloatTag.TYPE.getName())) {
            return FloatTag.TYPE;
        } else if (type.equals(LongTag.TYPE.getName())) {
            return LongTag.TYPE;
        } else if (type.equals(DoubleTag.TYPE.getName())) {
            return DoubleTag.TYPE;
        } else if (type.equals(ShortTag.TYPE.getName())) {
            return ShortTag.TYPE;
        } else if (type.equals(CompoundTag.TYPE.getName())) {
            return CompoundTag.TYPE;
        } else if (type.equals(IntArrayTag.TYPE.getName())) {
            return IntArrayTag.TYPE;
        } else if (type.equals(ByteArrayTag.TYPE.getName())) {
            return ByteArrayTag.TYPE;
        } else {
            return type.equals(LongArrayTag.TYPE.getName()) ? LongArrayTag.TYPE : StringTag.TYPE;
        }
    }

    public static boolean isValidPath(String s) {
        for (int i = 0; i < s.length(); i++) {
            if (!ResourceLocation.validPathChar(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}