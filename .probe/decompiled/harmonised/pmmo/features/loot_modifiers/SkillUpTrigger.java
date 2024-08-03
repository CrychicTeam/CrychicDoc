package harmonised.pmmo.features.loot_modifiers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class SkillUpTrigger extends SimpleCriterionTrigger<SkillUpTrigger.TriggerInstance> {

    public static final SkillUpTrigger SKILL_UP = new SkillUpTrigger();

    private static final ResourceLocation ID = new ResourceLocation("pmmo", "skill_up");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    protected SkillUpTrigger.TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPlayer, DeserializationContext pContext) {
        return new SkillUpTrigger.TriggerInstance(this.getId(), pPlayer);
    }

    public void trigger(ServerPlayer player) {
        this.m_66234_(player, p -> true);
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance {

        public TriggerInstance(ResourceLocation pCriterion, ContextAwarePredicate pPlayer) {
            super(pCriterion, pPlayer);
        }
    }
}