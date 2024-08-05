package net.blay09.mods.craftingtweaks.client;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface ITooltipProvider {

    List<Component> getTooltipComponents();
}