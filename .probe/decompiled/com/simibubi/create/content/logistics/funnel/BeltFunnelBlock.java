package com.simibubi.create.content.logistics.funnel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltSlope;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VoxelShaper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeltFunnelBlock extends AbstractHorizontalFunnelBlock implements ISpecialBlockItemRequirement {

    private BlockEntry<? extends FunnelBlock> parent;

    public static final EnumProperty<BeltFunnelBlock.Shape> SHAPE = EnumProperty.create("shape", BeltFunnelBlock.Shape.class);

    public BeltFunnelBlock(BlockEntry<? extends FunnelBlock> parent, BlockBehaviour.Properties p_i48377_1_) {
        super(p_i48377_1_);
        this.parent = parent;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(SHAPE, BeltFunnelBlock.Shape.RETRACTED));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_.add(SHAPE));
    }

    public boolean isOfSameType(FunnelBlock otherFunnel) {
        return this.parent.get() == otherFunnel;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.m_60734_() instanceof FunnelBlock fb && this.isOfSameType(fb)) {
            return;
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return ((BeltFunnelBlock.Shape) state.m_61143_(SHAPE)).shaper.get((Direction) state.m_61143_(HORIZONTAL_FACING));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return !(p_220071_4_ instanceof EntityCollisionContext) || !(((EntityCollisionContext) p_220071_4_).getEntity() instanceof ItemEntity) || p_220071_1_.m_61143_(SHAPE) != BeltFunnelBlock.Shape.PULLING && p_220071_1_.m_61143_(SHAPE) != BeltFunnelBlock.Shape.PUSHING ? this.getShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_) : AllShapes.FUNNEL_COLLISION.get(this.getFacing(p_220071_1_));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState stateForPlacement = super.m_5573_(ctx);
        BlockPos pos = ctx.getClickedPos();
        Level world = ctx.m_43725_();
        Direction facing = ctx.m_43719_().getAxis().isHorizontal() ? ctx.m_43719_() : ctx.m_8125_();
        BlockState state = (BlockState) stateForPlacement.m_61124_(HORIZONTAL_FACING, facing);
        boolean sneaking = ctx.m_43723_() != null && ctx.m_43723_().m_6144_();
        return (BlockState) state.m_61124_(SHAPE, getShapeForPosition(world, pos, facing, !sneaking));
    }

    public static BeltFunnelBlock.Shape getShapeForPosition(BlockGetter world, BlockPos pos, Direction facing, boolean extracting) {
        BlockPos posBelow = pos.below();
        BlockState stateBelow = world.getBlockState(posBelow);
        BeltFunnelBlock.Shape perpendicularState = extracting ? BeltFunnelBlock.Shape.PUSHING : BeltFunnelBlock.Shape.PULLING;
        if (!AllBlocks.BELT.has(stateBelow)) {
            return perpendicularState;
        } else {
            Direction movementFacing = (Direction) stateBelow.m_61143_(BeltBlock.HORIZONTAL_FACING);
            return movementFacing.getAxis() != facing.getAxis() ? perpendicularState : BeltFunnelBlock.Shape.RETRACTED;
        }
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return this.parent.asStack();
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        this.updateWater(world, state, pos);
        if (!isOnValidBelt(state, world, pos)) {
            BlockState parentState = ProperWaterloggedBlock.withWater(world, this.parent.getDefaultState(), pos);
            if ((Boolean) state.m_61145_(POWERED).orElse(false)) {
                parentState = (BlockState) parentState.m_61124_(POWERED, true);
            }
            if (state.m_61143_(SHAPE) == BeltFunnelBlock.Shape.PUSHING) {
                parentState = (BlockState) parentState.m_61124_(FunnelBlock.EXTRACTING, true);
            }
            return (BlockState) parentState.m_61124_(FunnelBlock.FACING, (Direction) state.m_61143_(HORIZONTAL_FACING));
        } else {
            BeltFunnelBlock.Shape updatedShape = getShapeForPosition(world, pos, (Direction) state.m_61143_(HORIZONTAL_FACING), state.m_61143_(SHAPE) == BeltFunnelBlock.Shape.PUSHING);
            BeltFunnelBlock.Shape currentShape = (BeltFunnelBlock.Shape) state.m_61143_(SHAPE);
            if (updatedShape == currentShape) {
                return state;
            } else if (updatedShape == BeltFunnelBlock.Shape.PUSHING && currentShape == BeltFunnelBlock.Shape.PULLING) {
                return state;
            } else {
                return updatedShape == BeltFunnelBlock.Shape.RETRACTED && currentShape == BeltFunnelBlock.Shape.EXTENDED ? state : (BlockState) state.m_61124_(SHAPE, updatedShape);
            }
        }
    }

    public static boolean isOnValidBelt(BlockState state, LevelReader world, BlockPos pos) {
        BlockState stateBelow = world.m_8055_(pos.below());
        if (stateBelow.m_60734_() instanceof BeltBlock) {
            return BeltBlock.canTransportObjects(stateBelow);
        } else {
            DirectBeltInputBehaviour directBeltInputBehaviour = BlockEntityBehaviour.get(world, pos.below(), DirectBeltInputBehaviour.TYPE);
            return directBeltInputBehaviour == null ? false : directBeltInputBehaviour.canSupportBeltFunnels();
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BeltFunnelBlock.Shape shape = (BeltFunnelBlock.Shape) state.m_61143_(SHAPE);
            BeltFunnelBlock.Shape newShape = shape;
            if (shape == BeltFunnelBlock.Shape.PULLING) {
                newShape = BeltFunnelBlock.Shape.PUSHING;
            } else if (shape == BeltFunnelBlock.Shape.PUSHING) {
                newShape = BeltFunnelBlock.Shape.PULLING;
            } else if (shape == BeltFunnelBlock.Shape.EXTENDED) {
                newShape = BeltFunnelBlock.Shape.RETRACTED;
            } else if (shape == BeltFunnelBlock.Shape.RETRACTED) {
                BlockState belt = world.getBlockState(context.getClickedPos().below());
                if (belt.m_60734_() instanceof BeltBlock && belt.m_61143_(BeltBlock.SLOPE) != BeltSlope.HORIZONTAL) {
                    newShape = BeltFunnelBlock.Shape.RETRACTED;
                } else {
                    newShape = BeltFunnelBlock.Shape.EXTENDED;
                }
            }
            if (newShape == shape) {
                return InteractionResult.SUCCESS;
            } else {
                world.setBlockAndUpdate(context.getClickedPos(), (BlockState) state.m_61124_(SHAPE, newShape));
                if (newShape == BeltFunnelBlock.Shape.EXTENDED) {
                    Direction facing = (Direction) state.m_61143_(HORIZONTAL_FACING);
                    BlockState opposite = world.getBlockState(context.getClickedPos().relative(facing));
                    if (opposite.m_60734_() instanceof BeltFunnelBlock && opposite.m_61143_(SHAPE) == BeltFunnelBlock.Shape.EXTENDED && opposite.m_61143_(HORIZONTAL_FACING) == facing.getOpposite()) {
                        AllAdvancements.FUNNEL_KISS.awardTo(context.getPlayer());
                    }
                }
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        return ItemRequirement.of(this.parent.getDefaultState(), be);
    }

    public static enum Shape implements StringRepresentable {

        RETRACTED(AllShapes.BELT_FUNNEL_RETRACTED), EXTENDED(AllShapes.BELT_FUNNEL_EXTENDED), PUSHING(AllShapes.BELT_FUNNEL_PERPENDICULAR), PULLING(AllShapes.BELT_FUNNEL_PERPENDICULAR);

        VoxelShaper shaper;

        private Shape(VoxelShaper shaper) {
            this.shaper = shaper;
        }

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}