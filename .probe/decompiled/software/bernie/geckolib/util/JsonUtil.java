package software.bernie.geckolib.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.loading.json.raw.Bone;
import software.bernie.geckolib.loading.json.raw.Cube;
import software.bernie.geckolib.loading.json.raw.FaceUV;
import software.bernie.geckolib.loading.json.raw.LocatorClass;
import software.bernie.geckolib.loading.json.raw.LocatorValue;
import software.bernie.geckolib.loading.json.raw.MinecraftGeometry;
import software.bernie.geckolib.loading.json.raw.Model;
import software.bernie.geckolib.loading.json.raw.ModelProperties;
import software.bernie.geckolib.loading.json.raw.PolyMesh;
import software.bernie.geckolib.loading.json.raw.PolysUnion;
import software.bernie.geckolib.loading.json.raw.TextureMesh;
import software.bernie.geckolib.loading.json.raw.UVFaces;
import software.bernie.geckolib.loading.json.raw.UVUnion;
import software.bernie.geckolib.loading.json.typeadapter.BakedAnimationsAdapter;
import software.bernie.geckolib.loading.json.typeadapter.KeyFramesAdapter;
import software.bernie.geckolib.loading.object.BakedAnimations;

public final class JsonUtil {

    public static final Gson GEO_GSON = new GsonBuilder().setLenient().registerTypeAdapter(Bone.class, Bone.deserializer()).registerTypeAdapter(Cube.class, Cube.deserializer()).registerTypeAdapter(FaceUV.class, FaceUV.deserializer()).registerTypeAdapter(LocatorClass.class, LocatorClass.deserializer()).registerTypeAdapter(LocatorValue.class, LocatorValue.deserializer()).registerTypeAdapter(MinecraftGeometry.class, MinecraftGeometry.deserializer()).registerTypeAdapter(Model.class, Model.deserializer()).registerTypeAdapter(ModelProperties.class, ModelProperties.deserializer()).registerTypeAdapter(PolyMesh.class, PolyMesh.deserializer()).registerTypeAdapter(PolysUnion.class, PolysUnion.deserializer()).registerTypeAdapter(TextureMesh.class, TextureMesh.deserializer()).registerTypeAdapter(UVFaces.class, UVFaces.deserializer()).registerTypeAdapter(UVUnion.class, UVUnion.deserializer()).registerTypeAdapter(Animation.Keyframes.class, new KeyFramesAdapter()).registerTypeAdapter(BakedAnimations.class, new BakedAnimationsAdapter()).create();

    public static double[] jsonArrayToDoubleArray(@Nullable JsonArray array) throws JsonParseException {
        if (array == null) {
            return new double[3];
        } else {
            double[] output = new double[array.size()];
            for (int i = 0; i < array.size(); i++) {
                output[i] = array.get(i).getAsDouble();
            }
            return output;
        }
    }

    public static <T> T[] jsonArrayToObjectArray(JsonArray array, JsonDeserializationContext context, Class<T> objectClass) {
        T[] objArray = (T[]) Array.newInstance(objectClass, array.size());
        for (int i = 0; i < array.size(); i++) {
            objArray[i] = (T) context.deserialize(array.get(i), objectClass);
        }
        return objArray;
    }

    public static <T> List<T> jsonArrayToList(@Nullable JsonArray array, Function<JsonElement, T> elementTransformer) {
        if (array == null) {
            return new ObjectArrayList();
        } else {
            List<T> list = new ObjectArrayList(array.size());
            for (JsonElement element : array) {
                list.add(elementTransformer.apply(element));
            }
            return list;
        }
    }

    public static <T> Map<String, T> jsonObjToMap(JsonObject obj, JsonDeserializationContext context, Class<T> objectType) {
        Map<String, T> map = new Object2ObjectOpenHashMap(obj.size());
        for (Entry<String, JsonElement> entry : obj.entrySet()) {
            map.put((String) entry.getKey(), context.deserialize((JsonElement) entry.getValue(), objectType));
        }
        return map;
    }

    @Nullable
    public static Long getOptionalLong(JsonObject obj, String elementName) {
        return obj.has(elementName) ? GsonHelper.getAsLong(obj, elementName) : null;
    }

    @Nullable
    public static Boolean getOptionalBoolean(JsonObject obj, String elementName) {
        return obj.has(elementName) ? GsonHelper.getAsBoolean(obj, elementName) : null;
    }

    @Nullable
    public static Float getOptionalFloat(JsonObject obj, String elementName) {
        return obj.has(elementName) ? GsonHelper.getAsFloat(obj, elementName) : null;
    }

    @Nullable
    public static Double getOptionalDouble(JsonObject obj, String elementName) {
        return obj.has(elementName) ? GsonHelper.getAsDouble(obj, elementName) : null;
    }

    @Nullable
    public static Integer getOptionalInteger(JsonObject obj, String elementName) {
        return obj.has(elementName) ? GsonHelper.getAsInt(obj, elementName) : null;
    }
}