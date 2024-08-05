package vectorwing.farmersdelight.common.advancement;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CuttingBoardTrigger extends SimpleCriterionTrigger<CuttingBoardTrigger.TriggerInstance> {

    private static final ResourceLocation ID = new ResourceLocation("farmersdelight", "use_cutting_board");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, CuttingBoardTrigger.TriggerInstance::test);
    }

    protected CuttingBoardTrigger.TriggerInstance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext conditionsParser) {
        return new CuttingBoardTrigger.TriggerInstance(player);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ContextAwarePredicate player) {
            super(CuttingBoardTrigger.ID, player);
        }

        public static CuttingBoardTrigger.TriggerInstance simple() {
            return new CuttingBoardTrigger.TriggerInstance(ContextAwarePredicate.ANY);
        }

        public boolean test() {
            return true;
        }
    }
}