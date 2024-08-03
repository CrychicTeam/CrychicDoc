package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WallHangingSignBlock extends SignBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final VoxelShape PLANK_NORTHSOUTH = Block.box(0.0, 14.0, 6.0, 16.0, 16.0, 10.0);

    public static final VoxelShape PLANK_EASTWEST = Block.box(6.0, 14.0, 0.0, 10.0, 16.0, 16.0);

    public static final VoxelShape SHAPE_NORTHSOUTH = Shapes.or(PLANK_NORTHSOUTH, Block.box(1.0, 0.0, 7.0, 15.0, 10.0, 9.0));

    public static final VoxelShape SHAPE_EASTWEST = Shapes.or(PLANK_EASTWEST, Block.box(7.0, 0.0, 1.0, 9.0, 10.0, 15.0));

    private static final Map<Direction, VoxelShape> AABBS = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, SHAPE_NORTHSOUTH, Direction.SOUTH, SHAPE_NORTHSOUTH, Direction.EAST, SHAPE_EASTWEST, Direction.WEST, SHAPE_EASTWEST));

    public WallHangingSignBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0.sound(woodType1.hangingSignSoundType()), woodType1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(f_56268_, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.getBlockEntity(blockPos2) instanceof SignBlockEntity $$6) {
            ItemStack $$7 = player3.m_21120_(interactionHand4);
            if (this.shouldTryToChainAnotherHangingSign(blockState0, player3, blockHitResult5, $$6, $$7)) {
                return InteractionResult.PASS;
            }
        }
        return super.use(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
    }

    private boolean shouldTryToChainAnotherHangingSign(BlockState blockState0, Player player1, BlockHitResult blockHitResult2, SignBlockEntity signBlockEntity3, ItemStack itemStack4) {
        return !signBlockEntity3.canExecuteClickCommands(signBlockEntity3.isFacingFrontText(player1), player1) && itemStack4.getItem() instanceof HangingSignItem && !this.isHittingEditableSide(blockHitResult2, blockState0);
    }

    private boolean isHittingEditableSide(BlockHitResult blockHitResult0, BlockState blockState1) {
        return blockHitResult0.getDirection().getAxis() == ((Direction) blockState1.m_61143_(FACING)).getAxis();
    }

    @Override
    public String getDescriptionId() {
        return this.m_5456_().getDescriptionId();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return (VoxelShape) AABBS.get(blockState0.m_61143_(FACING));
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.getShape(blockState0, blockGetter1, blockPos2, CollisionContext.empty());
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        switch((Direction) blockState0.m_61143_(FACING)) {
            case EAST:
            case WEST:
                return PLANK_EASTWEST;
            default:
                return PLANK_NORTHSOUTH;
        }
    }

    public boolean canPlace(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        Direction $$3 = ((Direction) blockState0.m_61143_(FACING)).getClockWise();
        Direction $$4 = ((Direction) blockState0.m_61143_(FACING)).getCounterClockWise();
        return this.canAttachTo(levelReader1, blockState0, blockPos2.relative($$3), $$4) || this.canAttachTo(levelReader1, blockState0, blockPos2.relative($$4), $$3);
    }

    public boolean canAttachTo(LevelReader levelReader0, BlockState blockState1, BlockPos blockPos2, Direction direction3) {
        BlockState $$4 = levelReader0.m_8055_(blockPos2);
        return $$4.m_204336_(BlockTags.WALL_HANGING_SIGNS) ? ((Direction) $$4.m_61143_(FACING)).getAxis().test((Direction) blockState1.m_61143_(FACING)) : $$4.m_60659_(levelReader0, blockPos2, direction3, SupportType.FULL);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = this.m_49966_();
        FluidState $$2 = blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos());
        LevelReader $$3 = blockPlaceContext0.m_43725_();
        BlockPos $$4 = blockPlaceContext0.getClickedPos();
        for (Direction $$5 : blockPlaceContext0.getNearestLookingDirections()) {
            if ($$5.getAxis().isHorizontal() && !$$5.getAxis().test(blockPlaceContext0.m_43719_())) {
                Direction $$6 = $$5.getOpposite();
                $$1 = (BlockState) $$1.m_61124_(FACING, $$6);
                if ($$1.m_60710_($$3, $$4) && this.canPlace($$1, $$3, $$4)) {
                    return (BlockState) $$1.m_61124_(f_56268_, $$2.getType() == Fluids.WATER);
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1.getAxis() == ((Direction) blockState0.m_61143_(FACING)).getClockWise().getAxis() && !blockState0.m_60710_(levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public float getYRotationDegrees(BlockState blockState0) {
        return ((Direction) blockState0.m_61143_(FACING)).toYRot();
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, f_56268_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new HangingSignBlockEntity(blockPos0, blockState1);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.HANGING_SIGN, SignBlockEntity::m_276836_);
    }
}