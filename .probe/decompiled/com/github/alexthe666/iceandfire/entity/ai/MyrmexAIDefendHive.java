package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class MyrmexAIDefendHive extends TargetGoal {

    EntityMyrmexBase myrmex;

    LivingEntity villageAgressorTarget;

    public MyrmexAIDefendHive(EntityMyrmexBase myrmex) {
        super(myrmex, false, true);
        this.myrmex = myrmex;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        MyrmexHive village = this.myrmex.getHive();
        if (this.myrmex.canMove() && village != null) {
            this.villageAgressorTarget = village.findNearestVillageAggressor(this.myrmex);
            if (this.m_26150_(this.villageAgressorTarget, TargetingConditions.DEFAULT)) {
                return true;
            } else if (this.f_26135_.m_217043_().nextInt(20) == 0) {
                this.villageAgressorTarget = village.getNearestTargetPlayer(this.myrmex, this.myrmex.m_9236_());
                return this.m_26150_(this.villageAgressorTarget, TargetingConditions.DEFAULT);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.myrmex.m_6710_(this.villageAgressorTarget);
        super.start();
    }
}