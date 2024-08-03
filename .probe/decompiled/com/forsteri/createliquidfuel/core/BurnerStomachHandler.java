package com.forsteri.createliquidfuel.core;

import com.forsteri.createliquidfuel.mixin.BlazeBurnerAccessor;
import com.forsteri.createliquidfuel.util.Triplet;
import com.mojang.datafixers.util.Pair;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.fluid.SmartFluidTank;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BurnerStomachHandler {

    public static Map<Fluid, Pair<ResourceLocation, Triplet<Integer, Boolean, Integer>>> LIQUID_BURNER_FUEL_MAP = new HashMap();

    public static void tick(SmartBlockEntity entity) {
        if (entity instanceof BlazeBurnerAccessor burnerAccessor) {
            SmartFluidTank stomach = (SmartFluidTank) entity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
            if (stomach != null) {
                if (stomach.getFluid().getAmount() > 0) {
                    Triplet<Integer, Boolean, Integer> burnerProperty = (Triplet<Integer, Boolean, Integer>) ((Pair) LIQUID_BURNER_FUEL_MAP.get(stomach.getFluid().getFluid())).getSecond();
                    if (burnerProperty != null) {
                        boolean fluidSuperHeats = burnerProperty.getSecond();
                        int mbConsuming = burnerProperty.getThird();
                        if (stomach.getFluid().getAmount() < mbConsuming) {
                            stomach.getFluid().setAmount(0);
                        } else {
                            if (fluidSuperHeats) {
                                burnerAccessor.createliquidfuel$invokeSetBlockHeat(BlazeBurnerBlock.HeatLevel.SEETHING);
                            } else {
                                burnerAccessor.createliquidfuel$invokeSetBlockHeat(BlazeBurnerBlock.HeatLevel.FADING);
                            }
                            int newBurnTime = burnerAccessor.createliquidfuel$getRemainingBurnTime() + burnerProperty.getFirst();
                            if (newBurnTime <= 10000) {
                                burnerAccessor.createliquidfuel$setRemainingBurnTime(newBurnTime);
                                stomach.getFluid().shrink(mbConsuming);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void tryUpdateFuel(@NotNull SmartBlockEntity entity, ItemStack itemStack, boolean forceOverflow, boolean simulate, CallbackInfoReturnable<Boolean> cir) {
        SmartFluidTank stomach = (SmartFluidTank) entity.getCapability(ForgeCapabilities.FLUID_HANDLER).orElse(null);
        if (stomach != null) {
            if (itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()) {
                IFluidHandler handler = (IFluidHandler) itemStack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).orElse(null);
                if (stomach.getFluid().isEmpty() || handler.getFluidInTank(0).getFluid() == stomach.getFluid().getFluid()) {
                    if (handler.getTanks() == 1) {
                        FluidStack fluidStack = handler.getFluidInTank(0);
                        if (!fluidStack.isEmpty()) {
                            if (LIQUID_BURNER_FUEL_MAP.containsKey(fluidStack.getFluid())) {
                                if (stomach.getFluid().getAmount() + fluidStack.getAmount() <= stomach.getCapacity() || forceOverflow) {
                                    if (!simulate) {
                                        if (stomach.getFluid().isEmpty()) {
                                            stomach.setFluid(fluidStack.copy());
                                        } else {
                                            stomach.getFluid().grow(fluidStack.getAmount());
                                        }
                                    }
                                    cir.setReturnValue(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}