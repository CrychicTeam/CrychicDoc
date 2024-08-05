package com.mna.items.artifice;

import com.mna.api.items.IRelic;
import com.mna.blocks.artifice.FluidJugBlock;
import com.mna.items.base.InfiniteFluidCapabilityHandler;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;

public class ItemInfiniteFluidJug extends ItemFluidJug implements IRelic {

    final Fluid contained;

    final FluidStack containedStack;

    final boolean relic;

    public ItemInfiniteFluidJug(FluidJugBlock forBlock, Fluid contained, boolean relic) {
        super(forBlock);
        this.contained = contained;
        this.containedStack = new FluidStack(contained, Integer.MAX_VALUE);
        this.relic = relic;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new InfiniteFluidCapabilityHandler(this.contained, stack);
    }

    @Override
    public FluidStack getFluidTagData(ItemStack stack) {
        return this.containedStack;
    }

    @Override
    public Component getHoverAddition(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        return this.relic ? IRelic.super.getHoverAddition(stack, worldIn, tooltip, flagIn) : null;
    }
}