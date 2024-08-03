package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SculkShriekerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty SHRIEKING = BlockStateProperties.SHRIEKING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty CAN_SUMMON = BlockStateProperties.CAN_SUMMON;

    protected static final VoxelShape COLLIDER = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final double TOP_Y = COLLIDER.max(Direction.Axis.Y);

    public SculkShriekerBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(SHRIEKING, false)).m_61124_(WATERLOGGED, false)).m_61124_(CAN_SUMMON, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(SHRIEKING);
        stateDefinitionBuilderBlockBlockState0.add(WATERLOGGED);
        stateDefinitionBuilderBlockBlockState0.add(CAN_SUMMON);
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        if (level0 instanceof ServerLevel $$4) {
            ServerPlayer $$5 = SculkShriekerBlockEntity.tryGetPlayer(entity3);
            if ($$5 != null) {
                $$4.m_141902_(blockPos1, BlockEntityType.SCULK_SHRIEKER).ifPresent(p_222163_ -> p_222163_.tryShriek($$4, $$5));
            }
        }
        super.m_141947_(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (level1 instanceof ServerLevel $$5 && (Boolean) blockState0.m_61143_(SHRIEKING) && !blockState0.m_60713_(blockState3.m_60734_())) {
            $$5.m_141902_(blockPos2, BlockEntityType.SCULK_SHRIEKER).ifPresent(p_222217_ -> p_222217_.tryRespond($$5));
        }
        super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(SHRIEKING)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(SHRIEKING, false), 3);
            serverLevel1.m_141902_(blockPos2, BlockEntityType.SCULK_SHRIEKER).ifPresent(p_222169_ -> p_222169_.tryRespond(serverLevel1));
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return COLLIDER;
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return COLLIDER;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new SculkShriekerBlockEntity(blockPos0, blockState1);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(WATERLOGGED, blockPlaceContext0.m_43725_().getFluidState(blockPlaceContext0.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
        super.m_213646_(blockState0, serverLevel1, blockPos2, itemStack3, boolean4);
        if (boolean4) {
            this.m_220822_(serverLevel1, blockPos2, itemStack3, ConstantInt.of(5));
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return !level0.isClientSide ? BaseEntityBlock.createTickerHelper(blockEntityTypeT2, BlockEntityType.SCULK_SHRIEKER, (p_281134_, p_281135_, p_281136_, p_281137_) -> VibrationSystem.Ticker.tick(p_281134_, p_281137_.getVibrationData(), p_281137_.getVibrationUser())) : null;
    }
}