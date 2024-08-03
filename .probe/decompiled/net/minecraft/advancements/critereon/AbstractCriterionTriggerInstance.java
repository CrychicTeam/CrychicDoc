package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractCriterionTriggerInstance implements CriterionTriggerInstance {

    private final ResourceLocation criterion;

    private final ContextAwarePredicate player;

    public AbstractCriterionTriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1) {
        this.criterion = resourceLocation0;
        this.player = contextAwarePredicate1;
    }

    @Override
    public ResourceLocation getCriterion() {
        return this.criterion;
    }

    protected ContextAwarePredicate getPlayerPredicate() {
        return this.player;
    }

    @Override
    public JsonObject serializeToJson(SerializationContext serializationContext0) {
        JsonObject $$1 = new JsonObject();
        $$1.add("player", this.player.toJson(serializationContext0));
        return $$1;
    }

    public String toString() {
        return "AbstractCriterionInstance{criterion=" + this.criterion + "}";
    }
}