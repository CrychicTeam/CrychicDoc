package net.mehvahdjukaar.moonlight.core.criteria_triggers;

import com.google.gson.JsonObject;
import java.util.function.Predicate;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OptionalAdvancementTrigger extends SimpleCriterionTrigger<OptionalAdvancementTrigger.Instance> {

    private final ResourceLocation id;

    private final Predicate<String> predicate;

    public OptionalAdvancementTrigger(ResourceLocation id, Predicate<String> predicate) {
        this.id = id;
        this.predicate = predicate;
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public OptionalAdvancementTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate predicate, DeserializationContext deserializationContext) {
        String condition = json.get("flag").getAsString();
        return new OptionalAdvancementTrigger.Instance(predicate, condition);
    }

    public void trigger(ServerPlayer playerEntity, ItemStack stack) {
        this.m_66234_(playerEntity, instance -> this.predicate.test(instance.condition));
    }

    protected class Instance extends AbstractCriterionTriggerInstance {

        private final String condition;

        public Instance(ContextAwarePredicate composite, String condition) {
            super(OptionalAdvancementTrigger.this.id, composite);
            this.condition = condition;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializer) {
            JsonObject jsonobject = super.serializeToJson(serializer);
            jsonobject.addProperty("flag", this.condition);
            return jsonobject;
        }
    }
}