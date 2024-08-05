package pm.meh.icterine.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import pm.meh.icterine.Common;

public class ReloadListenerHandlerBase extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().create();

    private static final String FOLDER = "advancements";

    public ReloadListenerHandlerBase() {
        super(GSON, "advancements");
    }

    protected void apply(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        if (Common.config.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS) {
            StackSizeThresholdManager.clear();
            String inventoryChangedTriggerId = CriteriaTriggers.INVENTORY_CHANGED.getId().toString();
            for (JsonElement advancementElement : object.values()) {
                JsonObject advancementCriteria = advancementElement.getAsJsonObject().getAsJsonObject("criteria");
                if (advancementCriteria != null && !advancementCriteria.isJsonNull()) {
                    for (Entry<String, JsonElement> criterionEntry : advancementCriteria.entrySet()) {
                        JsonObject criterion = ((JsonElement) criterionEntry.getValue()).getAsJsonObject();
                        JsonElement criterionTrigger = criterion.get("trigger");
                        JsonObject criterionConditions = criterion.getAsJsonObject("conditions");
                        if (criterionTrigger != null && criterionConditions != null && !criterionTrigger.isJsonNull() && !criterionConditions.isJsonNull() && criterionTrigger.getAsString().equals(inventoryChangedTriggerId) && criterionConditions.has("items")) {
                            for (JsonElement itemElement : criterionConditions.getAsJsonArray("items")) {
                                JsonElement itemCount = itemElement.getAsJsonObject().get("count");
                                if (itemCount != null && !itemCount.isJsonNull()) {
                                    int itemCountMinValue = 0;
                                    if (itemCount.isJsonObject()) {
                                        JsonElement itemCountMin = itemCount.getAsJsonObject().get("min");
                                        if (itemCountMin != null && !itemCountMin.isJsonNull()) {
                                            itemCountMinValue = itemCountMin.getAsInt();
                                        }
                                    } else {
                                        itemCountMinValue = itemCount.getAsInt();
                                    }
                                    if (itemCountMinValue > 1) {
                                        StackSizeThresholdManager.add(itemCountMinValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            StackSizeThresholdManager.debugPrint();
        }
    }
}