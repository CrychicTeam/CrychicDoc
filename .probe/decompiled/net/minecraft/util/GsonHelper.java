package net.minecraft.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Contract;

public class GsonHelper {

    private static final Gson GSON = new GsonBuilder().create();

    public static boolean isStringValue(JsonObject jsonObject0, String string1) {
        return !isValidPrimitive(jsonObject0, string1) ? false : jsonObject0.getAsJsonPrimitive(string1).isString();
    }

    public static boolean isStringValue(JsonElement jsonElement0) {
        return !jsonElement0.isJsonPrimitive() ? false : jsonElement0.getAsJsonPrimitive().isString();
    }

    public static boolean isNumberValue(JsonObject jsonObject0, String string1) {
        return !isValidPrimitive(jsonObject0, string1) ? false : jsonObject0.getAsJsonPrimitive(string1).isNumber();
    }

    public static boolean isNumberValue(JsonElement jsonElement0) {
        return !jsonElement0.isJsonPrimitive() ? false : jsonElement0.getAsJsonPrimitive().isNumber();
    }

    public static boolean isBooleanValue(JsonObject jsonObject0, String string1) {
        return !isValidPrimitive(jsonObject0, string1) ? false : jsonObject0.getAsJsonPrimitive(string1).isBoolean();
    }

    public static boolean isBooleanValue(JsonElement jsonElement0) {
        return !jsonElement0.isJsonPrimitive() ? false : jsonElement0.getAsJsonPrimitive().isBoolean();
    }

    public static boolean isArrayNode(JsonObject jsonObject0, String string1) {
        return !isValidNode(jsonObject0, string1) ? false : jsonObject0.get(string1).isJsonArray();
    }

    public static boolean isObjectNode(JsonObject jsonObject0, String string1) {
        return !isValidNode(jsonObject0, string1) ? false : jsonObject0.get(string1).isJsonObject();
    }

    public static boolean isValidPrimitive(JsonObject jsonObject0, String string1) {
        return !isValidNode(jsonObject0, string1) ? false : jsonObject0.get(string1).isJsonPrimitive();
    }

    public static boolean isValidNode(@Nullable JsonObject jsonObject0, String string1) {
        return jsonObject0 == null ? false : jsonObject0.get(string1) != null;
    }

    public static JsonElement getNonNull(JsonObject jsonObject0, String string1) {
        JsonElement $$2 = jsonObject0.get(string1);
        if ($$2 != null && !$$2.isJsonNull()) {
            return $$2;
        } else {
            throw new JsonSyntaxException("Missing field " + string1);
        }
    }

    public static String convertToString(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive()) {
            return jsonElement0.getAsString();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a string, was " + getType(jsonElement0));
        }
    }

    public static String getAsString(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToString(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a string");
        }
    }

    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static String getAsString(JsonObject jsonObject0, String string1, @Nullable String string2) {
        return jsonObject0.has(string1) ? convertToString(jsonObject0.get(string1), string1) : string2;
    }

    public static Item convertToItem(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive()) {
            String $$2 = jsonElement0.getAsString();
            return (Item) BuiltInRegistries.ITEM.m_6612_(new ResourceLocation($$2)).orElseThrow(() -> new JsonSyntaxException("Expected " + string1 + " to be an item, was unknown string '" + $$2 + "'"));
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be an item, was " + getType(jsonElement0));
        }
    }

    public static Item getAsItem(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToItem(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find an item");
        }
    }

    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static Item getAsItem(JsonObject jsonObject0, String string1, @Nullable Item item2) {
        return jsonObject0.has(string1) ? convertToItem(jsonObject0.get(string1), string1) : item2;
    }

    public static boolean convertToBoolean(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive()) {
            return jsonElement0.getAsBoolean();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Boolean, was " + getType(jsonElement0));
        }
    }

    public static boolean getAsBoolean(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToBoolean(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Boolean");
        }
    }

    public static boolean getAsBoolean(JsonObject jsonObject0, String string1, boolean boolean2) {
        return jsonObject0.has(string1) ? convertToBoolean(jsonObject0.get(string1), string1) : boolean2;
    }

    public static double convertToDouble(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsDouble();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Double, was " + getType(jsonElement0));
        }
    }

    public static double getAsDouble(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToDouble(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Double");
        }
    }

    public static double getAsDouble(JsonObject jsonObject0, String string1, double double2) {
        return jsonObject0.has(string1) ? convertToDouble(jsonObject0.get(string1), string1) : double2;
    }

    public static float convertToFloat(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsFloat();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Float, was " + getType(jsonElement0));
        }
    }

    public static float getAsFloat(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToFloat(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Float");
        }
    }

    public static float getAsFloat(JsonObject jsonObject0, String string1, float float2) {
        return jsonObject0.has(string1) ? convertToFloat(jsonObject0.get(string1), string1) : float2;
    }

    public static long convertToLong(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsLong();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Long, was " + getType(jsonElement0));
        }
    }

    public static long getAsLong(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToLong(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Long");
        }
    }

    public static long getAsLong(JsonObject jsonObject0, String string1, long long2) {
        return jsonObject0.has(string1) ? convertToLong(jsonObject0.get(string1), string1) : long2;
    }

    public static int convertToInt(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsInt();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Int, was " + getType(jsonElement0));
        }
    }

    public static int getAsInt(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToInt(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Int");
        }
    }

    public static int getAsInt(JsonObject jsonObject0, String string1, int int2) {
        return jsonObject0.has(string1) ? convertToInt(jsonObject0.get(string1), string1) : int2;
    }

    public static byte convertToByte(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsByte();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Byte, was " + getType(jsonElement0));
        }
    }

    public static byte getAsByte(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToByte(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Byte");
        }
    }

    public static byte getAsByte(JsonObject jsonObject0, String string1, byte byte2) {
        return jsonObject0.has(string1) ? convertToByte(jsonObject0.get(string1), string1) : byte2;
    }

    public static char convertToCharacter(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsCharacter();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Character, was " + getType(jsonElement0));
        }
    }

    public static char getAsCharacter(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToCharacter(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Character");
        }
    }

    public static char getAsCharacter(JsonObject jsonObject0, String string1, char char2) {
        return jsonObject0.has(string1) ? convertToCharacter(jsonObject0.get(string1), string1) : char2;
    }

    public static BigDecimal convertToBigDecimal(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsBigDecimal();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a BigDecimal, was " + getType(jsonElement0));
        }
    }

    public static BigDecimal getAsBigDecimal(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToBigDecimal(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a BigDecimal");
        }
    }

    public static BigDecimal getAsBigDecimal(JsonObject jsonObject0, String string1, BigDecimal bigDecimal2) {
        return jsonObject0.has(string1) ? convertToBigDecimal(jsonObject0.get(string1), string1) : bigDecimal2;
    }

    public static BigInteger convertToBigInteger(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsBigInteger();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a BigInteger, was " + getType(jsonElement0));
        }
    }

    public static BigInteger getAsBigInteger(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToBigInteger(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a BigInteger");
        }
    }

    public static BigInteger getAsBigInteger(JsonObject jsonObject0, String string1, BigInteger bigInteger2) {
        return jsonObject0.has(string1) ? convertToBigInteger(jsonObject0.get(string1), string1) : bigInteger2;
    }

    public static short convertToShort(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonPrimitive() && jsonElement0.getAsJsonPrimitive().isNumber()) {
            return jsonElement0.getAsShort();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a Short, was " + getType(jsonElement0));
        }
    }

    public static short getAsShort(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToShort(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a Short");
        }
    }

    public static short getAsShort(JsonObject jsonObject0, String string1, short short2) {
        return jsonObject0.has(string1) ? convertToShort(jsonObject0.get(string1), string1) : short2;
    }

    public static JsonObject convertToJsonObject(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonObject()) {
            return jsonElement0.getAsJsonObject();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a JsonObject, was " + getType(jsonElement0));
        }
    }

    public static JsonObject getAsJsonObject(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToJsonObject(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a JsonObject");
        }
    }

    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static JsonObject getAsJsonObject(JsonObject jsonObject0, String string1, @Nullable JsonObject jsonObject2) {
        return jsonObject0.has(string1) ? convertToJsonObject(jsonObject0.get(string1), string1) : jsonObject2;
    }

    public static JsonArray convertToJsonArray(JsonElement jsonElement0, String string1) {
        if (jsonElement0.isJsonArray()) {
            return jsonElement0.getAsJsonArray();
        } else {
            throw new JsonSyntaxException("Expected " + string1 + " to be a JsonArray, was " + getType(jsonElement0));
        }
    }

    public static JsonArray getAsJsonArray(JsonObject jsonObject0, String string1) {
        if (jsonObject0.has(string1)) {
            return convertToJsonArray(jsonObject0.get(string1), string1);
        } else {
            throw new JsonSyntaxException("Missing " + string1 + ", expected to find a JsonArray");
        }
    }

    @Nullable
    @Contract("_,_,!null->!null;_,_,null->_")
    public static JsonArray getAsJsonArray(JsonObject jsonObject0, String string1, @Nullable JsonArray jsonArray2) {
        return jsonObject0.has(string1) ? convertToJsonArray(jsonObject0.get(string1), string1) : jsonArray2;
    }

    public static <T> T convertToObject(@Nullable JsonElement jsonElement0, String string1, JsonDeserializationContext jsonDeserializationContext2, Class<? extends T> classExtendsT3) {
        if (jsonElement0 != null) {
            return (T) jsonDeserializationContext2.deserialize(jsonElement0, classExtendsT3);
        } else {
            throw new JsonSyntaxException("Missing " + string1);
        }
    }

    public static <T> T getAsObject(JsonObject jsonObject0, String string1, JsonDeserializationContext jsonDeserializationContext2, Class<? extends T> classExtendsT3) {
        if (jsonObject0.has(string1)) {
            return convertToObject(jsonObject0.get(string1), string1, jsonDeserializationContext2, classExtendsT3);
        } else {
            throw new JsonSyntaxException("Missing " + string1);
        }
    }

    @Nullable
    @Contract("_,_,!null,_,_->!null;_,_,null,_,_->_")
    public static <T> T getAsObject(JsonObject jsonObject0, String string1, @Nullable T t2, JsonDeserializationContext jsonDeserializationContext3, Class<? extends T> classExtendsT4) {
        return jsonObject0.has(string1) ? convertToObject(jsonObject0.get(string1), string1, jsonDeserializationContext3, classExtendsT4) : t2;
    }

    public static String getType(@Nullable JsonElement jsonElement0) {
        String $$1 = StringUtils.abbreviateMiddle(String.valueOf(jsonElement0), "...", 10);
        if (jsonElement0 == null) {
            return "null (missing)";
        } else if (jsonElement0.isJsonNull()) {
            return "null (json)";
        } else if (jsonElement0.isJsonArray()) {
            return "an array (" + $$1 + ")";
        } else if (jsonElement0.isJsonObject()) {
            return "an object (" + $$1 + ")";
        } else {
            if (jsonElement0.isJsonPrimitive()) {
                JsonPrimitive $$2 = jsonElement0.getAsJsonPrimitive();
                if ($$2.isNumber()) {
                    return "a number (" + $$1 + ")";
                }
                if ($$2.isBoolean()) {
                    return "a boolean (" + $$1 + ")";
                }
            }
            return $$1;
        }
    }

    @Nullable
    public static <T> T fromNullableJson(Gson gson0, Reader reader1, Class<T> classT2, boolean boolean3) {
        try {
            JsonReader $$4 = new JsonReader(reader1);
            $$4.setLenient(boolean3);
            return (T) gson0.getAdapter(classT2).read($$4);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }

    public static <T> T fromJson(Gson gson0, Reader reader1, Class<T> classT2, boolean boolean3) {
        T $$4 = fromNullableJson(gson0, reader1, classT2, boolean3);
        if ($$4 == null) {
            throw new JsonParseException("JSON data was null or empty");
        } else {
            return $$4;
        }
    }

    @Nullable
    public static <T> T fromNullableJson(Gson gson0, Reader reader1, TypeToken<T> typeTokenT2, boolean boolean3) {
        try {
            JsonReader $$4 = new JsonReader(reader1);
            $$4.setLenient(boolean3);
            return (T) gson0.getAdapter(typeTokenT2).read($$4);
        } catch (IOException var5) {
            throw new JsonParseException(var5);
        }
    }

    public static <T> T fromJson(Gson gson0, Reader reader1, TypeToken<T> typeTokenT2, boolean boolean3) {
        T $$4 = fromNullableJson(gson0, reader1, typeTokenT2, boolean3);
        if ($$4 == null) {
            throw new JsonParseException("JSON data was null or empty");
        } else {
            return $$4;
        }
    }

    @Nullable
    public static <T> T fromNullableJson(Gson gson0, String string1, TypeToken<T> typeTokenT2, boolean boolean3) {
        return fromNullableJson(gson0, new StringReader(string1), typeTokenT2, boolean3);
    }

    public static <T> T fromJson(Gson gson0, String string1, Class<T> classT2, boolean boolean3) {
        return fromJson(gson0, new StringReader(string1), classT2, boolean3);
    }

    @Nullable
    public static <T> T fromNullableJson(Gson gson0, String string1, Class<T> classT2, boolean boolean3) {
        return fromNullableJson(gson0, new StringReader(string1), classT2, boolean3);
    }

    public static <T> T fromJson(Gson gson0, Reader reader1, TypeToken<T> typeTokenT2) {
        return fromJson(gson0, reader1, typeTokenT2, false);
    }

    @Nullable
    public static <T> T fromNullableJson(Gson gson0, String string1, TypeToken<T> typeTokenT2) {
        return fromNullableJson(gson0, string1, typeTokenT2, false);
    }

    public static <T> T fromJson(Gson gson0, Reader reader1, Class<T> classT2) {
        return fromJson(gson0, reader1, classT2, false);
    }

    public static <T> T fromJson(Gson gson0, String string1, Class<T> classT2) {
        return fromJson(gson0, string1, classT2, false);
    }

    public static JsonObject parse(String string0, boolean boolean1) {
        return parse(new StringReader(string0), boolean1);
    }

    public static JsonObject parse(Reader reader0, boolean boolean1) {
        return fromJson(GSON, reader0, JsonObject.class, boolean1);
    }

    public static JsonObject parse(String string0) {
        return parse(string0, false);
    }

    public static JsonObject parse(Reader reader0) {
        return parse(reader0, false);
    }

    public static JsonArray parseArray(String string0) {
        return parseArray(new StringReader(string0));
    }

    public static JsonArray parseArray(Reader reader0) {
        return fromJson(GSON, reader0, JsonArray.class, false);
    }

    public static String toStableString(JsonElement jsonElement0) {
        StringWriter $$1 = new StringWriter();
        JsonWriter $$2 = new JsonWriter($$1);
        try {
            writeValue($$2, jsonElement0, Comparator.naturalOrder());
        } catch (IOException var4) {
            throw new AssertionError(var4);
        }
        return $$1.toString();
    }

    public static void writeValue(JsonWriter jsonWriter0, @Nullable JsonElement jsonElement1, @Nullable Comparator<String> comparatorString2) throws IOException {
        if (jsonElement1 == null || jsonElement1.isJsonNull()) {
            jsonWriter0.nullValue();
        } else if (jsonElement1.isJsonPrimitive()) {
            JsonPrimitive $$3 = jsonElement1.getAsJsonPrimitive();
            if ($$3.isNumber()) {
                jsonWriter0.value($$3.getAsNumber());
            } else if ($$3.isBoolean()) {
                jsonWriter0.value($$3.getAsBoolean());
            } else {
                jsonWriter0.value($$3.getAsString());
            }
        } else if (jsonElement1.isJsonArray()) {
            jsonWriter0.beginArray();
            for (JsonElement $$4 : jsonElement1.getAsJsonArray()) {
                writeValue(jsonWriter0, $$4, comparatorString2);
            }
            jsonWriter0.endArray();
        } else {
            if (!jsonElement1.isJsonObject()) {
                throw new IllegalArgumentException("Couldn't write " + jsonElement1.getClass());
            }
            jsonWriter0.beginObject();
            for (Entry<String, JsonElement> $$5 : sortByKeyIfNeeded(jsonElement1.getAsJsonObject().entrySet(), comparatorString2)) {
                jsonWriter0.name((String) $$5.getKey());
                writeValue(jsonWriter0, (JsonElement) $$5.getValue(), comparatorString2);
            }
            jsonWriter0.endObject();
        }
    }

    private static Collection<Entry<String, JsonElement>> sortByKeyIfNeeded(Collection<Entry<String, JsonElement>> collectionEntryStringJsonElement0, @Nullable Comparator<String> comparatorString1) {
        if (comparatorString1 == null) {
            return collectionEntryStringJsonElement0;
        } else {
            List<Entry<String, JsonElement>> $$2 = new ArrayList(collectionEntryStringJsonElement0);
            $$2.sort(Entry.comparingByKey(comparatorString1));
            return $$2;
        }
    }
}