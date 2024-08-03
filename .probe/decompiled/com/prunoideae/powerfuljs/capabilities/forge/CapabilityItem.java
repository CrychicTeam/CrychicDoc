package com.prunoideae.powerfuljs.capabilities.forge;

import java.util.function.BiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class CapabilityItem {

    public CapabilityItem.BlockEntityBuilder blockEntity() {
        return new CapabilityItem.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IItemHandler> {

        private ToIntFunction<BlockEntity> getSlots;

        private BiFunction<BlockEntity, Integer, ItemStack> getStackInSlot;

        private CapabilityItem.InsertItem insertItem;

        private CapabilityItem.ExtractItem extractItem;

        private ToIntBiFunction<BlockEntity, Integer> getSlotLimit;

        private CapabilityItem.IsItemValid isItemValid;

        public CapabilityItem.BlockEntityBuilder getSlots(ToIntFunction<BlockEntity> getSlots) {
            this.getSlots = getSlots;
            return this;
        }

        public CapabilityItem.BlockEntityBuilder getStackInSlot(BiFunction<BlockEntity, Integer, ItemStack> getStackInSlot) {
            this.getStackInSlot = getStackInSlot;
            return this;
        }

        public CapabilityItem.BlockEntityBuilder insertItem(CapabilityItem.InsertItem insertItem) {
            this.insertItem = insertItem;
            return this;
        }

        public CapabilityItem.BlockEntityBuilder extractItem(CapabilityItem.ExtractItem extractItem) {
            this.extractItem = extractItem;
            return this;
        }

        public CapabilityItem.BlockEntityBuilder getSlotLimit(ToIntBiFunction<BlockEntity, Integer> getSlotLimit) {
            this.getSlotLimit = getSlotLimit;
            return this;
        }

        public CapabilityItem.BlockEntityBuilder isItemValid(CapabilityItem.IsItemValid isItemValid) {
            this.isItemValid = isItemValid;
            return this;
        }

        public IItemHandler getCapability(BlockEntity instance) {
            return new IItemHandler() {

                @Override
                public int getSlots() {
                    return BlockEntityBuilder.this.getSlots != null ? BlockEntityBuilder.this.getSlots.applyAsInt(instance) : 0;
                }

                @NotNull
                @Override
                public ItemStack getStackInSlot(int i) {
                    return BlockEntityBuilder.this.getStackInSlot == null ? ItemStack.EMPTY : (ItemStack) BlockEntityBuilder.this.getStackInSlot.apply(instance, i);
                }

                @NotNull
                @Override
                public ItemStack insertItem(int i, @NotNull ItemStack arg, boolean bl) {
                    return BlockEntityBuilder.this.insertItem == null ? ItemStack.EMPTY : BlockEntityBuilder.this.insertItem.insert(instance, i, arg, bl);
                }

                @NotNull
                @Override
                public ItemStack extractItem(int i, int j, boolean bl) {
                    return BlockEntityBuilder.this.extractItem == null ? ItemStack.EMPTY : BlockEntityBuilder.this.extractItem.extract(instance, i, j, bl);
                }

                @Override
                public int getSlotLimit(int i) {
                    return BlockEntityBuilder.this.getSlotLimit == null ? 64 : BlockEntityBuilder.this.getSlotLimit.applyAsInt(instance, i);
                }

                @Override
                public boolean isItemValid(int i, @NotNull ItemStack arg) {
                    return BlockEntityBuilder.this.isItemValid == null || BlockEntityBuilder.this.isItemValid.isValid(instance, i, arg);
                }
            };
        }

        public Capability<IItemHandler> getCapabilityKey() {
            return ForgeCapabilities.ITEM_HANDLER;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:item_be");
        }
    }

    @FunctionalInterface
    public interface ExtractItem {

        ItemStack extract(BlockEntity var1, int var2, int var3, boolean var4);
    }

    @FunctionalInterface
    public interface InsertItem {

        ItemStack insert(BlockEntity var1, int var2, ItemStack var3, boolean var4);
    }

    @FunctionalInterface
    public interface IsItemValid {

        boolean isValid(BlockEntity var1, int var2, ItemStack var3);
    }
}