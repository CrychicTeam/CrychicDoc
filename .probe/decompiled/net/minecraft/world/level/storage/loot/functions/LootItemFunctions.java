package net.minecraft.world.level.storage.loot.functions;

import java.util.function.BiFunction;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.GsonAdapterFactory;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;

public class LootItemFunctions {

    public static final BiFunction<ItemStack, LootContext, ItemStack> IDENTITY = (p_80760_, p_80761_) -> p_80760_;

    public static final LootItemFunctionType SET_COUNT = register("set_count", new SetItemCountFunction.Serializer());

    public static final LootItemFunctionType ENCHANT_WITH_LEVELS = register("enchant_with_levels", new EnchantWithLevelsFunction.Serializer());

    public static final LootItemFunctionType ENCHANT_RANDOMLY = register("enchant_randomly", new EnchantRandomlyFunction.Serializer());

    public static final LootItemFunctionType SET_ENCHANTMENTS = register("set_enchantments", new SetEnchantmentsFunction.Serializer());

    public static final LootItemFunctionType SET_NBT = register("set_nbt", new SetNbtFunction.Serializer());

    public static final LootItemFunctionType FURNACE_SMELT = register("furnace_smelt", new SmeltItemFunction.Serializer());

    public static final LootItemFunctionType LOOTING_ENCHANT = register("looting_enchant", new LootingEnchantFunction.Serializer());

    public static final LootItemFunctionType SET_DAMAGE = register("set_damage", new SetItemDamageFunction.Serializer());

    public static final LootItemFunctionType SET_ATTRIBUTES = register("set_attributes", new SetAttributesFunction.Serializer());

    public static final LootItemFunctionType SET_NAME = register("set_name", new SetNameFunction.Serializer());

    public static final LootItemFunctionType EXPLORATION_MAP = register("exploration_map", new ExplorationMapFunction.Serializer());

    public static final LootItemFunctionType SET_STEW_EFFECT = register("set_stew_effect", new SetStewEffectFunction.Serializer());

    public static final LootItemFunctionType COPY_NAME = register("copy_name", new CopyNameFunction.Serializer());

    public static final LootItemFunctionType SET_CONTENTS = register("set_contents", new SetContainerContents.Serializer());

    public static final LootItemFunctionType LIMIT_COUNT = register("limit_count", new LimitCount.Serializer());

    public static final LootItemFunctionType APPLY_BONUS = register("apply_bonus", new ApplyBonusCount.Serializer());

    public static final LootItemFunctionType SET_LOOT_TABLE = register("set_loot_table", new SetContainerLootTable.Serializer());

    public static final LootItemFunctionType EXPLOSION_DECAY = register("explosion_decay", new ApplyExplosionDecay.Serializer());

    public static final LootItemFunctionType SET_LORE = register("set_lore", new SetLoreFunction.Serializer());

    public static final LootItemFunctionType FILL_PLAYER_HEAD = register("fill_player_head", new FillPlayerHead.Serializer());

    public static final LootItemFunctionType COPY_NBT = register("copy_nbt", new CopyNbtFunction.Serializer());

    public static final LootItemFunctionType COPY_STATE = register("copy_state", new CopyBlockState.Serializer());

    public static final LootItemFunctionType SET_BANNER_PATTERN = register("set_banner_pattern", new SetBannerPatternFunction.Serializer());

    public static final LootItemFunctionType SET_POTION = register("set_potion", new SetPotionFunction.Serializer());

    public static final LootItemFunctionType SET_INSTRUMENT = register("set_instrument", new SetInstrumentFunction.Serializer());

    public static final LootItemFunctionType REFERENCE = register("reference", new FunctionReference.Serializer());

    private static LootItemFunctionType register(String string0, Serializer<? extends LootItemFunction> serializerExtendsLootItemFunction1) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(string0), new LootItemFunctionType(serializerExtendsLootItemFunction1));
    }

    public static Object createGsonAdapter() {
        return GsonAdapterFactory.<LootItemFunction, LootItemFunctionType>builder(BuiltInRegistries.LOOT_FUNCTION_TYPE, "function", "function", LootItemFunction::m_7162_).build();
    }

    public static BiFunction<ItemStack, LootContext, ItemStack> compose(BiFunction<ItemStack, LootContext, ItemStack>[] biFunctionItemStackLootContextItemStack0) {
        switch(biFunctionItemStackLootContextItemStack0.length) {
            case 0:
                return IDENTITY;
            case 1:
                return biFunctionItemStackLootContextItemStack0[0];
            case 2:
                BiFunction<ItemStack, LootContext, ItemStack> $$1 = biFunctionItemStackLootContextItemStack0[0];
                BiFunction<ItemStack, LootContext, ItemStack> $$2 = biFunctionItemStackLootContextItemStack0[1];
                return (p_80768_, p_80769_) -> (ItemStack) $$2.apply((ItemStack) $$1.apply(p_80768_, p_80769_), p_80769_);
            default:
                return (p_80774_, p_80775_) -> {
                    for (BiFunction<ItemStack, LootContext, ItemStack> $$3 : biFunctionItemStackLootContextItemStack0) {
                        p_80774_ = (ItemStack) $$3.apply(p_80774_, p_80775_);
                    }
                    return p_80774_;
                };
        }
    }
}