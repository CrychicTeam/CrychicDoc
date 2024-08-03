package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityCockatrice;
import java.util.EnumSet;
import java.util.function.Predicate;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class CockatriceAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    private final EntityCockatrice cockatrice;

    public CockatriceAITarget(EntityCockatrice entityIn, Class<T> classTarget, boolean checkSight, Predicate<LivingEntity> targetSelector) {
        super(entityIn, classTarget, 0, checkSight, false, targetSelector);
        this.cockatrice = entityIn;
        this.m_7021_(EnumSet.of(Goal.Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        if (this.f_26135_.m_217043_().nextInt(20) != 0 || this.cockatrice.m_9236_().m_46791_() == Difficulty.PEACEFUL) {
            return false;
        } else if (!super.canUse() || this.f_26050_ == null || this.f_26050_.getClass().equals(this.cockatrice.getClass())) {
            return false;
        } else {
            return this.f_26050_ instanceof Player && !this.cockatrice.m_21830_(this.f_26050_) ? !this.cockatrice.m_21824_() : !this.cockatrice.m_21830_(this.f_26050_) && this.cockatrice.canMove();
        }
    }
}