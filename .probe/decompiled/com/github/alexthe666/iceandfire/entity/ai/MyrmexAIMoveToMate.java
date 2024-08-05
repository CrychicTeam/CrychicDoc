package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexRoyal;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class MyrmexAIMoveToMate extends Goal {

    private final EntityMyrmexRoyal myrmex;

    private final double movementSpeed;

    public MyrmexAIMoveToMate(EntityMyrmexRoyal entityIn, double movementSpeedIn) {
        this.myrmex = entityIn;
        this.movementSpeed = movementSpeedIn;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.mate != null && this.myrmex.canSeeSky();
    }

    @Override
    public void tick() {
        if (this.myrmex.mate != null && (this.myrmex.m_20270_(this.myrmex.mate) > 30.0F || this.myrmex.m_21573_().isDone())) {
            this.myrmex.m_21566_().setWantedPosition(this.myrmex.mate.m_20185_(), this.myrmex.m_20186_(), this.myrmex.mate.m_20189_(), this.movementSpeed);
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.myrmex.canMove() && this.myrmex.m_5448_() == null && this.myrmex.mate != null && this.myrmex.mate.m_6084_() && (this.myrmex.m_20270_(this.myrmex.mate) < 15.0F || !this.myrmex.m_21573_().isDone());
    }
}