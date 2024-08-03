package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetBannerPatternFunction extends LootItemConditionalFunction {

    final List<Pair<Holder<BannerPattern>, DyeColor>> patterns;

    final boolean append;

    SetBannerPatternFunction(LootItemCondition[] lootItemCondition0, List<Pair<Holder<BannerPattern>, DyeColor>> listPairHolderBannerPatternDyeColor1, boolean boolean2) {
        super(lootItemCondition0);
        this.patterns = listPairHolderBannerPatternDyeColor1;
        this.append = boolean2;
    }

    @Override
    protected ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        CompoundTag $$2 = BlockItem.getBlockEntityData(itemStack0);
        if ($$2 == null) {
            $$2 = new CompoundTag();
        }
        BannerPattern.Builder $$3 = new BannerPattern.Builder();
        this.patterns.forEach($$3::m_155048_);
        ListTag $$4 = $$3.toListTag();
        ListTag $$5;
        if (this.append) {
            $$5 = $$2.getList("Patterns", 10).copy();
            $$5.addAll($$4);
        } else {
            $$5 = $$4;
        }
        $$2.put("Patterns", $$5);
        BlockItem.setBlockEntityData(itemStack0, BlockEntityType.BANNER, $$2);
        return itemStack0;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_BANNER_PATTERN;
    }

    public static SetBannerPatternFunction.Builder setBannerPattern(boolean boolean0) {
        return new SetBannerPatternFunction.Builder(boolean0);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetBannerPatternFunction.Builder> {

        private final com.google.common.collect.ImmutableList.Builder<Pair<Holder<BannerPattern>, DyeColor>> patterns = ImmutableList.builder();

        private final boolean append;

        Builder(boolean boolean0) {
            this.append = boolean0;
        }

        protected SetBannerPatternFunction.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetBannerPatternFunction(this.m_80699_(), this.patterns.build(), this.append);
        }

        public SetBannerPatternFunction.Builder addPattern(ResourceKey<BannerPattern> resourceKeyBannerPattern0, DyeColor dyeColor1) {
            return this.addPattern(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(resourceKeyBannerPattern0), dyeColor1);
        }

        public SetBannerPatternFunction.Builder addPattern(Holder<BannerPattern> holderBannerPattern0, DyeColor dyeColor1) {
            this.patterns.add(Pair.of(holderBannerPattern0, dyeColor1));
            return this;
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetBannerPatternFunction> {

        public void serialize(JsonObject jsonObject0, SetBannerPatternFunction setBannerPatternFunction1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setBannerPatternFunction1, jsonSerializationContext2);
            JsonArray $$3 = new JsonArray();
            setBannerPatternFunction1.patterns.forEach(p_231003_ -> {
                JsonObject $$2 = new JsonObject();
                $$2.addProperty("pattern", ((ResourceKey) ((Holder) p_231003_.getFirst()).unwrapKey().orElseThrow(() -> new JsonSyntaxException("Unknown pattern: " + p_231003_.getFirst()))).location().toString());
                $$2.addProperty("color", ((DyeColor) p_231003_.getSecond()).getName());
                $$3.add($$2);
            });
            jsonObject0.add("patterns", $$3);
            jsonObject0.addProperty("append", setBannerPatternFunction1.append);
        }

        public SetBannerPatternFunction deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            com.google.common.collect.ImmutableList.Builder<Pair<Holder<BannerPattern>, DyeColor>> $$3 = ImmutableList.builder();
            JsonArray $$4 = GsonHelper.getAsJsonArray(jsonObject0, "patterns");
            for (int $$5 = 0; $$5 < $$4.size(); $$5++) {
                JsonObject $$6 = GsonHelper.convertToJsonObject($$4.get($$5), "pattern[" + $$5 + "]");
                String $$7 = GsonHelper.getAsString($$6, "pattern");
                Optional<? extends Holder<BannerPattern>> $$8 = BuiltInRegistries.BANNER_PATTERN.getHolder(ResourceKey.create(Registries.BANNER_PATTERN, new ResourceLocation($$7)));
                if ($$8.isEmpty()) {
                    throw new JsonSyntaxException("Unknown pattern: " + $$7);
                }
                String $$9 = GsonHelper.getAsString($$6, "color");
                DyeColor $$10 = DyeColor.byName($$9, null);
                if ($$10 == null) {
                    throw new JsonSyntaxException("Unknown color: " + $$9);
                }
                $$3.add(Pair.of((Holder) $$8.get(), $$10));
            }
            boolean $$11 = GsonHelper.getAsBoolean(jsonObject0, "append");
            return new SetBannerPatternFunction(lootItemCondition2, $$3.build(), $$11);
        }
    }
}