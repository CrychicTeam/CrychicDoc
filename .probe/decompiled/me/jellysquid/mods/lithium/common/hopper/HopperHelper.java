package me.jellysquid.mods.lithium.common.hopper;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.Hopper;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HopperHelper {

    private static final VoxelShape CACHED_INPUT_VOLUME = Hopper.SUCK;

    private static final AABB[] CACHED_INPUT_VOLUME_BOXES = (AABB[]) CACHED_INPUT_VOLUME.toAabbs().toArray(new AABB[0]);

    public static AABB[] getHopperPickupVolumeBoxes(Hopper hopper) {
        VoxelShape inputAreaShape = hopper.getSuckShape();
        return inputAreaShape == CACHED_INPUT_VOLUME ? CACHED_INPUT_VOLUME_BOXES : (AABB[]) inputAreaShape.toAabbs().toArray(new AABB[0]);
    }

    @Nullable
    public static Container vanillaGetBlockInventory(Level world, BlockPos blockPos) {
        Container inventory = null;
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.m_60734_();
        if (block instanceof WorldlyContainerHolder) {
            inventory = ((WorldlyContainerHolder) block).getContainer(blockState, world, blockPos);
        } else if (blockState.m_155947_()) {
            BlockEntity blockEntity = world.getBlockEntity(blockPos);
            if (blockEntity instanceof Container) {
                inventory = (Container) blockEntity;
                if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
                    inventory = ChestBlock.getContainer((ChestBlock) block, blockState, world, blockPos, true);
                }
            }
        }
        return inventory;
    }

    public static boolean tryMoveSingleItem(Container to, ItemStack stack, @Nullable Direction fromDirection) {
        WorldlyContainer toSided = to instanceof WorldlyContainer ? (WorldlyContainer) to : null;
        if (toSided != null && fromDirection != null) {
            int[] slots = toSided.getSlotsForFace(fromDirection);
            for (int slotIndex = 0; slotIndex < slots.length; slotIndex++) {
                if (tryMoveSingleItem(to, toSided, stack, slots[slotIndex], fromDirection)) {
                    return true;
                }
            }
        } else {
            int j = to.getContainerSize();
            for (int slot = 0; slot < j; slot++) {
                if (tryMoveSingleItem(to, toSided, stack, slot, fromDirection)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean tryMoveSingleItem(Container to, @Nullable WorldlyContainer toSided, ItemStack transferStack, int targetSlot, @Nullable Direction fromDirection) {
        ItemStack toStack = to.getItem(targetSlot);
        if (to.canPlaceItem(targetSlot, transferStack) && (toSided == null || toSided.canPlaceItemThroughFace(targetSlot, transferStack, fromDirection))) {
            if (toStack.isEmpty()) {
                ItemStack singleItem = transferStack.split(1);
                to.setItem(targetSlot, singleItem);
                return true;
            }
            int toCount;
            if (toStack.is(transferStack.getItem()) && toStack.getMaxStackSize() > (toCount = toStack.getCount()) && to.getMaxStackSize() > toCount && areNbtEqual(toStack, transferStack)) {
                transferStack.shrink(1);
                toStack.grow(1);
                return true;
            }
        }
        return false;
    }

    private static boolean areNbtEqual(ItemStack stack1, ItemStack stack2) {
        return Objects.equals(stack1.getTag(), stack2.getTag());
    }

    private static int calculateReducedSignalStrength(float contentWeight, int inventorySize, int inventoryMaxCountPerStack, int numOccupiedSlots, int itemStackCount, int itemStackMaxCount) {
        int maxStackSize = Math.min(inventoryMaxCountPerStack, itemStackMaxCount);
        int newNumOccupiedSlots = numOccupiedSlots - (itemStackCount == 1 ? 1 : 0);
        float newContentWeight = contentWeight - 1.0F / (float) maxStackSize;
        newContentWeight /= (float) inventorySize;
        return Mth.floor(newContentWeight * 14.0F) + (newNumOccupiedSlots > 0 ? 1 : 0);
    }

    public static ComparatorUpdatePattern determineComparatorUpdatePattern(Container from, LithiumStackList fromStackList) {
        if (!(from instanceof HopperBlockEntity) && from instanceof RandomizableContainerBlockEntity) {
            float contentWeight = 0.0F;
            int numOccupiedSlots = 0;
            for (int j = 0; j < from.getContainerSize(); j++) {
                ItemStack itemStack = from.getItem(j);
                if (!itemStack.isEmpty()) {
                    int maxStackSize = Math.min(from.getMaxStackSize(), itemStack.getMaxStackSize());
                    contentWeight += (float) itemStack.getCount() / (float) maxStackSize;
                    numOccupiedSlots++;
                }
            }
            float var14 = contentWeight / (float) from.getContainerSize();
            int originalSignalStrength = Mth.floor(var14 * 14.0F) + (numOccupiedSlots > 0 ? 1 : 0);
            ComparatorUpdatePattern updatePattern = ComparatorUpdatePattern.NO_UPDATE;
            int[] availableSlots = from instanceof WorldlyContainer ? ((WorldlyContainer) from).getSlotsForFace(Direction.DOWN) : null;
            WorldlyContainer sidedInventory = from instanceof WorldlyContainer ? (WorldlyContainer) from : null;
            int fromSize = availableSlots != null ? availableSlots.length : from.getContainerSize();
            for (int i = 0; i < fromSize; i++) {
                int fromSlot = availableSlots != null ? availableSlots[i] : i;
                ItemStack itemStack = fromStackList.get(fromSlot);
                if (!itemStack.isEmpty() && (sidedInventory == null || sidedInventory.canTakeItemThroughFace(fromSlot, itemStack, Direction.DOWN))) {
                    int newSignalStrength = calculateReducedSignalStrength(contentWeight, from.getContainerSize(), from.getMaxStackSize(), numOccupiedSlots, itemStack.getCount(), itemStack.getMaxStackSize());
                    if (newSignalStrength != originalSignalStrength) {
                        updatePattern = updatePattern.thenDecrementUpdateIncrementUpdate();
                    } else {
                        updatePattern = updatePattern.thenUpdate();
                    }
                    if (!updatePattern.isChainable()) {
                        break;
                    }
                }
            }
            return updatePattern;
        } else {
            return ComparatorUpdatePattern.NO_UPDATE;
        }
    }

    public static Container replaceDoubleInventory(Container blockInventory) {
        if (blockInventory instanceof CompoundContainer doubleInventory) {
            CompoundContainer var2 = LithiumDoubleInventory.getLithiumInventory(doubleInventory);
            if (var2 != null) {
                return var2;
            }
        }
        return blockInventory;
    }
}