package net.minecraft.advancements.critereon;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class DeserializationContext {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation id;

    private final LootDataManager lootData;

    private final Gson predicateGson = Deserializers.createConditionSerializer().create();

    public DeserializationContext(ResourceLocation resourceLocation0, LootDataManager lootDataManager1) {
        this.id = resourceLocation0;
        this.lootData = lootDataManager1;
    }

    public final LootItemCondition[] deserializeConditions(JsonArray jsonArray0, String string1, LootContextParamSet lootContextParamSet2) {
        LootItemCondition[] $$3 = (LootItemCondition[]) this.predicateGson.fromJson(jsonArray0, LootItemCondition[].class);
        ValidationContext $$4 = new ValidationContext(lootContextParamSet2, this.lootData);
        for (LootItemCondition $$5 : $$3) {
            $$5.m_6169_($$4);
            $$4.getProblems().forEach((p_25880_, p_25881_) -> LOGGER.warn("Found validation problem in advancement trigger {}/{}: {}", new Object[] { string1, p_25880_, p_25881_ }));
        }
        return $$3;
    }

    public ResourceLocation getAdvancementId() {
        return this.id;
    }
}