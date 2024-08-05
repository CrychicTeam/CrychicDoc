package com.github.alexthe666.iceandfire.pathfinding;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Target;
import org.jetbrains.annotations.NotNull;

public class NodeProcessorDeathWorm extends NodeEvaluator {

    @NotNull
    @Override
    public Node getStart() {
        return this.m_5676_(Mth.floor(this.f_77313_.m_20191_().minX), Mth.floor(this.f_77313_.m_20191_().minY + 0.5), Mth.floor(this.f_77313_.m_20191_().minZ));
    }

    @NotNull
    @Override
    public Target getGoal(double x, double y, double z) {
        return new Target(this.m_5676_(Mth.floor(x - 0.4), Mth.floor(y + 0.5), Mth.floor(z - 0.4)));
    }

    @NotNull
    @Override
    public BlockPathTypes getBlockPathType(@NotNull BlockGetter blockaccessIn, int x, int y, int z, @NotNull Mob entitylivingIn) {
        return this.getBlockPathType(blockaccessIn, x, y, z);
    }

    @NotNull
    @Override
    public BlockPathTypes getBlockPathType(BlockGetter worldIn, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        if (this.isPassable(worldIn, blockpos.below()) || !blockstate.m_60795_() && !this.isPassable(worldIn, blockpos)) {
            return this.isPassable(worldIn, blockpos) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
        } else {
            return BlockPathTypes.BREACH;
        }
    }

    @Override
    public int getNeighbors(@NotNull Node[] p_222859_1_, @NotNull Node p_222859_2_) {
        int i = 0;
        for (Direction direction : Direction.values()) {
            Node pathpoint = this.getSandNode(p_222859_2_.x + direction.getStepX(), p_222859_2_.y + direction.getStepY(), p_222859_2_.z + direction.getStepZ());
            if (pathpoint != null && !pathpoint.closed) {
                p_222859_1_[i++] = pathpoint;
            }
        }
        return i;
    }

    @Nullable
    private Node getSandNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        BlockPathTypes pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return pathnodetype != BlockPathTypes.BREACH && pathnodetype != BlockPathTypes.WATER ? null : this.m_5676_(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    private BlockPathTypes isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (int i = p_186327_1_; i < p_186327_1_ + this.f_77315_; i++) {
            for (int j = p_186327_2_; j < p_186327_2_ + this.f_77316_; j++) {
                for (int k = p_186327_3_; k < p_186327_3_ + this.f_77317_; k++) {
                    BlockState blockstate = this.f_77312_.getBlockState(blockpos$mutable.set(i, j, k));
                    if (!this.isPassable(this.f_77312_, blockpos$mutable.m_7495_()) && (blockstate.m_60795_() || this.isPassable(this.f_77312_, blockpos$mutable))) {
                        return BlockPathTypes.BREACH;
                    }
                }
            }
        }
        BlockState blockstate1 = this.f_77312_.getBlockState(blockpos$mutable);
        return this.isPassable(blockstate1) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }

    private boolean isPassable(BlockGetter world, BlockPos pos) {
        return world.getBlockState(pos).m_204336_(BlockTags.SAND) || world.getBlockState(pos).m_60795_();
    }

    private boolean isPassable(BlockState state) {
        return state.m_204336_(BlockTags.SAND) || state.m_60795_();
    }
}