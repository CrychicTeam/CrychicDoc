package net.minecraft.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class WaterBoundPathNavigation extends PathNavigation {

    private boolean allowBreaching;

    public WaterBoundPathNavigation(Mob mob0, Level level1) {
        super(mob0, level1);
    }

    @Override
    protected PathFinder createPathFinder(int int0) {
        this.allowBreaching = this.f_26494_.m_6095_() == EntityType.DOLPHIN;
        this.f_26508_ = new SwimNodeEvaluator(this.allowBreaching);
        return new PathFinder(this.f_26508_, int0);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.allowBreaching || this.m_26574_();
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
        return m_262402_(this.f_26494_, vec0, vec1, false);
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos0) {
        return !this.f_26495_.getBlockState(blockPos0).m_60804_(this.f_26495_, blockPos0);
    }

    @Override
    public void setCanFloat(boolean boolean0) {
    }
}