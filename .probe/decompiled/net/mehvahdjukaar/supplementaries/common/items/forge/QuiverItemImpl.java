package net.mehvahdjukaar.supplementaries.common.items.forge;

import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.capabilities.CapabilityHandler;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QuiverItemImpl {

    public static ItemStack getQuiver(LivingEntity entity) {
        if (entity instanceof Player player) {
            ItemStack curioQuiver = CompatHandler.getQuiverFromModsSlots(player);
            if (!curioQuiver.isEmpty()) {
                return curioQuiver;
            }
            if ((Boolean) CommonConfigs.Tools.QUIVER_CURIO_ONLY.get()) {
                return ItemStack.EMPTY;
            }
        } else if (entity instanceof IQuiverEntity e) {
            return e.supplementaries$getQuiver();
        }
        IItemHandler cap = CapabilityHandler.get(entity, ForgeCapabilities.ITEM_HANDLER);
        if (cap != null) {
            for (int i = 0; i < cap.getSlots(); i++) {
                ItemStack quiver = cap.getStackInSlot(i);
                if (quiver.getItem() == ModRegistry.QUIVER_ITEM.get()) {
                    return quiver;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public static QuiverItem.Data getQuiverData(ItemStack stack) {
        return CapabilityHandler.get(stack, CapabilityHandler.QUIVER_ITEM_HANDLER);
    }

    public static class QuiverCapability extends ItemStackHandler implements ICapabilitySerializable<CompoundTag>, QuiverItem.Data {

        private final LazyOptional<IItemHandler> lazyOptional = LazyOptional.of(() -> this);

        private final LazyOptional<QuiverItemImpl.QuiverCapability> lazyOptional2 = LazyOptional.of(() -> this);

        private int selectedSlot = 0;

        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            LazyOptional<T> v = ForgeCapabilities.ITEM_HANDLER.orEmpty(cap, this.lazyOptional);
            return v.isPresent() ? v : CapabilityHandler.QUIVER_ITEM_HANDLER.orEmpty(cap, this.lazyOptional2);
        }

        @Override
        public CompoundTag serializeNBT() {
            CompoundTag c = super.serializeNBT();
            c.putInt("SelectedSlot", this.selectedSlot);
            return c;
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            super.deserializeNBT(nbt);
            this.selectedSlot = nbt.getByte("SelectedSlot");
        }

        public QuiverCapability() {
            super((Integer) CommonConfigs.Tools.QUIVER_SLOTS.get());
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return this.canAcceptItem(stack);
        }

        @Override
        public List<ItemStack> getContentView() {
            return this.stacks;
        }

        @Override
        public int getSelectedSlot() {
            return this.selectedSlot;
        }

        @Override
        public void setSelectedSlot(int selectedSlot) {
            if (!this.stacks.get(selectedSlot).isEmpty()) {
                this.selectedSlot = selectedSlot;
            }
        }

        @Override
        public boolean cycle(int slotsMoved) {
            int originalSlot = this.selectedSlot;
            int maxSlots = this.stacks.size();
            slotsMoved %= maxSlots;
            this.selectedSlot = (maxSlots + this.selectedSlot + slotsMoved) % maxSlots;
            for (int i = 0; i < maxSlots; i++) {
                ItemStack stack = this.getStackInSlot(this.selectedSlot);
                if (!stack.isEmpty()) {
                    break;
                }
                this.selectedSlot = (maxSlots + this.selectedSlot + (slotsMoved >= 0 ? 1 : -1)) % maxSlots;
            }
            return originalSlot != this.selectedSlot;
        }

        @Override
        public ItemStack tryAdding(ItemStack toInsert, boolean onlyOnExisting) {
            if (toInsert.isEmpty() || !toInsert.getItem().canFitInsideContainerItems()) {
                return toInsert;
            } else if (!onlyOnExisting) {
                return ItemHandlerHelper.insertItem(this, toInsert, false);
            } else {
                int finalCount = toInsert.getCount();
                for (int i = 0; i < this.getSlots() && finalCount > 0; i++) {
                    ItemStack s = this.getStackInSlot(i);
                    if (ItemStack.isSameItemSameTags(s, toInsert)) {
                        int newCount = Math.min(s.getMaxStackSize(), s.getCount() + finalCount);
                        int increment = newCount - s.getCount();
                        finalCount -= increment;
                        s.grow(increment);
                        this.onContentsChanged(i);
                    }
                }
                toInsert.setCount(finalCount);
                return toInsert;
            }
        }

        @Override
        public Optional<ItemStack> removeOneStack() {
            int i = 0;
            for (ItemStack s : this.getContentView()) {
                if (!s.isEmpty()) {
                    ItemStack extracted = this.extractItem(i, s.getCount(), false);
                    this.updateSelectedIfNeeded();
                    return Optional.of(extracted);
                }
                i++;
            }
            return Optional.empty();
        }

        @Override
        public void consumeArrow() {
            ItemStack s = this.getSelected();
            s.shrink(1);
            if (s.isEmpty()) {
                this.stacks.set(this.selectedSlot, ItemStack.EMPTY);
            }
            this.updateSelectedIfNeeded();
        }
    }
}