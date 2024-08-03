package dev.xkmc.l2backpack.content.common;

import dev.xkmc.l2backpack.content.capability.MergedInvBackpackCap;
import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupModeCap;
import dev.xkmc.l2backpack.content.remote.common.StorageContainer;
import dev.xkmc.l2backpack.content.remote.common.WorldStorage;
import dev.xkmc.l2backpack.content.restore.DimensionSourceData;
import dev.xkmc.l2screentracker.screen.source.PlayerSlot;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BaseBagInvWrapper extends MergedInvBackpackCap implements ICapabilityProvider {

    private final ItemStack stack;

    private final BaseBagItem bag;

    private final LazyOptional<BaseBagInvWrapper> holder = LazyOptional.of(() -> this);

    private ListTag cachedTag;

    private List<ItemStack> itemStacksCache;

    private BaseBagInvWrapper.CallbackData callbackData = null;

    public BaseBagInvWrapper(ItemStack stack) {
        this.stack = stack;
        this.bag = (BaseBagItem) stack.getItem();
    }

    @Override
    public int getSlots() {
        return this.bag.getRows(this.stack) * 9;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return (ItemStack) this.getItemList().get(slot);
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
            List<ItemStack> itemStacks = this.getItemList();
            ItemStack existing = (ItemStack) itemStacks.get(slot);
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
        List<ItemStack> itemStacks = this.getItemList();
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = (ItemStack) itemStacks.get(slot);
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
    public boolean isItemValid(int slot, ItemStack stack) {
        return stack.isEmpty() || this.bag.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.validateSlotIndex(slot);
        if (!this.isItemValid(slot, stack)) {
            throw new RuntimeException("Invalid stack " + stack + " for slot " + slot + ")");
        } else {
            List<ItemStack> itemStacks = this.getItemList();
            itemStacks.set(slot, stack);
            this.setItemList(itemStacks);
        }
    }

    private List<ItemStack> getItemList() {
        ListTag rootTag = BaseBagItem.getListTag(this.stack);
        if (this.itemStacksCache == null || this.cachedTag == null || !this.cachedTag.equals(rootTag)) {
            this.itemStacksCache = this.refreshItemList(rootTag);
        }
        return this.itemStacksCache;
    }

    private List<ItemStack> refreshItemList(ListTag rootTag) {
        List<ItemStack> list = BaseBagItem.getItems(this.stack);
        int size = this.getSlots();
        while (list.size() < size) {
            list.add(ItemStack.EMPTY);
        }
        this.cachedTag = rootTag;
        return list;
    }

    private void setItemList(List<ItemStack> itemStacks) {
        BaseBagItem.setItems(this.stack, itemStacks);
        this.cachedTag = BaseBagItem.getListTag(this.stack);
        this.itemStacksCache = null;
        this.saveCallback();
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.holder.cast();
        } else {
            return cap == PickupModeCap.TOKEN ? this.holder.cast() : LazyOptional.empty();
        }
    }

    @Override
    public PickupConfig getPickupMode() {
        return PickupConfig.getPickupMode(this.stack);
    }

    @Override
    public int getSignature() {
        return this.stack.hashCode();
    }

    public void attachEnv(ServerPlayer player, PlayerSlot<?> hand) {
        if (hand.data() instanceof DimensionSourceData data) {
            this.callbackData = new BaseBagInvWrapper.CallbackData(this, player, data);
        }
    }

    private void saveCallback() {
        if (this.callbackData != null) {
            this.callbackData.setChanged();
        }
    }

    private static record CallbackData(BaseBagInvWrapper parent, ServerPlayer player, DimensionSourceData data) {

        private void setChanged() {
            Optional<StorageContainer> opt = WorldStorage.get(this.player.serverLevel()).getStorageWithoutPassword(this.data.uuid(), this.data.color());
            if (!opt.isEmpty()) {
                StorageContainer cont = (StorageContainer) opt.get();
                ItemStack slot = cont.container.getItem(this.data.slot());
                if (this.parent.stack != slot) {
                    this.parent.callbackData = null;
                } else {
                    cont.container.setChanged();
                }
            }
        }
    }
}