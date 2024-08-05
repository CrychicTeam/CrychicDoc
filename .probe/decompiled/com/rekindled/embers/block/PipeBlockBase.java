package com.rekindled.embers.block;

import com.rekindled.embers.api.block.IPipeConnection;
import com.rekindled.embers.blockentity.PipeBlockEntityBase;
import com.rekindled.embers.datagen.EmbersItemTags;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.util.Misc;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public abstract class PipeBlockBase extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape CENTER_AABB = Block.box(6.0, 6.0, 6.0, 10.0, 10.0, 10.0);

    public static final VoxelShape PIPE_DOWN_AABB = Block.box(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);

    public static final VoxelShape END_DOWN_AABB = Shapes.or(Block.box(5.0, 0.0, 5.0, 11.0, 4.0, 11.0), PIPE_DOWN_AABB);

    public static final VoxelShape PIPE_UP_AABB = Block.box(6.0, 10.0, 6.0, 10.0, 16.0, 10.0);

    public static final VoxelShape END_UP_AABB = Shapes.or(Block.box(5.0, 12.0, 5.0, 11.0, 16.0, 11.0), PIPE_UP_AABB);

    public static final VoxelShape PIPE_NORTH_AABB = Block.box(6.0, 6.0, 0.0, 10.0, 10.0, 6.0);

    public static final VoxelShape END_NORTH_AABB = Shapes.or(Block.box(5.0, 5.0, 0.0, 11.0, 11.0, 4.0), PIPE_NORTH_AABB);

    public static final VoxelShape PIPE_SOUTH_AABB = Block.box(6.0, 6.0, 10.0, 10.0, 10.0, 16.0);

    public static final VoxelShape END_SOUTH_AABB = Shapes.or(Block.box(5.0, 5.0, 12.0, 11.0, 11.0, 16.0), PIPE_SOUTH_AABB);

    public static final VoxelShape PIPE_WEST_AABB = Block.box(0.0, 6.0, 6.0, 6.0, 10.0, 10.0);

    public static final VoxelShape END_WEST_AABB = Shapes.or(Block.box(0.0, 5.0, 5.0, 4.0, 11.0, 11.0), PIPE_WEST_AABB);

    public static final VoxelShape PIPE_EAST_AABB = Block.box(10.0, 6.0, 6.0, 16.0, 10.0, 10.0);

    public static final VoxelShape END_EAST_AABB = Shapes.or(Block.box(12.0, 5.0, 5.0, 16.0, 11.0, 11.0), PIPE_EAST_AABB);

    public static final VoxelShape[] PIPE_AABBS = new VoxelShape[] { PIPE_DOWN_AABB, PIPE_UP_AABB, PIPE_NORTH_AABB, PIPE_SOUTH_AABB, PIPE_WEST_AABB, PIPE_EAST_AABB };

    public static final VoxelShape[] END_AABBS = new VoxelShape[] { END_DOWN_AABB, END_UP_AABB, END_NORTH_AABB, END_SOUTH_AABB, END_WEST_AABB, END_EAST_AABB };

    public static final VoxelShape[] SHAPES = new VoxelShape[729];

    public abstract TagKey<Block> getConnectionTag();

    public abstract TagKey<Block> getToggleConnectionTag();

    public abstract boolean connectToTile(BlockEntity var1, Direction var2);

    public abstract boolean unclog(BlockEntity var1, Level var2, BlockPos var3);

    public PipeBlockBase(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!Misc.isHoldingHammer(player, hand)) {
            return player.m_21120_(hand).is(EmbersItemTags.PIPE_UNCLOGGER) && this.unclog(level.getBlockEntity(pos), level, pos) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        } else {
            if (level.getBlockEntity(pos) instanceof PipeBlockEntityBase pipe) {
                double reach = player.getBlockReach();
                Vec3 eyePosition = player.m_146892_();
                Vec3 lookVector = player.m_20154_().multiply(reach, reach, reach).add(eyePosition);
                Vec3[] hitPositions = new Vec3[6];
                BlockHitResult centerHit = this.getCenterShape().clip(eyePosition, lookVector, pos);
                for (int i = 0; i < 6; i++) {
                    BlockHitResult partHit = null;
                    if (pipe.connections[i] == PipeBlockEntityBase.PipeConnection.END) {
                        partHit = END_AABBS[i].clip(eyePosition, lookVector, pos);
                    } else if (pipe.connections[i] == PipeBlockEntityBase.PipeConnection.PIPE) {
                        partHit = PIPE_AABBS[i].clip(eyePosition, lookVector, pos);
                    }
                    if (partHit != null) {
                        hitPositions[i] = partHit.m_82450_();
                    }
                }
                int closestHit = -1;
                double closestDistance = reach;
                if (centerHit != null) {
                    closestDistance = eyePosition.distanceTo(centerHit.m_82450_());
                }
                for (int i = 0; i < 6; i++) {
                    if (hitPositions[i] != null) {
                        double dist = eyePosition.distanceTo(hitPositions[i]);
                        if (dist < closestDistance) {
                            closestDistance = dist;
                            closestHit = i;
                        }
                    }
                }
                if (closestHit == -1) {
                    Direction face = hit.getDirection();
                    if (pipe.getConnection(face) != PipeBlockEntityBase.PipeConnection.DISABLED) {
                        return InteractionResult.PASS;
                    }
                    BlockPos facingPos = pos.relative(face);
                    BlockState facingState = level.getBlockState(facingPos);
                    if (facingState.m_204336_(this.getToggleConnectionTag()) && level.getBlockEntity(facingPos) instanceof PipeBlockEntityBase facingPipe) {
                        pipe.setConnection(face, PipeBlockEntityBase.PipeConnection.PIPE);
                        facingPipe.setConnection(face.getOpposite(), PipeBlockEntityBase.PipeConnection.PIPE);
                        level.updateNeighbourForOutputSignal(pos, this);
                        level.updateNeighbourForOutputSignal(facingPos, this);
                        level.playLocalSound((double) pos.m_123341_() + 0.5 + (double) face.getStepX() * 0.5, (double) pos.m_123342_() + 0.5 + (double) face.getStepY() * 0.5, (double) pos.m_123343_() + 0.5 + (double) face.getStepZ() * 0.5, EmbersSounds.PIPE_CONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        return InteractionResult.SUCCESS;
                    }
                    BlockEntity blockEntity = level.getBlockEntity(facingPos);
                    if (this.connectToTile(blockEntity, face)) {
                        if (facingState.m_60734_() instanceof IPipeConnection) {
                            pipe.setConnection(face, ((IPipeConnection) facingState.m_60734_()).getPipeConnection(facingState, face.getOpposite()));
                        } else {
                            pipe.setConnection(face, PipeBlockEntityBase.PipeConnection.END);
                        }
                        level.updateNeighbourForOutputSignal(pos, this);
                        facingState.m_60728_(face.getOpposite(), state, level, facingPos, pos);
                        level.playLocalSound((double) pos.m_123341_() + 0.5 + (double) face.getStepX() * 0.4, (double) pos.m_123342_() + 0.5 + (double) face.getStepY() * 0.4, (double) pos.m_123343_() + 0.5 + (double) face.getStepZ() * 0.4, EmbersSounds.PIPE_CONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    Direction direction = Direction.from3DDataValue(closestHit);
                    if (!pipe.getConnection(direction).transfer) {
                        return InteractionResult.PASS;
                    }
                    BlockPos facingPosx = pos.relative(direction);
                    BlockState facingStatex = level.getBlockState(facingPosx);
                    if (pipe.getConnection(direction) == PipeBlockEntityBase.PipeConnection.PIPE && facingStatex.m_204336_(this.getToggleConnectionTag()) && level.getBlockEntity(facingPosx) instanceof PipeBlockEntityBase facingPipe) {
                        pipe.setConnection(direction, PipeBlockEntityBase.PipeConnection.DISABLED);
                        facingPipe.setConnection(direction.getOpposite(), PipeBlockEntityBase.PipeConnection.DISABLED);
                        level.updateNeighbourForOutputSignal(pos, this);
                        level.updateNeighbourForOutputSignal(facingPosx, this);
                        level.playLocalSound((double) pos.m_123341_() + 0.5 + (double) direction.getStepX() * 0.5, (double) pos.m_123342_() + 0.5 + (double) direction.getStepY() * 0.5, (double) pos.m_123343_() + 0.5 + (double) direction.getStepZ() * 0.5, EmbersSounds.PIPE_DISCONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        return InteractionResult.SUCCESS;
                    }
                    if (pipe.getConnection(direction).transfer && !facingStatex.m_204336_(this.getConnectionTag()) && !this.connected(direction, facingStatex)) {
                        pipe.setConnection(direction, PipeBlockEntityBase.PipeConnection.DISABLED);
                        level.updateNeighbourForOutputSignal(pos, this);
                        facingStatex.m_60728_(direction.getOpposite(), state, level, facingPosx, pos);
                        level.playLocalSound((double) pos.m_123341_() + 0.5 + (double) direction.getStepX() * 0.4, (double) pos.m_123342_() + 0.5 + (double) direction.getStepY() * 0.4, (double) pos.m_123343_() + 0.5 + (double) direction.getStepZ() * 0.4, EmbersSounds.PIPE_DISCONNECT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            return InteractionResult.PASS;
        }
    }

    public static int getShapeIndex(PipeBlockEntityBase.PipeConnection down, PipeBlockEntityBase.PipeConnection up, PipeBlockEntityBase.PipeConnection north, PipeBlockEntityBase.PipeConnection south, PipeBlockEntityBase.PipeConnection west, PipeBlockEntityBase.PipeConnection east) {
        return ((((down.visualIndex * 3 + up.visualIndex) * 3 + north.visualIndex) * 3 + south.visualIndex) * 3 + west.visualIndex) * 3 + east.visualIndex;
    }

    public VoxelShape getCenterShape() {
        return CENTER_AABB;
    }

    public static void makeShapes(VoxelShape center, VoxelShape[] shapes) {
        for (PipeBlockEntityBase.PipeConnection down : PipeBlockEntityBase.PipeConnection.visual()) {
            for (PipeBlockEntityBase.PipeConnection up : PipeBlockEntityBase.PipeConnection.visual()) {
                for (PipeBlockEntityBase.PipeConnection north : PipeBlockEntityBase.PipeConnection.visual()) {
                    for (PipeBlockEntityBase.PipeConnection south : PipeBlockEntityBase.PipeConnection.visual()) {
                        for (PipeBlockEntityBase.PipeConnection west : PipeBlockEntityBase.PipeConnection.visual()) {
                            for (PipeBlockEntityBase.PipeConnection east : PipeBlockEntityBase.PipeConnection.visual()) {
                                VoxelShape shape = center;
                                if (down == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(center, PIPE_DOWN_AABB, BooleanOp.OR);
                                } else if (down == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(center, END_DOWN_AABB, BooleanOp.OR);
                                }
                                if (up == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(shape, PIPE_UP_AABB, BooleanOp.OR);
                                } else if (up == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(shape, END_UP_AABB, BooleanOp.OR);
                                }
                                if (north == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(shape, PIPE_NORTH_AABB, BooleanOp.OR);
                                } else if (north == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(shape, END_NORTH_AABB, BooleanOp.OR);
                                }
                                if (south == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(shape, PIPE_SOUTH_AABB, BooleanOp.OR);
                                } else if (south == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(shape, END_SOUTH_AABB, BooleanOp.OR);
                                }
                                if (west == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(shape, PIPE_WEST_AABB, BooleanOp.OR);
                                } else if (west == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(shape, END_WEST_AABB, BooleanOp.OR);
                                }
                                if (east == PipeBlockEntityBase.PipeConnection.PIPE) {
                                    shape = Shapes.joinUnoptimized(shape, PIPE_EAST_AABB, BooleanOp.OR);
                                } else if (east == PipeBlockEntityBase.PipeConnection.END) {
                                    shape = Shapes.joinUnoptimized(shape, END_EAST_AABB, BooleanOp.OR);
                                }
                                shapes[getShapeIndex(down, up, north, south, west, east)] = shape.optimize();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return level.getBlockEntity(pos) instanceof PipeBlockEntityBase pipe ? SHAPES[getShapeIndex(pipe.connections[0], pipe.connections[1], pipe.connections[2], pipe.connections[3], pipe.connections[4], pipe.connections[5])] : CENTER_AABB;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        if (pLevel.m_7702_(pCurrentPos) instanceof PipeBlockEntityBase pipe) {
            BlockEntity facingBE = pLevel.m_7702_(pFacingPos);
            if (!(facingBE instanceof PipeBlockEntityBase) || ((PipeBlockEntityBase) facingBE).getConnection(pFacing.getOpposite()) != PipeBlockEntityBase.PipeConnection.DISABLED) {
                boolean enabled = pipe.getConnection(pFacing) != PipeBlockEntityBase.PipeConnection.DISABLED;
                if (pFacingState.m_204336_(this.getConnectionTag()) && enabled) {
                    if (facingBE instanceof PipeBlockEntityBase && ((PipeBlockEntityBase) facingBE).getConnection(pFacing.getOpposite()) == PipeBlockEntityBase.PipeConnection.DISABLED) {
                        pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.DISABLED);
                    } else {
                        pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.PIPE);
                    }
                } else {
                    BlockEntity blockEntity = pLevel.m_7702_(pFacingPos);
                    if (this.connected(pFacing, pFacingState)) {
                        pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.LEVER);
                    } else if (this.connectToTile(blockEntity, pFacing) && enabled) {
                        if (pFacingState.m_60734_() instanceof IPipeConnection) {
                            pipe.setConnection(pFacing, ((IPipeConnection) pFacingState.m_60734_()).getPipeConnection(pFacingState, pFacing.getOpposite()));
                        } else {
                            pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.END);
                        }
                    } else if (enabled) {
                        pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.NONE);
                    }
                }
            }
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public static boolean facingConnected(Direction facing, BlockState state, DirectionProperty property) {
        return !state.m_61138_(property) || state.m_61143_(property) == facing;
    }

    public abstract boolean connected(Direction var1, BlockState var2);

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.m_60713_(pNewState.m_60734_())) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity != null) {
                IItemHandler handler = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
                if (handler != null) {
                    Misc.spawnInventoryInWorld(pLevel, (double) pPos.m_123341_() + 0.5, (double) pPos.m_123342_() + 0.5, (double) pPos.m_123343_() + 0.5, handler);
                    pLevel.updateNeighbourForOutputSignal(pPos, this);
                }
            }
            super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }

    static {
        makeShapes(CENTER_AABB, SHAPES);
    }
}