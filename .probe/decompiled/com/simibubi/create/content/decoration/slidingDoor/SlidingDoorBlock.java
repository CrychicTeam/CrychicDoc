package com.simibubi.create.content.decoration.slidingDoor;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SlidingDoorBlock extends DoorBlock implements IWrenchable, IBE<SlidingDoorBlockEntity> {

    public static final Supplier<BlockSetType> TRAIN_SET_TYPE = () -> new BlockSetType("train", true, SoundType.NETHERITE_BLOCK, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);

    public static final Supplier<BlockSetType> GLASS_SET_TYPE = () -> new BlockSetType("train", true, SoundType.NETHERITE_BLOCK, SoundEvents.IRON_DOOR_CLOSE, SoundEvents.IRON_DOOR_OPEN, SoundEvents.IRON_TRAPDOOR_CLOSE, SoundEvents.IRON_TRAPDOOR_OPEN, SoundEvents.METAL_PRESSURE_PLATE_CLICK_OFF, SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF, SoundEvents.STONE_BUTTON_CLICK_ON);

    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    private boolean folds;

    public static SlidingDoorBlock metal(BlockBehaviour.Properties blockBehaviourProperties0, boolean folds) {
        return new SlidingDoorBlock(blockBehaviourProperties0, (BlockSetType) TRAIN_SET_TYPE.get(), folds);
    }

    public static SlidingDoorBlock glass(BlockBehaviour.Properties blockBehaviourProperties0, boolean folds) {
        return new SlidingDoorBlock(blockBehaviourProperties0, (BlockSetType) GLASS_SET_TYPE.get(), folds);
    }

    public SlidingDoorBlock(BlockBehaviour.Properties blockBehaviourProperties0, BlockSetType type, boolean folds) {
        super(blockBehaviourProperties0, type);
        this.folds = folds;
    }

    public boolean isFoldingDoor() {
        return this.folds;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(VISIBLE));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if ((Boolean) pState.m_61143_(f_52727_) || !(Boolean) pState.m_61143_(VISIBLE) && !(pLevel instanceof ContraptionWorld)) {
            Direction direction = (Direction) pState.m_61143_(f_52726_);
            boolean hinge = pState.m_61143_(f_52728_) == DoorHingeSide.RIGHT;
            return SlidingDoorShapes.get(direction, hinge, this.isFoldingDoor());
        } else {
            return super.getShape(pState, pLevel, pPos, pContext);
        }
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return pState.m_61143_(f_52730_) == DoubleBlockHalf.LOWER || pLevel.m_8055_(pPos.below()).m_60713_(this);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return this.getShape(pState, pLevel, pPos, CollisionContext.empty());
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        return stateForPlacement != null && stateForPlacement.m_61143_(f_52727_) ? (BlockState) ((BlockState) stateForPlacement.m_61124_(f_52727_, false)).m_61124_(f_52729_, false) : stateForPlacement;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.m_60713_(this)) {
            this.deferUpdate(pLevel, pPos);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BlockState blockState = super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        if (blockState.m_60795_()) {
            return blockState;
        } else {
            DoubleBlockHalf doubleblockhalf = (DoubleBlockHalf) blockState.m_61143_(f_52730_);
            if (pFacing.getAxis() == Direction.Axis.Y && doubleblockhalf == DoubleBlockHalf.LOWER == (pFacing == Direction.UP)) {
                return pFacingState.m_60713_(this) && pFacingState.m_61143_(f_52730_) != doubleblockhalf ? (BlockState) blockState.m_61124_(VISIBLE, (Boolean) pFacingState.m_61143_(VISIBLE)) : Blocks.AIR.defaultBlockState();
            } else {
                return blockState;
            }
        }
    }

    @Override
    public void setOpen(@Nullable Entity entity, Level level, BlockState state, BlockPos pos, boolean open) {
        if (state.m_60713_(this)) {
            if ((Boolean) state.m_61143_(f_52727_) != open) {
                BlockState changedState = (BlockState) state.m_61124_(f_52727_, open);
                if (open) {
                    changedState = (BlockState) changedState.m_61124_(VISIBLE, false);
                }
                level.setBlock(pos, changedState, 10);
                DoorHingeSide hinge = (DoorHingeSide) changedState.m_61143_(f_52728_);
                Direction facing = (Direction) changedState.m_61143_(f_52726_);
                BlockPos otherPos = pos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
                BlockState otherDoor = level.getBlockState(otherPos);
                if (isDoubleDoor(changedState, hinge, facing, otherDoor)) {
                    this.setOpen(entity, level, otherDoor, otherPos, open);
                }
                this.playSound(entity, level, pos, open);
                level.m_142346_(entity, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
            }
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean lower = pState.m_61143_(f_52730_) == DoubleBlockHalf.LOWER;
        boolean isPowered = isDoorPowered(pLevel, pPos, pState);
        if (!this.m_49966_().m_60713_(pBlock)) {
            if (isPowered != (Boolean) pState.m_61143_(f_52729_)) {
                SlidingDoorBlockEntity be = this.getBlockEntity(pLevel, lower ? pPos : pPos.below());
                if (be == null || !be.deferUpdate) {
                    BlockState changedState = (BlockState) ((BlockState) pState.m_61124_(f_52729_, isPowered)).m_61124_(f_52727_, isPowered);
                    if (isPowered) {
                        changedState = (BlockState) changedState.m_61124_(VISIBLE, false);
                    }
                    if (isPowered != (Boolean) pState.m_61143_(f_52727_)) {
                        this.playSound(null, pLevel, pPos, isPowered);
                        pLevel.m_142346_(null, isPowered ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
                        DoorHingeSide hinge = (DoorHingeSide) changedState.m_61143_(f_52728_);
                        Direction facing = (Direction) changedState.m_61143_(f_52726_);
                        BlockPos otherPos = pPos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
                        BlockState otherDoor = pLevel.getBlockState(otherPos);
                        if (isDoubleDoor(changedState, hinge, facing, otherDoor)) {
                            otherDoor = (BlockState) ((BlockState) otherDoor.m_61124_(f_52729_, isPowered)).m_61124_(f_52727_, isPowered);
                            if (isPowered) {
                                otherDoor = (BlockState) otherDoor.m_61124_(VISIBLE, false);
                            }
                            pLevel.setBlock(otherPos, otherDoor, 2);
                        }
                    }
                    pLevel.setBlock(pPos, changedState, 2);
                }
            }
        }
    }

    public static boolean isDoorPowered(Level pLevel, BlockPos pPos, BlockState state) {
        boolean lower = state.m_61143_(f_52730_) == DoubleBlockHalf.LOWER;
        DoorHingeSide hinge = (DoorHingeSide) state.m_61143_(f_52728_);
        Direction facing = (Direction) state.m_61143_(f_52726_);
        BlockPos otherPos = pPos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
        BlockState otherDoor = pLevel.getBlockState(otherPos);
        return !isDoubleDoor((BlockState) state.m_61122_(f_52727_), hinge, facing, otherDoor) || !pLevel.m_276867_(otherPos) && !pLevel.m_276867_(otherPos.relative(lower ? Direction.UP : Direction.DOWN)) ? pLevel.m_276867_(pPos) || pLevel.m_276867_(pPos.relative(lower ? Direction.UP : Direction.DOWN)) : true;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pState = (BlockState) pState.m_61122_(f_52727_);
        if ((Boolean) pState.m_61143_(f_52727_)) {
            pState = (BlockState) pState.m_61124_(VISIBLE, false);
        }
        pLevel.setBlock(pPos, pState, 10);
        pLevel.m_142346_(pPlayer, this.m_52815_(pState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pPos);
        DoorHingeSide hinge = (DoorHingeSide) pState.m_61143_(f_52728_);
        Direction facing = (Direction) pState.m_61143_(f_52726_);
        BlockPos otherPos = pPos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
        BlockState otherDoor = pLevel.getBlockState(otherPos);
        if (isDoubleDoor(pState, hinge, facing, otherDoor)) {
            this.use(otherDoor, pLevel, otherPos, pPlayer, pHand, pHit);
        } else if ((Boolean) pState.m_61143_(f_52727_)) {
            pLevel.m_142346_(pPlayer, GameEvent.BLOCK_OPEN, pPos);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    public void deferUpdate(LevelAccessor level, BlockPos pos) {
        this.withBlockEntityDo(level, pos, sdte -> sdte.deferUpdate = true);
    }

    public static boolean isDoubleDoor(BlockState pState, DoorHingeSide hinge, Direction facing, BlockState otherDoor) {
        return otherDoor.m_60734_() == pState.m_60734_() && otherDoor.m_61143_(f_52728_) != hinge && otherDoor.m_61143_(f_52726_) == facing && otherDoor.m_61143_(f_52727_) != pState.m_61143_(f_52727_) && otherDoor.m_61143_(f_52730_) == pState.m_61143_(f_52730_);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return pState.m_61143_(VISIBLE) ? RenderShape.MODEL : RenderShape.ENTITYBLOCK_ANIMATED;
    }

    private void playSound(@Nullable Entity pSource, Level pLevel, BlockPos pPos, boolean pIsOpening) {
        pLevel.playSound(pSource, pPos, pIsOpening ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.IRON_DOOR_CLOSE, SoundSource.BLOCKS, 1.0F, pLevel.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.m_61143_(f_52730_) == DoubleBlockHalf.UPPER ? null : IBE.super.newBlockEntity(pos, state);
    }

    @Override
    public Class<SlidingDoorBlockEntity> getBlockEntityClass() {
        return SlidingDoorBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SlidingDoorBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SlidingDoorBlockEntity>) AllBlockEntityTypes.SLIDING_DOOR.get();
    }
}