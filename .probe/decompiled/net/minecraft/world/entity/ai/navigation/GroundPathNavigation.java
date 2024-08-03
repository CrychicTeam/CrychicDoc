package net.minecraft.world.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;

public class GroundPathNavigation extends PathNavigation {

    private boolean avoidSun;

    public GroundPathNavigation(Mob mob0, Level level1) {
        super(mob0, level1);
    }

    @Override
    protected PathFinder createPathFinder(int int0) {
        this.f_26508_ = new WalkNodeEvaluator();
        this.f_26508_.setCanPassDoors(true);
        return new PathFinder(this.f_26508_, int0);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.f_26494_.m_20096_() || this.m_26574_() || this.f_26494_.m_20159_();
    }

    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.f_26494_.m_20185_(), (double) this.getSurfaceY(), this.f_26494_.m_20189_());
    }

    @Override
    public Path createPath(BlockPos blockPos0, int int1) {
        if (this.f_26495_.getBlockState(blockPos0).m_60795_()) {
            BlockPos $$2 = blockPos0.below();
            while ($$2.m_123342_() > this.f_26495_.m_141937_() && this.f_26495_.getBlockState($$2).m_60795_()) {
                $$2 = $$2.below();
            }
            if ($$2.m_123342_() > this.f_26495_.m_141937_()) {
                return super.createPath($$2.above(), int1);
            }
            while ($$2.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState($$2).m_60795_()) {
                $$2 = $$2.above();
            }
            blockPos0 = $$2;
        }
        if (!this.f_26495_.getBlockState(blockPos0).m_280296_()) {
            return super.createPath(blockPos0, int1);
        } else {
            BlockPos $$3 = blockPos0.above();
            while ($$3.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState($$3).m_280296_()) {
                $$3 = $$3.above();
            }
            return super.createPath($$3, int1);
        }
    }

    @Override
    public Path createPath(Entity entity0, int int1) {
        return this.createPath(entity0.blockPosition(), int1);
    }

    private int getSurfaceY() {
        if (this.f_26494_.m_20069_() && this.m_26576_()) {
            int $$0 = this.f_26494_.m_146904_();
            BlockState $$1 = this.f_26495_.getBlockState(BlockPos.containing(this.f_26494_.m_20185_(), (double) $$0, this.f_26494_.m_20189_()));
            int $$2 = 0;
            while ($$1.m_60713_(Blocks.WATER)) {
                $$1 = this.f_26495_.getBlockState(BlockPos.containing(this.f_26494_.m_20185_(), (double) (++$$0), this.f_26494_.m_20189_()));
                if (++$$2 > 16) {
                    return this.f_26494_.m_146904_();
                }
            }
            return $$0;
        } else {
            return Mth.floor(this.f_26494_.m_20186_() + 0.5);
        }
    }

    @Override
    protected void trimPath() {
        super.trimPath();
        if (this.avoidSun) {
            if (this.f_26495_.m_45527_(BlockPos.containing(this.f_26494_.m_20185_(), this.f_26494_.m_20186_() + 0.5, this.f_26494_.m_20189_()))) {
                return;
            }
            for (int $$0 = 0; $$0 < this.f_26496_.getNodeCount(); $$0++) {
                Node $$1 = this.f_26496_.getNode($$0);
                if (this.f_26495_.m_45527_(new BlockPos($$1.x, $$1.y, $$1.z))) {
                    this.f_26496_.truncateNodes($$0);
                    return;
                }
            }
        }
    }

    protected boolean hasValidPathType(BlockPathTypes blockPathTypes0) {
        if (blockPathTypes0 == BlockPathTypes.WATER) {
            return false;
        } else {
            return blockPathTypes0 == BlockPathTypes.LAVA ? false : blockPathTypes0 != BlockPathTypes.OPEN;
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

    public void setAvoidSun(boolean boolean0) {
        this.avoidSun = boolean0;
    }

    public void setCanWalkOverFences(boolean boolean0) {
        this.f_26508_.setCanWalkOverFences(boolean0);
    }
}