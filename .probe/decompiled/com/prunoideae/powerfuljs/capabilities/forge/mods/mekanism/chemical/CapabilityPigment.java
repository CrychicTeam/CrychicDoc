package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.pigment.IPigmentHandler;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilityPigment {

    public CapabilityPigment.ItemStackBuilder itemStack() {
        return new CapabilityPigment.ItemStackBuilder();
    }

    public CapabilityPigment.BlockEntityBuilder blockEntity() {
        return new CapabilityPigment.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityPigment.PigmentBuilder<BlockEntity> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:pigment_be_custom");
        }
    }

    public static class ItemStackBuilder extends CapabilityPigment.PigmentBuilder<ItemStack> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:pigment_item_custom");
        }
    }

    public abstract static class PigmentBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, IPigmentHandler, Pigment, PigmentStack> {

        public Capability<IPigmentHandler> getCapabilityKey() {
            return Capabilities.PIGMENT_HANDLER;
        }

        public IPigmentHandler getCapability(I instance) {
            return new IPigmentHandler() {

                public int getTanks() {
                    return PigmentBuilder.this.getTanks == null ? 1 : PigmentBuilder.this.getTanks.applyAsInt(instance);
                }

                @NotNull
                public PigmentStack getChemicalInTank(int i) {
                    return PigmentBuilder.this.getChemicalInTank == null ? PigmentStack.EMPTY : (PigmentStack) PigmentBuilder.this.getChemicalInTank.apply(instance, i);
                }

                public void setChemicalInTank(int i, @NotNull PigmentStack stack) {
                    if (PigmentBuilder.this.setChemicalInTank != null) {
                        PigmentBuilder.this.setChemicalInTank.accept(instance, i, stack);
                    }
                }

                public long getTankCapacity(int i) {
                    return PigmentBuilder.this.getTankCapacity == null ? 0L : (Long) PigmentBuilder.this.getTankCapacity.apply(instance, i);
                }

                public boolean isValid(int i, @NotNull PigmentStack stack) {
                    return PigmentBuilder.this.isValid == null || PigmentBuilder.this.isValid.test(instance, i, stack);
                }

                @NotNull
                public PigmentStack insertChemical(int i, @NotNull PigmentStack stack, @NotNull Action action) {
                    return PigmentBuilder.this.insertChemical == null ? stack : (PigmentStack) PigmentBuilder.this.insertChemical.apply(instance, i, stack, action.simulate());
                }

                @NotNull
                public PigmentStack extractChemical(int i, long l, @NotNull Action action) {
                    return PigmentBuilder.this.extractChemical == null ? PigmentStack.EMPTY : (PigmentStack) PigmentBuilder.this.extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }
}