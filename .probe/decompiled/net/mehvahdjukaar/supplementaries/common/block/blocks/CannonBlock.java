package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Optional;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.moonlight.api.block.IRotatable;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.tiles.CannonBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CannonBlock extends DirectionalBlock implements EntityBlock, ILightable, IRotatable {

    protected static final VoxelShape SHAPE_DOWN = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0);

    protected static final VoxelShape SHAPE_UP = Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_SOUTH = Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_NORTH = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0);

    protected static final VoxelShape SHAPE_EAST = Block.box(14.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    protected static final VoxelShape SHAPE_WEST = Block.box(0.0, 0.0, 0.0, 2.0, 16.0, 16.0);

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public CannonBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(f_52588_, POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52588_, context.m_43719_().getOpposite())).m_61124_(POWERED, context.m_43725_().m_276867_(context.getClickedPos()));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(f_52588_, rot.rotate((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.m_6402_(level, pos, state, placer, stack);
        if (placer != null && level.getBlockEntity(pos) instanceof CannonBlockTile cannon) {
            Direction dir = Direction.orderedByNearest(placer)[0];
            Direction myDir = (Direction) state.m_61143_(f_52588_);
            int i = dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1;
            if (dir.getAxis() == Direction.Axis.Y) {
                float pitch = dir == Direction.UP ? -90.0F : 90.0F;
                cannon.setPitch(myDir.getOpposite() == dir ? pitch + 180.0F : pitch);
            } else {
                float yaw = dir.toYRot();
                cannon.setYaw(myDir.getOpposite() == dir ? yaw + 180.0F : yaw);
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CannonBlockTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return Utils.getTicker(pBlockEntityType, null, CannonBlockTile::tick);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof CannonBlockTile tile) {
            tile.use(player, hand, hit);
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return super.m_6227_(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public boolean isLitUp(BlockState blockState) {
        return false;
    }

    @Override
    public BlockState toggleLitState(BlockState blockState, boolean b) {
        return null;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return switch((Direction) state.m_61143_(f_52588_)) {
            case UP ->
                SHAPE_UP;
            case DOWN ->
                SHAPE_DOWN;
            case NORTH ->
                SHAPE_NORTH;
            case SOUTH ->
                SHAPE_SOUTH;
            case WEST ->
                SHAPE_WEST;
            case EAST ->
                SHAPE_EAST;
        };
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext ec && ec.getEntity() instanceof LivingEntity) {
            return super.m_5939_(state, level, pos, context);
        }
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(f_52588_)) {
            case UP ->
                SHAPE_UP;
            case DOWN ->
                SHAPE_DOWN;
            case NORTH ->
                SHAPE_NORTH;
            case SOUTH ->
                SHAPE_SOUTH;
            case WEST ->
                SHAPE_WEST;
            case EAST ->
                SHAPE_EAST;
        };
    }

    @Override
    public Optional<BlockState> getRotatedState(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, Rotation rotation, Direction direction, @Nullable Vec3 vec3) {
        return Optional.empty();
    }
}