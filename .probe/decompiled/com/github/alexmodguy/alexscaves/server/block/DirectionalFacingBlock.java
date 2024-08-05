package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class DirectionalFacingBlock extends DirectionalBlock {

    private final boolean facesPlayer;

    protected DirectionalFacingBlock(BlockBehaviour.Properties properties) {
        this(properties, false);
    }

    protected DirectionalFacingBlock(BlockBehaviour.Properties properties, boolean facesPlayer) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(f_52588_, Direction.NORTH));
        this.facesPlayer = facesPlayer;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(f_52588_);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(f_52588_, rotation.rotate((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(f_52588_, this.facesPlayer ? context.getNearestLookingDirection().getOpposite() : context.m_43719_());
    }
}