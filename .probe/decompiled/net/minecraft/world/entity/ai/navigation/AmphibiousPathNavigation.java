package net.minecraft.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class AmphibiousPathNavigation extends PathNavigation {

    public AmphibiousPathNavigation(Mob mob0, Level level1) {
        super(mob0, level1);
    }

    @Override
    protected PathFinder createPathFinder(int int0) {
        this.f_26508_ = new AmphibiousNodeEvaluator(false);
        this.f_26508_.setCanPassDoors(true);
        return new PathFinder(this.f_26508_, int0);
    }

    @Override
    protected boolean canUpdatePath() {
        return true;
    }

    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.f_26494_.m_20185_(), this.f_26494_.m_20227_(0.5), this.f_26494_.m_20189_());
    }

    @Override
    protected double getGroundY(Vec3 vec0) {
        return vec0.y;
    }

    @Override
    protected boolean canMoveDirectly(Vec3 vec0, Vec3 vec1) {
        return this.m_26574_() ? m_262402_(this.f_26494_, vec0, vec1, false) : false;
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos0) {
        return !this.f_26495_.getBlockState(blockPos0.below()).m_60795_();
    }

    @Override
    public void setCanFloat(boolean boolean0) {
    }
}