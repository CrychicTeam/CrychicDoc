package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import java.util.Collection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class FishingRodHookedTrigger extends SimpleCriterionTrigger<FishingRodHookedTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("fishing_rod_hooked");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public FishingRodHookedTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        ItemPredicate $$3 = ItemPredicate.fromJson(jsonObject0.get("rod"));
        ContextAwarePredicate $$4 = EntityPredicate.fromJson(jsonObject0, "entity", deserializationContext2);
        ItemPredicate $$5 = ItemPredicate.fromJson(jsonObject0.get("item"));
        return new FishingRodHookedTrigger.TriggerInstance(contextAwarePredicate1, $$3, $$4, $$5);
    }

    public void trigger(ServerPlayer serverPlayer0, ItemStack itemStack1, FishingHook fishingHook2, Collection<ItemStack> collectionItemStack3) {
        LootContext $$4 = EntityPredicate.createContext(serverPlayer0, (Entity) (fishingHook2.getHookedIn() != null ? fishingHook2.getHookedIn() : fishingHook2));
        this.m_66234_(serverPlayer0, p_40425_ -> p_40425_.matches(itemStack1, $$4, collectionItemStack3));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        private final ItemPredicate rod;

        private final ContextAwarePredicate entity;

        private final ItemPredicate item;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, ItemPredicate itemPredicate1, ContextAwarePredicate contextAwarePredicate2, ItemPredicate itemPredicate3) {
            super(FishingRodHookedTrigger.ID, contextAwarePredicate0);
            this.rod = itemPredicate1;
            this.entity = contextAwarePredicate2;
            this.item = itemPredicate3;
        }

        public static FishingRodHookedTrigger.TriggerInstance fishedItem(ItemPredicate itemPredicate0, EntityPredicate entityPredicate1, ItemPredicate itemPredicate2) {
            return new FishingRodHookedTrigger.TriggerInstance(ContextAwarePredicate.ANY, itemPredicate0, EntityPredicate.wrap(entityPredicate1), itemPredicate2);
        }

        public boolean matches(ItemStack itemStack0, LootContext lootContext1, Collection<ItemStack> collectionItemStack2) {
            if (!this.rod.matches(itemStack0)) {
                return false;
            } else if (!this.entity.matches(lootContext1)) {
                return false;
            } else {
                if (this.item != ItemPredicate.ANY) {
                    boolean $$3 = false;
                    Entity $$4 = lootContext1.getParamOrNull(LootContextParams.THIS_ENTITY);
                    if ($$4 instanceof ItemEntity $$5 && this.item.matches($$5.getItem())) {
                        $$3 = true;
                    }
                    for (ItemStack $$6 : collectionItemStack2) {
                        if (this.item.matches($$6)) {
                            $$3 = true;
                            break;
                        }
                    }
                    if (!$$3) {
                        return false;
                    }
                }
                return true;
            }
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            $$1.add("rod", this.rod.serializeToJson());
            $$1.add("entity", this.entity.toJson(serializationContext0));
            $$1.add("item", this.item.serializeToJson());
            return $$1;
        }
    }
}