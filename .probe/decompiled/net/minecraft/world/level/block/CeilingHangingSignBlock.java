package net.minecraft.world.level.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CeilingHangingSignBlock extends SignBlock {

    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;

    public static final BooleanProperty ATTACHED = BlockStateProperties.ATTACHED;

    protected static final float AABB_OFFSET = 5.0F;

    protected static final VoxelShape SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);

    private static final Map<Integer, VoxelShape> AABBS = Maps.newHashMap(ImmutableMap.of(0, Block.box(1.0, 0.0, 7.0, 15.0, 10.0, 9.0), 4, Block.box(7.0, 0.0, 1.0, 9.0, 10.0, 15.0), 8, Block.box(1.0, 0.0, 7.0, 15.0, 10.0, 9.0), 12, Block.box(7.0, 0.0, 1.0, 9.0, 10.0, 15.0)));

    public CeilingHangingSignBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0.sound(woodType1.hangingSignSoundType()), woodType1);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(ROTATION, 0)).m_61124_(ATTACHED, false)).m_61124_(f_56268_, false));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.getBlockEntity(blockPos2) instanceof SignBlockEntity $$6) {
            ItemStack $$7 = player3.m_21120_(interactionHand4);
            if (this.shouldTryToChainAnotherHangingSign(player3, blockHitResult5, $$6, $$7)) {
                return InteractionResult.PASS;
            }
        }
        return super.use(blockState0, level1, blockPos2, player3, interactionHand4, blockHitResult5);
    }

    private boolean shouldTryToChainAnotherHangingSign(Player player0, BlockHitResult blockHitResult1, SignBlockEntity signBlockEntity2, ItemStack itemStack3) {
        return !signBlockEntity2.canExecuteClickCommands(signBlockEntity2.isFacingFrontText(player0), player0) && itemStack3.getItem() instanceof HangingSignItem && blockHitResult1.getDirection().equals(Direction.DOWN);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return levelReader1.m_8055_(blockPos2.above()).m_60659_(levelReader1, blockPos2.above(), Direction.DOWN, SupportType.CENTER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Level $$1 = blockPlaceContext0.m_43725_();
        FluidState $$2 = $$1.getFluidState(blockPlaceContext0.getClickedPos());
        BlockPos $$3 = blockPlaceContext0.getClickedPos().above();
        BlockState $$4 = $$1.getBlockState($$3);
        boolean $$5 = $$4.m_204336_(BlockTags.ALL_HANGING_SIGNS);
        Direction $$6 = Direction.fromYRot((double) blockPlaceContext0.m_7074_());
        boolean $$7 = !Block.isFaceFull($$4.m_60812_($$1, $$3), Direction.DOWN) || blockPlaceContext0.m_7078_();
        if ($$5 && !blockPlaceContext0.m_7078_()) {
            if ($$4.m_61138_(WallHangingSignBlock.FACING)) {
                Direction $$8 = (Direction) $$4.m_61143_(WallHangingSignBlock.FACING);
                if ($$8.getAxis().test($$6)) {
                    $$7 = false;
                }
            } else if ($$4.m_61138_(ROTATION)) {
                Optional<Direction> $$9 = RotationSegment.convertToDirection((Integer) $$4.m_61143_(ROTATION));
                if ($$9.isPresent() && ((Direction) $$9.get()).getAxis().test($$6)) {
                    $$7 = false;
                }
            }
        }
        int $$10 = !$$7 ? RotationSegment.convertToSegment($$6.getOpposite()) : RotationSegment.convertToSegment(blockPlaceContext0.m_7074_() + 180.0F);
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(ATTACHED, $$7)).m_61124_(ROTATION, $$10)).m_61124_(f_56268_, $$2.getType() == Fluids.WATER);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        VoxelShape $$4 = (VoxelShape) AABBS.get(blockState0.m_61143_(ROTATION));
        return $$4 == null ? SHAPE : $$4;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.getShape(blockState0, blockGetter1, blockPos2, CollisionContext.empty());
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return direction1 == Direction.UP && !this.canSurvive(blockState0, levelAccessor3, blockPos4) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public float getYRotationDegrees(BlockState blockState0) {
        return RotationSegment.convertToDegrees((Integer) blockState0.m_61143_(ROTATION));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(ROTATION, rotation1.rotate((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return (BlockState) blockState0.m_61124_(ROTATION, mirror1.mirror((Integer) blockState0.m_61143_(ROTATION), 16));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(ROTATION, ATTACHED, f_56268_);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new HangingSignBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.HANGING_SIGN, SignBlockEntity::m_276836_);
    }
}