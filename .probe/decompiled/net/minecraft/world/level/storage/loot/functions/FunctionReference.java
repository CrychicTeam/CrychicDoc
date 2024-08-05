package net.minecraft.world.level.storage.loot.functions;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.slf4j.Logger;

public class FunctionReference extends LootItemConditionalFunction {

    private static final Logger LOGGER = LogUtils.getLogger();

    final ResourceLocation name;

    FunctionReference(LootItemCondition[] lootItemCondition0, ResourceLocation resourceLocation1) {
        super(lootItemCondition0);
        this.name = resourceLocation1;
    }

    @Override
    public LootItemFunctionType getType() {
        return LootItemFunctions.REFERENCE;
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        LootDataId<LootItemFunction> $$1 = new LootDataId<>(LootDataType.MODIFIER, this.name);
        if (validationContext0.hasVisitedElement($$1)) {
            validationContext0.reportProblem("Function " + this.name + " is recursively called");
        } else {
            super.validate(validationContext0);
            validationContext0.resolver().getElementOptional($$1).ifPresentOrElse(p_279367_ -> p_279367_.m_6169_(validationContext0.enterElement(".{" + this.name + "}", $$1)), () -> validationContext0.reportProblem("Unknown function table called " + this.name));
        }
    }

    @Override
    protected ItemStack run(ItemStack itemStack0, LootContext lootContext1) {
        LootItemFunction $$2 = lootContext1.getResolver().getElement(LootDataType.MODIFIER, this.name);
        if ($$2 == null) {
            LOGGER.warn("Unknown function: {}", this.name);
            return itemStack0;
        } else {
            LootContext.VisitedEntry<?> $$3 = LootContext.createVisitedEntry($$2);
            if (lootContext1.pushVisitedElement($$3)) {
                ItemStack var5;
                try {
                    var5 = (ItemStack) $$2.apply(itemStack0, lootContext1);
                } finally {
                    lootContext1.popVisitedElement($$3);
                }
                return var5;
            } else {
                LOGGER.warn("Detected infinite loop in loot tables");
                return itemStack0;
            }
        }
    }

    public static LootItemConditionalFunction.Builder<?> functionReference(ResourceLocation resourceLocation0) {
        return m_80683_(p_279452_ -> new FunctionReference(p_279452_, resourceLocation0));
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<FunctionReference> {

        public void serialize(JsonObject jsonObject0, FunctionReference functionReference1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("name", functionReference1.name.toString());
        }

        public FunctionReference deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1, LootItemCondition[] lootItemCondition2) {
            ResourceLocation $$3 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            return new FunctionReference(lootItemCondition2, $$3);
        }
    }
}