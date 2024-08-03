package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class SetContainerLootTable extends LootItemConditionalFunction {

    final ResourceLocation name;

    final long seed;

    final BlockEntityType<?> type;

    SetContainerLootTable(LootItemCondition[] lootItemCondition0, ResourceLocation resourceLocation1, long long2, BlockEntityType<?> blockEntityType3) {
        super(lootItemCondition0);
        this.name = resourceLocation1;
        this.seed = long2;
        this.type = blockEntityType3;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.SET_LOOT_TABLE;
    }

    @Override
    public ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        if (itemStack0.isEmpty()) {
            return itemStack0;
        } else {
            CompoundTag $$2 = BlockItem.getBlockEntityData(itemStack0);
            if ($$2 == null) {
                $$2 = new CompoundTag();
            }
            $$2.putString("LootTable", this.name.toString());
            if (this.seed != 0L) {
                $$2.putLong("LootTableSeed", this.seed);
            }
            BlockItem.setBlockEntityData(itemStack0, this.type, $$2);
            return itemStack0;
        }
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        super.validate(validationContext0);
        LootDataId<LootTable> $$1 = new LootDataId<>(LootDataType.TABLE, this.name);
        if (validationContext0.resolver().getElementOptional($$1).isEmpty()) {
            validationContext0.reportProblem("Missing loot table used for container: " + this.name);
        }
    }

    public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> blockEntityType0, ResourceLocation resourceLocation1) {
        return m_80683_(p_193064_ -> new SetContainerLootTable(p_193064_, resourceLocation1, 0L, blockEntityType0));
    }

    public static LootItemConditionalFunction.Builder<?> withLootTable(BlockEntityType<?> blockEntityType0, ResourceLocation resourceLocation1, long long2) {
        return m_80683_(p_193060_ -> new SetContainerLootTable(p_193060_, resourceLocation1, long2, blockEntityType0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetContainerLootTable> {

        public void serialize(JsonObject jsonObject0, SetContainerLootTable setContainerLootTable1, JsonSerializationContext jsonSerializationContext2) {
            super.serialize(jsonObject0, setContainerLootTable1, jsonSerializationContext2);
            jsonObject0.addProperty("name", setContainerLootTable1.name.toString());
            jsonObject0.addProperty("type", BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(setContainerLootTable1.type).toString());
            if (setContainerLootTable1.seed != 0L) {
                jsonObject0.addProperty("seed", setContainerLootTable1.seed);
            }
        }

        public SetContainerLootTable deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            long $$4 = GsonHelper.getAsLong(jsonObject0, "seed", 0L);
            ResourceLocation $$5 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "type"));
            BlockEntityType<?> $$6 = (BlockEntityType<?>) BuiltInRegistries.BLOCK_ENTITY_TYPE.getOptional($$5).orElseThrow(() -> new JsonSyntaxException("Unknown block entity type id '" + $$5 + "'"));
            return new SetContainerLootTable(lootItemCondition2, $$3, $$4, $$6);
        }
    }
}