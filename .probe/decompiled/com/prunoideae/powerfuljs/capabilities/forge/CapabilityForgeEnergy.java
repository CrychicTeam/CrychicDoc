package com.prunoideae.powerfuljs.capabilities.forge;

import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.energy.IEnergyStorage;

public class CapabilityForgeEnergy {

    public CapabilityForgeEnergy.ItemStorageBuilder normalItemStack(int capacity, boolean canExtract, boolean canReceive) {
        return new CapabilityForgeEnergy.ItemStorageBuilder(capacity, canExtract, canReceive);
    }

    public CapabilityForgeEnergy.ItemStackBuilder customItemStack() {
        return new CapabilityForgeEnergy.ItemStackBuilder();
    }

    public CapabilityForgeEnergy.BlockEntityBuilder customBlockEntity() {
        return new CapabilityForgeEnergy.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IEnergyStorage> {

        private CapabilityForgeEnergy.EnergyIOBlockEntity receiveEnergy;

        private CapabilityForgeEnergy.EnergyIOBlockEntity extractEnergy;

        private ToIntFunction<BlockEntity> getEnergyStored;

        private ToIntFunction<BlockEntity> getMaxEnergyStored;

        private Predicate<BlockEntity> canExtract;

        private Predicate<BlockEntity> canReceive;

        public CapabilityForgeEnergy.BlockEntityBuilder receiveEnergy(CapabilityForgeEnergy.EnergyIOBlockEntity receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public CapabilityForgeEnergy.BlockEntityBuilder extractEnergy(CapabilityForgeEnergy.EnergyIOBlockEntity extractEnergy) {
            this.extractEnergy = extractEnergy;
            return this;
        }

        public CapabilityForgeEnergy.BlockEntityBuilder getEnergyStored(ToIntFunction<BlockEntity> getEnergyStored) {
            this.getEnergyStored = getEnergyStored;
            return this;
        }

        public CapabilityForgeEnergy.BlockEntityBuilder withCapacity(int capacity) {
            return this.getMaxEnergyStored(be -> capacity);
        }

        public CapabilityForgeEnergy.BlockEntityBuilder getMaxEnergyStored(ToIntFunction<BlockEntity> getMaxEnergyStored) {
            this.getMaxEnergyStored = getMaxEnergyStored;
            return this;
        }

        public CapabilityForgeEnergy.BlockEntityBuilder canExtract(Predicate<BlockEntity> canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public CapabilityForgeEnergy.BlockEntityBuilder canReceive(Predicate<BlockEntity> canReceive) {
            this.canReceive = canReceive;
            return this;
        }

        @HideFromJS
        public IEnergyStorage getCapability(BlockEntity instance) {
            return new IEnergyStorage() {

                @Override
                public int receiveEnergy(int i, boolean bl) {
                    return BlockEntityBuilder.this.receiveEnergy == null ? 0 : BlockEntityBuilder.this.receiveEnergy.transfer(instance, i, bl);
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    return BlockEntityBuilder.this.extractEnergy == null ? 0 : BlockEntityBuilder.this.extractEnergy.transfer(instance, i, bl);
                }

                @Override
                public int getEnergyStored() {
                    return BlockEntityBuilder.this.getEnergyStored == null ? 0 : BlockEntityBuilder.this.getEnergyStored.applyAsInt(instance);
                }

                @Override
                public int getMaxEnergyStored() {
                    return BlockEntityBuilder.this.getMaxEnergyStored == null ? 0 : BlockEntityBuilder.this.getMaxEnergyStored.applyAsInt(instance);
                }

                @Override
                public boolean canExtract() {
                    return BlockEntityBuilder.this.canExtract != null && BlockEntityBuilder.this.canExtract.test(instance);
                }

                @Override
                public boolean canReceive() {
                    return BlockEntityBuilder.this.canReceive != null && BlockEntityBuilder.this.canReceive.test(instance);
                }
            };
        }

        @HideFromJS
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_be_custom");
        }
    }

    @FunctionalInterface
    public interface EnergyIOBlockEntity {

        int transfer(BlockEntity var1, int var2, boolean var3);
    }

    @FunctionalInterface
    public interface EnergyIOItemStack {

        int transfer(ItemStack var1, int var2, boolean var3);
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, IEnergyStorage> {

        private CapabilityForgeEnergy.EnergyIOItemStack receiveEnergy;

        private CapabilityForgeEnergy.EnergyIOItemStack extractEnergy;

        private ToIntFunction<ItemStack> getEnergyStored;

        private ToIntFunction<ItemStack> getMaxEnergyStored;

        private Predicate<ItemStack> canExtract;

        private Predicate<ItemStack> canReceive;

        public CapabilityForgeEnergy.ItemStackBuilder getEnergyStored(ToIntFunction<ItemStack> getEnergyStored) {
            this.getEnergyStored = getEnergyStored;
            return this;
        }

        public CapabilityForgeEnergy.ItemStackBuilder withCapacity(int capacity) {
            return this.getMaxEnergyStored(i -> capacity);
        }

        public CapabilityForgeEnergy.ItemStackBuilder getMaxEnergyStored(ToIntFunction<ItemStack> getMaxEnergyStored) {
            this.getMaxEnergyStored = getMaxEnergyStored;
            return this;
        }

        public CapabilityForgeEnergy.ItemStackBuilder canExtract(Predicate<ItemStack> canExtract) {
            this.canExtract = canExtract;
            return this;
        }

        public CapabilityForgeEnergy.ItemStackBuilder canReceive(Predicate<ItemStack> canReceive) {
            this.canReceive = canReceive;
            return this;
        }

        public CapabilityForgeEnergy.ItemStackBuilder receiveEnergy(CapabilityForgeEnergy.EnergyIOItemStack receiveEnergy) {
            this.receiveEnergy = receiveEnergy;
            return this;
        }

        public CapabilityForgeEnergy.ItemStackBuilder extractEnergy(CapabilityForgeEnergy.EnergyIOItemStack extractEnergy) {
            this.extractEnergy = extractEnergy;
            return this;
        }

        @HideFromJS
        public IEnergyStorage getCapability(ItemStack instance) {
            return new IEnergyStorage() {

                @Override
                public int receiveEnergy(int i, boolean bl) {
                    return ItemStackBuilder.this.receiveEnergy == null ? 0 : ItemStackBuilder.this.receiveEnergy.transfer(instance, i, bl);
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    return ItemStackBuilder.this.extractEnergy == null ? 0 : ItemStackBuilder.this.extractEnergy.transfer(instance, i, bl);
                }

                @Override
                public int getEnergyStored() {
                    return ItemStackBuilder.this.getEnergyStored == null ? 0 : ItemStackBuilder.this.getEnergyStored.applyAsInt(instance);
                }

                @Override
                public int getMaxEnergyStored() {
                    return ItemStackBuilder.this.getMaxEnergyStored == null ? 0 : ItemStackBuilder.this.getMaxEnergyStored.applyAsInt(instance);
                }

                @Override
                public boolean canExtract() {
                    return ItemStackBuilder.this.canExtract != null && ItemStackBuilder.this.canExtract.test(instance);
                }

                @Override
                public boolean canReceive() {
                    return ItemStackBuilder.this.canReceive != null && ItemStackBuilder.this.canReceive.test(instance);
                }
            };
        }

        @HideFromJS
        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_item_custom");
        }
    }

    public static class ItemStorageBuilder extends CapabilityBuilderForge<ItemStack, IEnergyStorage> {

        private final int capacity;

        private final boolean canExtract;

        private final boolean canReceive;

        private int receiveRate = Integer.MAX_VALUE;

        private int extractRate = Integer.MAX_VALUE;

        private static final String ENERGY_TAG = "pjs:fe_energy";

        public ItemStorageBuilder(int capacity, boolean canExtract, boolean canReceive) {
            this.capacity = capacity;
            this.canExtract = canExtract;
            this.canReceive = canReceive;
        }

        public CapabilityForgeEnergy.ItemStorageBuilder receiveRate(int receiveRate) {
            this.receiveRate = receiveRate;
            return this;
        }

        public CapabilityForgeEnergy.ItemStorageBuilder extractRate(int extractRate) {
            this.extractRate = extractRate;
            return this;
        }

        public IEnergyStorage getCapability(ItemStack instance) {
            return new IEnergyStorage() {

                @Override
                public int receiveEnergy(int i, boolean bl) {
                    if (!ItemStorageBuilder.this.canReceive) {
                        return 0;
                    } else {
                        int received = Math.min(ItemStorageBuilder.this.receiveRate, Math.min(this.getMaxEnergyStored() - this.getEnergyStored(), i));
                        if (!bl) {
                            instance.getOrCreateTag().putInt("pjs:fe_energy", received + this.getEnergyStored());
                        }
                        return received;
                    }
                }

                @Override
                public int extractEnergy(int i, boolean bl) {
                    if (!ItemStorageBuilder.this.canExtract) {
                        return 0;
                    } else {
                        int extracted = Math.min(ItemStorageBuilder.this.extractRate, Math.min(this.getEnergyStored(), i));
                        if (!bl) {
                            instance.getOrCreateTag().putInt("pjs:fe_energy", this.getEnergyStored() - extracted);
                        }
                        return extracted;
                    }
                }

                @Override
                public int getEnergyStored() {
                    return instance.getOrCreateTag().getInt("pjs:fe_energy");
                }

                @Override
                public int getMaxEnergyStored() {
                    return ItemStorageBuilder.this.capacity;
                }

                @Override
                public boolean canExtract() {
                    return ItemStorageBuilder.this.canExtract;
                }

                @Override
                public boolean canReceive() {
                    return ItemStorageBuilder.this.canReceive;
                }
            };
        }

        public Capability<IEnergyStorage> getCapabilityKey() {
            return ForgeCapabilities.ENERGY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:fe_item");
        }
    }
}