package net.minecraftforge.items;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropperBlock;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaInventoryCodeHooks {

    @Nullable
    public static Boolean extractHook(Level level, Hopper dest) {
        return (Boolean) getItemHandler(level, dest, Direction.UP).map(itemHandlerResult -> {
            IItemHandler handler = (IItemHandler) itemHandlerResult.getKey();
            for (int i = 0; i < handler.getSlots(); i++) {
                ItemStack extractItem = handler.extractItem(i, 1, true);
                if (!extractItem.isEmpty()) {
                    for (int j = 0; j < dest.m_6643_(); j++) {
                        ItemStack destStack = dest.m_8020_(j);
                        if (dest.m_7013_(j, extractItem) && (destStack.isEmpty() || destStack.getCount() < destStack.getMaxStackSize() && destStack.getCount() < dest.m_6893_() && ItemHandlerHelper.canItemStacksStack(extractItem, destStack))) {
                            extractItem = handler.extractItem(i, 1, false);
                            if (destStack.isEmpty()) {
                                dest.m_6836_(j, extractItem);
                            } else {
                                destStack.grow(1);
                                dest.m_6836_(j, destStack);
                            }
                            dest.m_6596_();
                            return true;
                        }
                    }
                }
            }
            return false;
        }).orElse(null);
    }

    public static boolean dropperInsertHook(Level level, BlockPos pos, DispenserBlockEntity dropper, int slot, @NotNull ItemStack stack) {
        Direction enumfacing = (Direction) level.getBlockState(pos).m_61143_(DropperBlock.f_52659_);
        BlockPos blockpos = pos.relative(enumfacing);
        return (Boolean) getItemHandler(level, (double) blockpos.m_123341_(), (double) blockpos.m_123342_(), (double) blockpos.m_123343_(), enumfacing.getOpposite()).map(destinationResult -> {
            IItemHandler itemHandler = (IItemHandler) destinationResult.getKey();
            Object destination = destinationResult.getValue();
            ItemStack dispensedStack = stack.copy().split(1);
            ItemStack remainder = putStackInInventoryAllSlots(dropper, destination, itemHandler, dispensedStack);
            if (remainder.isEmpty()) {
                remainder = stack.copy();
                remainder.shrink(1);
            } else {
                remainder = stack.copy();
            }
            dropper.m_6836_(slot, remainder);
            return false;
        }).orElse(true);
    }

    public static boolean insertHook(HopperBlockEntity hopper) {
        Direction hopperFacing = (Direction) hopper.m_58900_().m_61143_(HopperBlock.FACING);
        return (Boolean) getItemHandler(hopper.m_58904_(), hopper, hopperFacing).map(destinationResult -> {
            IItemHandler itemHandler = (IItemHandler) destinationResult.getKey();
            Object destination = destinationResult.getValue();
            if (isFull(itemHandler)) {
                return false;
            } else {
                for (int i = 0; i < hopper.getContainerSize(); i++) {
                    if (!hopper.m_8020_(i).isEmpty()) {
                        ItemStack originalSlotContents = hopper.m_8020_(i).copy();
                        ItemStack insertStack = hopper.removeItem(i, 1);
                        ItemStack remainder = putStackInInventoryAllSlots(hopper, destination, itemHandler, insertStack);
                        if (remainder.isEmpty()) {
                            return true;
                        }
                        hopper.setItem(i, originalSlotContents);
                    }
                }
                return false;
            }
        }).orElse(false);
    }

    private static ItemStack putStackInInventoryAllSlots(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack) {
        for (int slot = 0; slot < destInventory.getSlots() && !stack.isEmpty(); slot++) {
            stack = insertStack(source, destination, destInventory, stack, slot);
        }
        return stack;
    }

    private static ItemStack insertStack(BlockEntity source, Object destination, IItemHandler destInventory, ItemStack stack, int slot) {
        ItemStack itemstack = destInventory.getStackInSlot(slot);
        if (destInventory.insertItem(slot, stack, true).isEmpty()) {
            boolean insertedItem = false;
            boolean inventoryWasEmpty = isEmpty(destInventory);
            if (itemstack.isEmpty()) {
                destInventory.insertItem(slot, stack, false);
                stack = ItemStack.EMPTY;
                insertedItem = true;
            } else if (ItemHandlerHelper.canItemStacksStack(itemstack, stack)) {
                int originalSize = stack.getCount();
                stack = destInventory.insertItem(slot, stack, false);
                insertedItem = originalSize < stack.getCount();
            }
            if (insertedItem && inventoryWasEmpty && destination instanceof HopperBlockEntity destinationHopper && !destinationHopper.isOnCustomCooldown()) {
                int k = 0;
                if (source instanceof HopperBlockEntity && destinationHopper.getLastUpdateTime() >= ((HopperBlockEntity) source).getLastUpdateTime()) {
                    k = 1;
                }
                destinationHopper.setCooldown(8 - k);
            }
        }
        return stack;
    }

    private static Optional<Pair<IItemHandler, Object>> getItemHandler(Level level, Hopper hopper, Direction hopperFacing) {
        double x = hopper.getLevelX() + (double) hopperFacing.getStepX();
        double y = hopper.getLevelY() + (double) hopperFacing.getStepY();
        double z = hopper.getLevelZ() + (double) hopperFacing.getStepZ();
        return getItemHandler(level, x, y, z, hopperFacing.getOpposite());
    }

    private static boolean isFull(IItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.isEmpty() || stackInSlot.getCount() < itemHandler.getSlotLimit(slot)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEmpty(IItemHandler itemHandler) {
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stackInSlot = itemHandler.getStackInSlot(slot);
            if (stackInSlot.getCount() > 0) {
                return false;
            }
        }
        return true;
    }

    public static Optional<Pair<IItemHandler, Object>> getItemHandler(Level worldIn, double x, double y, double z, Direction side) {
        int i = Mth.floor(x);
        int j = Mth.floor(y);
        int k = Mth.floor(z);
        BlockPos blockpos = new BlockPos(i, j, k);
        BlockState state = worldIn.getBlockState(blockpos);
        if (state.m_155947_()) {
            BlockEntity blockEntity = worldIn.getBlockEntity(blockpos);
            if (blockEntity != null) {
                return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).map(capability -> ImmutablePair.of(capability, blockEntity));
            }
        }
        return Optional.empty();
    }
}