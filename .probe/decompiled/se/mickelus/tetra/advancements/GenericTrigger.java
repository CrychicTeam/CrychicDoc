package se.mickelus.tetra.advancements;

import com.google.gson.JsonObject;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

@ParametersAreNonnullByDefault
public class GenericTrigger<T extends AbstractCriterionTriggerInstance> extends SimpleCriterionTrigger<T> {

    private final ResourceLocation id;

    private final GenericTrigger.TriggerDeserializer<T> deserializer;

    public GenericTrigger(String id, GenericTrigger.TriggerDeserializer<T> deserializer) {
        this.id = new ResourceLocation(id);
        this.deserializer = deserializer;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    protected T createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        return this.deserializer.apply(json, entityPredicate, conditionsParser);
    }

    public void fulfillCriterion(ServerPlayer player, Predicate<T> validationPredicate) {
        this.m_66234_(player, validationPredicate);
    }

    public interface TriggerDeserializer<T> {

        T apply(JsonObject var1, ContextAwarePredicate var2, DeserializationContext var3);
    }
}