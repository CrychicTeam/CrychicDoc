package net.minecraft.world.level.portal;

import java.util.Comparator;
import java.util.Optional;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.TicketType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;

public class PortalForcer {

    private static final int TICKET_RADIUS = 3;

    private static final int SEARCH_RADIUS = 128;

    private static final int CREATE_RADIUS = 16;

    private static final int FRAME_HEIGHT = 5;

    private static final int FRAME_WIDTH = 4;

    private static final int FRAME_BOX = 3;

    private static final int FRAME_HEIGHT_START = -1;

    private static final int FRAME_HEIGHT_END = 4;

    private static final int FRAME_WIDTH_START = -1;

    private static final int FRAME_WIDTH_END = 3;

    private static final int FRAME_BOX_START = -1;

    private static final int FRAME_BOX_END = 2;

    private static final int NOTHING_FOUND = -1;

    private final ServerLevel level;

    public PortalForcer(ServerLevel serverLevel0) {
        this.level = serverLevel0;
    }

    public Optional<BlockUtil.FoundRectangle> findPortalAround(BlockPos blockPos0, boolean boolean1, WorldBorder worldBorder2) {
        PoiManager $$3 = this.level.getPoiManager();
        int $$4 = boolean1 ? 16 : 128;
        $$3.ensureLoadedAndValid(this.level, blockPos0, $$4);
        Optional<PoiRecord> $$5 = $$3.getInSquare(p_230634_ -> p_230634_.is(PoiTypes.NETHER_PORTAL), blockPos0, $$4, PoiManager.Occupancy.ANY).filter(p_192981_ -> worldBorder2.isWithinBounds(p_192981_.getPos())).sorted(Comparator.comparingDouble(p_192984_ -> p_192984_.getPos().m_123331_(blockPos0)).thenComparingInt(p_192992_ -> p_192992_.getPos().m_123342_())).filter(p_192990_ -> this.level.m_8055_(p_192990_.getPos()).m_61138_(BlockStateProperties.HORIZONTAL_AXIS)).findFirst();
        return $$5.map(p_192975_ -> {
            BlockPos $$1 = p_192975_.getPos();
            this.level.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos($$1), 3, $$1);
            BlockState $$2 = this.level.m_8055_($$1);
            return BlockUtil.getLargestRectangleAround($$1, (Direction.Axis) $$2.m_61143_(BlockStateProperties.HORIZONTAL_AXIS), 21, Direction.Axis.Y, 21, p_192978_ -> this.level.m_8055_(p_192978_) == $$2);
        });
    }

    public Optional<BlockUtil.FoundRectangle> createPortal(BlockPos blockPos0, Direction.Axis directionAxis1) {
        Direction $$2 = Direction.get(Direction.AxisDirection.POSITIVE, directionAxis1);
        double $$3 = -1.0;
        BlockPos $$4 = null;
        double $$5 = -1.0;
        BlockPos $$6 = null;
        WorldBorder $$7 = this.level.m_6857_();
        int $$8 = Math.min(this.level.m_151558_(), this.level.m_141937_() + this.level.getLogicalHeight()) - 1;
        BlockPos.MutableBlockPos $$9 = blockPos0.mutable();
        for (BlockPos.MutableBlockPos $$10 : BlockPos.spiralAround(blockPos0, 16, Direction.EAST, Direction.SOUTH)) {
            int $$11 = Math.min($$8, this.level.m_6924_(Heightmap.Types.MOTION_BLOCKING, $$10.m_123341_(), $$10.m_123343_()));
            int $$12 = 1;
            if ($$7.isWithinBounds($$10) && $$7.isWithinBounds($$10.move($$2, 1))) {
                $$10.move($$2.getOpposite(), 1);
                for (int $$13 = $$11; $$13 >= this.level.m_141937_(); $$13--) {
                    $$10.setY($$13);
                    if (this.canPortalReplaceBlock($$10)) {
                        int $$14 = $$13;
                        while ($$13 > this.level.m_141937_() && this.canPortalReplaceBlock($$10.move(Direction.DOWN))) {
                            $$13--;
                        }
                        if ($$13 + 4 <= $$8) {
                            int $$15 = $$14 - $$13;
                            if ($$15 <= 0 || $$15 >= 3) {
                                $$10.setY($$13);
                                if (this.canHostFrame($$10, $$9, $$2, 0)) {
                                    double $$16 = blockPos0.m_123331_($$10);
                                    if (this.canHostFrame($$10, $$9, $$2, -1) && this.canHostFrame($$10, $$9, $$2, 1) && ($$3 == -1.0 || $$3 > $$16)) {
                                        $$3 = $$16;
                                        $$4 = $$10.immutable();
                                    }
                                    if ($$3 == -1.0 && ($$5 == -1.0 || $$5 > $$16)) {
                                        $$5 = $$16;
                                        $$6 = $$10.immutable();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if ($$3 == -1.0 && $$5 != -1.0) {
            $$4 = $$6;
            $$3 = $$5;
        }
        if ($$3 == -1.0) {
            int $$17 = Math.max(this.level.m_141937_() - -1, 70);
            int $$18 = $$8 - 9;
            if ($$18 < $$17) {
                return Optional.empty();
            }
            $$4 = new BlockPos(blockPos0.m_123341_(), Mth.clamp(blockPos0.m_123342_(), $$17, $$18), blockPos0.m_123343_()).immutable();
            Direction $$19 = $$2.getClockWise();
            if (!$$7.isWithinBounds($$4)) {
                return Optional.empty();
            }
            for (int $$20 = -1; $$20 < 2; $$20++) {
                for (int $$21 = 0; $$21 < 2; $$21++) {
                    for (int $$22 = -1; $$22 < 3; $$22++) {
                        BlockState $$23 = $$22 < 0 ? Blocks.OBSIDIAN.defaultBlockState() : Blocks.AIR.defaultBlockState();
                        $$9.setWithOffset($$4, $$21 * $$2.getStepX() + $$20 * $$19.getStepX(), $$22, $$21 * $$2.getStepZ() + $$20 * $$19.getStepZ());
                        this.level.m_46597_($$9, $$23);
                    }
                }
            }
        }
        for (int $$24 = -1; $$24 < 3; $$24++) {
            for (int $$25 = -1; $$25 < 4; $$25++) {
                if ($$24 == -1 || $$24 == 2 || $$25 == -1 || $$25 == 3) {
                    $$9.setWithOffset($$4, $$24 * $$2.getStepX(), $$25, $$24 * $$2.getStepZ());
                    this.level.m_7731_($$9, Blocks.OBSIDIAN.defaultBlockState(), 3);
                }
            }
        }
        BlockState $$26 = (BlockState) Blocks.NETHER_PORTAL.defaultBlockState().m_61124_(NetherPortalBlock.AXIS, directionAxis1);
        for (int $$27 = 0; $$27 < 2; $$27++) {
            for (int $$28 = 0; $$28 < 3; $$28++) {
                $$9.setWithOffset($$4, $$27 * $$2.getStepX(), $$28, $$27 * $$2.getStepZ());
                this.level.m_7731_($$9, $$26, 18);
            }
        }
        return Optional.of(new BlockUtil.FoundRectangle($$4.immutable(), 2, 3));
    }

    private boolean canPortalReplaceBlock(BlockPos.MutableBlockPos blockPosMutableBlockPos0) {
        BlockState $$1 = this.level.m_8055_(blockPosMutableBlockPos0);
        return $$1.m_247087_() && $$1.m_60819_().isEmpty();
    }

    private boolean canHostFrame(BlockPos blockPos0, BlockPos.MutableBlockPos blockPosMutableBlockPos1, Direction direction2, int int3) {
        Direction $$4 = direction2.getClockWise();
        for (int $$5 = -1; $$5 < 3; $$5++) {
            for (int $$6 = -1; $$6 < 4; $$6++) {
                blockPosMutableBlockPos1.setWithOffset(blockPos0, direction2.getStepX() * $$5 + $$4.getStepX() * int3, $$6, direction2.getStepZ() * $$5 + $$4.getStepZ() * int3);
                if ($$6 < 0 && !this.level.m_8055_(blockPosMutableBlockPos1).m_280296_()) {
                    return false;
                }
                if ($$6 >= 0 && !this.canPortalReplaceBlock(blockPosMutableBlockPos1)) {
                    return false;
                }
            }
        }
        return true;
    }
}