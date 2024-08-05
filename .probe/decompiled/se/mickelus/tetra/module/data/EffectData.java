package se.mickelus.tetra.module.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.effect.ItemEffect;

@ParametersAreNonnullByDefault
public class EffectData extends TierData<ItemEffect> {

    public static EffectData overwrite(EffectData a, EffectData b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            EffectData result = new EffectData();
            result.levelMap.putAll(a.levelMap);
            result.efficiencyMap.putAll(a.efficiencyMap);
            b.levelMap.forEach(result.levelMap::put);
            b.efficiencyMap.forEach(result.efficiencyMap::put);
            return result;
        }
    }

    public static EffectData merge(Collection<EffectData> data) {
        return (EffectData) data.stream().reduce(null, EffectData::merge);
    }

    public static EffectData merge(EffectData a, EffectData b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            EffectData result = new EffectData();
            result.levelMap = (Map<ItemEffect, Float>) Stream.of(a, b).map(toolData -> toolData.levelMap).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Float::sum));
            result.efficiencyMap = (Map<ItemEffect, Float>) Stream.of(a, b).map(toolData -> toolData.efficiencyMap).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Float::sum));
            return result;
        }
    }

    public static EffectData multiply(EffectData effectData, float levelMultiplier, float efficiencyMultiplier) {
        return (EffectData) Optional.ofNullable(effectData).map(data -> {
            EffectData result = new EffectData();
            result.levelMap = (Map<ItemEffect, Float>) data.levelMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> (Float) entry.getValue() * levelMultiplier));
            result.efficiencyMap = (Map<ItemEffect, Float>) data.efficiencyMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> (Float) entry.getValue() * efficiencyMultiplier));
            return result;
        }).orElse(null);
    }

    public static class Deserializer implements JsonDeserializer<EffectData> {

        public EffectData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            EffectData data = new EffectData();
            jsonObject.entrySet().forEach(entry -> {
                JsonElement entryValue = (JsonElement) entry.getValue();
                ItemEffect effect = ItemEffect.get((String) entry.getKey());
                if (entryValue.isJsonArray()) {
                    JsonArray entryArray = entryValue.getAsJsonArray();
                    if (entryArray.size() == 2) {
                        data.levelMap.put(effect, entryArray.get(0).getAsFloat());
                        data.efficiencyMap.put(effect, entryArray.get(1).getAsFloat());
                    }
                } else {
                    data.levelMap.put(effect, entryValue.getAsFloat());
                }
            });
            return data;
        }
    }
}