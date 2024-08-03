package net.minecraft.world.level.pathfinder;

import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class SwimNodeEvaluator extends NodeEvaluator {

    private final boolean allowBreaching;

    private final Long2ObjectMap<BlockPathTypes> pathTypesByPosCache = new Long2ObjectOpenHashMap();

    public SwimNodeEvaluator(boolean boolean0) {
        this.allowBreaching = boolean0;
    }

    @Override
    public void prepare(PathNavigationRegion pathNavigationRegion0, Mob mob1) {
        super.prepare(pathNavigationRegion0, mob1);
        this.pathTypesByPosCache.clear();
    }

    @Override
    public void done() {
        super.done();
        this.pathTypesByPosCache.clear();
    }

    @Override
    public Node getStart() {
        return this.m_5676_(Mth.floor(this.f_77313_.m_20191_().minX), Mth.floor(this.f_77313_.m_20191_().minY + 0.5), Mth.floor(this.f_77313_.m_20191_().minZ));
    }

    @Override
    public Target getGoal(double double0, double double1, double double2) {
        return this.m_230615_(this.m_5676_(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2)));
    }

    @Override
    public int getNeighbors(Node[] node0, Node node1) {
        int $$2 = 0;
        Map<Direction, Node> $$3 = Maps.newEnumMap(Direction.class);
        for (Direction $$4 : Direction.values()) {
            Node $$5 = this.findAcceptedNode(node1.x + $$4.getStepX(), node1.y + $$4.getStepY(), node1.z + $$4.getStepZ());
            $$3.put($$4, $$5);
            if (this.isNodeValid($$5)) {
                node0[$$2++] = $$5;
            }
        }
        for (Direction $$6 : Direction.Plane.HORIZONTAL) {
            Direction $$7 = $$6.getClockWise();
            Node $$8 = this.findAcceptedNode(node1.x + $$6.getStepX() + $$7.getStepX(), node1.y, node1.z + $$6.getStepZ() + $$7.getStepZ());
            if (this.isDiagonalNodeValid($$8, (Node) $$3.get($$6), (Node) $$3.get($$7))) {
                node0[$$2++] = $$8;
            }
        }
        return $$2;
    }

    protected boolean isNodeValid(@Nullable Node node0) {
        return node0 != null && !node0.closed;
    }

    protected boolean isDiagonalNodeValid(@Nullable Node node0, @Nullable Node node1, @Nullable Node node2) {
        return this.isNodeValid(node0) && node1 != null && node1.costMalus >= 0.0F && node2 != null && node2.costMalus >= 0.0F;
    }

    @Nullable
    protected Node findAcceptedNode(int int0, int int1, int int2) {
        Node $$3 = null;
        BlockPathTypes $$4 = this.getCachedBlockType(int0, int1, int2);
        if (this.allowBreaching && $$4 == BlockPathTypes.BREACH || $$4 == BlockPathTypes.WATER) {
            float $$5 = this.f_77313_.getPathfindingMalus($$4);
            if ($$5 >= 0.0F) {
                $$3 = this.m_5676_(int0, int1, int2);
                $$3.type = $$4;
                $$3.costMalus = Math.max($$3.costMalus, $$5);
                if (this.f_77312_.getFluidState(new BlockPos(int0, int1, int2)).isEmpty()) {
                    $$3.costMalus += 8.0F;
                }
            }
        }
        return $$3;
    }

    protected BlockPathTypes getCachedBlockType(int int0, int int1, int int2) {
        return (BlockPathTypes) this.pathTypesByPosCache.computeIfAbsent(BlockPos.asLong(int0, int1, int2), p_192957_ -> this.getBlockPathType(this.f_77312_, int0, int1, int2));
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3) {
        return this.getBlockPathType(blockGetter0, int1, int2, int3, this.f_77313_);
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3, Mob mob4) {
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (int $$6 = int1; $$6 < int1 + this.f_77315_; $$6++) {
            for (int $$7 = int2; $$7 < int2 + this.f_77316_; $$7++) {
                for (int $$8 = int3; $$8 < int3 + this.f_77317_; $$8++) {
                    FluidState $$9 = blockGetter0.getFluidState($$5.set($$6, $$7, $$8));
                    BlockState $$10 = blockGetter0.getBlockState($$5.set($$6, $$7, $$8));
                    if ($$9.isEmpty() && $$10.m_60647_(blockGetter0, $$5.m_7495_(), PathComputationType.WATER) && $$10.m_60795_()) {
                        return BlockPathTypes.BREACH;
                    }
                    if (!$$9.is(FluidTags.WATER)) {
                        return BlockPathTypes.BLOCKED;
                    }
                }
            }
        }
        BlockState $$11 = blockGetter0.getBlockState($$5);
        return $$11.m_60647_(blockGetter0, $$5, PathComputationType.WATER) ? BlockPathTypes.WATER : BlockPathTypes.BLOCKED;
    }
}