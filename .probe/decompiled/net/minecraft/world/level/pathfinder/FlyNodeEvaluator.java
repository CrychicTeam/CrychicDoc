package net.minecraft.world.level.pathfinder;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class FlyNodeEvaluator extends WalkNodeEvaluator {

    private final Long2ObjectMap<BlockPathTypes> pathTypeByPosCache = new Long2ObjectOpenHashMap();

    private static final float SMALL_MOB_INFLATED_START_NODE_BOUNDING_BOX = 1.5F;

    private static final int MAX_START_NODE_CANDIDATES = 10;

    @Override
    public void prepare(PathNavigationRegion pathNavigationRegion0, Mob mob1) {
        super.prepare(pathNavigationRegion0, mob1);
        this.pathTypeByPosCache.clear();
        mob1.onPathfindingStart();
    }

    @Override
    public void done() {
        this.f_77313_.onPathfindingDone();
        this.pathTypeByPosCache.clear();
        super.done();
    }

    @Override
    public Node getStart() {
        int $$0;
        if (this.m_77361_() && this.f_77313_.m_20069_()) {
            $$0 = this.f_77313_.m_146904_();
            BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos(this.f_77313_.m_20185_(), (double) $$0, this.f_77313_.m_20189_());
            for (BlockState $$2 = this.f_77312_.getBlockState($$1); $$2.m_60713_(Blocks.WATER); $$2 = this.f_77312_.getBlockState($$1)) {
                $$1.set(this.f_77313_.m_20185_(), (double) (++$$0), this.f_77313_.m_20189_());
            }
        } else {
            $$0 = Mth.floor(this.f_77313_.m_20186_() + 0.5);
        }
        BlockPos $$4 = BlockPos.containing(this.f_77313_.m_20185_(), (double) $$0, this.f_77313_.m_20189_());
        if (!this.canStartAt($$4)) {
            for (BlockPos $$5 : this.iteratePathfindingStartNodeCandidatePositions(this.f_77313_)) {
                if (this.canStartAt($$5)) {
                    return super.getStartNode($$5);
                }
            }
        }
        return super.getStartNode($$4);
    }

    @Override
    protected boolean canStartAt(BlockPos blockPos0) {
        BlockPathTypes $$1 = this.m_77572_(this.f_77313_, blockPos0);
        return this.f_77313_.getPathfindingMalus($$1) >= 0.0F;
    }

    @Override
    public Target getGoal(double double0, double double1, double double2) {
        return this.m_230615_(this.m_5676_(Mth.floor(double0), Mth.floor(double1), Mth.floor(double2)));
    }

    @Override
    public int getNeighbors(Node[] node0, Node node1) {
        int $$2 = 0;
        Node $$3 = this.findAcceptedNode(node1.x, node1.y, node1.z + 1);
        if (this.isOpen($$3)) {
            node0[$$2++] = $$3;
        }
        Node $$4 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z);
        if (this.isOpen($$4)) {
            node0[$$2++] = $$4;
        }
        Node $$5 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z);
        if (this.isOpen($$5)) {
            node0[$$2++] = $$5;
        }
        Node $$6 = this.findAcceptedNode(node1.x, node1.y, node1.z - 1);
        if (this.isOpen($$6)) {
            node0[$$2++] = $$6;
        }
        Node $$7 = this.findAcceptedNode(node1.x, node1.y + 1, node1.z);
        if (this.isOpen($$7)) {
            node0[$$2++] = $$7;
        }
        Node $$8 = this.findAcceptedNode(node1.x, node1.y - 1, node1.z);
        if (this.isOpen($$8)) {
            node0[$$2++] = $$8;
        }
        Node $$9 = this.findAcceptedNode(node1.x, node1.y + 1, node1.z + 1);
        if (this.isOpen($$9) && this.hasMalus($$3) && this.hasMalus($$7)) {
            node0[$$2++] = $$9;
        }
        Node $$10 = this.findAcceptedNode(node1.x - 1, node1.y + 1, node1.z);
        if (this.isOpen($$10) && this.hasMalus($$4) && this.hasMalus($$7)) {
            node0[$$2++] = $$10;
        }
        Node $$11 = this.findAcceptedNode(node1.x + 1, node1.y + 1, node1.z);
        if (this.isOpen($$11) && this.hasMalus($$5) && this.hasMalus($$7)) {
            node0[$$2++] = $$11;
        }
        Node $$12 = this.findAcceptedNode(node1.x, node1.y + 1, node1.z - 1);
        if (this.isOpen($$12) && this.hasMalus($$6) && this.hasMalus($$7)) {
            node0[$$2++] = $$12;
        }
        Node $$13 = this.findAcceptedNode(node1.x, node1.y - 1, node1.z + 1);
        if (this.isOpen($$13) && this.hasMalus($$3) && this.hasMalus($$8)) {
            node0[$$2++] = $$13;
        }
        Node $$14 = this.findAcceptedNode(node1.x - 1, node1.y - 1, node1.z);
        if (this.isOpen($$14) && this.hasMalus($$4) && this.hasMalus($$8)) {
            node0[$$2++] = $$14;
        }
        Node $$15 = this.findAcceptedNode(node1.x + 1, node1.y - 1, node1.z);
        if (this.isOpen($$15) && this.hasMalus($$5) && this.hasMalus($$8)) {
            node0[$$2++] = $$15;
        }
        Node $$16 = this.findAcceptedNode(node1.x, node1.y - 1, node1.z - 1);
        if (this.isOpen($$16) && this.hasMalus($$6) && this.hasMalus($$8)) {
            node0[$$2++] = $$16;
        }
        Node $$17 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z - 1);
        if (this.isOpen($$17) && this.hasMalus($$6) && this.hasMalus($$5)) {
            node0[$$2++] = $$17;
        }
        Node $$18 = this.findAcceptedNode(node1.x + 1, node1.y, node1.z + 1);
        if (this.isOpen($$18) && this.hasMalus($$3) && this.hasMalus($$5)) {
            node0[$$2++] = $$18;
        }
        Node $$19 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z - 1);
        if (this.isOpen($$19) && this.hasMalus($$6) && this.hasMalus($$4)) {
            node0[$$2++] = $$19;
        }
        Node $$20 = this.findAcceptedNode(node1.x - 1, node1.y, node1.z + 1);
        if (this.isOpen($$20) && this.hasMalus($$3) && this.hasMalus($$4)) {
            node0[$$2++] = $$20;
        }
        Node $$21 = this.findAcceptedNode(node1.x + 1, node1.y + 1, node1.z - 1);
        if (this.isOpen($$21) && this.hasMalus($$17) && this.hasMalus($$6) && this.hasMalus($$5) && this.hasMalus($$7) && this.hasMalus($$12) && this.hasMalus($$11)) {
            node0[$$2++] = $$21;
        }
        Node $$22 = this.findAcceptedNode(node1.x + 1, node1.y + 1, node1.z + 1);
        if (this.isOpen($$22) && this.hasMalus($$18) && this.hasMalus($$3) && this.hasMalus($$5) && this.hasMalus($$7) && this.hasMalus($$9) && this.hasMalus($$11)) {
            node0[$$2++] = $$22;
        }
        Node $$23 = this.findAcceptedNode(node1.x - 1, node1.y + 1, node1.z - 1);
        if (this.isOpen($$23) && this.hasMalus($$19) && this.hasMalus($$6) && this.hasMalus($$4) && this.hasMalus($$7) && this.hasMalus($$12) && this.hasMalus($$10)) {
            node0[$$2++] = $$23;
        }
        Node $$24 = this.findAcceptedNode(node1.x - 1, node1.y + 1, node1.z + 1);
        if (this.isOpen($$24) && this.hasMalus($$20) && this.hasMalus($$3) && this.hasMalus($$4) && this.hasMalus($$7) && this.hasMalus($$9) && this.hasMalus($$10)) {
            node0[$$2++] = $$24;
        }
        Node $$25 = this.findAcceptedNode(node1.x + 1, node1.y - 1, node1.z - 1);
        if (this.isOpen($$25) && this.hasMalus($$17) && this.hasMalus($$6) && this.hasMalus($$5) && this.hasMalus($$8) && this.hasMalus($$16) && this.hasMalus($$15)) {
            node0[$$2++] = $$25;
        }
        Node $$26 = this.findAcceptedNode(node1.x + 1, node1.y - 1, node1.z + 1);
        if (this.isOpen($$26) && this.hasMalus($$18) && this.hasMalus($$3) && this.hasMalus($$5) && this.hasMalus($$8) && this.hasMalus($$13) && this.hasMalus($$15)) {
            node0[$$2++] = $$26;
        }
        Node $$27 = this.findAcceptedNode(node1.x - 1, node1.y - 1, node1.z - 1);
        if (this.isOpen($$27) && this.hasMalus($$19) && this.hasMalus($$6) && this.hasMalus($$4) && this.hasMalus($$8) && this.hasMalus($$16) && this.hasMalus($$14)) {
            node0[$$2++] = $$27;
        }
        Node $$28 = this.findAcceptedNode(node1.x - 1, node1.y - 1, node1.z + 1);
        if (this.isOpen($$28) && this.hasMalus($$20) && this.hasMalus($$3) && this.hasMalus($$4) && this.hasMalus($$8) && this.hasMalus($$13) && this.hasMalus($$14)) {
            node0[$$2++] = $$28;
        }
        return $$2;
    }

    private boolean hasMalus(@Nullable Node node0) {
        return node0 != null && node0.costMalus >= 0.0F;
    }

    private boolean isOpen(@Nullable Node node0) {
        return node0 != null && !node0.closed;
    }

    @Nullable
    protected Node findAcceptedNode(int int0, int int1, int int2) {
        Node $$3 = null;
        BlockPathTypes $$4 = this.getCachedBlockPathType(int0, int1, int2);
        float $$5 = this.f_77313_.getPathfindingMalus($$4);
        if ($$5 >= 0.0F) {
            $$3 = this.m_5676_(int0, int1, int2);
            $$3.type = $$4;
            $$3.costMalus = Math.max($$3.costMalus, $$5);
            if ($$4 == BlockPathTypes.WALKABLE) {
                $$3.costMalus++;
            }
        }
        return $$3;
    }

    private BlockPathTypes getCachedBlockPathType(int int0, int int1, int int2) {
        return (BlockPathTypes) this.pathTypeByPosCache.computeIfAbsent(BlockPos.asLong(int0, int1, int2), p_265010_ -> this.getBlockPathType(this.f_77312_, int0, int1, int2, this.f_77313_));
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3, Mob mob4) {
        EnumSet<BlockPathTypes> $$5 = EnumSet.noneOf(BlockPathTypes.class);
        BlockPathTypes $$6 = BlockPathTypes.BLOCKED;
        BlockPos $$7 = mob4.m_20183_();
        $$6 = super.getBlockPathTypes(blockGetter0, int1, int2, int3, $$5, $$6, $$7);
        if ($$5.contains(BlockPathTypes.FENCE)) {
            return BlockPathTypes.FENCE;
        } else {
            BlockPathTypes $$8 = BlockPathTypes.BLOCKED;
            for (BlockPathTypes $$9 : $$5) {
                if (mob4.getPathfindingMalus($$9) < 0.0F) {
                    return $$9;
                }
                if (mob4.getPathfindingMalus($$9) >= mob4.getPathfindingMalus($$8)) {
                    $$8 = $$9;
                }
            }
            return $$6 == BlockPathTypes.OPEN && mob4.getPathfindingMalus($$8) == 0.0F ? BlockPathTypes.OPEN : $$8;
        }
    }

    @Override
    public BlockPathTypes getBlockPathType(BlockGetter blockGetter0, int int1, int int2, int int3) {
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        BlockPathTypes $$5 = m_77643_(blockGetter0, $$4.set(int1, int2, int3));
        if ($$5 == BlockPathTypes.OPEN && int2 >= blockGetter0.m_141937_() + 1) {
            BlockPathTypes $$6 = m_77643_(blockGetter0, $$4.set(int1, int2 - 1, int3));
            if ($$6 == BlockPathTypes.DAMAGE_FIRE || $$6 == BlockPathTypes.LAVA) {
                $$5 = BlockPathTypes.DAMAGE_FIRE;
            } else if ($$6 == BlockPathTypes.DAMAGE_OTHER) {
                $$5 = BlockPathTypes.DAMAGE_OTHER;
            } else if ($$6 == BlockPathTypes.COCOA) {
                $$5 = BlockPathTypes.COCOA;
            } else if ($$6 == BlockPathTypes.FENCE) {
                if (!$$4.equals(this.f_77313_.m_20183_())) {
                    $$5 = BlockPathTypes.FENCE;
                }
            } else {
                $$5 = $$6 != BlockPathTypes.WALKABLE && $$6 != BlockPathTypes.OPEN && $$6 != BlockPathTypes.WATER ? BlockPathTypes.WALKABLE : BlockPathTypes.OPEN;
            }
        }
        if ($$5 == BlockPathTypes.WALKABLE || $$5 == BlockPathTypes.OPEN) {
            $$5 = m_77607_(blockGetter0, $$4.set(int1, int2, int3), $$5);
        }
        return $$5;
    }

    private Iterable<BlockPos> iteratePathfindingStartNodeCandidatePositions(Mob mob0) {
        float $$1 = 1.0F;
        AABB $$2 = mob0.m_20191_();
        boolean $$3 = $$2.getSize() < 1.0;
        if (!$$3) {
            return List.of(BlockPos.containing($$2.minX, (double) mob0.m_146904_(), $$2.minZ), BlockPos.containing($$2.minX, (double) mob0.m_146904_(), $$2.maxZ), BlockPos.containing($$2.maxX, (double) mob0.m_146904_(), $$2.minZ), BlockPos.containing($$2.maxX, (double) mob0.m_146904_(), $$2.maxZ));
        } else {
            double $$4 = Math.max(0.0, (1.5 - $$2.getZsize()) / 2.0);
            double $$5 = Math.max(0.0, (1.5 - $$2.getXsize()) / 2.0);
            double $$6 = Math.max(0.0, (1.5 - $$2.getYsize()) / 2.0);
            AABB $$7 = $$2.inflate($$5, $$6, $$4);
            return BlockPos.randomBetweenClosed(mob0.m_217043_(), 10, Mth.floor($$7.minX), Mth.floor($$7.minY), Mth.floor($$7.minZ), Mth.floor($$7.maxX), Mth.floor($$7.maxY), Mth.floor($$7.maxZ));
        }
    }
}