package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class PlayerTrigger extends SimpleCriterionTrigger<PlayerTrigger.TriggerInstance> {

    final ResourceLocation id;

    public PlayerTrigger(ResourceLocation resourceLocation0) {
        this.id = resourceLocation0;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    public PlayerTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        return new PlayerTrigger.TriggerInstance(this.id, contextAwarePredicate1);
    }

    public void trigger(ServerPlayer serverPlayer0) {
        this.m_66234_(serverPlayer0, p_222625_ -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation resourceLocation0, ContextAwarePredicate contextAwarePredicate1) {
            super(resourceLocation0, contextAwarePredicate1);
        }

        public static PlayerTrigger.TriggerInstance located(LocationPredicate locationPredicate0) {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.LOCATION.id, EntityPredicate.wrap(EntityPredicate.Builder.entity().located(locationPredicate0).build()));
        }

        public static PlayerTrigger.TriggerInstance located(EntityPredicate entityPredicate0) {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.LOCATION.id, EntityPredicate.wrap(entityPredicate0));
        }

        public static PlayerTrigger.TriggerInstance sleptInBed() {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.SLEPT_IN_BED.id, ContextAwarePredicate.ANY);
        }

        public static PlayerTrigger.TriggerInstance raidWon() {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.RAID_WIN.id, ContextAwarePredicate.ANY);
        }

        public static PlayerTrigger.TriggerInstance avoidVibration() {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.AVOID_VIBRATION.id, ContextAwarePredicate.ANY);
        }

        public static PlayerTrigger.TriggerInstance tick() {
            return new PlayerTrigger.TriggerInstance(CriteriaTriggers.TICK.id, ContextAwarePredicate.ANY);
        }

        public static PlayerTrigger.TriggerInstance walkOnBlockWithEquipment(Block block0, Item item1) {
            return located(EntityPredicate.Builder.entity().equipment(EntityEquipmentPredicate.Builder.equipment().feet(ItemPredicate.Builder.item().of(item1).build()).build()).steppingOn(LocationPredicate.Builder.location().setBlock(BlockPredicate.Builder.block().of(block0).build()).build()).build());
        }
    }
}