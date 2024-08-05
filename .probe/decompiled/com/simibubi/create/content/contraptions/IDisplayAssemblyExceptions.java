package com.simibubi.create.content.contraptions;

import com.simibubi.create.content.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface IDisplayAssemblyExceptions {

    default boolean addExceptionToTooltip(List<Component> tooltip) {
        AssemblyException e = this.getLastAssemblyException();
        if (e == null) {
            return false;
        } else {
            if (!tooltip.isEmpty()) {
                tooltip.add(Components.immutableEmpty());
            }
            tooltip.add(IHaveGoggleInformation.componentSpacing.plainCopy().append(Lang.translateDirect("gui.assembly.exception").withStyle(ChatFormatting.GOLD)));
            String text = e.component.getString();
            Arrays.stream(text.split("\n")).forEach(l -> TooltipHelper.cutStringTextComponent(l, TooltipHelper.Palette.GRAY_AND_WHITE).forEach(c -> tooltip.add(IHaveGoggleInformation.componentSpacing.plainCopy().append(c))));
            return true;
        }
    }

    AssemblyException getLastAssemblyException();
}