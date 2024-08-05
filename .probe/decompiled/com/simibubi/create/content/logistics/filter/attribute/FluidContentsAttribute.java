package com.simibubi.create.content.logistics.filter.attribute;

import com.simibubi.create.content.logistics.filter.ItemAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidContentsAttribute implements ItemAttribute {

    public static final FluidContentsAttribute EMPTY = new FluidContentsAttribute(null);

    private final Fluid fluid;

    public FluidContentsAttribute(@Nullable Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean appliesTo(ItemStack itemStack) {
        return this.extractFluids(itemStack).contains(this.fluid);
    }

    @Override
    public List<ItemAttribute> listAttributesOf(ItemStack itemStack) {
        return (List<ItemAttribute>) this.extractFluids(itemStack).stream().map(FluidContentsAttribute::new).collect(Collectors.toList());
    }

    @Override
    public String getTranslationKey() {
        return "has_fluid";
    }

    @Override
    public Object[] getTranslationParameters() {
        String parameter = "";
        if (this.fluid != null) {
            parameter = this.fluid.getFluidType().getDescription().getString();
        }
        return new Object[] { parameter };
    }

    @Override
    public void writeNBT(CompoundTag nbt) {
        if (this.fluid != null) {
            ResourceLocation id = ForgeRegistries.FLUIDS.getKey(this.fluid);
            if (id != null) {
                nbt.putString("id", id.toString());
            }
        }
    }

    @Override
    public ItemAttribute readNBT(CompoundTag nbt) {
        return nbt.contains("id") ? new FluidContentsAttribute(ForgeRegistries.FLUIDS.getValue(ResourceLocation.tryParse(nbt.getString("id")))) : EMPTY;
    }

    private List<Fluid> extractFluids(ItemStack stack) {
        List<Fluid> fluids = new ArrayList();
        LazyOptional<IFluidHandlerItem> capability = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
        capability.ifPresent(cap -> {
            for (int i = 0; i < cap.getTanks(); i++) {
                fluids.add(cap.getFluidInTank(i).getFluid());
            }
        });
        return fluids;
    }
}