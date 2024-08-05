package com.simibubi.create.content.equipment.goggles;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface IHaveHoveringInformation {

    default boolean addToTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
        return false;
    }
}