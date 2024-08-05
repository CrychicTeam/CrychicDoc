package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.infuse.IInfusionHandler;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilityInfusion {

    public CapabilityInfusion.ItemStackBuilder itemStack() {
        return new CapabilityInfusion.ItemStackBuilder();
    }

    public CapabilityInfusion.BlockEntityBuilder blockEntity() {
        return new CapabilityInfusion.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityInfusion.InfusionBuilder<BlockEntity> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:infusion_be_custom");
        }
    }

    public abstract static class InfusionBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, IInfusionHandler, InfuseType, InfusionStack> {

        public Capability<IInfusionHandler> getCapabilityKey() {
            return Capabilities.INFUSION_HANDLER;
        }

        public IInfusionHandler getCapability(I instance) {
            return new IInfusionHandler() {

                public int getTanks() {
                    return InfusionBuilder.this.getTanks == null ? 1 : InfusionBuilder.this.getTanks.applyAsInt(instance);
                }

                @NotNull
                public InfusionStack getChemicalInTank(int i) {
                    return InfusionBuilder.this.getChemicalInTank == null ? InfusionStack.EMPTY : (InfusionStack) InfusionBuilder.this.getChemicalInTank.apply(instance, i);
                }

                public void setChemicalInTank(int i, @NotNull InfusionStack stack) {
                    if (InfusionBuilder.this.setChemicalInTank != null) {
                        InfusionBuilder.this.setChemicalInTank.accept(instance, i, stack);
                    }
                }

                public long getTankCapacity(int i) {
                    return InfusionBuilder.this.getTankCapacity == null ? 0L : (Long) InfusionBuilder.this.getTankCapacity.apply(instance, i);
                }

                public boolean isValid(int i, @NotNull InfusionStack stack) {
                    return InfusionBuilder.this.isValid == null || InfusionBuilder.this.isValid.test(instance, i, stack);
                }

                @NotNull
                public InfusionStack insertChemical(int i, @NotNull InfusionStack stack, @NotNull Action action) {
                    return InfusionBuilder.this.insertChemical == null ? stack : (InfusionStack) InfusionBuilder.this.insertChemical.apply(instance, i, stack, action.simulate());
                }

                @NotNull
                public InfusionStack extractChemical(int i, long l, @NotNull Action action) {
                    return InfusionBuilder.this.extractChemical == null ? InfusionStack.EMPTY : (InfusionStack) InfusionBuilder.this.extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }

    public static class ItemStackBuilder extends CapabilityInfusion.InfusionBuilder<ItemStack> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:infusion_item_custom");
        }
    }
}