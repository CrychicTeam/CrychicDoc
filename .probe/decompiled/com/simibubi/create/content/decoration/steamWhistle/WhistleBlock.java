package com.simibubi.create.content.decoration.steamWhistle;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WhistleBlock extends Block implements IBE<WhistleBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty WALL = BooleanProperty.create("wall");

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public static final EnumProperty<WhistleBlock.WhistleSize> SIZE = EnumProperty.create("size", WhistleBlock.WhistleSize.class);

    public WhistleBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(POWERED, false)).m_61124_(WALL, false)).m_61124_(SIZE, WhistleBlock.WhistleSize.MEDIUM));
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return FluidTankBlock.isTank(pLevel.m_8055_(pPos.relative(getAttachedDirection(pState))));
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return (BlockState) originalState.m_61122_(SIZE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, POWERED, SIZE, WALL));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.m_43725_();
        BlockPos clickedPos = pContext.getClickedPos();
        Direction face = pContext.m_43719_();
        boolean wall = true;
        if (face.getAxis() == Direction.Axis.Y) {
            face = pContext.m_8125_().getOpposite();
            wall = false;
        }
        BlockState state = (BlockState) ((BlockState) ((BlockState) super.getStateForPlacement(pContext).m_61124_(FACING, face.getOpposite())).m_61124_(POWERED, level.m_276867_(clickedPos))).m_61124_(WALL, wall);
        return !this.canSurvive(state, level, clickedPos) ? null : state;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer == null) {
            return InteractionResult.PASS;
        } else {
            ItemStack heldItem = pPlayer.m_21120_(pHand);
            if (AllBlocks.STEAM_WHISTLE.isIn(heldItem)) {
                incrementSize(pLevel, pPos);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    public static void incrementSize(LevelAccessor pLevel, BlockPos pPos) {
        BlockState base = pLevel.m_8055_(pPos);
        if (base.m_61138_(SIZE)) {
            WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) base.m_61143_(SIZE);
            SoundType soundtype = base.m_60827_();
            BlockPos currentPos = pPos.above();
            for (int i = 1; i <= 6; i++) {
                BlockState blockState = pLevel.m_8055_(currentPos);
                float pVolume = (soundtype.getVolume() + 1.0F) / 2.0F;
                SoundEvent growSound = (SoundEvent) SoundEvents.NOTE_BLOCK_XYLOPHONE.get();
                SoundEvent hitSound = soundtype.getHitSound();
                if (!AllBlocks.STEAM_WHISTLE_EXTENSION.has(blockState)) {
                    if (!blockState.m_247087_()) {
                        return;
                    }
                    pLevel.m_7731_(currentPos, (BlockState) AllBlocks.STEAM_WHISTLE_EXTENSION.getDefaultState().m_61124_(SIZE, size), 3);
                    if (soundtype != null) {
                        float pPitch = (float) Math.pow(2.0, (double) (-(i * 2 - 1)) / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4.0F, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }
                    return;
                }
                if (blockState.m_61143_(WhistleExtenderBlock.SHAPE) == WhistleExtenderBlock.WhistleExtenderShape.SINGLE) {
                    pLevel.m_7731_(currentPos, (BlockState) blockState.m_61124_(WhistleExtenderBlock.SHAPE, WhistleExtenderBlock.WhistleExtenderShape.DOUBLE), 3);
                    if (soundtype != null) {
                        float pPitch = (float) Math.pow(2.0, (double) (-(i * 2)) / 12.0);
                        pLevel.playSound(null, currentPos, growSound, SoundSource.BLOCKS, pVolume / 4.0F, pPitch);
                        pLevel.playSound(null, currentPos, hitSound, SoundSource.BLOCKS, pVolume, pPitch);
                    }
                    return;
                }
                currentPos = currentPos.above();
            }
        }
    }

    public static void queuePitchUpdate(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.m_8055_(pos);
        if (blockState.m_60734_() instanceof WhistleBlock whistle && !level.getBlockTicks().m_183582_(pos, whistle)) {
            level.scheduleTick(pos, whistle, 1);
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        this.withBlockEntityDo(pLevel, pPos, WhistleBlockEntity::updatePitch);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
        if (pOldState.m_60734_() != this || pOldState.m_61143_(SIZE) != pState.m_61143_(SIZE)) {
            queuePitchUpdate(pLevel, pPos);
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        IBE.onRemove(pState, pLevel, pPos, pNewState);
        FluidTankBlock.updateBoilerState(pState, pLevel, pPos.relative(getAttachedDirection(pState)));
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if (!worldIn.isClientSide) {
            boolean previouslyPowered = (Boolean) state.m_61143_(POWERED);
            if (previouslyPowered != worldIn.m_276867_(pos)) {
                worldIn.setBlock(pos, (BlockState) state.m_61122_(POWERED), 2);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return getAttachedDirection(pState) == pFacing && !pState.m_60710_(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : pState;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) pState.m_61143_(SIZE);
        if (!(Boolean) pState.m_61143_(WALL)) {
            return size == WhistleBlock.WhistleSize.SMALL ? AllShapes.WHISTLE_SMALL_FLOOR : (size == WhistleBlock.WhistleSize.MEDIUM ? AllShapes.WHISTLE_MEDIUM_FLOOR : AllShapes.WHISTLE_LARGE_FLOOR);
        } else {
            Direction direction = (Direction) pState.m_61143_(FACING);
            return (size == WhistleBlock.WhistleSize.SMALL ? AllShapes.WHISTLE_SMALL_WALL : (size == WhistleBlock.WhistleSize.MEDIUM ? AllShapes.WHISTLE_MEDIUM_WALL : AllShapes.WHISTLE_LARGE_WALL)).get(direction);
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    public static Direction getAttachedDirection(BlockState state) {
        return state.m_61143_(WALL) ? (Direction) state.m_61143_(FACING) : Direction.DOWN;
    }

    @Override
    public Class<WhistleBlockEntity> getBlockEntityClass() {
        return WhistleBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends WhistleBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends WhistleBlockEntity>) AllBlockEntityTypes.STEAM_WHISTLE.get();
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(FACING, pRotation.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pMirror == Mirror.NONE ? pState : pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    public static enum WhistleSize implements StringRepresentable {

        SMALL, MEDIUM, LARGE;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}