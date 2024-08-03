package com.simibubi.create.content.logistics.vault;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.api.connectivity.ConnectivityHandler;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemVaultCTBehaviour extends ConnectedTextureBehaviour.Base {

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        Direction.Axis vaultBlockAxis = ItemVaultBlock.getVaultBlockAxis(state);
        boolean small = !ItemVaultBlock.isLarge(state);
        if (vaultBlockAxis == null) {
            return null;
        } else if (direction.getAxis() == vaultBlockAxis) {
            return AllSpriteShifts.VAULT_FRONT.get(small);
        } else if (direction == Direction.UP) {
            return AllSpriteShifts.VAULT_TOP.get(small);
        } else {
            return direction == Direction.DOWN ? AllSpriteShifts.VAULT_BOTTOM.get(small) : AllSpriteShifts.VAULT_SIDE.get(small);
        }
    }

    @Override
    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis vaultBlockAxis = ItemVaultBlock.getVaultBlockAxis(state);
        boolean alongX = vaultBlockAxis == Direction.Axis.X;
        if (face.getAxis().isVertical() && alongX) {
            return super.getUpDirection(reader, pos, state, face).getClockWise();
        } else {
            return face.getAxis() != vaultBlockAxis && !face.getAxis().isVertical() ? Direction.fromAxisAndDirection(vaultBlockAxis, alongX ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE) : super.getUpDirection(reader, pos, state, face);
        }
    }

    @Override
    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis vaultBlockAxis = ItemVaultBlock.getVaultBlockAxis(state);
        if (face.getAxis().isVertical() && vaultBlockAxis == Direction.Axis.X) {
            return super.getRightDirection(reader, pos, state, face).getClockWise();
        } else {
            return face.getAxis() != vaultBlockAxis && !face.getAxis().isVertical() ? Direction.fromAxisAndDirection(Direction.Axis.Y, face.getAxisDirection()) : super.getRightDirection(reader, pos, state, face);
        }
    }

    @Override
    public boolean buildContextForOccludedDirections() {
        return super.buildContextForOccludedDirections();
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return state == other && ConnectivityHandler.isConnected(reader, pos, otherPos);
    }
}