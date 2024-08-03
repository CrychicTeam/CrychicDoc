package se.mickelus.tetra.module.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.util.TierHelper;

public class ToolData extends TierData<ToolAction> {

    public static ToolData overwrite(ToolData a, ToolData b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            ToolData result = new ToolData();
            result.levelMap.putAll(a.levelMap);
            result.efficiencyMap.putAll(a.efficiencyMap);
            b.levelMap.forEach(result.levelMap::put);
            b.efficiencyMap.forEach(result.efficiencyMap::put);
            return result;
        }
    }

    public static ToolData merge(Collection<ToolData> data) {
        return (ToolData) data.stream().reduce(null, ToolData::merge);
    }

    public static ToolData merge(ToolData a, ToolData b) {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        } else {
            ToolData result = new ToolData();
            result.levelMap = (Map<ToolAction, Float>) Stream.of(a, b).map(toolData -> toolData.levelMap).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Float::sum));
            result.efficiencyMap = (Map<ToolAction, Float>) Stream.of(a, b).map(toolData -> toolData.efficiencyMap).map(Map::entrySet).flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Float::sum));
            return result;
        }
    }

    public static ToolData multiply(ToolData toolData, float levelMultiplier, float efficiencyMultiplier) {
        return (ToolData) Optional.ofNullable(toolData).map(data -> {
            ToolData result = new ToolData();
            result.levelMap = (Map<ToolAction, Float>) data.levelMap.entrySet().stream().map(entry -> new Pair((ToolAction) entry.getKey(), (Float) entry.getValue() * levelMultiplier)).filter(pair -> (Float) pair.getSecond() != 0.0F).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            result.efficiencyMap = (Map<ToolAction, Float>) data.efficiencyMap.entrySet().stream().map(entry -> new Pair((ToolAction) entry.getKey(), (Float) entry.getValue() * efficiencyMultiplier)).filter(pair -> (Float) pair.getSecond() != 0.0F).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
            return result;
        }).orElse(null);
    }

    public static ToolData offsetLevel(ToolData toolData, float multiplier, int offset) {
        return (ToolData) Optional.ofNullable(toolData).map(data -> {
            ToolData result = new ToolData();
            result.levelMap = (Map<ToolAction, Float>) data.levelMap.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> (Float) entry.getValue() * multiplier + (float) offset));
            result.efficiencyMap = data.efficiencyMap;
            return result;
        }).orElse(null);
    }

    public static ToolData retainMax(Collection<ToolData> dataCollection) {
        ToolData result = new ToolData();
        dataCollection.forEach(data -> data.getValues().forEach(tool -> {
            float newLevel = (Float) data.levelMap.getOrDefault(tool, 0.0F);
            float currentLevel = (Float) result.levelMap.getOrDefault(tool, 0.0F);
            if (newLevel >= (Float) result.levelMap.getOrDefault(tool, 0.0F)) {
                result.levelMap.put(tool, newLevel);
                if (currentLevel < newLevel) {
                    result.efficiencyMap.put(tool, data.getEfficiency(tool));
                } else if (data.getEfficiency(tool) > result.getEfficiency(tool)) {
                    result.efficiencyMap.put(tool, data.getEfficiency(tool));
                }
            }
        }));
        return result;
    }

    public static class Deserializer implements JsonDeserializer<ToolData> {

        private static float getLevel(JsonElement element) {
            return element.getAsJsonPrimitive().isNumber() ? element.getAsFloat() : (float) ((Integer) Optional.ofNullable(TierSortingRegistry.byName(new ResourceLocation(element.getAsString()))).map(TierHelper::getIndex).map(index -> index + 1).orElse(0)).intValue();
        }

        public ToolData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            ToolData data = new ToolData();
            jsonObject.entrySet().forEach(entry -> {
                JsonElement entryValue = (JsonElement) entry.getValue();
                ToolAction toolAction = ToolAction.get((String) entry.getKey());
                if (entryValue.isJsonArray()) {
                    JsonArray entryArray = entryValue.getAsJsonArray();
                    if (entryArray.size() == 2) {
                        data.levelMap.put(toolAction, getLevel(entryArray.get(0)));
                        data.efficiencyMap.put(toolAction, entryArray.get(1).getAsFloat());
                    }
                } else {
                    data.levelMap.put(toolAction, getLevel(entryValue));
                }
            });
            return data;
        }
    }
}