package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.tileentity.AMTileEntityRegistry;
import com.github.alexthe666.alexsmobs.tileentity.TileEntityEndPirateFlag;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockEndPirateFlag extends BaseEntityBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape AABB = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public BlockEndPirateFlag() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_WHITE).noOcclusion().sound(SoundType.WOOD).strength(1.0F).lightLevel(i -> 15).noCollission().requiresCorrectToolForDrops());
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor level, BlockPos pos, BlockPos blockPos0) {
        return !state.m_60710_(level, pos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state2, level, pos, blockPos0);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return AABB;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return m_49863_(world, pos.below(), Direction.UP) || m_49863_(world, pos.above(), Direction.DOWN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileEntityEndPirateFlag(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, AMTileEntityRegistry.END_PIRATE_FLAG.get(), TileEntityEndPirateFlag::commonTick);
    }

    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, Random random3) {
        if (random3.nextInt(5) == 0) {
            double d0 = (double) blockPos2.m_123341_() + 0.55 - (double) (random3.nextFloat() * 0.1F);
            double d1 = (double) blockPos2.m_123342_() + 0.55 - (double) (random3.nextFloat() * 0.1F);
            double d2 = (double) blockPos2.m_123343_() + 0.55 - (double) (random3.nextFloat() * 0.1F);
            double d3 = (double) (0.4F - (random3.nextFloat() + random3.nextFloat()) * 0.4F);
            level1.addParticle(ParticleTypes.END_ROD, d0, d1 + d3, d2, random3.nextGaussian() * 0.005, random3.nextGaussian() * 0.005, random3.nextGaussian() * 0.005);
        }
    }
}