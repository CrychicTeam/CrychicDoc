package fr.lucreeper74.createmetallurgy.content.redstone.lightbulb;

import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import fr.lucreeper74.createmetallurgy.registries.CMBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightBulbBlock extends WrenchableDirectionalBlock implements IBE<LightBulbBlockEntity>, SimpleWaterloggedBlock {

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected final DyeColor color;

    public LightBulbBlock(BlockBehaviour.Properties pProperties, DyeColor color) {
        super(pProperties);
        this.color = color;
        this.m_49959_((BlockState) ((BlockState) super.m_49966_().m_61124_(LEVEL, 0)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.m_43725_().getFluidState(pContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, pContext.m_43719_())).m_61124_(WATERLOGGED, flag)).m_61124_(LEVEL, pContext.m_43725_().m_277086_(pContext.getClickedPos()));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(f_52588_)) {
            case DOWN ->
                Shapes.or(Block.box(5.0, 4.0, 5.0, 11.0, 16.0, 11.0));
            case NORTH ->
                Shapes.or(Block.box(5.0, 5.0, 4.0, 11.0, 11.0, 16.0));
            case SOUTH ->
                Shapes.or(Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 12.0));
            case EAST ->
                Shapes.or(Block.box(0.0, 5.0, 5.0, 12.0, 11.0, 11.0));
            case WEST ->
                Shapes.or(Block.box(4.0, 5.0, 5.0, 16.0, 11.0, 11.0));
            default ->
                Shapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 12.0, 11.0));
        };
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if ((Boolean) pState.m_61143_(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return false;
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide()) {
            if ((Integer) state.m_61143_(LEVEL) != worldIn.m_277086_(pos)) {
                this.transmit(worldIn, pos);
            }
        }
    }

    public void transmit(Level worldIn, BlockPos pos) {
        if (!worldIn.isClientSide) {
            this.withBlockEntityDo(worldIn, pos, be -> be.transmit(worldIn.m_277086_(pos)));
        }
    }

    public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public Class<LightBulbBlockEntity> getBlockEntityClass() {
        return LightBulbBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends LightBulbBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends LightBulbBlockEntity>) CMBlockEntityTypes.LIGHT_BULB.get();
    }
}