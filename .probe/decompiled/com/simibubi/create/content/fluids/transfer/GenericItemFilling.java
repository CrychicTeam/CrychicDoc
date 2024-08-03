package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.foundation.fluid.FluidHelper;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class GenericItemFilling {

    public static boolean isFluidHandlerValid(ItemStack stack, IFluidHandlerItem fluidHandler) {
        if (fluidHandler.getClass() == FluidBucketWrapper.class) {
            Item item = stack.getItem();
            if (item.getClass() != BucketItem.class && !(item instanceof MilkBucketItem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canItemBeFilled(Level world, ItemStack stack) {
        if (stack.getItem() == Items.GLASS_BOTTLE) {
            return true;
        } else if (stack.getItem() == Items.MILK_BUCKET) {
            return false;
        } else {
            LazyOptional<IFluidHandlerItem> capability = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            IFluidHandlerItem tank = capability.orElse(null);
            if (tank == null) {
                return false;
            } else if (!isFluidHandlerValid(stack, tank)) {
                return false;
            } else {
                for (int i = 0; i < tank.getTanks(); i++) {
                    if (tank.getFluidInTank(i).getAmount() < tank.getTankCapacity(i)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    public static int getRequiredAmountForItem(Level world, ItemStack stack, FluidStack availableFluid) {
        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(availableFluid)) {
            return PotionFluidHandler.getRequiredAmountForFilledBottle(stack, availableFluid);
        } else if (stack.getItem() == Items.BUCKET && canFillBucketInternally(availableFluid)) {
            return 1000;
        } else {
            LazyOptional<IFluidHandlerItem> capability = stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            IFluidHandlerItem tank = capability.orElse(null);
            if (tank == null) {
                return -1;
            } else if (tank instanceof FluidBucketWrapper) {
                Item filledBucket = availableFluid.getFluid().getBucket();
                if (filledBucket == null || filledBucket == Items.AIR) {
                    return -1;
                } else {
                    return !((FluidBucketWrapper) tank).getFluid().isEmpty() ? -1 : 1000;
                }
            } else {
                int filled = tank.fill(availableFluid, IFluidHandler.FluidAction.SIMULATE);
                return filled == 0 ? -1 : filled;
            }
        }
    }

    private static boolean canFillGlassBottleInternally(FluidStack availableFluid) {
        Fluid fluid = availableFluid.getFluid();
        if (fluid.isSame(Fluids.WATER)) {
            return true;
        } else {
            return fluid.isSame((Fluid) AllFluids.POTION.get()) ? true : fluid.isSame((Fluid) AllFluids.TEA.get());
        }
    }

    private static boolean canFillBucketInternally(FluidStack availableFluid) {
        return false;
    }

    public static ItemStack fillItem(Level world, int requiredAmount, ItemStack stack, FluidStack availableFluid) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount);
        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(toFill)) {
            ItemStack fillBottle = ItemStack.EMPTY;
            Fluid fluid = toFill.getFluid();
            if (FluidHelper.isWater(fluid)) {
                fillBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
            } else if (fluid.isSame((Fluid) AllFluids.TEA.get())) {
                fillBottle = AllItems.BUILDERS_TEA.asStack();
            } else {
                fillBottle = PotionFluidHandler.fillBottle(stack, toFill);
            }
            stack.shrink(1);
            return fillBottle;
        } else {
            ItemStack split = stack.copy();
            split.setCount(1);
            LazyOptional<IFluidHandlerItem> capability = split.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM);
            IFluidHandlerItem tank = capability.orElse(null);
            if (tank == null) {
                return ItemStack.EMPTY;
            } else {
                tank.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
                ItemStack container = tank.getContainer().copy();
                stack.shrink(1);
                return container;
            }
        }
    }
}