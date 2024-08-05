package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityPixie;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PixieAIMoveRandom extends Goal {

    BlockPos target;

    EntityPixie pixie;

    RandomSource random;

    public PixieAIMoveRandom(EntityPixie entityPixieIn) {
        this.pixie = entityPixieIn;
        this.random = entityPixieIn.m_217043_();
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        this.target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.m_9236_(), this.pixie.m_20185_() + (double) this.random.nextInt(15) - 7.0, this.pixie.m_20189_() + (double) this.random.nextInt(15) - 7.0, this.random);
        return !this.pixie.isOwnerClose() && !this.pixie.isPixieSitting() && this.isDirectPathBetweenPoints(this.pixie.m_20183_(), this.target) && !this.pixie.m_21566_().hasWanted() && this.random.nextInt(4) == 0 && this.pixie.getHousePos() == null;
    }

    protected boolean isDirectPathBetweenPoints(BlockPos posVec31, BlockPos posVec32) {
        return this.pixie.m_9236_().m_45547_(new ClipContext(new Vec3((double) posVec31.m_123341_() + 0.5, (double) posVec31.m_123342_() + 0.5, (double) posVec31.m_123343_() + 0.5), new Vec3((double) posVec32.m_123341_() + 0.5, (double) posVec32.m_123342_() + (double) this.pixie.m_20206_() * 0.5, (double) posVec32.m_123343_() + 0.5), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.pixie)).getType() == HitResult.Type.MISS;
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    @Override
    public void tick() {
        if (!this.isDirectPathBetweenPoints(this.pixie.m_20183_(), this.target)) {
            this.target = EntityPixie.getPositionRelativetoGround(this.pixie, this.pixie.m_9236_(), this.pixie.m_20185_() + (double) this.random.nextInt(15) - 7.0, this.pixie.m_20189_() + (double) this.random.nextInt(15) - 7.0, this.random);
        }
        if (this.pixie.m_9236_().m_46859_(this.target)) {
            this.pixie.m_21566_().setWantedPosition((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 0.25);
            if (this.pixie.m_5448_() == null) {
                this.pixie.m_21563_().setLookAt((double) this.target.m_123341_() + 0.5, (double) this.target.m_123342_() + 0.5, (double) this.target.m_123343_() + 0.5, 180.0F, 20.0F);
            }
        }
    }
}