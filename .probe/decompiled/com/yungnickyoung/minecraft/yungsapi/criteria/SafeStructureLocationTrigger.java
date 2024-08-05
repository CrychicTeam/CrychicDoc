package com.yungnickyoung.minecraft.yungsapi.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;

public class SafeStructureLocationTrigger extends SimpleCriterionTrigger<SafeStructureLocationTrigger.TriggerInstance> {

    private final ResourceLocation id;

    public SafeStructureLocationTrigger(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public SafeStructureLocationTrigger.TriggerInstance createInstance(JsonObject jsonObject, ContextAwarePredicate contextAwarePredicate, DeserializationContext deserializationContext) {
        JsonObject jsonobject = GsonHelper.getAsJsonObject(jsonObject, "location", jsonObject);
        SafeStructureLocationPredicate safeStructureLocationPredicate = SafeStructureLocationPredicate.fromJson(jsonobject);
        return new SafeStructureLocationTrigger.TriggerInstance(this.id, contextAwarePredicate, safeStructureLocationPredicate);
    }

    public void trigger(Player player) {
        if (player instanceof ServerPlayer) {
            this.m_66234_((ServerPlayer) player, triggerInstance -> triggerInstance.matches(((ServerPlayer) player).serverLevel(), player.m_20185_(), player.m_20186_(), player.m_20189_()));
        }
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final SafeStructureLocationPredicate location;

        public TriggerInstance(ResourceLocation id, ContextAwarePredicate contextAwarePredicate, SafeStructureLocationPredicate locationPredicate) {
            super(id, contextAwarePredicate);
            this.location = locationPredicate;
        }

        public static SafeStructureLocationTrigger.TriggerInstance located(SafeStructureLocationPredicate safeStructureLocationPredicate) {
            return new SafeStructureLocationTrigger.TriggerInstance(CriteriaTriggers.LOCATION.getId(), ContextAwarePredicate.ANY, safeStructureLocationPredicate);
        }

        public boolean matches(ServerLevel serverLevel, double x, double y, double z) {
            return this.location.matches(serverLevel, x, y, z);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext) {
            JsonObject jsonObject = super.serializeToJson(serializationContext);
            jsonObject.add("location", this.location.serializeToJson());
            return jsonObject;
        }
    }
}