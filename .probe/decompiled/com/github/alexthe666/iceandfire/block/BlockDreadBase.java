package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDreadBase extends BlockGeneric implements IDragonProof, IDreadBlock {

    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public static BlockDreadBase builder(float hardness, float resistance, SoundType sound, MapColor color, NoteBlockInstrument instrument, boolean ignited) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of().mapColor(color).sound(sound).strength(hardness, resistance);
        if (instrument != null) {
            props.instrument(instrument);
        }
        if (ignited) {
            props.ignitedByLava();
        }
        return new BlockDreadBase(props);
    }

    public BlockDreadBase(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public float getDestroyProgress(BlockState state, @NotNull Player player, @NotNull BlockGetter worldIn, @NotNull BlockPos pos) {
        if ((Boolean) state.m_61143_(PLAYER_PLACED)) {
            float f = 8.0F;
            return player.getDigSpeed(state, pos) / f / 30.0F;
        } else {
            return super.m_5880_(state, player, worldIn, pos);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PLAYER_PLACED);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(PLAYER_PLACED, true);
    }
}