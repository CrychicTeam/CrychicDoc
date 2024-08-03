package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public class MultipleDinosaurEggsBlock extends DinosaurEggBlock {

    public static final IntegerProperty EGGS = BlockStateProperties.EGGS;

    private final int maxEggs;

    private static final VoxelShape ONE_EGG_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 9.0, 12.0);

    private static final VoxelShape MULTI_EGG_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0);

    public MultipleDinosaurEggsBlock(BlockBehaviour.Properties properties, RegistryObject births, int maxEggs) {
        super(properties, births, Shapes.block());
        this.maxEggs = maxEggs;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(HATCH, 0)).m_61124_(EGGS, 1)).m_61124_(NEEDS_PLAYER, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return state.m_61143_(EGGS) > 1 ? MULTI_EGG_SHAPE : ONE_EGG_SHAPE;
    }

    @Override
    public boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        return useContext.m_43722_().getItem() == this.m_5456_() && (Integer) state.m_61143_(EGGS) < 4 || super.m_6864_(state, useContext);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        return blockstate.m_60734_() == this ? (BlockState) blockstate.m_61124_(EGGS, Math.min(this.maxEggs, (Integer) blockstate.m_61143_(EGGS) + 1)) : super.m_5573_(context);
    }

    @Override
    protected void removeOneEgg(Level worldIn, BlockPos pos, BlockState state) {
        worldIn.playSound(null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + worldIn.random.nextFloat() * 0.2F);
        int i = (Integer) state.m_61143_(EGGS);
        if (i <= 1) {
            worldIn.m_46961_(pos, false);
        } else {
            worldIn.setBlock(pos, (BlockState) state.m_61124_(EGGS, i - 1), 2);
            worldIn.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            worldIn.m_46796_(2001, pos, Block.getId(state));
        }
    }

    @Override
    protected int getDinosaursBornFrom(BlockState state) {
        return (Integer) state.m_61143_(EGGS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(EGGS);
    }
}