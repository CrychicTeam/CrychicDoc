package net.blay09.mods.waystones.client.gui.widget;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface ITooltipProvider {

    boolean shouldShowTooltip();

    List<Component> getTooltipComponents();
}