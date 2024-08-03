package net.minecraft.world.level.storage.loot.predicates;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.ValidationContext;
import org.slf4j.Logger;

public class ConditionReference implements LootItemCondition {

    private static final Logger LOGGER = LogUtils.getLogger();

    final ResourceLocation name;

    ConditionReference(ResourceLocation resourceLocation0) {
        this.name = resourceLocation0;
    }

    @Override
    public LootItemConditionType getType() {
        return LootItemConditions.REFERENCE;
    }

    @Override
    public void validate(ValidationContext validationContext0) {
        LootDataId<LootItemCondition> $$1 = new LootDataId<>(LootDataType.PREDICATE, this.name);
        if (validationContext0.hasVisitedElement($$1)) {
            validationContext0.reportProblem("Condition " + this.name + " is recursively called");
        } else {
            LootItemCondition.super.m_6169_(validationContext0);
            validationContext0.resolver().getElementOptional($$1).ifPresentOrElse(p_279085_ -> p_279085_.m_6169_(validationContext0.enterElement(".{" + this.name + "}", $$1)), () -> validationContext0.reportProblem("Unknown condition table called " + this.name));
        }
    }

    public boolean test(LootContext lootContext0) {
        LootItemCondition $$1 = lootContext0.getResolver().getElement(LootDataType.PREDICATE, this.name);
        if ($$1 == null) {
            LOGGER.warn("Tried using unknown condition table called {}", this.name);
            return false;
        } else {
            LootContext.VisitedEntry<?> $$2 = LootContext.createVisitedEntry($$1);
            if (lootContext0.pushVisitedElement($$2)) {
                boolean var4;
                try {
                    var4 = $$1.test(lootContext0);
                } finally {
                    lootContext0.popVisitedElement($$2);
                }
                return var4;
            } else {
                LOGGER.warn("Detected infinite loop in loot tables");
                return false;
            }
        }
    }

    public static LootItemCondition.Builder conditionReference(ResourceLocation resourceLocation0) {
        return () -> new ConditionReference(resourceLocation0);
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ConditionReference> {

        public void serialize(JsonObject jsonObject0, ConditionReference conditionReference1, JsonSerializationContext jsonSerializationContext2) {
            jsonObject0.addProperty("name", conditionReference1.name.toString());
        }

        public ConditionReference deserialize(JsonObject jsonObject0, JsonDeserializationContext jsonDeserializationContext1) {
            ResourceLocation $$2 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "name"));
            return new ConditionReference($$2);
        }
    }
}