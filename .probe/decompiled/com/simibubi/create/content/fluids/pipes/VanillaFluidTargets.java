package com.simibubi.create.content.fluids.pipes;

import com.simibubi.create.AllFluids;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class VanillaFluidTargets {

    public static boolean shouldPipesConnectTo(BlockState state) {
        return state.m_61138_(BlockStateProperties.LEVEL_HONEY) ? true : state.m_204336_(BlockTags.CAULDRONS);
    }

    public static FluidStack drainBlock(Level level, BlockPos pos, BlockState state, boolean simulate) {
        if (state.m_61138_(BlockStateProperties.LEVEL_HONEY) && (Integer) state.m_61143_(BlockStateProperties.LEVEL_HONEY) >= 5) {
            if (!simulate) {
                level.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.LEVEL_HONEY, 0), 3);
            }
            return new FluidStack(((ForgeFlowingFluid.Flowing) AllFluids.HONEY.get()).m_5613_(), 250);
        } else if (state.m_60734_() == Blocks.LAVA_CAULDRON) {
            if (!simulate) {
                level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
            }
            return new FluidStack(Fluids.LAVA, 1000);
        } else if (state.m_60734_() == Blocks.WATER_CAULDRON) {
            if (!simulate) {
                level.setBlock(pos, Blocks.CAULDRON.defaultBlockState(), 3);
            }
            return new FluidStack(Fluids.WATER, 1000);
        } else {
            return FluidStack.EMPTY;
        }
    }
}