package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.VegetationPatchConfiguration;

public class VegetationPatchFeature extends Feature<VegetationPatchConfiguration> {

    public VegetationPatchFeature(Codec<VegetationPatchConfiguration> codecVegetationPatchConfiguration0) {
        super(codecVegetationPatchConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<VegetationPatchConfiguration> featurePlaceContextVegetationPatchConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextVegetationPatchConfiguration0.level();
        VegetationPatchConfiguration $$2 = featurePlaceContextVegetationPatchConfiguration0.config();
        RandomSource $$3 = featurePlaceContextVegetationPatchConfiguration0.random();
        BlockPos $$4 = featurePlaceContextVegetationPatchConfiguration0.origin();
        Predicate<BlockState> $$5 = p_204782_ -> p_204782_.m_204336_($$2.replaceable);
        int $$6 = $$2.xzRadius.sample($$3) + 1;
        int $$7 = $$2.xzRadius.sample($$3) + 1;
        Set<BlockPos> $$8 = this.placeGroundPatch($$1, $$2, $$3, $$4, $$5, $$6, $$7);
        this.distributeVegetation(featurePlaceContextVegetationPatchConfiguration0, $$1, $$2, $$3, $$8, $$6, $$7);
        return !$$8.isEmpty();
    }

    protected Set<BlockPos> placeGroundPatch(WorldGenLevel worldGenLevel0, VegetationPatchConfiguration vegetationPatchConfiguration1, RandomSource randomSource2, BlockPos blockPos3, Predicate<BlockState> predicateBlockState4, int int5, int int6) {
        BlockPos.MutableBlockPos $$7 = blockPos3.mutable();
        BlockPos.MutableBlockPos $$8 = $$7.m_122032_();
        Direction $$9 = vegetationPatchConfiguration1.surface.getDirection();
        Direction $$10 = $$9.getOpposite();
        Set<BlockPos> $$11 = new HashSet();
        for (int $$12 = -int5; $$12 <= int5; $$12++) {
            boolean $$13 = $$12 == -int5 || $$12 == int5;
            for (int $$14 = -int6; $$14 <= int6; $$14++) {
                boolean $$15 = $$14 == -int6 || $$14 == int6;
                boolean $$16 = $$13 || $$15;
                boolean $$17 = $$13 && $$15;
                boolean $$18 = $$16 && !$$17;
                if (!$$17 && (!$$18 || vegetationPatchConfiguration1.extraEdgeColumnChance != 0.0F && !(randomSource2.nextFloat() > vegetationPatchConfiguration1.extraEdgeColumnChance))) {
                    $$7.setWithOffset(blockPos3, $$12, 0, $$14);
                    for (int $$19 = 0; worldGenLevel0.m_7433_($$7, BlockBehaviour.BlockStateBase::m_60795_) && $$19 < vegetationPatchConfiguration1.verticalRange; $$19++) {
                        $$7.move($$9);
                    }
                    for (int var25 = 0; worldGenLevel0.m_7433_($$7, p_284926_ -> !p_284926_.m_60795_()) && var25 < vegetationPatchConfiguration1.verticalRange; var25++) {
                        $$7.move($$10);
                    }
                    $$8.setWithOffset($$7, vegetationPatchConfiguration1.surface.getDirection());
                    BlockState $$20 = worldGenLevel0.m_8055_($$8);
                    if (worldGenLevel0.m_46859_($$7) && $$20.m_60783_(worldGenLevel0, $$8, vegetationPatchConfiguration1.surface.getDirection().getOpposite())) {
                        int $$21 = vegetationPatchConfiguration1.depth.sample(randomSource2) + (vegetationPatchConfiguration1.extraBottomBlockChance > 0.0F && randomSource2.nextFloat() < vegetationPatchConfiguration1.extraBottomBlockChance ? 1 : 0);
                        BlockPos $$22 = $$8.immutable();
                        boolean $$23 = this.placeGround(worldGenLevel0, vegetationPatchConfiguration1, predicateBlockState4, randomSource2, $$8, $$21);
                        if ($$23) {
                            $$11.add($$22);
                        }
                    }
                }
            }
        }
        return $$11;
    }

    protected void distributeVegetation(FeaturePlaceContext<VegetationPatchConfiguration> featurePlaceContextVegetationPatchConfiguration0, WorldGenLevel worldGenLevel1, VegetationPatchConfiguration vegetationPatchConfiguration2, RandomSource randomSource3, Set<BlockPos> setBlockPos4, int int5, int int6) {
        for (BlockPos $$7 : setBlockPos4) {
            if (vegetationPatchConfiguration2.vegetationChance > 0.0F && randomSource3.nextFloat() < vegetationPatchConfiguration2.vegetationChance) {
                this.placeVegetation(worldGenLevel1, vegetationPatchConfiguration2, featurePlaceContextVegetationPatchConfiguration0.chunkGenerator(), randomSource3, $$7);
            }
        }
    }

    protected boolean placeVegetation(WorldGenLevel worldGenLevel0, VegetationPatchConfiguration vegetationPatchConfiguration1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BlockPos blockPos4) {
        return vegetationPatchConfiguration1.vegetationFeature.value().place(worldGenLevel0, chunkGenerator2, randomSource3, blockPos4.relative(vegetationPatchConfiguration1.surface.getDirection().getOpposite()));
    }

    protected boolean placeGround(WorldGenLevel worldGenLevel0, VegetationPatchConfiguration vegetationPatchConfiguration1, Predicate<BlockState> predicateBlockState2, RandomSource randomSource3, BlockPos.MutableBlockPos blockPosMutableBlockPos4, int int5) {
        for (int $$6 = 0; $$6 < int5; $$6++) {
            BlockState $$7 = vegetationPatchConfiguration1.groundState.getState(randomSource3, blockPosMutableBlockPos4);
            BlockState $$8 = worldGenLevel0.m_8055_(blockPosMutableBlockPos4);
            if (!$$7.m_60713_($$8.m_60734_())) {
                if (!predicateBlockState2.test($$8)) {
                    return $$6 != 0;
                }
                worldGenLevel0.m_7731_(blockPosMutableBlockPos4, $$7, 2);
                blockPosMutableBlockPos4.move(vegetationPatchConfiguration1.surface.getDirection());
            }
        }
        return true;
    }
}