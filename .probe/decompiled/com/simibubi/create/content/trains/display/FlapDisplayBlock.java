package com.simibubi.create.content.trains.display;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.clipboard.ClipboardEntry;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;

public class FlapDisplayBlock extends HorizontalKineticBlock implements IBE<FlapDisplayBlockEntity>, IWrenchable, ICogWheel, SimpleWaterloggedBlock {

    public static final BooleanProperty UP = BooleanProperty.create("up");

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    private static final int placementHelperId = PlacementHelpers.register(new FlapDisplayBlock.PlacementHelper());

    public FlapDisplayBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(UP, false)).m_61124_(DOWN, false)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    protected boolean areStatesKineticallyEquivalent(BlockState oldState, BlockState newState) {
        return super.areStatesKineticallyEquivalent(oldState, newState);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(UP, DOWN, BlockStateProperties.WATERLOGGED));
    }

    @Override
    public IRotate.SpeedLevel getMinimumRequiredSpeedLevel() {
        return IRotate.SpeedLevel.MEDIUM;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.m_43719_();
        BlockPos clickedPos = context.getClickedPos();
        BlockPos placedOnPos = clickedPos.relative(face.getOpposite());
        Level level = context.m_43725_();
        BlockState blockState = level.getBlockState(placedOnPos);
        BlockState stateForPlacement = this.m_49966_();
        FluidState ifluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        if (blockState.m_60734_() == this && (context.m_43723_() == null || !context.m_43723_().m_6144_())) {
            Direction otherFacing = (Direction) blockState.m_61143_(HORIZONTAL_FACING);
            stateForPlacement = (BlockState) stateForPlacement.m_61124_(HORIZONTAL_FACING, otherFacing);
        } else {
            stateForPlacement = super.getStateForPlacement(context);
        }
        return this.updateColumn(level, clickedPos, (BlockState) stateForPlacement.m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER), true);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else {
            ItemStack heldItem = player.m_21120_(hand);
            IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
            if (placementHelper.matchesItem(heldItem)) {
                return placementHelper.getOffset(player, world, state, pos, ray).placeInWorld(world, (BlockItem) heldItem.getItem(), player, hand, ray);
            } else {
                FlapDisplayBlockEntity flapBE = this.getBlockEntity(world, pos);
                if (flapBE == null) {
                    return InteractionResult.PASS;
                } else {
                    flapBE = flapBE.getController();
                    if (flapBE == null) {
                        return InteractionResult.PASS;
                    } else {
                        double yCoord = ray.m_82450_().add(Vec3.atLowerCornerOf(ray.getDirection().getOpposite().getNormal()).scale(0.125)).y;
                        int lineIndex = flapBE.getLineIndexAt(yCoord);
                        if (heldItem.isEmpty()) {
                            if (!flapBE.isSpeedRequirementFulfilled()) {
                                return InteractionResult.PASS;
                            } else {
                                flapBE.applyTextManually(lineIndex, null);
                                return InteractionResult.SUCCESS;
                            }
                        } else if (heldItem.getItem() == Items.GLOW_INK_SAC) {
                            if (!world.isClientSide) {
                                world.playSound(null, pos, SoundEvents.INK_SAC_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                flapBE.setGlowing(lineIndex);
                            }
                            return InteractionResult.SUCCESS;
                        } else {
                            boolean display = heldItem.getItem() == Items.NAME_TAG && heldItem.hasCustomHoverName() || AllBlocks.CLIPBOARD.isIn(heldItem);
                            DyeColor dye = DyeColor.getColor(heldItem);
                            if (!display && dye == null) {
                                return InteractionResult.PASS;
                            } else if (dye == null && !flapBE.isSpeedRequirementFulfilled()) {
                                return InteractionResult.PASS;
                            } else if (world.isClientSide) {
                                return InteractionResult.SUCCESS;
                            } else {
                                CompoundTag tag = heldItem.getTagElement("display");
                                String tagElement = tag != null && tag.contains("Name", 8) ? tag.getString("Name") : null;
                                if (display) {
                                    if (AllBlocks.CLIPBOARD.isIn(heldItem)) {
                                        List<ClipboardEntry> entries = ClipboardEntry.getLastViewedEntries(heldItem);
                                        int line = lineIndex;
                                        for (int i = 0; i < entries.size(); i++) {
                                            for (String string : ((ClipboardEntry) entries.get(i)).text.getString().split("\n")) {
                                                flapBE.applyTextManually(line++, Component.Serializer.toJson(Components.literal(string)));
                                            }
                                        }
                                        return InteractionResult.SUCCESS;
                                    }
                                    flapBE.applyTextManually(lineIndex, tagElement);
                                }
                                if (dye != null) {
                                    world.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                                    flapBE.setColour(lineIndex, dye);
                                }
                                return InteractionResult.SUCCESS;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.FLAP_DISPLAY.get((Direction) pState.m_61143_(HORIZONTAL_FACING));
    }

    @Override
    public Class<FlapDisplayBlockEntity> getBlockEntityClass() {
        return FlapDisplayBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends FlapDisplayBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends FlapDisplayBlockEntity>) AllBlockEntityTypes.FLAP_DISPLAY.get();
    }

    @Override
    public float getParticleTargetRadius() {
        return 0.85F;
    }

    @Override
    public float getParticleInitialRadius() {
        return 0.75F;
    }

    private BlockState updateColumn(Level level, BlockPos pos, BlockState state, boolean present) {
        BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
        Direction.Axis axis = this.getConnectionAxis(state);
        for (Direction connection : Iterate.directionsInAxis(Direction.Axis.Y)) {
            boolean connect = true;
            label48: for (Direction movement : Iterate.directionsInAxis(axis)) {
                currentPos.set(pos);
                int i = 0;
                while (true) {
                    if (i < 1000 && level.isLoaded(currentPos)) {
                        BlockState other1 = currentPos.equals(pos) ? state : level.getBlockState(currentPos);
                        BlockState other2 = level.getBlockState(currentPos.m_121945_(connection));
                        boolean col1 = this.canConnect(state, other1);
                        boolean col2 = this.canConnect(state, other2);
                        currentPos.move(movement);
                        if (col1 || col2) {
                            if (!col1 || !col2) {
                                connect = false;
                                break label48;
                            }
                            i++;
                            continue;
                        }
                    }
                }
            }
            state = setConnection(state, connection, connect);
        }
        return state;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.m_6807_(pState, pLevel, pPos, pOldState, pIsMoving);
        if (pOldState.m_60734_() != this) {
            LevelTickAccess<Block> blockTicks = pLevel.m_183326_();
            if (!blockTicks.m_183582_(pPos, this)) {
                pLevel.m_186460_(pPos, this, 1);
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.m_60734_() == this) {
            BlockPos belowPos = pPos.relative(Direction.fromAxisAndDirection(this.getConnectionAxis(pState), Direction.AxisDirection.NEGATIVE));
            BlockState belowState = pLevel.m_8055_(belowPos);
            if (!this.canConnect(pState, belowState)) {
                KineticBlockEntity.switchToBlockState(pLevel, pPos, this.updateColumn(pLevel, pPos, pState, true));
            }
            this.withBlockEntityDo(pLevel, pPos, FlapDisplayBlockEntity::updateControllerStatus);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        return this.updatedShapeInner(state, pDirection, pNeighborState, pLevel, pCurrentPos);
    }

    private BlockState updatedShapeInner(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        if (!this.canConnect(state, pNeighborState)) {
            return setConnection(state, pDirection, false);
        } else {
            return pDirection.getAxis() == this.getConnectionAxis(state) ? (BlockState) this.m_152465_(pNeighborState).m_61124_(BlockStateProperties.WATERLOGGED, (Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) : setConnection(state, pDirection, getConnection(pNeighborState, pDirection.getOpposite()));
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    protected boolean canConnect(BlockState state, BlockState other) {
        return other.m_60734_() == this && state.m_61143_(HORIZONTAL_FACING) == other.m_61143_(HORIZONTAL_FACING);
    }

    protected Direction.Axis getConnectionAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise().getAxis();
    }

    public static boolean getConnection(BlockState state, Direction side) {
        BooleanProperty property = side == Direction.DOWN ? DOWN : (side == Direction.UP ? UP : null);
        return property != null && (Boolean) state.m_61143_(property);
    }

    public static BlockState setConnection(BlockState state, Direction side, boolean connect) {
        BooleanProperty property = side == Direction.DOWN ? DOWN : (side == Direction.UP ? UP : null);
        if (property != null) {
            state = (BlockState) state.m_61124_(property, connect);
        }
        return state;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
        if (!pIsMoving && pNewState.m_60734_() != this) {
            for (Direction d : Iterate.directionsInAxis(this.getConnectionAxis(pState))) {
                BlockPos relative = pPos.relative(d);
                BlockState adjacent = pLevel.getBlockState(relative);
                if (this.canConnect(pState, adjacent)) {
                    KineticBlockEntity.switchToBlockState(pLevel, relative, this.updateColumn(pLevel, relative, adjacent, false));
                }
            }
        }
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.DISPLAY_BOARD::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return AllBlocks.DISPLAY_BOARD::has;
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), ((Direction) state.m_61143_(FlapDisplayBlock.HORIZONTAL_FACING)).getAxis(), dir -> world.getBlockState(pos.relative(dir)).m_247087_());
            return directions.isEmpty() ? PlacementOffset.fail() : PlacementOffset.success(pos.relative((Direction) directions.get(0)), s -> ((FlapDisplayBlock) AllBlocks.DISPLAY_BOARD.get()).updateColumn(world, pos.relative((Direction) directions.get(0)), (BlockState) s.m_61124_(HorizontalKineticBlock.HORIZONTAL_FACING, (Direction) state.m_61143_(FlapDisplayBlock.HORIZONTAL_FACING)), true));
        }
    }
}