package org.violetmoon.quark.content.world.gen;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import org.violetmoon.quark.content.building.module.HollowLogsModule;
import org.violetmoon.quark.content.world.module.FallenLogsModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.MiscUtil;
import org.violetmoon.zeta.world.generator.Generator;

public class FallenLogGenerator extends Generator {

    public FallenLogGenerator(DimensionConfig dimConfig) {
        super(dimConfig);
    }

    @Override
    public void generateChunk(WorldGenRegion worldIn, ChunkGenerator generator, RandomSource rand, BlockPos corner) {
        int x = corner.m_123341_() + rand.nextInt(16);
        int z = corner.m_123343_() + rand.nextInt(16);
        BlockPos center = new BlockPos(x, 128, z);
        Holder<Biome> biome = this.getBiome(worldIn, center, false);
        int chance = biome.is(FallenLogsModule.reducedLogsTag) ? FallenLogsModule.sparseBiomeRarity : FallenLogsModule.rarity;
        if (rand.nextInt(chance) == 0) {
            BlockPos pos = worldIn.m_5452_(Heightmap.Types.WORLD_SURFACE_WG, center);
            placeFallenLogAt(worldIn, pos);
        }
    }

    private static void placeFallenLogAt(LevelAccessor level, BlockPos pos) {
        placeFallenLogAt(level, pos, getLogBLockForPos(level, pos));
    }

    private static void placeFallenLogAt(LevelAccessor level, BlockPos pos, Block logBlock) {
        if (logBlock != Blocks.AIR) {
            int attempts = 5;
            BlockState state = logBlock.defaultBlockState();
            RandomSource rand = level.getRandom();
            for (int attempt = 0; attempt < 5; attempt++) {
                int dirOrd = rand.nextInt(MiscUtil.HORIZONTALS.length);
                Direction dir = MiscUtil.HORIZONTALS[dirOrd];
                state = (BlockState) state.m_61124_(RotatedPillarBlock.AXIS, dir.getAxis());
                int len = 3 + rand.nextInt(2);
                boolean errored = false;
                for (int i = 0; i < len; i++) {
                    BlockPos testPos = pos.relative(dir, i);
                    BlockState testState = level.m_8055_(testPos);
                    if (!testState.m_60795_() && !testState.m_247087_() && !testState.m_204336_(BlockTags.FLOWERS)) {
                        errored = true;
                        break;
                    }
                    BlockPos belowPos = testPos.below();
                    BlockState belowState = level.m_8055_(belowPos);
                    if (!belowState.m_280296_()) {
                        errored = true;
                        break;
                    }
                }
                if (!errored) {
                    for (int i = 0; i < len; i++) {
                        BlockPos placePos = pos.relative(dir, i);
                        level.m_7731_(placePos, state, 3);
                        if (rand.nextInt(10) < 7) {
                            BlockPos abovePos = placePos.above();
                            BlockState aboveState = level.m_8055_(abovePos);
                            if (aboveState.m_60795_()) {
                                level.m_7731_(abovePos, Blocks.MOSS_CARPET.defaultBlockState(), 3);
                            }
                        }
                        Direction[][] sideDirections = new Direction[][] { { Direction.EAST, Direction.WEST }, { Direction.EAST, Direction.WEST }, { Direction.NORTH, Direction.SOUTH }, { Direction.NORTH, Direction.SOUTH } };
                        for (int j = 0; j < 2; j++) {
                            if (rand.nextInt(5) < 3) {
                                Direction side = sideDirections[dirOrd][j];
                                BlockPos sidePos = placePos.relative(side);
                                placeDecorIfPossible(level, rand, side, sidePos);
                            }
                        }
                        if (rand.nextInt(10) < 4) {
                            placeDecorIfPossible(level, rand, dir, pos.relative(dir.getOpposite()));
                        }
                        if (rand.nextInt(10) < 4) {
                            placeDecorIfPossible(level, rand, dir.getOpposite(), pos.relative(dir, len));
                        }
                    }
                    return;
                }
            }
        }
    }

    private static void placeDecorIfPossible(LevelAccessor level, RandomSource rand, Direction side, BlockPos sidePos) {
        BlockState sideState = level.m_8055_(sidePos);
        if (sideState.m_60795_()) {
            BlockState placeState = switch(rand.nextInt(3)) {
                case 0 ->
                    Blocks.MOSS_CARPET.defaultBlockState();
                case 1 ->
                    (BlockState) Blocks.VINE.defaultBlockState().m_61124_(VineBlock.getPropertyForFace(side.getOpposite()), true);
                default ->
                    Blocks.FERN.defaultBlockState();
            };
            if (placeState.m_60710_(level, sidePos)) {
                level.m_7731_(sidePos, placeState, 3);
            }
        }
    }

    private static Block getLogBLockForPos(LevelAccessor level, BlockPos pos) {
        Block base = getBaseLogBlockForPos(level, pos);
        if (FallenLogsModule.useHollowLogs && HollowLogsModule.staticEnabled) {
            Block hollow = (Block) HollowLogsModule.logMap.get(base);
            if (hollow != null) {
                return hollow;
            }
        }
        return base;
    }

    private static Block getBaseLogBlockForPos(LevelAccessor level, BlockPos pos) {
        Holder<Biome> biome = level.m_204166_(pos);
        List<Block> matched = new ArrayList();
        for (TagKey<Biome> tag : FallenLogsModule.blocksPerTag.keySet()) {
            if (biome.is(tag)) {
                matched.add((Block) FallenLogsModule.blocksPerTag.get(tag));
            }
        }
        return matched.size() == 0 ? Blocks.AIR : (Block) matched.get(level.getRandom().nextInt(matched.size()));
    }
}