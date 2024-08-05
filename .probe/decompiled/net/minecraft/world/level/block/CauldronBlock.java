package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class CauldronBlock extends AbstractCauldronBlock {

    private static final float RAIN_FILL_CHANCE = 0.05F;

    private static final float POWDER_SNOW_FILL_CHANCE = 0.1F;

    public CauldronBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, CauldronInteraction.EMPTY);
    }

    @Override
    public boolean isFull(BlockState blockState0) {
        return false;
    }

    protected static boolean shouldHandlePrecipitation(Level level0, Biome.Precipitation biomePrecipitation1) {
        if (biomePrecipitation1 == Biome.Precipitation.RAIN) {
            return level0.getRandom().nextFloat() < 0.05F;
        } else {
            return biomePrecipitation1 == Biome.Precipitation.SNOW ? level0.getRandom().nextFloat() < 0.1F : false;
        }
    }

    @Override
    public void handlePrecipitation(BlockState blockState0, Level level1, BlockPos blockPos2, Biome.Precipitation biomePrecipitation3) {
        if (shouldHandlePrecipitation(level1, biomePrecipitation3)) {
            if (biomePrecipitation3 == Biome.Precipitation.RAIN) {
                level1.setBlockAndUpdate(blockPos2, Blocks.WATER_CAULDRON.defaultBlockState());
                level1.m_142346_(null, GameEvent.BLOCK_CHANGE, blockPos2);
            } else if (biomePrecipitation3 == Biome.Precipitation.SNOW) {
                level1.setBlockAndUpdate(blockPos2, Blocks.POWDER_SNOW_CAULDRON.defaultBlockState());
                level1.m_142346_(null, GameEvent.BLOCK_CHANGE, blockPos2);
            }
        }
    }

    @Override
    protected boolean canReceiveStalactiteDrip(Fluid fluid0) {
        return true;
    }

    @Override
    protected void receiveStalactiteDrip(BlockState blockState0, Level level1, BlockPos blockPos2, Fluid fluid3) {
        if (fluid3 == Fluids.WATER) {
            BlockState $$4 = Blocks.WATER_CAULDRON.defaultBlockState();
            level1.setBlockAndUpdate(blockPos2, $$4);
            level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$4));
            level1.m_46796_(1047, blockPos2, 0);
        } else if (fluid3 == Fluids.LAVA) {
            BlockState $$5 = Blocks.LAVA_CAULDRON.defaultBlockState();
            level1.setBlockAndUpdate(blockPos2, $$5);
            level1.m_220407_(GameEvent.BLOCK_CHANGE, blockPos2, GameEvent.Context.of($$5));
            level1.m_46796_(1046, blockPos2, 0);
        }
    }
}