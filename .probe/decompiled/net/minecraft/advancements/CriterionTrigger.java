package net.minecraft.advancements;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;

public interface CriterionTrigger<T extends CriterionTriggerInstance> {

    ResourceLocation getId();

    void addPlayerListener(PlayerAdvancements var1, CriterionTrigger.Listener<T> var2);

    void removePlayerListener(PlayerAdvancements var1, CriterionTrigger.Listener<T> var2);

    void removePlayerListeners(PlayerAdvancements var1);

    T createInstance(JsonObject var1, DeserializationContext var2);

    public static class Listener<T extends CriterionTriggerInstance> {

        private final T trigger;

        private final Advancement advancement;

        private final String criterion;

        public Listener(T t0, Advancement advancement1, String string2) {
            this.trigger = t0;
            this.advancement = advancement1;
            this.criterion = string2;
        }

        public T getTriggerInstance() {
            return this.trigger;
        }

        public void run(PlayerAdvancements playerAdvancements0) {
            playerAdvancements0.award(this.advancement, this.criterion);
        }

        public boolean equals(Object object0) {
            if (this == object0) {
                return true;
            } else if (object0 != null && this.getClass() == object0.getClass()) {
                CriterionTrigger.Listener<?> $$1 = (CriterionTrigger.Listener<?>) object0;
                if (!this.trigger.equals($$1.trigger)) {
                    return false;
                } else {
                    return !this.advancement.equals($$1.advancement) ? false : this.criterion.equals($$1.criterion);
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int $$0 = this.trigger.hashCode();
            $$0 = 31 * $$0 + this.advancement.hashCode();
            return 31 * $$0 + this.criterion.hashCode();
        }
    }
}