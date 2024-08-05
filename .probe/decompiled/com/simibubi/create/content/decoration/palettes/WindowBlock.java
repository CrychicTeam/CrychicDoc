package com.simibubi.create.content.decoration.palettes;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WindowBlock extends ConnectedGlassBlock {

    protected final boolean translucent;

    public WindowBlock(BlockBehaviour.Properties p_i48392_1_, boolean translucent) {
        super(p_i48392_1_);
        this.translucent = translucent;
    }

    public boolean isTranslucent() {
        return this.translucent;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        if (state.m_60734_() == adjacentBlockState.m_60734_()) {
            return true;
        } else {
            if (state.m_60734_() instanceof WindowBlock windowBlock && adjacentBlockState.m_60734_() instanceof ConnectedGlassBlock) {
                return !windowBlock.isTranslucent() && side.getAxis().isHorizontal();
            }
            return super.skipRendering(state, adjacentBlockState, side);
        }
    }
}