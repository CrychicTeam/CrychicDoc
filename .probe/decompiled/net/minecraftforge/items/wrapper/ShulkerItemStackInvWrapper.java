package net.minecraftforge.items.wrapper;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ShulkerItemStackInvWrapper implements IItemHandlerModifiable, ICapabilityProvider {

    private final ItemStack stack;

    private final LazyOptional<IItemHandler> holder = LazyOptional.of(() -> this);

    private CompoundTag cachedTag;

    private NonNullList<ItemStack> itemStacksCache;

    @Internal
    @Nullable
    public static ICapabilityProvider createDefaultProvider(ItemStack itemStack) {
        Item item = itemStack.getItem();
        return item != Items.SHULKER_BOX && item != Items.BLACK_SHULKER_BOX && item != Items.BLUE_SHULKER_BOX && item != Items.BROWN_SHULKER_BOX && item != Items.CYAN_SHULKER_BOX && item != Items.GRAY_SHULKER_BOX && item != Items.GREEN_SHULKER_BOX && item != Items.LIGHT_BLUE_SHULKER_BOX && item != Items.LIGHT_GRAY_SHULKER_BOX && item != Items.LIME_SHULKER_BOX && item != Items.MAGENTA_SHULKER_BOX && item != Items.ORANGE_SHULKER_BOX && item != Items.PINK_SHULKER_BOX && item != Items.PURPLE_SHULKER_BOX && item != Items.RED_SHULKER_BOX && item != Items.WHITE_SHULKER_BOX && item != Items.YELLOW_SHULKER_BOX ? null : new ShulkerItemStackInvWrapper(itemStack);
    }

    private ShulkerItemStackInvWrapper(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.getItemList().get(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isItemValid(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            NonNullList<ItemStack> itemStacks = this.getItemList();
            ItemStack existing = itemStacks.get(slot);
            int limit = Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }
                limit -= existing.getCount();
            }
            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        itemStacks.set(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                    } else {
                        existing.grow(reachedLimit ? limit : stack.getCount());
                    }
                    this.setItemList(itemStacks);
                }
                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        NonNullList<ItemStack> itemStacks = this.getItemList();
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = itemStacks.get(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        itemStacks.set(slot, ItemStack.EMPTY);
                        this.setItemList(itemStacks);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        itemStacks.set(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                        this.setItemList(itemStacks);
                    }
                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        }
    }

    private void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.getSlots()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.getSlots() + ")");
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getItem().canFitInsideContainerItems();
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.validateSlotIndex(slot);
        if (!this.isItemValid(slot, stack)) {
            throw new RuntimeException("Invalid stack " + stack + " for slot " + slot + ")");
        } else {
            NonNullList<ItemStack> itemStacks = this.getItemList();
            itemStacks.set(slot, stack);
            this.setItemList(itemStacks);
        }
    }

    private NonNullList<ItemStack> getItemList() {
        CompoundTag rootTag = BlockItem.getBlockEntityData(this.stack);
        if (this.cachedTag == null || !this.cachedTag.equals(rootTag)) {
            this.itemStacksCache = this.refreshItemList(rootTag);
        }
        return this.itemStacksCache;
    }

    private NonNullList<ItemStack> refreshItemList(CompoundTag rootTag) {
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(this.getSlots(), ItemStack.EMPTY);
        if (rootTag != null && rootTag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(rootTag, itemStacks);
        }
        this.cachedTag = rootTag;
        return itemStacks;
    }

    private void setItemList(NonNullList<ItemStack> itemStacks) {
        CompoundTag existing = BlockItem.getBlockEntityData(this.stack);
        CompoundTag rootTag = ContainerHelper.saveAllItems(existing == null ? new CompoundTag() : existing, itemStacks);
        BlockItem.setBlockEntityData(this.stack, BlockEntityType.SHULKER_BOX, rootTag);
        this.cachedTag = rootTag;
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.holder);
    }
}