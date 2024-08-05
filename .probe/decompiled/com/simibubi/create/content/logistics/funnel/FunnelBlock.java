package com.simibubi.create.content.logistics.funnel;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FunnelBlock extends AbstractDirectionalFunnelBlock {

    public static final BooleanProperty EXTRACTING = BooleanProperty.create("extracting");

    public FunnelBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(EXTRACTING, false));
    }

    public abstract BlockState getEquivalentBeltFunnel(BlockGetter var1, BlockPos var2, BlockState var3);

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.m_5573_(context);
        boolean sneak = context.m_43723_() != null && context.m_43723_().m_6144_();
        state = (BlockState) state.m_61124_(EXTRACTING, !sneak);
        for (Direction direction : context.getNearestLookingDirections()) {
            BlockState blockstate = (BlockState) state.m_61124_(FACING, direction.getOpposite());
            if (blockstate.m_60710_(context.m_43725_(), context.getClickedPos())) {
                return (BlockState) blockstate.m_61124_(POWERED, (Boolean) state.m_61143_(POWERED));
            }
        }
        return state;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(EXTRACTING));
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.m_60734_() instanceof BeltFunnelBlock bfb && bfb.isOfSameType(this)) {
            return;
        }
        super.m_6810_(state, world, pos, newState, isMoving);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.m_6402_(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.m_21120_(handIn);
        boolean shouldntInsertItem = AllBlocks.MECHANICAL_ARM.isIn(heldItem) || !this.canInsertIntoFunnel(state);
        if (AllItems.WRENCH.isIn(heldItem)) {
            return InteractionResult.PASS;
        } else if (hit.getDirection() == getFunnelFacing(state) && !shouldntInsertItem) {
            if (!worldIn.isClientSide) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    ItemStack toInsert = heldItem.copy();
                    ItemStack remainder = tryInsert(worldIn, pos, toInsert, false);
                    if (!ItemStack.matches(remainder, toInsert)) {
                        player.m_21008_(handIn, remainder);
                    }
                });
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        if (!world.isClientSide) {
            world.setBlockAndUpdate(context.getClickedPos(), (BlockState) state.m_61122_(EXTRACTING));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isClientSide) {
            if (entityIn instanceof ItemEntity) {
                if (this.canInsertIntoFunnel(state)) {
                    if (entityIn.isAlive()) {
                        ItemEntity itemEntity = (ItemEntity) entityIn;
                        Direction direction = getFunnelFacing(state);
                        Vec3 diff = entityIn.position().subtract(VecHelper.getCenterOf(pos).add(Vec3.atLowerCornerOf(direction.getNormal()).scale(-0.325F)));
                        double projectedDiff = direction.getAxis().choose(diff.x, diff.y, diff.z);
                        if (projectedDiff < 0.0 != (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE)) {
                            ItemStack toInsert = itemEntity.getItem();
                            ItemStack remainder = tryInsert(worldIn, pos, toInsert, false);
                            if (remainder.isEmpty()) {
                                itemEntity.m_146870_();
                            }
                            if (remainder.getCount() < toInsert.getCount()) {
                                itemEntity.setItem(remainder);
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canInsertIntoFunnel(BlockState state) {
        return !(Boolean) state.m_61143_(POWERED) && !(Boolean) state.m_61143_(EXTRACTING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction facing = (Direction) state.m_61143_(FACING);
        return facing == Direction.DOWN ? AllShapes.FUNNEL_CEILING : (facing == Direction.UP ? AllShapes.FUNNEL_FLOOR : AllShapes.FUNNEL_WALL.get(facing));
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return context instanceof EntityCollisionContext && ((EntityCollisionContext) context).getEntity() instanceof ItemEntity && this.getFacing(state).getAxis().isHorizontal() ? AllShapes.FUNNEL_COLLISION.get(this.getFacing(state)) : this.getShape(state, world, pos, context);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState p_196271_3_, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        this.updateWater(world, state, pos);
        if (!this.getFacing(state).getAxis().isVertical() && direction == Direction.DOWN) {
            BlockState equivalentFunnel = ProperWaterloggedBlock.withWater(world, this.getEquivalentBeltFunnel(null, null, state), pos);
            return BeltFunnelBlock.isOnValidBelt(equivalentFunnel, world, pos) ? (BlockState) equivalentFunnel.m_61124_(BeltFunnelBlock.SHAPE, BeltFunnelBlock.getShapeForPosition(world, pos, this.getFacing(state), (Boolean) state.m_61143_(EXTRACTING))) : state;
        } else {
            return state;
        }
    }
}