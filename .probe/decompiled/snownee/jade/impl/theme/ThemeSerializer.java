package snownee.jade.impl.theme;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import snownee.jade.api.theme.Theme;
import snownee.jade.util.Color;

public class ThemeSerializer implements JsonDeserializer<Theme> {

    private static int readColor(JsonElement el, int fallback) {
        if (el != null && !el.isJsonNull()) {
            JsonPrimitive e = el.getAsJsonPrimitive();
            if (e.isString()) {
                try {
                    return Color.valueOf(e.getAsString()).toInt();
                } catch (Throwable var4) {
                    return 0;
                }
            } else {
                return e.getAsInt();
            }
        } else {
            return fallback;
        }
    }

    private static void readImage(JsonObject o, String imageKey, Consumer<ResourceLocation> imageConsumer, Consumer<int[]> uvConsumer) {
        if (o.has(imageKey)) {
            JsonArray array = o.getAsJsonArray(imageKey);
            Preconditions.checkArgument(array.size() == 9, imageKey + " must have 9 elements");
            imageConsumer.accept(new ResourceLocation(array.get(0).getAsString()));
            int[] uv = new int[8];
            for (int i = 0; i < 8; i++) {
                uv[i] = array.get(i + 1).getAsInt();
            }
            uvConsumer.accept(uv);
        }
    }

    public Theme deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject o = json.getAsJsonObject();
        if (GsonHelper.getAsInt(o, "version", 0) > 1) {
            throw new JsonParseException("Unsupported theme version");
        } else {
            Theme theme = new Theme();
            if (o.has("backgroundImage")) {
                readImage(o, "backgroundImage", $ -> theme.backgroundTexture = $, $ -> theme.backgroundTextureUV = $);
                readImage(o, "backgroundImage_withIcon", $ -> theme.backgroundTexture_withIcon = $, $ -> theme.backgroundTextureUV_withIcon = $);
            } else {
                theme.backgroundColor = readColor(o.get("backgroundColor"), theme.backgroundColor);
                JsonArray array = o.get("borderColor").getAsJsonArray();
                Preconditions.checkArgument(array.size() == 4, "borderColor must have 4 elements");
                for (int i = 0; i < 4; i++) {
                    theme.borderColor[i] = readColor(array.get(i), theme.borderColor[i]);
                }
            }
            theme.titleColor = readColor(o.get("titleColor"), theme.titleColor);
            theme.normalColor = readColor(o.get("normalColor"), theme.normalColor);
            theme.infoColor = readColor(o.get("infoColor"), theme.infoColor);
            theme.successColor = readColor(o.get("successColor"), theme.successColor);
            theme.warningColor = readColor(o.get("warningColor"), theme.warningColor);
            theme.dangerColor = readColor(o.get("dangerColor"), theme.dangerColor);
            theme.failureColor = readColor(o.get("failureColor"), theme.failureColor);
            theme.boxBorderColor = readColor(o.get("boxBorderColor"), theme.boxBorderColor);
            theme.itemAmountColor = readColor(o.get("itemAmountColor"), theme.itemAmountColor);
            theme.bottomProgressNormalColor = readColor(o.get("bottomProgressNormalColor"), theme.bottomProgressNormalColor);
            theme.bottomProgressFailureColor = readColor(o.get("bottomProgressFailureColor"), theme.bottomProgressFailureColor);
            theme.textShadow = GsonHelper.getAsBoolean(o, "textShadow", true);
            if (o.has("padding")) {
                JsonArray array = o.getAsJsonArray("padding");
                Preconditions.checkArgument(array.size() == 4, "padding must have 4 elements");
                for (int i = 0; i < 4; i++) {
                    theme.padding[i] = array.get(i).getAsInt();
                }
            }
            if (o.has("squareBorder")) {
                theme.squareBorder = o.get("squareBorder").getAsBoolean();
            }
            theme.opacity = GsonHelper.getAsFloat(o, "opacity", 0.0F);
            if (o.has("bottomProgressOffset")) {
                JsonArray array = o.getAsJsonArray("bottomProgressOffset");
                Preconditions.checkArgument(array.size() == 4, "bottomProgressOffset must have 4 elements");
                theme.bottomProgressOffset = new int[4];
                for (int i = 0; i < 4; i++) {
                    theme.bottomProgressOffset[i] = array.get(i).getAsInt();
                }
            }
            theme.lightColorScheme = GsonHelper.getAsBoolean(o, "lightColorScheme", false);
            theme.hidden = GsonHelper.getAsBoolean(o, "hidden", false);
            return theme;
        }
    }
}