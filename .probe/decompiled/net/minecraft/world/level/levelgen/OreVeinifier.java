package net.minecraft.world.level.levelgen;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class OreVeinifier {

    private static final float VEININESS_THRESHOLD = 0.4F;

    private static final int EDGE_ROUNDOFF_BEGIN = 20;

    private static final double MAX_EDGE_ROUNDOFF = 0.2;

    private static final float VEIN_SOLIDNESS = 0.7F;

    private static final float MIN_RICHNESS = 0.1F;

    private static final float MAX_RICHNESS = 0.3F;

    private static final float MAX_RICHNESS_THRESHOLD = 0.6F;

    private static final float CHANCE_OF_RAW_ORE_BLOCK = 0.02F;

    private static final float SKIP_ORE_IF_GAP_NOISE_IS_BELOW = -0.3F;

    private OreVeinifier() {
    }

    protected static NoiseChunk.BlockStateFiller create(DensityFunction densityFunction0, DensityFunction densityFunction1, DensityFunction densityFunction2, PositionalRandomFactory positionalRandomFactory3) {
        BlockState $$4 = null;
        return p_209666_ -> {
            double $$6 = densityFunction0.compute(p_209666_);
            int $$7 = p_209666_.blockY();
            OreVeinifier.VeinType $$8 = $$6 > 0.0 ? OreVeinifier.VeinType.COPPER : OreVeinifier.VeinType.IRON;
            double $$9 = Math.abs($$6);
            int $$10 = $$8.maxY - $$7;
            int $$11 = $$7 - $$8.minY;
            if ($$11 >= 0 && $$10 >= 0) {
                int $$12 = Math.min($$10, $$11);
                double $$13 = Mth.clampedMap((double) $$12, 0.0, 20.0, -0.2, 0.0);
                if ($$9 + $$13 < 0.4F) {
                    return $$4;
                } else {
                    RandomSource $$14 = positionalRandomFactory3.at(p_209666_.blockX(), $$7, p_209666_.blockZ());
                    if ($$14.nextFloat() > 0.7F) {
                        return $$4;
                    } else if (densityFunction1.compute(p_209666_) >= 0.0) {
                        return $$4;
                    } else {
                        double $$15 = Mth.clampedMap($$9, 0.4F, 0.6F, 0.1F, 0.3F);
                        if ((double) $$14.nextFloat() < $$15 && densityFunction2.compute(p_209666_) > -0.3F) {
                            return $$14.nextFloat() < 0.02F ? $$8.rawOreBlock : $$8.ore;
                        } else {
                            return $$8.filler;
                        }
                    }
                }
            } else {
                return $$4;
            }
        };
    }

    protected static enum VeinType {

        COPPER(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState(), Blocks.GRANITE.defaultBlockState(), 0, 50), IRON(Blocks.DEEPSLATE_IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState(), Blocks.TUFF.defaultBlockState(), -60, -8);

        final BlockState ore;

        final BlockState rawOreBlock;

        final BlockState filler;

        protected final int minY;

        protected final int maxY;

        private VeinType(BlockState p_209684_, BlockState p_209685_, BlockState p_209686_, int p_209687_, int p_209688_) {
            this.ore = p_209684_;
            this.rawOreBlock = p_209685_;
            this.filler = p_209686_;
            this.minY = p_209687_;
            this.maxY = p_209688_;
        }
    }
}