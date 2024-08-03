package software.bernie.example.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import software.bernie.example.registry.BlockEntityRegistry;

public class GeckoHabitatBlock extends BaseEntityBlock implements EntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public GeckoHabitatBlock() {
        super(BlockBehaviour.Properties.of().noOcclusion());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getClockWise().getClockWise());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return BlockEntityRegistry.GECKO_HABITAT.get().create(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case NORTH ->
                Block.box(0.0, 0.0, 0.0, 32.0, 16.0, 16.0);
            case SOUTH ->
                Block.box(-16.0, 0.0, 0.0, 16.0, 16.0, 16.0);
            case WEST ->
                Block.box(0.0, 0.0, -16.0, 16.0, 16.0, 16.0);
            default ->
                Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 32.0);
        };
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        for (BlockPos testPos : BlockPos.betweenClosed(pos, pos.relative(((Direction) state.m_61143_(FACING)).getClockWise(), 2))) {
            if (!testPos.equals(pos) && !world.m_8055_(testPos).m_60795_()) {
                return false;
            }
        }
        return true;
    }
}