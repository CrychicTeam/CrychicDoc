package net.minecraft.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class FlyingPathNavigation extends PathNavigation {

    public FlyingPathNavigation(Mob mob0, Level level1) {
        super(mob0, level1);
    }

    @Override
    protected PathFinder createPathFinder(int int0) {
        this.f_26508_ = new FlyNodeEvaluator();
        this.f_26508_.setCanPassDoors(true);
        return new PathFinder(this.f_26508_, int0);
    }

    @Override
    protected boolean canMoveDirectly(Vec3 vec0, Vec3 vec1) {
        return m_262402_(this.f_26494_, vec0, vec1, true);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.m_26576_() && this.m_26574_() || !this.f_26494_.m_20159_();
    }

    @Override
    protected Vec3 getTempMobPos() {
        return this.f_26494_.m_20182_();
    }

    @Override
    public Path createPath(Entity entity0, int int1) {
        return this.m_7864_(entity0.blockPosition(), int1);
    }

    @Override
    public void tick() {
        this.f_26498_++;
        if (this.f_26506_) {
            this.m_26569_();
        }
        if (!this.m_26571_()) {
            if (this.canUpdatePath()) {
                this.m_7636_();
            } else if (this.f_26496_ != null && !this.f_26496_.isDone()) {
                Vec3 $$0 = this.f_26496_.getNextEntityPos(this.f_26494_);
                if (this.f_26494_.m_146903_() == Mth.floor($$0.x) && this.f_26494_.m_146904_() == Mth.floor($$0.y) && this.f_26494_.m_146907_() == Mth.floor($$0.z)) {
                    this.f_26496_.advance();
                }
            }
            DebugPackets.sendPathFindingPacket(this.f_26495_, this.f_26494_, this.f_26496_, this.f_26505_);
            if (!this.m_26571_()) {
                Vec3 $$1 = this.f_26496_.getNextEntityPos(this.f_26494_);
                this.f_26494_.getMoveControl().setWantedPosition($$1.x, $$1.y, $$1.z, this.f_26497_);
            }
        }
    }

    public void setCanOpenDoors(boolean boolean0) {
        this.f_26508_.setCanOpenDoors(boolean0);
    }

    public boolean canPassDoors() {
        return this.f_26508_.canPassDoors();
    }

    public void setCanPassDoors(boolean boolean0) {
        this.f_26508_.setCanPassDoors(boolean0);
    }

    public boolean canOpenDoors() {
        return this.f_26508_.canPassDoors();
    }

    @Override
    public boolean isStableDestination(BlockPos blockPos0) {
        return this.f_26495_.getBlockState(blockPos0).m_60634_(this.f_26495_, blockPos0, this.f_26494_);
    }
}