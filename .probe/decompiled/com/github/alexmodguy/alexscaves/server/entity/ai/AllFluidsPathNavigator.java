package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class AllFluidsPathNavigator extends SemiAquaticPathNavigatorNoSpin {

    public AllFluidsPathNavigator(Mob mob, Level worldIn) {
        super(mob, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int j) {
        this.f_26508_ = new AllFluidsNodeEvaluator(true);
        return new PathFinder(this.f_26508_, j);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.isInLiquid();
    }

    @Override
    protected Vec3 getTempMobPos() {
        return this.f_26494_.isInFluidType() ? super.m_7475_() : new Vec3(this.f_26494_.m_20185_(), Math.floor(this.f_26494_.m_20186_() + 0.5), this.f_26494_.m_20189_());
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.f_26495_.getBlockState(pos.below()).m_60795_();
    }

    @Override
    public void setCanFloat(boolean canSwim) {
    }

    @Override
    protected boolean isInLiquid() {
        return this.f_26494_.isInFluidType();
    }
}