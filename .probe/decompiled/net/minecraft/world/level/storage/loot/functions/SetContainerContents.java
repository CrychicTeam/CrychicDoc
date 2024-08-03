package net.minecraft.world.level.storage.loot.functions;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetContainerContents extends LootItemConditionalFunction {

    final List<LootPoolEntryContainer> entries;

    final BlockEntityType<?> type;

    SetContainerContents(LootItemCondition[] lootItemCondition0, BlockEntityType<?> blockEntityType1, List<LootPoolEntryContainer> listLootPoolEntryContainer2) {
        super(lootItemCondition0);
        this.type = blockEntityType1;
        this.entries = ImmutableList.copyOf(listLootPoolEntryContainer2);
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_CONTENTS;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.isEmpty()) {
            return itemStack0;
        } else {
            NonNullList<ItemStack> $$2 = NonNullList.create();
            this.entries.forEach(p_80916_ -> p_80916_.m_6562_(lootContext1, p_287573_ -> p_287573_.createItemStack(LootTable.createStackSplitter(lootContext1.getLevel(), $$2::add), lootContext1)));
            CompoundTag $$3 = new CompoundTag();
            ContainerHelper.saveAllItems($$3, $$2);
            CompoundTag $$4 = BlockItem.getBlockEntityData(itemStack0);
            if ($$4 == null) {
                $$4 = $$3;
            } else {
                $$4.merge($$3);
            }
            BlockItem.setBlockEntityData(itemStack0, this.type, $$4);
            return itemStack0;
        }
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        super.validate(validationContext0);
        for (int $$1 = 0; $$1 < this.entries.size(); $$1++) {
            ((LootPoolEntryContainer) this.entries.get($$1)).validate(validationContext0.forChild(".entry[" + $$1 + "]"));
        }
    }

    public static SetContainerContents.Builder setContents(BlockEntityType<?> blockEntityType0) {
        return new SetContainerContents.Builder(blockEntityType0);
    }

    public static class Builder extends LootItemConditionalFunction.Builder<SetContainerContents.Builder> {

        private final List<LootPoolEntryContainer> entries = Lists.newArrayList();

        private final BlockEntityType<?> type;

        public Builder(BlockEntityType<?> blockEntityType0) {
            this.type = blockEntityType0;
        }

        protected SetContainerContents.Builder getThis() {
            return this;
        }

        public SetContainerContents.Builder withEntry(LootPoolEntryContainer.Builder<?> lootPoolEntryContainerBuilder0) {
            this.entries.add(lootPoolEntryContainerBuilder0.build());
            return this;
        }

        @Override
        public LootItemFunction build() {
            return new SetContainerContents(this.m_80699_(), this.type, this.entries);
        }
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetContainerContents> {

        public void serialize(JsonObject jsonObject0, SetContainerContents setContainerContents1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setContainerContents1, jsonSerializationContext2);
            jsonObject0.addProperty("type", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(setContainerContents1.type).toString());
            jsonObject0.add("entries", jsonSerializationContext2.serialize(setContainerContents1.entries));
        }

        public SetContainerContents deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            LootPoolEntryContainer[] $$3 = GsonHelper.getAsObject(jsonObject0, "entries", jsonDeserializationContext1, LootPoolEntryContainer[].class);
            ResourceLocation $$4 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "type"));
            BlockEntityType<?> $$5 = (BlockEntityType<?>) BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional($$4).orElseThrow(() -> new JsonSyntaxException("Unknown block entity type id '" + $$4 + "'"));
            return new SetContainerContents(lootItemCondition2, $$5, Arrays.asList($$3));
        }
    }
}