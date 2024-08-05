package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public class SulfurBlock extends Block {

    public SulfurBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).strength(2.0F, 4.0F).sound(ACSoundTypes.SULFUR).randomTicks());
    }

    @Override
    public void randomTick(BlockState currentState, ServerLevel level, BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(10) == 0) {
            Direction direction = Util.getRandom(Direction.values(), randomSource);
            BlockPos offset = blockPos.relative(direction);
            if (level.m_8055_(offset).m_60795_() && this.isDrippingAcidAbove(level, offset)) {
                BlockState blockstate1 = (BlockState) ((BlockState) ACBlockRegistry.SULFUR_BUD_SMALL.get().defaultBlockState().m_61124_(SulfurBudBlock.FACING, direction)).m_61124_(SulfurBudBlock.LIQUID_LOGGED, SulfurBudBlock.getLiquidType(level.m_6425_(offset)));
                level.m_46597_(offset, blockstate1);
            }
        }
    }

    private boolean isDrippingAcidAbove(Level level, BlockPos pos) {
        if (level.getFluidState(pos).getFluidType() == ACFluidRegistry.ACID_FLUID_TYPE.get()) {
            return true;
        } else {
            while (level.getBlockState(pos).m_60795_() && pos.m_123342_() < level.m_151558_()) {
                pos = pos.above();
            }
            BlockState acidState = level.getBlockState(pos);
            return acidState.m_60713_(ACBlockRegistry.ACIDIC_RADROCK.get());
        }
    }
}