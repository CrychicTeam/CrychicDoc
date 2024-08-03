package net.minecraft.world.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PathfinderMob;

public class TryFindWaterGoal extends Goal {

    private final PathfinderMob mob;

    public TryFindWaterGoal(PathfinderMob pathfinderMob0) {
        this.mob = pathfinderMob0;
    }

    @Override
    public boolean canUse() {
        return this.mob.m_20096_() && !this.mob.m_9236_().getFluidState(this.mob.m_20183_()).is(FluidTags.WATER);
    }

    @Override
    public void start() {
        BlockPos $$0 = null;
        for (BlockPos $$2 : BlockPos.betweenClosed(Mth.floor(this.mob.m_20185_() - 2.0), Mth.floor(this.mob.m_20186_() - 2.0), Mth.floor(this.mob.m_20189_() - 2.0), Mth.floor(this.mob.m_20185_() + 2.0), this.mob.m_146904_(), Mth.floor(this.mob.m_20189_() + 2.0))) {
            if (this.mob.m_9236_().getFluidState($$2).is(FluidTags.WATER)) {
                $$0 = $$2;
                break;
            }
        }
        if ($$0 != null) {
            this.mob.m_21566_().setWantedPosition((double) $$0.m_123341_(), (double) $$0.m_123342_(), (double) $$0.m_123343_(), 1.0);
        }
    }
}