package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTType;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BrassTunnelCTBehaviour extends ConnectedTextureBehaviour.Base {

    @Nullable
    @Override
    public CTType getDataType(BlockAndTintGetter world, BlockPos pos, BlockState state, Direction direction) {
        if (world.m_7702_(pos) instanceof BrassTunnelBlockEntity tunnelBE && tunnelBE.hasDistributionBehaviour()) {
            return super.getDataType(world, pos, state, direction);
        }
        return null;
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        return direction == Direction.UP ? AllSpriteShifts.BRASS_TUNNEL_TOP : null;
    }

    @Override
    protected boolean reverseUVs(BlockState state, Direction face) {
        return true;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        int yDiff = otherPos.m_123342_() - pos.m_123342_();
        int zDiff = otherPos.m_123343_() - pos.m_123343_();
        if (yDiff != 0) {
            return false;
        } else if (reader.m_7702_(pos) instanceof BrassTunnelBlockEntity tunnelBE) {
            boolean leftSide = zDiff > 0;
            return tunnelBE.isConnected(leftSide);
        } else {
            return false;
        }
    }
}