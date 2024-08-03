package net.minecraft.world.level.redstone;

import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public interface NeighborUpdater {

    Direction[] UPDATE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.DOWN, Direction.UP, Direction.NORTH, Direction.SOUTH };

    void shapeUpdate(Direction var1, BlockState var2, BlockPos var3, BlockPos var4, int var5, int var6);

    void neighborChanged(BlockPos var1, Block var2, BlockPos var3);

    void neighborChanged(BlockState var1, BlockPos var2, Block var3, BlockPos var4, boolean var5);

    default void updateNeighborsAtExceptFromFacing(BlockPos blockPos0, Block block1, @Nullable Direction direction2) {
        for (Direction $$3 : UPDATE_ORDER) {
            if ($$3 != direction2) {
                this.neighborChanged(blockPos0.relative($$3), block1, blockPos0);
            }
        }
    }

    static void executeShapeUpdate(LevelAccessor levelAccessor0, Direction direction1, BlockState blockState2, BlockPos blockPos3, BlockPos blockPos4, int int5, int int6) {
        BlockState $$7 = levelAccessor0.m_8055_(blockPos3);
        BlockState $$8 = $$7.m_60728_(direction1, blockState2, levelAccessor0, blockPos3, blockPos4);
        Block.updateOrDestroy($$7, $$8, levelAccessor0, blockPos3, int5, int6);
    }

    static void executeUpdate(Level level0, BlockState blockState1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        try {
            blockState1.m_60690_(level0, blockPos2, block3, blockPos4, boolean5);
        } catch (Throwable var9) {
            CrashReport $$7 = CrashReport.forThrowable(var9, "Exception while updating neighbours");
            CrashReportCategory $$8 = $$7.addCategory("Block being updated");
            $$8.setDetail("Source block type", (CrashReportDetail<String>) (() -> {
                try {
                    return String.format(Locale.ROOT, "ID #%s (%s // %s)", BuiltInRegistries.BLOCK.getKey(block3), block3.getDescriptionId(), block3.getClass().getCanonicalName());
                } catch (Throwable var2x) {
                    return "ID #" + BuiltInRegistries.BLOCK.getKey(block3);
                }
            }));
            CrashReportCategory.populateBlockDetails($$8, level0, blockPos2, blockState1);
            throw new ReportedException($$7);
        }
    }
}