package net.mehvahdjukaar.supplementaries.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class BasaltAshFeature extends Feature<BasaltAshFeature.Config> {

    public BasaltAshFeature(Codec<BasaltAshFeature.Config> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BasaltAshFeature.Config> context) {
        BasaltAshFeature.Config config = context.config();
        int xzSpread = config.xzSpread + 1;
        int ySpread = config.ySpread;
        int tries = config.tries;
        RuleTest test = config.target;
        BlockStateProvider ash = config.ash;
        Optional<BlockState> belowAsh = config.belowAsh;
        RandomSource randomSource = context.random();
        BlockPos blockPos = context.origin();
        WorldGenLevel worldGenLevel = context.level();
        int placed = 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int l = 0; l < tries; l++) {
            mutableBlockPos.setWithOffset(blockPos, randomSource.nextInt(xzSpread) - randomSource.nextInt(xzSpread), 0, randomSource.nextInt(xzSpread) - randomSource.nextInt(xzSpread));
            if (this.placeAsh(worldGenLevel, ySpread, mutableBlockPos, test, ash, belowAsh, randomSource)) {
                placed++;
            }
        }
        return placed > 0;
    }

    public boolean placeAsh(WorldGenLevel worldGenLevel, int ySpread, BlockPos origin, RuleTest basaltTest, BlockStateProvider ash, Optional<BlockState> belowAsh, RandomSource random) {
        BlockPos.MutableBlockPos pos = origin.mutable();
        int inY = pos.m_123342_();
        boolean success = false;
        int dy = 0;
        BlockState state = worldGenLevel.m_8055_(pos.setY(inY + dy++));
        boolean up = false;
        while (basaltTest.test(state, random) && dy < ySpread) {
            up = true;
            state = worldGenLevel.m_8055_(pos.setY(inY + dy++));
            if (state.m_60795_()) {
                success = true;
                dy--;
                break;
            }
        }
        if (!up) {
            while (state.m_60795_() && dy > -ySpread) {
                state = worldGenLevel.m_8055_(pos.setY(inY + dy--));
                if (basaltTest.test(state, random)) {
                    success = true;
                    dy += 2;
                    break;
                }
            }
        }
        if (success) {
            pos.setY(inY + dy);
            worldGenLevel.m_7731_(pos, ash.getState(random, pos), 2);
            pos.setY(inY + dy - 1);
            belowAsh.ifPresent(blockState -> worldGenLevel.m_7731_(pos, blockState, 2));
        }
        return success;
    }

    public static record Config(int tries, int xzSpread, int ySpread, RuleTest target, BlockStateProvider ash, Optional<BlockState> belowAsh) implements FeatureConfiguration {

        public static final Codec<BasaltAshFeature.Config> CODEC = RecordCodecBuilder.create(instance -> instance.group(ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(64).forGetter(BasaltAshFeature.Config::tries), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(BasaltAshFeature.Config::xzSpread), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(BasaltAshFeature.Config::ySpread), RuleTest.CODEC.fieldOf("target_predicate").forGetter(BasaltAshFeature.Config::target), BlockStateProvider.CODEC.fieldOf("top_block").forGetter(BasaltAshFeature.Config::ash), StrOpt.of(BlockState.CODEC, "below_block").forGetter(BasaltAshFeature.Config::belowAsh)).apply(instance, BasaltAshFeature.Config::new));
    }
}