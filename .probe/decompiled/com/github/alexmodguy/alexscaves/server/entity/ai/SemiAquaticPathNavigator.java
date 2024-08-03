package com.github.alexmodguy.alexscaves.server.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SemiAquaticPathNavigator extends WaterBoundPathNavigation {

    public SemiAquaticPathNavigator(Mob entitylivingIn, Level worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Override
    protected PathFinder createPathFinder(int p_179679_1_) {
        this.f_26508_ = new AmphibiousNodeEvaluator(true);
        return new PathFinder(this.f_26508_, p_179679_1_);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
        Vec3 vector3d = new Vec3(posVec32.x, posVec32.y + (double) this.f_26494_.m_20206_() * 0.5, posVec32.z);
        return this.f_26495_.m_45547_(new ClipContext(posVec31, vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.f_26494_)).getType() == HitResult.Type.MISS;
    }

    @Override
    protected Vec3 getTempMobPos() {
        return this.f_26494_.m_20072_() ? super.getTempMobPos() : new Vec3(this.f_26494_.m_20185_(), Math.floor(this.f_26494_.m_20186_() + 0.5), this.f_26494_.m_20189_());
    }

    @Override
    public boolean isStableDestination(BlockPos pos) {
        return !this.f_26495_.getBlockState(pos.below()).m_60795_();
    }

    @Override
    public void setCanFloat(boolean canSwim) {
    }
}