package org.violetmoon.quark.content.building.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.api.ICrawlSpaceBlock;
import org.violetmoon.zeta.block.ZetaPillarBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class HollowPillarBlock extends ZetaPillarBlock implements SimpleWaterloggedBlock, ICrawlSpaceBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    private static final VoxelShape SHAPE_TOP = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_SOUTH = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_EAST = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);

    private static final VoxelShape SHAPE_WEST = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);

    private static final VoxelShape SHAPE_X = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP, SHAPE_EAST, SHAPE_WEST);

    private static final VoxelShape SHAPE_Y = Shapes.or(SHAPE_NORTH, SHAPE_SOUTH, SHAPE_EAST, SHAPE_WEST);

    private static final VoxelShape SHAPE_Z = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP, SHAPE_NORTH, SHAPE_SOUTH);

    public HollowPillarBlock(String regname, ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    public boolean canCrawl(Level level, BlockState state, BlockPos pos, Direction direction) {
        return state.m_61143_(f_55923_) == direction.getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext ctx) {
        return switch((Direction.Axis) state.m_61143_(f_55923_)) {
            case X ->
                SHAPE_X;
            case Y ->
                SHAPE_Y;
            case Z ->
                SHAPE_Z;
        };
    }

    @Override
    public boolean isLadderZeta(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        if (state.m_61143_(f_55923_) != Direction.Axis.Y) {
            return false;
        } else {
            Vec3 eyePos = entity.m_146892_();
            double pad = 0.125;
            return eyePos.x > (double) pos.m_123341_() + pad && eyePos.z > (double) pos.m_123343_() + pad && eyePos.x < (double) (pos.m_123341_() + 1) - pad && eyePos.z < (double) (pos.m_123343_() + 1) - pad ? true : super.isLadderZeta(state, level, pos, entity);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return (BlockState) super.m_5573_(ctx).m_61124_(WATERLOGGED, ctx.m_43725_().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return !(Boolean) state.m_61143_(WATERLOGGED) && state.m_61143_(f_55923_) == Direction.Axis.Y;
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> def) {
        super.m_7926_(def);
        def.add(WATERLOGGED);
    }
}