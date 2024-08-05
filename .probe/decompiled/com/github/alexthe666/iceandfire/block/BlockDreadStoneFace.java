package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDreadStoneFace extends HorizontalDirectionalBlock implements IDreadBlock, IDragonProof {

    public static final BooleanProperty PLAYER_PLACED = BooleanProperty.create("player_placed");

    public BlockDreadStoneFace() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).strength(-1.0F, 10000.0F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(PLAYER_PLACED, Boolean.FALSE));
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_54117_, context.m_8125_().getOpposite())).m_61124_(PLAYER_PLACED, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_54117_);
        builder.add(PLAYER_PLACED);
    }
}