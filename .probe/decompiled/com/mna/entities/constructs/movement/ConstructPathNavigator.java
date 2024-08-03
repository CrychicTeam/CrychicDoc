package com.mna.entities.constructs.movement;

import com.mna.entities.constructs.animated.Construct;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.Vec3;

public class ConstructPathNavigator extends PathNavigation {

    private final Construct construct;

    public ConstructPathNavigator(Construct construct, Level level) {
        super(construct, level);
        this.construct = construct;
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        this.f_26508_ = new ConstructNodeEvaluator();
        this.f_26508_.setCanPassDoors(true);
        this.f_26508_.setCanFloat(true);
        this.f_26508_.setCanOpenDoors(true);
        return new PathFinder(this.f_26508_, maxVisitedNodes);
    }

    @Override
    protected Vec3 getTempMobPos() {
        return this.construct.canFly() && !this.construct.m_20096_() ? this.f_26494_.m_20182_() : new Vec3(this.f_26494_.m_20185_(), (double) this.getSurfaceY(), this.f_26494_.m_20189_());
    }

    @Override
    protected boolean canUpdatePath() {
        return this.construct.canFly() ? this.m_26576_() && this.m_26574_() || !this.f_26494_.m_20159_() : this.f_26494_.m_20096_() || this.m_26574_() || this.f_26494_.m_20159_();
    }

    @Override
    public boolean isStableDestination(BlockPos target) {
        return this.construct.canFly() && !this.construct.m_20096_() ? this.f_26495_.getBlockState(target).m_60634_(this.f_26495_, target, this.f_26494_) : super.isStableDestination(target);
    }

    @Override
    public Path createPath(BlockPos target, int reachRange) {
        if (!this.construct.canFly() && (!this.construct.canSwim() || !this.construct.m_20072_())) {
            if (this.f_26495_.getBlockState(target).m_60795_()) {
                BlockPos blockpos = target.below();
                while (blockpos.m_123342_() > this.f_26495_.m_141937_() && this.f_26495_.getBlockState(blockpos).m_60795_()) {
                    blockpos = blockpos.below();
                }
                if (blockpos.m_123342_() > this.f_26495_.m_141937_()) {
                    return super.createPath(blockpos.above(), reachRange);
                }
                while (blockpos.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState(blockpos).m_60795_()) {
                    blockpos = blockpos.above();
                }
                target = blockpos;
            }
            if (!this.f_26495_.getBlockState(target).m_280296_()) {
                return super.createPath(target, reachRange);
            } else {
                BlockPos blockpos1 = target.above();
                while (blockpos1.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState(blockpos1).m_280296_()) {
                    blockpos1 = blockpos1.above();
                }
                return super.createPath(blockpos1, reachRange);
            }
        } else {
            return super.createPath(target, reachRange);
        }
    }

    @Override
    public Path createPath(Entity target, int reachRange) {
        return this.createPath(target.blockPosition(), reachRange);
    }

    @Override
    protected boolean canMoveDirectly(Vec3 from, Vec3 to) {
        return m_262402_(this.f_26494_, from, to, false);
    }

    private int getSurfaceY() {
        if (this.f_26494_.m_20069_() && this.m_26576_()) {
            int mobBlockY = this.f_26494_.m_146904_();
            BlockState blockstate = this.f_26495_.getBlockState(BlockPos.containing(this.f_26494_.m_20185_(), (double) mobBlockY, this.f_26494_.m_20189_()));
            int count = 0;
            while (blockstate.m_60713_(Blocks.WATER)) {
                blockstate = this.f_26495_.getBlockState(BlockPos.containing(this.f_26494_.m_20185_(), (double) (++mobBlockY), this.f_26494_.m_20189_()));
                if (++count > 16) {
                    return this.f_26494_.m_146904_();
                }
            }
            return mobBlockY;
        } else {
            return Mth.floor(this.f_26494_.m_20186_() + 0.5);
        }
    }

    public void setEvaluatorFlags(boolean flying, boolean amphibious) {
        if (this.f_26508_ instanceof ConstructNodeEvaluator) {
            ((ConstructNodeEvaluator) this.f_26508_).setFlying(flying);
            ((ConstructNodeEvaluator) this.f_26508_).setAmphibious(amphibious);
        }
    }
}