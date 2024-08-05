package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import dev.latvian.mods.rhino.util.HideFromJS;
import mekanism.api.Action;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasHandler;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilityGas {

    public CapabilityGas.ItemStackBuilder itemStack() {
        return new CapabilityGas.ItemStackBuilder();
    }

    public CapabilityGas.BlockEntityBuilder blockEntity() {
        return new CapabilityGas.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityGas.GasBuilder<BlockEntity> {

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:gas_be_custom");
        }
    }

    public abstract static class GasBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, IGasHandler, Gas, GasStack> {

        @HideFromJS
        public Capability<IGasHandler> getCapabilityKey() {
            return Capabilities.GAS_HANDLER;
        }

        @HideFromJS
        public IGasHandler getCapability(I instance) {
            return new IGasHandler() {

                public int getTanks() {
                    return GasBuilder.this.getTanks == null ? 1 : GasBuilder.this.getTanks.applyAsInt(instance);
                }

                @NotNull
                public GasStack getChemicalInTank(int i) {
                    return GasBuilder.this.getChemicalInTank == null ? GasStack.EMPTY : (GasStack) GasBuilder.this.getChemicalInTank.apply(instance, i);
                }

                public void setChemicalInTank(int i, @NotNull GasStack stack) {
                    if (GasBuilder.this.setChemicalInTank != null) {
                        GasBuilder.this.setChemicalInTank.accept(instance, i, stack);
                    }
                }

                public long getTankCapacity(int i) {
                    return GasBuilder.this.getTankCapacity == null ? 0L : (Long) GasBuilder.this.getTankCapacity.apply(instance, i);
                }

                public boolean isValid(int i, @NotNull GasStack stack) {
                    return GasBuilder.this.isValid == null || GasBuilder.this.isValid.test(instance, i, stack);
                }

                @NotNull
                public GasStack insertChemical(int i, @NotNull GasStack stack, @NotNull Action action) {
                    return GasBuilder.this.insertChemical == null ? stack : (GasStack) GasBuilder.this.insertChemical.apply(instance, i, stack, action.simulate());
                }

                @NotNull
                public GasStack extractChemical(int i, long l, @NotNull Action action) {
                    return GasBuilder.this.extractChemical == null ? GasStack.EMPTY : (GasStack) GasBuilder.this.extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends CapabilityGas.GasBuilder<ItemStack> {

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:gas_item_custom");
        }
    }
}