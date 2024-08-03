package vectorwing.farmersdelight.common.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import vectorwing.farmersdelight.common.block.WildRiceBlock;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public class WildRiceFeature extends Feature<RandomPatchConfiguration> {

    public WildRiceFeature(Codec<RandomPatchConfiguration> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(FeaturePlaceContext<RandomPatchConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomPatchConfiguration config = context.config();
        RandomSource rand = context.random();
        BlockPos blockpos = level.m_5452_(Heightmap.Types.OCEAN_FLOOR_WG, origin);
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
        for (int j = 0; j < config.tries(); j++) {
            blockpos$mutable.set(blockpos).move(rand.nextInt(config.xzSpread() + 1) - rand.nextInt(config.xzSpread() + 1), rand.nextInt(config.ySpread() + 1) - rand.nextInt(config.ySpread() + 1), rand.nextInt(config.xzSpread() + 1) - rand.nextInt(config.xzSpread() + 1));
            if (level.m_8055_(blockpos$mutable).m_60734_() == Blocks.WATER && level.m_8055_(blockpos$mutable.m_7494_()).m_60734_() == Blocks.AIR) {
                BlockState bottomRiceState = (BlockState) ModBlocks.WILD_RICE.get().defaultBlockState().m_61124_(WildRiceBlock.f_52858_, DoubleBlockHalf.LOWER);
                if (bottomRiceState.m_60710_(level, blockpos$mutable)) {
                    DoublePlantBlock.placeAt(level, bottomRiceState, blockpos$mutable, 2);
                    i++;
                }
            }
        }
        return i > 0;
    }
}