package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PingPongSpongeBlock extends BushBlock implements LiquidBlockContainer {

    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public static final VoxelShape SHAPE = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    public static final VoxelShape SHAPE_TOP = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    public PingPongSpongeBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.WHITE).dynamicShape().instabreak().lightLevel(blockstate -> 5).sound(SoundType.WET_GRASS).offsetType(BlockBehaviour.OffsetType.XZ));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(TOP, true));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        Vec3 vec3 = state.m_60824_(getter, pos);
        VoxelShape shape = state.m_61143_(TOP) ? SHAPE_TOP : SHAPE;
        return shape.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public long getSeed(BlockState blockState, BlockPos pos) {
        return Mth.getSeed(pos.m_123341_(), 0, pos.m_123343_());
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60783_(level, pos, Direction.UP) && !state.m_60713_(Blocks.MAGMA_BLOCK) || state.m_60734_() == this;
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.4F;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState prev = super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
        if (!prev.m_60795_()) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        if (prev.m_60734_() == this) {
            if (levelAccessor.m_8055_(blockPos.above()).m_60734_() == this) {
                prev = (BlockState) prev.m_61124_(TOP, false);
            } else {
                prev = (BlockState) prev.m_61124_(TOP, true);
            }
        }
        return prev;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockState above = levelaccessor.m_8055_(blockpos.above());
        FluidState fluidstate = context.m_43725_().getFluidState(blockpos);
        return fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8 ? (BlockState) this.m_49966_().m_61124_(TOP, above.m_60734_() != this) : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(TOP);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, Fluid fluid3) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
        return false;
    }
}