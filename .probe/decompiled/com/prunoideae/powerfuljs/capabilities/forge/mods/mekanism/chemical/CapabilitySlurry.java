package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism.chemical;

import mekanism.api.Action;
import mekanism.api.chemical.slurry.ISlurryHandler;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.jetbrains.annotations.NotNull;

public class CapabilitySlurry {

    public CapabilitySlurry.ItemStackBuilder itemStack() {
        return new CapabilitySlurry.ItemStackBuilder();
    }

    public CapabilitySlurry.BlockEntityBuilder blockEntity() {
        return new CapabilitySlurry.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilitySlurry.SlurryBuilder<BlockEntity> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:slurry_be_custom");
        }
    }

    public static class ItemStackBuilder extends CapabilitySlurry.SlurryBuilder<ItemStack> {

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:slurry_item_custom");
        }
    }

    public abstract static class SlurryBuilder<I extends CapabilityProvider<I>> extends CapabilityChemical.ChemicalBuilder<I, ISlurryHandler, Slurry, SlurryStack> {

        public Capability<ISlurryHandler> getCapabilityKey() {
            return Capabilities.SLURRY_HANDLER;
        }

        public ISlurryHandler getCapability(I instance) {
            return new ISlurryHandler() {

                public int getTanks() {
                    return SlurryBuilder.this.getTanks == null ? 1 : SlurryBuilder.this.getTanks.applyAsInt(instance);
                }

                @NotNull
                public SlurryStack getChemicalInTank(int i) {
                    return SlurryBuilder.this.getChemicalInTank == null ? SlurryStack.EMPTY : (SlurryStack) SlurryBuilder.this.getChemicalInTank.apply(instance, i);
                }

                public void setChemicalInTank(int i, @NotNull SlurryStack stack) {
                    if (SlurryBuilder.this.setChemicalInTank != null) {
                        SlurryBuilder.this.setChemicalInTank.accept(instance, i, stack);
                    }
                }

                public long getTankCapacity(int i) {
                    return SlurryBuilder.this.getTankCapacity == null ? 0L : (Long) SlurryBuilder.this.getTankCapacity.apply(instance, i);
                }

                public boolean isValid(int i, @NotNull SlurryStack stack) {
                    return SlurryBuilder.this.isValid == null || SlurryBuilder.this.isValid.test(instance, i, stack);
                }

                @NotNull
                public SlurryStack insertChemical(int i, @NotNull SlurryStack stack, @NotNull Action action) {
                    return SlurryBuilder.this.insertChemical == null ? stack : (SlurryStack) SlurryBuilder.this.insertChemical.apply(instance, i, stack, action.simulate());
                }

                @NotNull
                public SlurryStack extractChemical(int i, long l, @NotNull Action action) {
                    return SlurryBuilder.this.extractChemical == null ? SlurryStack.EMPTY : (SlurryStack) SlurryBuilder.this.extractChemical.apply(instance, i, l, action.simulate());
                }
            };
        }
    }
}