package journeymap.common.properties.config;

import com.google.common.base.Joiner;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.awt.Color;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import journeymap.client.cartography.color.RGB;
import journeymap.client.model.GridSpec;
import journeymap.common.Journeymap;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.catagory.CategorySet;
import journeymap.common.version.Version;

public abstract class GsonHelper<T extends ConfigField> {

    protected final boolean verbose;

    protected final boolean saveType;

    public GsonHelper(Boolean verbose) {
        this(verbose, false);
    }

    public GsonHelper(Boolean verbose, Boolean saveType) {
        this.saveType = saveType;
        this.verbose = verbose;
    }

    public JsonElement serializeAttributes(ConfigField<?> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if (!this.verbose && !this.saveType) {
            return context.serialize(src.getStringAttr("value"));
        } else {
            if (this.saveType) {
                jsonObject.addProperty("value", src.getStringAttr("value"));
                jsonObject.addProperty("type", src.getStringAttr("type"));
            } else {
                for (String attrName : src.getAttributeNames()) {
                    jsonObject.addProperty(attrName, src.getStringAttr(attrName));
                }
            }
            return jsonObject;
        }
    }

    protected T deserializeAttributes(T result, JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if ((!this.verbose || !json.isJsonObject()) && !this.saveType) {
            result.put("value", json.getAsString());
        } else {
            for (Entry<String, JsonElement> entry : json.getAsJsonObject().entrySet()) {
                try {
                    result.put((String) entry.getKey(), ((JsonElement) entry.getValue()).getAsString());
                } catch (Throwable var9) {
                    Journeymap.getLogger().warn("Error deserializing %s in %s: %s", entry, json, var9);
                }
            }
        }
        return result;
    }

    public static class BooleanFieldSerializer extends GsonHelper<BooleanField> implements JsonSerializer<BooleanField>, JsonDeserializer<BooleanField> {

        public BooleanFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(BooleanField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public BooleanField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new BooleanField(), json, typeOfT, context);
        }
    }

    public static class CategorySetSerializer implements JsonSerializer<CategorySet>, JsonDeserializer<CategorySet> {

        protected final boolean verbose;

        public CategorySetSerializer(boolean verbose) {
            this.verbose = verbose;
        }

        public JsonElement serialize(CategorySet src, Type typeOfSrc, JsonSerializationContext context) {
            if (!this.verbose) {
                return null;
            } else {
                Category[] array = new Category[src.size()];
                return context.serialize(src.toArray(array));
            }
        }

        public CategorySet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            CategorySet categorySet = new CategorySet();
            if (this.verbose) {
                for (JsonElement jsonElement : json.getAsJsonArray()) {
                    categorySet.add((Category) context.deserialize(jsonElement, Category.class));
                }
            }
            return categorySet;
        }
    }

    public static class EnumFieldSerializer extends GsonHelper<EnumField> implements JsonSerializer<EnumField>, JsonDeserializer<EnumField> {

        public EnumFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(EnumField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public EnumField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new EnumField(), json, typeOfT, context);
        }
    }

    public static class FloatFieldSerializer extends GsonHelper<FloatField> implements JsonSerializer<FloatField>, JsonDeserializer<FloatField> {

        public FloatFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(FloatField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public FloatField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new FloatField(), json, typeOfT, context);
        }
    }

    public static class GridSpecSerializer implements JsonSerializer<GridSpec>, JsonDeserializer<GridSpec> {

        public GridSpecSerializer(boolean verbose) {
        }

        public JsonElement serialize(GridSpec src, Type typeOfSrc, JsonSerializationContext context) {
            String string = Joiner.on(",").join(src.style, RGB.toHexString(src.getColor()), new Object[] { src.alpha, src.getColorX(), src.getColorY() });
            return context.serialize(string);
        }

        public GridSpec deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                GridSpec gridSpec = new GridSpec((GridSpec.Style) GridSpec.Style.valueOf(GridSpec.Style.class, jo.get("style").getAsString()), jo.get("red").getAsFloat(), jo.get("green").getAsFloat(), jo.get("blue").getAsFloat(), jo.get("alpha").getAsFloat());
                gridSpec.setColorCoords(jo.get("colorX").getAsInt(), jo.get("colorY").getAsInt());
                return gridSpec;
            } else {
                String[] parts = json.getAsString().split(",");
                GridSpec gridSpec = new GridSpec((GridSpec.Style) GridSpec.Style.valueOf(GridSpec.Style.class, parts[0]), new Color(RGB.hexToInt(parts[1])), Float.parseFloat(parts[2]));
                gridSpec.setColorCoords(Integer.parseInt(parts[3]), Integer.parseInt(parts[4]));
                return gridSpec;
            }
        }
    }

    public static class IntegerFieldSerializer extends GsonHelper<IntegerField> implements JsonSerializer<IntegerField>, JsonDeserializer<IntegerField> {

        public IntegerFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(IntegerField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public IntegerField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new IntegerField(), json, typeOfT, context);
        }
    }

    public static class MapFieldSerializer extends GsonHelper<ConfigField> implements JsonSerializer<Map<String, ConfigField>>, JsonDeserializer<Map<String, ConfigField>> {

        public MapFieldSerializer(boolean verbose, boolean saveType) {
            super(verbose, saveType);
        }

        public Map<String, ConfigField> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Map<String, ConfigField> map = new HashMap();
            jsonObject.keySet().forEach(key -> {
                JsonObject element = jsonObject.get(key).getAsJsonObject();
                String type = element.get("type").getAsString();
                switch(type) {
                    case "BooleanField":
                        map.put(key, this.deserializeAttributes(new BooleanField(), element, typeOfT, context));
                        break;
                    case "FloatField":
                        map.put(key, this.deserializeAttributes(new FloatField(), element, typeOfT, context));
                        break;
                    case "IntegerField":
                        map.put(key, this.deserializeAttributes(new IntegerField(), element, typeOfT, context));
                        break;
                    case "StringField":
                        map.put(key, this.deserializeAttributes(new StringField(), element, typeOfT, context));
                        break;
                    case "CustomField":
                        map.put(key, this.deserializeAttributes(new CustomField(), element, typeOfT, context));
                        break;
                    case "EnumField":
                        map.put(key, this.deserializeAttributes(new EnumField(), element, typeOfT, context));
                }
            });
            return map;
        }

        public JsonElement serialize(Map<String, ConfigField> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            src.forEach((name, field) -> jsonObject.add(name, this.serializeAttributes(field, typeOfSrc, context)));
            return jsonObject;
        }
    }

    public static class StringFieldSerializer extends GsonHelper<StringField> implements JsonSerializer<StringField>, JsonDeserializer<StringField> {

        public StringFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(StringField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public StringField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new StringField(), json, typeOfT, context);
        }
    }

    public static class TextFieldSerializer extends GsonHelper<CustomField> implements JsonSerializer<CustomField>, JsonDeserializer<CustomField> {

        public TextFieldSerializer(boolean verbose) {
            super(verbose);
        }

        public JsonElement serialize(CustomField src, Type typeOfSrc, JsonSerializationContext context) {
            return this.serializeAttributes(src, typeOfSrc, context);
        }

        public CustomField deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return this.deserializeAttributes(new CustomField(), json, typeOfT, context);
        }
    }

    public static class VersionSerializer implements JsonSerializer<Version>, JsonDeserializer<Version> {

        public VersionSerializer(boolean verbose) {
        }

        public JsonElement serialize(Version src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString());
        }

        public Version deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jo = json.getAsJsonObject();
                return Version.from(jo.get("major").getAsString(), jo.get("minor").getAsString(), jo.get("micro").getAsString(), jo.get("patch").getAsString(), Journeymap.JM_VERSION);
            } else {
                return Version.from(json.getAsString(), Journeymap.JM_VERSION);
            }
        }
    }
}