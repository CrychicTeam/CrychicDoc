package com.rekindled.embers.upgrade;

import com.rekindled.embers.api.event.MachineRecipeEvent;
import com.rekindled.embers.api.event.UpgradeEvent;
import com.rekindled.embers.api.upgrades.UpgradeContext;
import com.rekindled.embers.blockentity.GeologicSeparatorBlockEntity;
import com.rekindled.embers.recipe.MeltingRecipe;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class GeologicSeparatorUpgrade extends DefaultUpgradeProvider {

    public GeologicSeparatorUpgrade(BlockEntity tile) {
        super(new ResourceLocation("embers", "geologic_separator"), tile);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public void throwEvent(BlockEntity tile, List<UpgradeContext> upgrades, UpgradeEvent event, int distance, int count) {
        if (distance <= 0 && event instanceof MachineRecipeEvent.Success) {
            Object recipe = ((MachineRecipeEvent) event).getRecipe();
            if (recipe instanceof MeltingRecipe) {
                FluidStack bonus = ((MeltingRecipe) recipe).getBonus();
                if (!bonus.isEmpty() && this.tile instanceof GeologicSeparatorBlockEntity) {
                    IFluidHandler fluidHandler = (IFluidHandler) this.tile.getCapability(ForgeCapabilities.FLUID_HANDLER, null).orElse(null);
                    if (fluidHandler != null) {
                        fluidHandler.fill(bonus.copy(), IFluidHandler.FluidAction.EXECUTE);
                    }
                }
            }
        }
    }
}