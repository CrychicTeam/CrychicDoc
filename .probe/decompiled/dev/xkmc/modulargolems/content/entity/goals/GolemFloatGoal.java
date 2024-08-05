package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.init.registrate.GolemModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FloatGoal;

public class GolemFloatGoal extends FloatGoal {

    private final AbstractGolemEntity<?, ?> golem;

    public GolemFloatGoal(AbstractGolemEntity<?, ?> golem) {
        super(golem);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        boolean canSwim = (Integer) this.golem.getModifiers().getOrDefault(GolemModifiers.SWIM.get(), 0) > 0;
        boolean canFloat = (Integer) this.golem.getModifiers().getOrDefault(GolemModifiers.FLOAT.get(), 0) > 0;
        boolean fireImmune = this.golem.hasFlag(GolemFlags.FIRE_IMMUNE);
        if (this.golem.m_20069_() && canSwim) {
            LivingEntity target = this.golem.m_5448_();
            if (target != null && target.m_20069_()) {
                return false;
            }
            if (target == null && this.golem.getOwner() != null && this.golem.getOwner().m_20186_() < this.golem.m_20186_() + 2.0) {
                return false;
            }
            if (this.golem.m_20184_().y() > 0.01) {
                return false;
            }
        }
        return (canSwim || canFloat || this.golem.m_20077_() && fireImmune) && super.canUse();
    }
}