package org.violetmoon.quark.content.world.gen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import org.violetmoon.quark.content.world.module.MonsterBoxModule;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.util.BlockUtils;
import org.violetmoon.zeta.world.generator.Generator;

public class MonsterBoxGenerator extends Generator {

    public MonsterBoxGenerator(DimensionConfig dimConfig) {
        super(dimConfig);
    }

    @Override
    public void generateChunk(WorldGenRegion world, ChunkGenerator generator, RandomSource rand, BlockPos chunkCorner) {
        if (!(generator instanceof FlatLevelSource)) {
            for (double chance = MonsterBoxModule.chancePerChunk; chance > 0.0 && rand.nextDouble() <= chance; chance--) {
                BlockPos.MutableBlockPos pos = chunkCorner.offset(rand.nextInt(16), rand.nextInt(MonsterBoxModule.minY, MonsterBoxModule.maxY), rand.nextInt(16)).mutable();
                for (int moves = 0; moves < MonsterBoxModule.searchRange && pos.m_123342_() > MonsterBoxModule.minY; moves++) {
                    BlockState state = world.getBlockState(pos);
                    if (this.canPlaceHere(world, pos, state)) {
                        world.m_7731_(pos, MonsterBoxModule.monster_box.defaultBlockState(), 0);
                        break;
                    }
                    pos = pos.move(0, -1, 0);
                }
            }
        }
    }

    private boolean canPlaceHere(WorldGenRegion level, BlockPos.MutableBlockPos pos, BlockState state) {
        if (state.m_247087_() && !state.m_278721_()) {
            BlockPos below = pos.move(0, -1, 0);
            BlockState belowState = level.getBlockState(below);
            boolean result = BlockUtils.isStoneBased(belowState, level, below) && belowState.m_60783_(level, below, Direction.UP);
            pos.move(0, 1, 0);
            return result;
        } else {
            return false;
        }
    }
}