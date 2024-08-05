package org.violetmoon.quark.content.world.undergroundstyle;

import it.unimi.dsi.fastutil.ints.Int2ByteArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ByteMap;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import org.violetmoon.quark.content.world.block.CorundumBlock;
import org.violetmoon.quark.content.world.block.CorundumClusterBlock;
import org.violetmoon.quark.content.world.module.CorundumModule;
import org.violetmoon.quark.content.world.undergroundstyle.base.BasicUndergroundStyle;
import org.violetmoon.quark.content.world.undergroundstyle.base.UndergroundStyleGenerator;
import org.violetmoon.zeta.util.MiscUtil;

public class CorundumStyle extends BasicUndergroundStyle {

    private static final Int2ByteMap CRYSTAL_DATA = new Int2ByteArrayMap();

    public CorundumStyle() {
        super(Blocks.AIR.defaultBlockState(), Blocks.STONE.defaultBlockState(), Blocks.STONE.defaultBlockState());
    }

    @Override
    public void fillCeiling(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        byte raw = calculateRawColorData(context.source);
        int floorIdx = raw & 15;
        int ceilIdx = raw >> 4 & 15;
        if (ceilIdx >= floorIdx) {
            ceilIdx++;
        }
        if (context.random.nextDouble() < CorundumModule.crystalChance) {
            makeCrystalIfApt(context, pos, Direction.DOWN, ceilIdx);
        }
    }

    @Override
    public void fillFloor(UndergroundStyleGenerator.Context context, BlockPos pos, BlockState state) {
        byte raw = calculateRawColorData(context.source);
        int floorIdx = raw & 15;
        if (context.random.nextDouble() < CorundumModule.crystalChance) {
            makeCrystalIfApt(context, pos, Direction.UP, floorIdx);
        }
    }

    private static void makeCrystalIfApt(UndergroundStyleGenerator.Context context, BlockPos pos, Direction offset, int color) {
        BlockPos crystalPos = pos.relative(offset);
        boolean hasHorizontal = false;
        WorldGenRegion world = context.world;
        for (Direction dir : MiscUtil.HORIZONTALS) {
            BlockPos testPos = crystalPos.relative(dir);
            if (world.getBlockState(testPos).m_60815_()) {
                hasHorizontal = true;
                break;
            }
        }
        if (hasHorizontal) {
            makeCrystalAt(context, crystalPos, offset, color, CorundumModule.crystalClusterChance);
            if (context.random.nextDouble() < CorundumModule.doubleCrystalChance) {
                crystalPos = crystalPos.relative(offset);
                if (world.m_46859_(crystalPos)) {
                    makeCrystalAt(context, crystalPos, offset, color, 0.0);
                }
            }
        }
    }

    private static void makeCrystalAt(UndergroundStyleGenerator.Context context, BlockPos crystalPos, Direction offset, int color, double clusterChance) {
        CorundumBlock crystal = (CorundumBlock) CorundumModule.crystals.get(color);
        CorundumClusterBlock cluster = crystal.cluster;
        WorldGenRegion world = context.world;
        if (context.random.nextDouble() < clusterChance) {
            world.m_7731_(crystalPos, (BlockState) ((BlockState) cluster.m_49966_().m_61124_(CorundumClusterBlock.FACING, offset)).m_61124_(CorundumClusterBlock.WATERLOGGED, world.getFluidState(crystalPos).getType() == Fluids.WATER), 0);
        } else {
            world.m_7731_(crystalPos, crystal.m_49966_(), 0);
            for (Direction dir : Direction.values()) {
                BlockPos clusterPos = crystalPos.relative(dir);
                if (world.m_46859_(clusterPos) && context.random.nextDouble() < CorundumModule.crystalClusterOnSidesChance) {
                    world.m_7731_(clusterPos, (BlockState) ((BlockState) cluster.m_49966_().m_61124_(CorundumClusterBlock.FACING, dir)).m_61124_(CorundumClusterBlock.WATERLOGGED, world.getFluidState(clusterPos).getType() == Fluids.WATER), 0);
                }
            }
        }
    }

    private static byte calculateRawColorData(BlockPos source) {
        return CRYSTAL_DATA.computeIfAbsent(source.hashCode(), src -> {
            Random rand = new Random((long) src);
            return (byte) (rand.nextInt(8) << 4 | rand.nextInt(9));
        });
    }
}