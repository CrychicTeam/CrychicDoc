package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PathNavigateDeathWormLand extends PathNavigation {

    private boolean shouldAvoidSun;

    private final EntityDeathWorm worm;

    public PathNavigateDeathWormLand(EntityDeathWorm worm, Level worldIn) {
        super(worm, worldIn);
        this.worm = worm;
    }

    @NotNull
    @Override
    protected PathFinder createPathFinder(int i) {
        this.f_26508_ = new WalkNodeEvaluator();
        this.f_26508_.setCanPassDoors(true);
        this.f_26508_.setCanFloat(true);
        return new PathFinder(this.f_26508_, i);
    }

    @Override
    protected boolean canUpdatePath() {
        return this.f_26494_.m_20096_() || this.worm.isInSand() || this.f_26494_.m_20159_();
    }

    @NotNull
    @Override
    protected Vec3 getTempMobPos() {
        return new Vec3(this.f_26494_.m_20185_(), (double) this.getPathablePosY(), this.f_26494_.m_20189_());
    }

    @Override
    public Path createPath(@NotNull BlockPos pos, int i) {
        if (this.f_26495_.getBlockState(pos).m_60795_()) {
            BlockPos blockpos = pos.below();
            while (blockpos.m_123342_() > 0 && this.f_26495_.getBlockState(blockpos).m_60795_()) {
                blockpos = blockpos.below();
            }
            if (blockpos.m_123342_() > 0) {
                return super.createPath(blockpos.above(), i);
            }
            while (blockpos.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState(blockpos).m_60795_()) {
                blockpos = blockpos.above();
            }
            pos = blockpos;
        }
        if (!this.f_26495_.getBlockState(pos).m_280296_()) {
            return super.createPath(pos, i);
        } else {
            BlockPos blockpos1 = pos.above();
            while (blockpos1.m_123342_() < this.f_26495_.m_151558_() && this.f_26495_.getBlockState(blockpos1).m_280296_()) {
                blockpos1 = blockpos1.above();
            }
            return super.createPath(blockpos1, i);
        }
    }

    @Override
    public Path createPath(Entity entityIn, int i) {
        return this.createPath(entityIn.blockPosition(), i);
    }

    private int getPathablePosY() {
        if (this.worm.isInSand()) {
            int i = (int) this.f_26494_.m_20191_().minY;
            BlockState blockstate = this.f_26495_.getBlockState(new BlockPos(this.f_26494_.m_146903_(), i, this.f_26494_.m_146907_()));
            int j = 0;
            while (blockstate.m_204336_(BlockTags.SAND)) {
                blockstate = this.f_26495_.getBlockState(new BlockPos(this.f_26494_.m_146903_(), ++i, this.f_26494_.m_146907_()));
                if (++j > 16) {
                    return (int) this.f_26494_.m_20191_().minY;
                }
            }
            return i;
        } else {
            return (int) (this.f_26494_.m_20191_().minY + 0.5);
        }
    }

    protected void removeSunnyPath() {
        if (this.shouldAvoidSun) {
            if (this.f_26495_.m_45527_(BlockPos.containing((double) this.f_26494_.m_146903_(), this.f_26494_.m_20191_().minY + 0.5, (double) this.f_26494_.m_146907_()))) {
                return;
            }
            for (int i = 0; i < this.f_26496_.getNodeCount(); i++) {
                Node pathpoint = this.f_26496_.getNode(i);
                if (this.f_26495_.m_45527_(new BlockPos(pathpoint.x, pathpoint.y, pathpoint.z))) {
                    this.f_26496_.truncateNodes(i - 1);
                    return;
                }
            }
        }
    }

    @Override
    protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
        int i = Mth.floor(posVec31.x);
        int j = Mth.floor(posVec31.z);
        double d0 = posVec32.x - posVec31.x;
        double d1 = posVec32.z - posVec31.z;
        double d2 = d0 * d0 + d1 * d1;
        int sizeX = (int) this.worm.m_20191_().getXsize();
        int sizeY = (int) this.worm.m_20191_().getYsize();
        int sizeZ = (int) this.worm.m_20191_().getZsize();
        if (d2 < 1.0E-8) {
            return false;
        } else {
            double d3 = 1.0 / Math.sqrt(d2);
            d0 *= d3;
            d1 *= d3;
            sizeX += 2;
            sizeZ += 2;
            if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                return false;
            } else {
                sizeX -= 2;
                sizeZ -= 2;
                double d4 = 1.0 / Math.abs(d0);
                double d5 = 1.0 / Math.abs(d1);
                double d6 = (double) i - posVec31.x;
                double d7 = (double) j - posVec31.z;
                if (d0 >= 0.0) {
                    d6++;
                }
                if (d1 >= 0.0) {
                    d7++;
                }
                d6 /= d0;
                d7 /= d1;
                int k = d0 < 0.0 ? -1 : 1;
                int l = d1 < 0.0 ? -1 : 1;
                int i1 = Mth.floor(posVec32.x);
                int j1 = Mth.floor(posVec32.z);
                int k1 = i1 - i;
                int l1 = j1 - j;
                while (k1 * k > 0 || l1 * l > 0) {
                    if (d6 < d7) {
                        d6 += d4;
                        i += k;
                        k1 = i1 - i;
                    } else {
                        d7 += d5;
                        j += l;
                        l1 = j1 - j;
                    }
                    if (!this.isSafeToStandAt(i, (int) posVec31.y, j, sizeX, sizeY, sizeZ, posVec31, d0, d1)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private boolean isSafeToStandAt(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 vec31, double p_179683_8_, double p_179683_10_) {
        int i = x - sizeX / 2;
        int j = z - sizeZ / 2;
        if (!this.isPositionClear(i, y, j, sizeX, sizeY, sizeZ, vec31, p_179683_8_, p_179683_10_)) {
            return false;
        } else {
            for (int k = i; k < i + sizeX; k++) {
                for (int l = j; l < j + sizeZ; l++) {
                    double d0 = (double) k + 0.5 - vec31.x;
                    double d1 = (double) l + 0.5 - vec31.z;
                    if (d0 * p_179683_8_ + d1 * p_179683_10_ >= 0.0) {
                        BlockPathTypes pathnodetype = this.f_26508_.getBlockPathType(this.f_26495_, k, y - 1, l, this.f_26494_);
                        if (pathnodetype == BlockPathTypes.LAVA) {
                            return false;
                        }
                        pathnodetype = this.f_26508_.getBlockPathType(this.f_26495_, k, y, l, this.f_26494_);
                        float f = this.f_26494_.getPathfindingMalus(pathnodetype);
                        if (f < 0.0F || f >= 8.0F) {
                            return false;
                        }
                        if (pathnodetype == BlockPathTypes.DAMAGE_FIRE || pathnodetype == BlockPathTypes.DANGER_FIRE || pathnodetype == BlockPathTypes.DAMAGE_OTHER) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    private boolean isPositionClear(int x, int y, int z, int sizeX, int sizeY, int sizeZ, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_) {
        for (BlockPos blockpos : (List) BlockPos.betweenClosedStream(new BlockPos(x, y, z), new BlockPos(x + sizeX - 1, y + sizeY - 1, z + sizeZ - 1)).collect(Collectors.toList())) {
            double d0 = (double) blockpos.m_123341_() + 0.5 - p_179692_7_.x;
            double d1 = (double) blockpos.m_123343_() + 0.5 - p_179692_7_.z;
            if (d0 * p_179692_8_ + d1 * p_179692_10_ >= 0.0) {
                Block block = this.f_26495_.getBlockState(blockpos).m_60734_();
                if (this.f_26495_.getBlockState(blockpos).m_280555_() || this.f_26495_.getBlockState(blockpos).m_204336_(BlockTags.SAND)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void setBreakDoors(boolean canBreakDoors) {
        this.f_26508_.setCanOpenDoors(canBreakDoors);
    }

    public boolean getEnterDoors() {
        return this.f_26508_.canPassDoors();
    }

    public void setEnterDoors(boolean enterDoors) {
        this.f_26508_.setCanPassDoors(enterDoors);
    }

    @Override
    public boolean canFloat() {
        return this.f_26508_.canFloat();
    }

    @Override
    public void setCanFloat(boolean canSwim) {
        this.f_26508_.setCanFloat(canSwim);
    }

    public void setAvoidSun(boolean avoidSun) {
        this.shouldAvoidSun = avoidSun;
    }
}