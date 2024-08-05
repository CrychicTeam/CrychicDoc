package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class FlaxBaleBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public FlaxBaleBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_());
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
        entity.causeFallDamage(height, 0.2F, level.damageSources().fall());
    }
}