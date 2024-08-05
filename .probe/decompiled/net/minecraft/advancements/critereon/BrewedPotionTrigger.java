package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.annotation.Nullable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.alchemy.Potion;

public class BrewedPotionTrigger extends SimpleCriterionTrigger<BrewedPotionTrigger.TriggerInstance> {

    static final ResourceLocation ID = new ResourceLocation("brewed_potion");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public BrewedPotionTrigger.TriggerInstance createInstance(JsonObject jsonObject0, ContextAwarePredicate contextAwarePredicate1, DeserializationContext deserializationContext2) {
        Potion $$3 = null;
        if (jsonObject0.has("potion")) {
            ResourceLocation $$4 = new ResourceLocation(GsonHelper.getAsString(jsonObject0, "potion"));
            $$3 = (Potion) BuiltInRegistries.POTION.m_6612_($$4).orElseThrow(() -> new JsonSyntaxException("Unknown potion '" + $$4 + "'"));
        }
        return new BrewedPotionTrigger.TriggerInstance(contextAwarePredicate1, $$3);
    }

    public void trigger(ServerPlayer serverPlayer0, Potion potion1) {
        this.m_66234_(serverPlayer0, p_19125_ -> p_19125_.matches(potion1));
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        @Nullable
        private final Potion potion;

        public TriggerInstance(ContextAwarePredicate contextAwarePredicate0, @Nullable Potion potion1) {
            super(BrewedPotionTrigger.ID, contextAwarePredicate0);
            this.potion = potion1;
        }

        public static BrewedPotionTrigger.TriggerInstance brewedPotion() {
            return new BrewedPotionTrigger.TriggerInstance(ContextAwarePredicate.ANY, null);
        }

        public boolean matches(Potion potion0) {
            return this.potion == null || this.potion == potion0;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext serializationContext0) {
            JsonObject $$1 = super.serializeToJson(serializationContext0);
            if (this.potion != null) {
                $$1.addProperty("potion", BuiltInRegistries.POTION.getKey(this.potion).toString());
            }
            return $$1;
        }
    }
}