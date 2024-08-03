package com.simibubi.create.compat.tconstruct;

import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.compat.Mods;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.fluid.FluidHelper;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import com.simibubi.create.infrastructure.config.AllConfigs;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class SpoutCasting extends BlockSpoutingBehaviour {

    static Boolean TICON_PRESENT = null;

    ResourceLocation TABLE = new ResourceLocation("tconstruct", "table");

    ResourceLocation BASIN = new ResourceLocation("tconstruct", "basin");

    @Override
    public int fillBlock(Level level, BlockPos pos, SpoutBlockEntity spout, FluidStack availableFluid, boolean simulate) {
        if (!this.enabled()) {
            return 0;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity == null) {
                return 0;
            } else {
                IFluidHandler handler = (IFluidHandler) blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, Direction.UP).orElse(null);
                if (handler == null) {
                    return 0;
                } else if (handler.getTanks() != 1) {
                    return 0;
                } else {
                    ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(blockEntity.getType());
                    if (!registryName.equals(this.TABLE) && !registryName.equals(this.BASIN)) {
                        return 0;
                    } else if (!handler.isFluidValid(0, availableFluid)) {
                        return 0;
                    } else {
                        FluidStack containedFluid = handler.getFluidInTank(0);
                        if (!containedFluid.isEmpty() && !containedFluid.isFluidEqual(availableFluid)) {
                            return 0;
                        } else {
                            int amount = availableFluid.getAmount();
                            return amount < 1000 && handler.fill(FluidHelper.copyStackWithAmount(availableFluid, amount + 1), IFluidHandler.FluidAction.SIMULATE) > amount ? 0 : handler.fill(availableFluid, simulate ? IFluidHandler.FluidAction.SIMULATE : IFluidHandler.FluidAction.EXECUTE);
                        }
                    }
                }
            }
        }
    }

    private boolean enabled() {
        if (TICON_PRESENT == null) {
            TICON_PRESENT = Mods.TCONSTRUCT.isLoaded();
        }
        return !TICON_PRESENT ? false : AllConfigs.server().recipes.allowCastingBySpout.get();
    }
}