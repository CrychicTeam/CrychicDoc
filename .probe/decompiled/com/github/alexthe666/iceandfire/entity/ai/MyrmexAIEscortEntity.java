package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexSoldier;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class MyrmexAIEscortEntity extends Goal {

    private final EntityMyrmexSoldier myrmex;

    private final double movementSpeed;

    public MyrmexAIEscortEntity(EntityMyrmexSoldier entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.guardingEntity != null && (this.myrmex.guardingEntity.canSeeSky() || !this.myrmex.canSeeSky()) && !this.myrmex.isEnteringHive;
    }

    @Override
    public void tick() {
        if (this.myrmex.guardingEntity != null && (this.myrmex.m_20270_(this.myrmex.guardingEntity) > 30.0F || this.myrmex.m_21573_().isDone())) {
            this.myrmex.m_21573_().moveTo(this.myrmex.guardingEntity, this.movementSpeed);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.guardingEntity != null && this.myrmex.guardingEntity.m_6084_() && (this.myrmex.m_20270_(this.myrmex.guardingEntity) < 15.0F || !this.myrmex.m_21573_().isDone()) && this.myrmex.canSeeSky() == this.myrmex.guardingEntity.canSeeSky() && !this.myrmex.guardingEntity.canSeeSky();
    }
}