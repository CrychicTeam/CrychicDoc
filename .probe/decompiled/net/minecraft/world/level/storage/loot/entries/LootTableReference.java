package net.minecraft.world.level.storage.loot.entries;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class LootTableReference extends LootPoolSingletonContainer {

    final ResourceLocation name;

    LootTableReference(ResourceLocation resourceLocation0, int int1, int int2, LootItemCondition[] lootItemCondition3, LootItemFunction[] lootItemFunction4) {
        super(int1, int2, lootItemCondition3, lootItemFunction4);
        this.name = resourceLocation0;
    }

    @Override
    public LootPoolEntryType getType() {
        return LootPoolEntries.REFERENCE;
    }

    @Override
    public void createItemStack(Consumer<ItemStack> consumerItemStack0, LootContext lootContext1) {
        LootTable $$2 = lootContext1.getResolver().getLootTable(this.name);
        $$2.getRandomItemsRaw(lootContext1, consumerItemStack0);
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        LootDataId<LootTable> $$1 = new LootDataId<>(LootDataType.TABLE, this.name);
        if (validationContext0.hasVisitedElement($$1)) {
            validationContext0.reportProblem("Table " + this.name + " is recursively called");
        } else {
            super.validate(validationContext0);
            validationContext0.resolver().getElementOptional($$1).ifPresentOrElse(p_279078_ -> p_279078_.validate(validationContext0.enterElement("->{" + this.name + "}", $$1)), () -> validationContext0.reportProblem("Unknown loot table called " + this.name));
        }
    }

    public static LootPoolSingletonContainer.Builder<?> lootTableReference(ResourceLocation resourceLocation0) {
        return m_79687_((p_79780_, p_79781_, p_79782_, p_79783_) -> new LootTableReference(resourceLocation0, p_79780_, p_79781_, p_79782_, p_79783_));
    }

    public static class Serializer extends LootPoolSingletonContainer.Serializer<LootTableReference> {

        public void serializeCustom(JsonObject jsonObject0, LootTableReference lootTableReference1, JsonSerializationContext jsonSerializationContext2) {
            super.serializeCustom(jsonObject0, lootTableReference1, jsonSerializationContext2);
            jsonObject0.addProperty("name", lootTableReference1.name.toString());
        }

        protected LootTableReference deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, int int2, int int3, LootItemCondition[] lootItemCondition4, LootItemFunction[] lootItemFunction5) {
            ResourceLocation $$6 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            return new LootTableReference($$6, int2, int3, lootItemCondition4, lootItemFunction5);
        }
    }
}