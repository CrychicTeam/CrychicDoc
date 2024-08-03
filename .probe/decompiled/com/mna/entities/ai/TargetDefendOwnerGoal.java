package com.mna.entities.ai;

import com.mna.api.entities.ISummonTargetPredicate;
import com.mna.tools.SummonUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class TargetDefendOwnerGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public TargetDefendOwnerGoal(Mob goalOwnerIn, Class<T> targetClassIn, int targetChanceIn, boolean checkSight, boolean nearbyOnlyIn) {
        super(goalOwnerIn, targetClassIn, targetChanceIn, checkSight, nearbyOnlyIn, null);
        this.f_26051_ = new SummonEntityPredicate().m_26883_(this.m_7623_() * 2.0).selector(e -> {
            boolean friendly = SummonUtils.isTargetFriendly(e, SummonUtils.getSummoner(goalOwnerIn));
            boolean result = !friendly;
            if (goalOwnerIn instanceof ISummonTargetPredicate) {
                result = ((ISummonTargetPredicate) goalOwnerIn).shouldSummonTarget(e, friendly);
            }
            return result;
        }).ignoreInvisibilityTesting();
    }

    @Override
    protected void findTarget() {
        this.f_26050_ = this.f_26135_.m_9236_().m_45963_(this.f_26048_, this.f_26051_, this.f_26135_, this.f_26135_.m_20185_(), this.f_26135_.m_20188_(), this.f_26135_.m_20189_(), this.m_7255_(this.m_7623_()));
    }
}