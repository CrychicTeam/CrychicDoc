package yesman.epicfight.world.capabilities.entitypatch.mob;

import net.minecraft.world.entity.PathfinderMob;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class CaveSpiderPatch<T extends PathfinderMob> extends SpiderPatch<T> {

    @Override
    protected void initAI() {
        super.initAI();
        this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, MobCombatBehaviors.SPIDER.build(this)));
        this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), 1.0, false));
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        return super.getModelMatrix(partialTicks).scale(0.7F, 0.7F, 0.7F);
    }
}