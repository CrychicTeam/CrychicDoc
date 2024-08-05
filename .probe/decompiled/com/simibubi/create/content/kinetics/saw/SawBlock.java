package com.simibubi.create.content.kinetics.saw;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.damageTypes.CreateDamageSources;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SawBlock extends DirectionalAxisKineticBlock implements IBE<SawBlockEntity> {

    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");

    private static final int placementHelperId = PlacementHelpers.register(new SawBlock.PlacementHelper());

    public SawBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FLIPPED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        Direction facing = (Direction) stateForPlacement.m_61143_(FACING);
        return (BlockState) stateForPlacement.m_61124_(FLIPPED, facing.getAxis() == Direction.Axis.Y && context.m_8125_().getAxisDirection() == Direction.AxisDirection.POSITIVE);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        BlockState newState = super.getRotatedBlockState(originalState, targetedFace);
        if (((Direction) newState.m_61143_(FACING)).getAxis() != Direction.Axis.Y) {
            return newState;
        } else if (targetedFace.getAxis() != Direction.Axis.Y) {
            return newState;
        } else {
            if (!(Boolean) originalState.m_61143_(AXIS_ALONG_FIRST_COORDINATE)) {
                newState = (BlockState) newState.m_61122_(FLIPPED);
            }
            return newState;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        BlockState newState = super.rotate(state, rot);
        if (((Direction) state.m_61143_(FACING)).getAxis() != Direction.Axis.Y) {
            return newState;
        } else {
            if (rot.ordinal() % 2 == 1 && rot == Rotation.CLOCKWISE_90 != (Boolean) state.m_61143_(AXIS_ALONG_FIRST_COORDINATE)) {
                newState = (BlockState) newState.m_61122_(FLIPPED);
            }
            if (rot == Rotation.CLOCKWISE_180) {
                newState = (BlockState) newState.m_61122_(FLIPPED);
            }
            return newState;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        BlockState newState = super.m_6943_(state, mirrorIn);
        if (((Direction) state.m_61143_(FACING)).getAxis() != Direction.Axis.Y) {
            return newState;
        } else {
            boolean alongX = (Boolean) state.m_61143_(AXIS_ALONG_FIRST_COORDINATE);
            if (alongX && mirrorIn == Mirror.FRONT_BACK) {
                newState = (BlockState) newState.m_61122_(FLIPPED);
            }
            if (!alongX && mirrorIn == Mirror.LEFT_RIGHT) {
                newState = (BlockState) newState.m_61122_(FLIPPED);
            }
            return newState;
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.CASING_12PX.get((Direction) state.m_61143_(FACING));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.m_21120_(handIn);
        IPlacementHelper placementHelper = PlacementHelpers.get(placementHelperId);
        if (!player.m_6144_() && player.mayBuild() && placementHelper.matchesItem(heldItem) && placementHelper.getOffset(player, worldIn, state, pos, hit).placeInWorld(worldIn, (BlockItem) heldItem.getItem(), player, handIn, hit).consumesAction()) {
            return InteractionResult.SUCCESS;
        } else if (!player.isSpectator() && player.m_21120_(handIn).isEmpty()) {
            return state.m_61145_(FACING).orElse(Direction.WEST) != Direction.UP ? InteractionResult.PASS : this.onBlockEntityUse(worldIn, pos, be -> {
                for (int i = 0; i < be.inventory.getSlots(); i++) {
                    ItemStack heldItemStack = be.inventory.getStackInSlot(i);
                    if (!worldIn.isClientSide && !heldItemStack.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(heldItemStack);
                    }
                }
                be.inventory.clear();
                be.notifyUpdate();
                return InteractionResult.SUCCESS;
            });
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!(entityIn instanceof ItemEntity)) {
            if (new AABB(pos).deflate(0.1F).intersects(entityIn.getBoundingBox())) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    if (be.getSpeed() != 0.0F) {
                        entityIn.hurt(CreateDamageSources.saw(worldIn), (float) DrillBlock.getDamage(be.getSpeed()));
                    }
                });
            }
        }
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.m_5548_(worldIn, entityIn);
        if (entityIn instanceof ItemEntity) {
            if (!entityIn.level().isClientSide) {
                BlockPos pos = entityIn.blockPosition();
                this.withBlockEntityDo(entityIn.level(), pos, be -> {
                    if (be.getSpeed() != 0.0F) {
                        be.insertItem((ItemEntity) entityIn);
                    }
                });
            }
        }
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    public static boolean isHorizontal(BlockState state) {
        return ((Direction) state.m_61143_(FACING)).getAxis().isHorizontal();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return isHorizontal(state) ? ((Direction) state.m_61143_(FACING)).getAxis() : super.getRotationAxis(state);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return isHorizontal(state) ? face == ((Direction) state.m_61143_(FACING)).getOpposite() : super.hasShaftTowards(world, pos, state, face);
    }

    @Override
    public Class<SawBlockEntity> getBlockEntityClass() {
        return SawBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends SawBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends SawBlockEntity>) AllBlockEntityTypes.SAW.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @MethodsReturnNonnullByDefault
    private static class PlacementHelper implements IPlacementHelper {

        @Override
        public Predicate<ItemStack> getItemPredicate() {
            return AllBlocks.MECHANICAL_SAW::isIn;
        }

        @Override
        public Predicate<BlockState> getStatePredicate() {
            return state -> AllBlocks.MECHANICAL_SAW.has(state);
        }

        @Override
        public PlacementOffset getOffset(Player player, Level world, BlockState state, BlockPos pos, BlockHitResult ray) {
            List<Direction> directions = IPlacementHelper.orderedByDistanceExceptAxis(pos, ray.m_82450_(), ((Direction) state.m_61143_(DirectionalKineticBlock.FACING)).getAxis(), dir -> world.getBlockState(pos.relative(dir)).m_247087_());
            return directions.isEmpty() ? PlacementOffset.fail() : PlacementOffset.success(pos.relative((Direction) directions.get(0)), s -> (BlockState) ((BlockState) ((BlockState) s.m_61124_(DirectionalKineticBlock.FACING, (Direction) state.m_61143_(DirectionalKineticBlock.FACING))).m_61124_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE, (Boolean) state.m_61143_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE))).m_61124_(SawBlock.FLIPPED, (Boolean) state.m_61143_(SawBlock.FLIPPED)));
        }
    }
}