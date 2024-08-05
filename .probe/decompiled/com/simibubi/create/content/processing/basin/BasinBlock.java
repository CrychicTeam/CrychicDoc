package com.simibubi.create.content.processing.basin;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.Create;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class BasinBlock extends Block implements IBE<BasinBlockEntity>, IWrenchable {

    public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

    public BasinBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FACING, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_.add(FACING));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        BlockEntity blockEntity = world.m_7702_(pos.above());
        return !(blockEntity instanceof BasinOperatingBlockEntity);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        if (!context.getLevel().isClientSide) {
            this.withBlockEntityDo(context.getLevel(), context.getClickedPos(), bte -> bte.onWrenched(context.getClickedFace()));
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.m_21120_(handIn);
        return this.onBlockEntityUse(worldIn, pos, be -> {
            if (!heldItem.isEmpty()) {
                if (FluidHelper.tryEmptyItemIntoBE(worldIn, player, handIn, heldItem, be)) {
                    return InteractionResult.SUCCESS;
                } else if (FluidHelper.tryFillItemFromBE(worldIn, player, handIn, heldItem, be)) {
                    return InteractionResult.SUCCESS;
                } else if (!GenericItemEmptying.canItemBeEmptied(worldIn, heldItem) && !GenericItemFilling.canItemBeFilled(worldIn, heldItem)) {
                    return heldItem.getItem().equals(Items.SPONGE) && !((FluidStack) be.getCapability(ForgeCapabilities.FLUID_HANDLER).map(iFluidHandler -> iFluidHandler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.EXECUTE)).orElse(FluidStack.EMPTY)).isEmpty() ? InteractionResult.SUCCESS : InteractionResult.PASS;
                } else {
                    return InteractionResult.SUCCESS;
                }
            } else {
                IItemHandlerModifiable inv = be.itemCapability.orElse(new ItemStackHandler(1));
                boolean success = false;
                for (int slot = 0; slot < inv.getSlots(); slot++) {
                    ItemStack stackInSlot = inv.getStackInSlot(slot);
                    if (!stackInSlot.isEmpty()) {
                        player.getInventory().placeItemBackInInventory(stackInSlot);
                        inv.setStackInSlot(slot, ItemStack.EMPTY);
                        success = true;
                    }
                }
                if (success) {
                    worldIn.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
                }
                be.onEmptied();
                return InteractionResult.SUCCESS;
            }
        });
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        if (AllBlocks.BASIN.has(worldIn.getBlockState(entityIn.blockPosition()))) {
            if (entityIn instanceof ItemEntity) {
                if (entityIn.isAlive()) {
                    ItemEntity itemEntity = (ItemEntity) entityIn;
                    this.withBlockEntityDo(worldIn, entityIn.blockPosition(), be -> {
                        be.inputInventory.withMaxStackSize(64);
                        ItemStack insertItem = ItemHandlerHelper.insertItem(be.inputInventory, itemEntity.getItem().copy(), false);
                        be.inputInventory.withMaxStackSize(16);
                        if (insertItem.isEmpty()) {
                            itemEntity.m_146870_();
                        } else {
                            itemEntity.setItem(insertItem);
                        }
                    });
                }
            }
        }
    }

    @Override
    public VoxelShape getInteractionShape(BlockState p_199600_1_, BlockGetter p_199600_2_, BlockPos p_199600_3_) {
        return AllShapes.BASIN_RAYTRACE_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.BASIN_BLOCK_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext ctx) {
        return ctx instanceof EntityCollisionContext && ((EntityCollisionContext) ctx).getEntity() instanceof ItemEntity ? AllShapes.BASIN_COLLISION_SHAPE : this.getShape(state, reader, pos, ctx);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, worldIn, pos, newState);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        return (Integer) this.getBlockEntityOptional(worldIn, pos).map(BasinBlockEntity::getInputInventory).map(ItemHelper::calcRedstoneFromInventory).orElse(0);
    }

    @Override
    public Class<BasinBlockEntity> getBlockEntityClass() {
        return BasinBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BasinBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BasinBlockEntity>) AllBlockEntityTypes.BASIN.get();
    }

    public static boolean canOutputTo(BlockGetter world, BlockPos basinPos, Direction direction) {
        BlockPos neighbour = basinPos.relative(direction);
        BlockPos output = neighbour.below();
        BlockState blockState = world.getBlockState(neighbour);
        if (FunnelBlock.isFunnel(blockState)) {
            if (FunnelBlock.getFunnelFacing(blockState) == direction) {
                return false;
            }
        } else {
            if (!blockState.m_60812_(world, neighbour).isEmpty()) {
                return false;
            }
            if (world.getBlockEntity(output) instanceof BeltBlockEntity belt) {
                return belt.getSpeed() == 0.0F || belt.getMovementFacing() != direction.getOpposite();
            }
        }
        DirectBeltInputBehaviour directBeltInputBehaviour = BlockEntityBehaviour.get(world, output, DirectBeltInputBehaviour.TYPE);
        return directBeltInputBehaviour != null ? directBeltInputBehaviour.canInsertFromSide(direction) : false;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}