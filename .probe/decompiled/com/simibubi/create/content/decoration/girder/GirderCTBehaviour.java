package com.simibubi.create.content.decoration.girder;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GirderCTBehaviour extends ConnectedTextureBehaviour.Base {

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        if (!state.m_61138_(GirderBlock.X)) {
            return null;
        } else {
            return !state.m_61143_(GirderBlock.X) && !state.m_61143_(GirderBlock.Z) && direction.getAxis() != Direction.Axis.Y ? AllSpriteShifts.GIRDER_POLE : null;
        }
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return other.m_60734_() != state.m_60734_() ? false : !(Boolean) other.m_61143_(GirderBlock.X) && !(Boolean) other.m_61143_(GirderBlock.Z);
    }
}