package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class CopyBlockState extends LootItemConditionalFunction {

    final Block block;

    final Set<Property<?>> properties;

    CopyBlockState(LootItemCondition[] lootItemCondition0, Block block1, Set<Property<?>> setProperty2) {
        super(lootItemCondition0);
        this.block = block1;
        this.properties = setProperty2;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.COPY_STATE;
    }

    @Override
    public Set<LootContextParam<?>> getReferencedContextParams() {
        return ImmutableSet.of(LootContextParams.BLOCK_STATE);
    }

    @Override
    protected ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        BlockState $$2 = lootContext1.getParamOrNull(LootContextParams.BLOCK_STATE);
        if ($$2 != null) {
            CompoundTag $$3 = itemStack0.getOrCreateTag();
            CompoundTag $$4;
            if ($$3.contains("BlockStateTag", 10)) {
                $$4 = $$3.getCompound("BlockStateTag");
            } else {
                $$4 = new CompoundTag();
                $$3.put("BlockStateTag", $$4);
            }
            this.properties.stream().filter($$2::m_61138_).forEach(p_80072_ -> $$4.putString(p_80072_.getName(), serialize($$2, p_80072_)));
        }
        return itemStack0;
    }

    public static CopyBlockState.Builder copyState(Block block0) {
        return new CopyBlockState.Builder(block0);
    }

    private static <T extends Comparable<T>> String serialize(BlockState blockState0, Property<T> propertyT1) {
        T $$2 = (T) blockState0.m_61143_(propertyT1);
        return propertyT1.getName($$2);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<CopyBlockState.Builder> {

        private final Block block;

        private final Set<Property<?>> properties = Sets.newHashSet();

        Builder(Block block0) {
            this.block = block0;
        }

        public CopyBlockState.Builder copy(Property<?> property0) {
            if (!this.block.getStateDefinition().getProperties().contains(property0)) {
                throw new IllegalStateException("Property " + property0 + " is not present on block " + this.block);
            } else {
                this.properties.add(property0);
                return this;
            }
        }

        protected CopyBlockState.Builder getThis() {
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new CopyBlockState(this.m_80699_(), this.block, this.properties);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<CopyBlockState> {

        public void serialize(JsonObject jsonObject0, CopyBlockState copyBlockState1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, copyBlockState1, jsonSerializationContext2);
            jsonObject0.addProperty("block", BuiltInRegistries.BLOCK.getKey(copyBlockState1.block).toString());
            JsonArray $$3 = new JsonArray();
            copyBlockState1.properties.forEach(p_80091_ -> $$3.add(p_80091_.getName()));
            jsonObject0.add("properties", $$3);
        }

        public CopyBlockState deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "block"));
            Block $$4 = (Block) BuiltInRegistries.BLOCK.m_6612_($$3).orElseThrow(() -> new IllegalArgumentException("Can't find block " + $$3));
            StateDefinition<Block, BlockState> $$5 = $$4.getStateDefinition();
            Set<Property<?>> $$6 = Sets.newHashSet();
            JsonArray $$7 = GsonHelper.getAsJsonArray(jsonObject0, "properties", null);
            if ($$7 != null) {
                $$7.forEach(p_80111_ -> $$6.add($$5.getProperty(GsonHelper.convertToString(p_80111_, "property"))));
            }
            return new CopyBlockState(lootItemCondition2, $$4, $$6);
        }
    }
}