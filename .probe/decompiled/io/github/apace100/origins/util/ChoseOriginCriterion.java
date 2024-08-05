package io.github.apace100.origins.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import java.util.Objects;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;

public class ChoseOriginCriterion extends SimpleCriterionTrigger<ChoseOriginCriterion.Conditions> {

    public static final ChoseOriginCriterion INSTANCE = new ChoseOriginCriterion();

    private static final ResourceLocation ID = new ResourceLocation("origins", "chose_origin");

    @NotNull
    protected ChoseOriginCriterion.Conditions createInstance(@NotNull JsonObject obj, @NotNull ContextAwarePredicate playerPredicate, @NotNull DeserializationContext predicateDeserializer) {
        ResourceLocation id = ResourceLocation.tryParse(GsonHelper.getAsString(obj, "origin"));
        return new ChoseOriginCriterion.Conditions(playerPredicate, id);
    }

    public void trigger(ServerPlayer player, ResourceKey<Origin> origin) {
        this.m_66234_(player, conditions -> conditions.matches(origin));
    }

    @NotNull
    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {

        private final ResourceLocation originId;

        public Conditions(ContextAwarePredicate player, ResourceLocation originId) {
            super(ChoseOriginCriterion.ID, player);
            this.originId = originId;
        }

        public boolean matches(ResourceKey<Origin> origin) {
            return Objects.equals(origin.location(), this.originId);
        }

        @NotNull
        @Override
        public JsonObject serializeToJson(@NotNull SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("origin", new JsonPrimitive(this.originId.toString()));
            return jsonObject;
        }
    }
}