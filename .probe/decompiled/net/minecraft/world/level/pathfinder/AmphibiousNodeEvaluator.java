package net.minecraft.world.level.pathfinder;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;

public class AmphibiousNodeEvaluator extends WalkNodeEvaluator {

    private final boolean prefersShallowSwimming;

    private float oldWalkableCost;

    private float oldWaterBorderCost;

    public AmphibiousNodeEvaluator(boolean boolean0) {
        this.prefersShallowSwimming = boolean0;
    }

    @Override
    public void prepare(PathNavigationRegion pathNavigationRegion0, Mob mob1) {
        super.prepare(pathNavigationRegion0, mob1);
        mob1.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.oldWalkableCost = mob1.getPathfindingMalus(BlockPathTypes.WALKABLE);
        mob1.setPathfindingMalus(BlockPathTypes.WALKABLE, 6.0F);
        this.oldWaterBorderCost = mob1.getPathfindingMalus(BlockPathTypes.WATER_BORDER);
        mob1.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 4.0F);
    }

    @Override
    public void done() {
        this.f_77313_.setPathfindingMalus(BlockPathTypes.WALKABLE, this.oldWalkableCost);
        this.f_77313_.setPathfindingMalus(BlockPathTypes.WATER_BORDER, this.oldWaterBorderCost);
        super.done();
    }

    @Override
    public Node getStart() {
        return !this.f_77313_.m_20069_() ? super.getStart() : this.m_230631_(new BlockPos(Mth.floor(this.f_77313_.m_20191_().minX), Mth.floor(this.f_77313_.m_20191_().minY + 0.5), Mth.floor(this.f_77313_.m_20191_().minZ)));
    }

    @Override
    public Target getGoal(double double0, double double1, double double2) {
        return this.m_230615_(this.m_5676_(Mth.floor(double0), Mth.floor(double1 + 0.5), Mth.floor(double2)));
    }

    @Override
    public int getNeighbors(Node[] node0, Node node1) {
        int $$2 = super.getNeighbors(node0, node1);
        BlockPathTypes $$3 = this.m_77567_(this.f_77313_, node1.x, node1.y + 1, node1.z);
        BlockPathTypes $$4 = this.m_77567_(this.f_77313_, node1.x, node1.y, node1.z);
        int $$5;
        if (this.f_77313_.getPathfindingMalus($$3) >= 0.0F && $$4 != BlockPathTypes.STICKY_HONEY) {
            $$5 = Mth.floor(Math.max(1.0F, this.f_77313_.m_274421_()));
        } else {
            $$5 = 0;
        }
        double $$7 = this.m_142213_(new BlockPos(node1.x, node1.y, node1.z));
        Node $$8 = this.m_164725_(node1.x, node1.y + 1, node1.z, Math.max(0, $$5 - 1), $$7, Direction.UP, $$4);
        Node $$9 = this.m_164725_(node1.x, node1.y - 1, node1.z, $$5, $$7, Direction.DOWN, $$4);
        if (this.isVerticalNeighborValid($$8, node1)) {
            node0[$$2++] = $$8;
        }
        if (this.isVerticalNeighborValid($$9, node1) && $$4 != BlockPathTypes.TRAPDOOR) {
            node0[$$2++] = $$9;
        }
        for (int $$10 = 0; $$10 < $$2; $$10++) {
            Node $$11 = node0[$$10];
            if ($$11.type == BlockPathTypes.WATER && this.prefersShallowSwimming && $$11.y < this.f_77313_.m_9236_().getSeaLevel() - 10) {
                $$11.costMalus++;
            }
        }
        return $$2;
    }

    private boolean isVerticalNeighborValid(@Nullable Node node0, Node node1) {
        return this.m_77626_(node0, node1) && node0.type == BlockPathTypes.WATER;
    }

    @Override
    protected boolean isAmphibious() {
        return true;
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3) {
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        BlockPathTypes $$5 = m_77643_(blockGetter0, $$4.set(int1, int2, int3));
        if ($$5 == BlockPathTypes.WATER) {
            for (Direction $$6 : Direction.values()) {
                BlockPathTypes $$7 = m_77643_(blockGetter0, $$4.set(int1, int2, int3).move($$6));
                if ($$7 == BlockPathTypes.BLOCKED) {
                    return BlockPathTypes.WATER_BORDER;
                }
            }
            return BlockPathTypes.WATER;
        } else {
            return m_77604_(blockGetter0, $$4);
        }
    }
}