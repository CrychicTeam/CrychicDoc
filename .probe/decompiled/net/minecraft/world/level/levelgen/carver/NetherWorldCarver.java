package net.minecraft.world.level.levelgen.carver;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class NetherWorldCarver extends CaveWorldCarver {

    public NetherWorldCarver(Codec<CaveCarverConfiguration> codecCaveCarverConfiguration0) {
        super(codecCaveCarverConfiguration0);
        this.f_64984_ = ImmutableSet.of(Fluids.LAVA, Fluids.WATER);
    }

    @Override
    protected int getCaveBound() {
        return 10;
    }

    @Override
    protected float getThickness(RandomSource randomSource0) {
        return (randomSource0.nextFloat() * 2.0F + randomSource0.nextFloat()) * 2.0F;
    }

    @Override
    protected double getYScale() {
        return 5.0;
    }

    protected boolean carveBlock(CarvingContext carvingContext0, CaveCarverConfiguration caveCarverConfiguration1, ChunkAccess chunkAccess2, Function<BlockPos, Holder<Biome>> functionBlockPosHolderBiome3, CarvingMask carvingMask4, BlockPos.MutableBlockPos blockPosMutableBlockPos5, BlockPos.MutableBlockPos blockPosMutableBlockPos6, Aquifer aquifer7, MutableBoolean mutableBoolean8) {
        if (this.m_224910_(caveCarverConfiguration1, chunkAccess2.m_8055_(blockPosMutableBlockPos5))) {
            BlockState $$9;
            if (blockPosMutableBlockPos5.m_123342_() <= carvingContext0.m_142201_() + 31) {
                $$9 = f_64982_.createLegacyBlock();
            } else {
                $$9 = f_64980_;
            }
            chunkAccess2.setBlockState(blockPosMutableBlockPos5, $$9, false);
            return true;
        } else {
            return false;
        }
    }
}