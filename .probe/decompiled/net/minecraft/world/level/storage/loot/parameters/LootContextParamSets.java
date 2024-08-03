package net.minecraft.world.level.storage.loot.parameters;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class LootContextParamSets {

    private static final BiMap<ResourceLocation, LootContextParamSet> REGISTRY = HashBiMap.create();

    public static final LootContextParamSet EMPTY = register("empty", p_81454_ -> {
    });

    public static final LootContextParamSet CHEST = register("chest", p_81452_ -> p_81452_.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet COMMAND = register("command", p_81450_ -> p_81450_.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet SELECTOR = register("selector", p_81448_ -> p_81448_.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet FISHING = register("fishing", p_81446_ -> p_81446_.required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet ENTITY = register("entity", p_81444_ -> p_81444_.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.DAMAGE_SOURCE).optional(LootContextParams.KILLER_ENTITY).optional(LootContextParams.DIRECT_KILLER_ENTITY).optional(LootContextParams.LAST_DAMAGE_PLAYER));

    public static final LootContextParamSet ARCHAEOLOGY = register("archaeology", p_272589_ -> p_272589_.required(LootContextParams.ORIGIN).optional(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet GIFT = register("gift", p_81442_ -> p_81442_.required(LootContextParams.ORIGIN).required(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet PIGLIN_BARTER = register("barter", p_81440_ -> p_81440_.required(LootContextParams.THIS_ENTITY));

    public static final LootContextParamSet ADVANCEMENT_REWARD = register("advancement_reward", p_81436_ -> p_81436_.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));

    public static final LootContextParamSet ADVANCEMENT_ENTITY = register("advancement_entity", p_81438_ -> p_81438_.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN));

    public static final LootContextParamSet ADVANCEMENT_LOCATION = register("advancement_location", p_286218_ -> p_286218_.required(LootContextParams.THIS_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).required(LootContextParams.BLOCK_STATE));

    public static final LootContextParamSet ALL_PARAMS = register("generic", p_81434_ -> p_81434_.required(LootContextParams.THIS_ENTITY).required(LootContextParams.LAST_DAMAGE_PLAYER).required(LootContextParams.DAMAGE_SOURCE).required(LootContextParams.KILLER_ENTITY).required(LootContextParams.DIRECT_KILLER_ENTITY).required(LootContextParams.ORIGIN).required(LootContextParams.BLOCK_STATE).required(LootContextParams.BLOCK_ENTITY).required(LootContextParams.TOOL).required(LootContextParams.EXPLOSION_RADIUS));

    public static final LootContextParamSet BLOCK = register("block", p_81425_ -> p_81425_.required(LootContextParams.BLOCK_STATE).required(LootContextParams.ORIGIN).required(LootContextParams.TOOL).optional(LootContextParams.THIS_ENTITY).optional(LootContextParams.BLOCK_ENTITY).optional(LootContextParams.EXPLOSION_RADIUS));

    private static LootContextParamSet register(String string0, Consumer<LootContextParamSet.Builder> consumerLootContextParamSetBuilder1) {
        LootContextParamSet.Builder $$2 = new LootContextParamSet.Builder();
        consumerLootContextParamSetBuilder1.accept($$2);
        LootContextParamSet $$3 = $$2.build();
        ResourceLocation $$4 = new ResourceLocation(string0);
        LootContextParamSet $$5 = (LootContextParamSet) REGISTRY.put($$4, $$3);
        if ($$5 != null) {
            throw new IllegalStateException("Loot table parameter set " + $$4 + " is already registered");
        } else {
            return $$3;
        }
    }

    @Nullable
    public static LootContextParamSet get(ResourceLocation resourceLocation0) {
        return (LootContextParamSet) REGISTRY.get(resourceLocation0);
    }

    @Nullable
    public static ResourceLocation getKey(LootContextParamSet lootContextParamSet0) {
        return (ResourceLocation) REGISTRY.inverse().get(lootContextParamSet0);
    }
}