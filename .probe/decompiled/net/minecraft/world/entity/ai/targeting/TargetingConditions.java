package net.minecraft.world.entity.ai.targeting;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class TargetingConditions {

    public static final TargetingConditions DEFAULT = forCombat();

    private static final double MIN_VISIBILITY_DISTANCE_FOR_INVISIBLE_TARGET = 2.0;

    private final boolean isCombat;

    private double range = -1.0;

    private boolean checkLineOfSight = true;

    private boolean testInvisible = true;

    @Nullable
    private Predicate<LivingEntity> selector;

    private TargetingConditions(boolean boolean0) {
        this.isCombat = boolean0;
    }

    public static TargetingConditions forCombat() {
        return new TargetingConditions(true);
    }

    public static TargetingConditions forNonCombat() {
        return new TargetingConditions(false);
    }

    public TargetingConditions copy() {
        TargetingConditions $$0 = this.isCombat ? forCombat() : forNonCombat();
        $$0.range = this.range;
        $$0.checkLineOfSight = this.checkLineOfSight;
        $$0.testInvisible = this.testInvisible;
        $$0.selector = this.selector;
        return $$0;
    }

    public TargetingConditions range(double double0) {
        this.range = double0;
        return this;
    }

    public TargetingConditions ignoreLineOfSight() {
        this.checkLineOfSight = false;
        return this;
    }

    public TargetingConditions ignoreInvisibilityTesting() {
        this.testInvisible = false;
        return this;
    }

    public TargetingConditions selector(@Nullable Predicate<LivingEntity> predicateLivingEntity0) {
        this.selector = predicateLivingEntity0;
        return this;
    }

    public boolean test(@Nullable LivingEntity livingEntity0, LivingEntity livingEntity1) {
        if (livingEntity0 == livingEntity1) {
            return false;
        } else if (!livingEntity1.canBeSeenByAnyone()) {
            return false;
        } else if (this.selector != null && !this.selector.test(livingEntity1)) {
            return false;
        } else {
            if (livingEntity0 == null) {
                if (this.isCombat && (!livingEntity1.canBeSeenAsEnemy() || livingEntity1.m_9236_().m_46791_() == Difficulty.PEACEFUL)) {
                    return false;
                }
            } else {
                if (this.isCombat && (!livingEntity0.canAttack(livingEntity1) || !livingEntity0.canAttackType(livingEntity1.m_6095_()) || livingEntity0.m_7307_(livingEntity1))) {
                    return false;
                }
                if (this.range > 0.0) {
                    double $$2 = this.testInvisible ? livingEntity1.getVisibilityPercent(livingEntity0) : 1.0;
                    double $$3 = Math.max(this.range * $$2, 2.0);
                    double $$4 = livingEntity0.m_20275_(livingEntity1.m_20185_(), livingEntity1.m_20186_(), livingEntity1.m_20189_());
                    if ($$4 > $$3 * $$3) {
                        return false;
                    }
                }
                if (this.checkLineOfSight && livingEntity0 instanceof Mob $$5 && !$$5.getSensing().hasLineOfSight(livingEntity1)) {
                    return false;
                }
            }
            return true;
        }
    }
}