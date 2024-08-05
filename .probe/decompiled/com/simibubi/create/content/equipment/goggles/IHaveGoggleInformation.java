package com.simibubi.create.content.equipment.goggles;

import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.LangBuilder;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface IHaveGoggleInformation {

    String spacing = "    ";

    @Deprecated
    Component componentSpacing = Components.literal("    ");

    default boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }

    default boolean containedFluidTooltip(List<Component> tooltip, boolean isPlayerSneaking, LazyOptional<IFluidHandler> handler) {
        Optional<IFluidHandler> resolve = handler.resolve();
        if (!resolve.isPresent()) {
            return false;
        } else {
            IFluidHandler tank = (IFluidHandler) resolve.get();
            if (tank.getTanks() == 0) {
                return false;
            } else {
                LangBuilder mb = Lang.translate("generic.unit.millibuckets");
                Lang.translate("gui.goggles.fluid_container").forGoggles(tooltip);
                boolean isEmpty = true;
                for (int i = 0; i < tank.getTanks(); i++) {
                    FluidStack fluidStack = tank.getFluidInTank(i);
                    if (!fluidStack.isEmpty()) {
                        Lang.fluidName(fluidStack).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                        Lang.builder().add(Lang.number((double) fluidStack.getAmount()).add(mb).style(ChatFormatting.GOLD)).text(ChatFormatting.GRAY, " / ").add(Lang.number((double) tank.getTankCapacity(i)).add(mb).style(ChatFormatting.DARK_GRAY)).forGoggles(tooltip, 1);
                        isEmpty = false;
                    }
                }
                if (tank.getTanks() > 1) {
                    if (isEmpty) {
                        tooltip.remove(tooltip.size() - 1);
                    }
                    return true;
                } else if (!isEmpty) {
                    return true;
                } else {
                    Lang.translate("gui.goggles.fluid_container.capacity").add(Lang.number((double) tank.getTankCapacity(0)).add(mb).style(ChatFormatting.GOLD)).style(ChatFormatting.GRAY).forGoggles(tooltip, 1);
                    return true;
                }
            }
        }
    }
}